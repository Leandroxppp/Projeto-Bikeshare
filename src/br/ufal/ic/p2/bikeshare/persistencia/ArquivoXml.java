package br.ufal.ic.p2.bikeshare.persistencia;

import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.excecoes.PersistenciaException;
import br.ufal.ic.p2.bikeshare.repositorio.BaseDados;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.Optional;

/**
 * Grava o estado num arquivo XML. O conteúdo do estado é gerado pela serialização nativa do Java
 * ({@link java.io.Serializable}, exigida pelo enunciado) e embalado em base64 dentro do
 * elemento {@code <estado>}, de modo que o arquivo é um XML válido sem parser manual.
 */
public final class ArquivoXml {
    private static final String RAIZ = "bikeshare";
    private static final String ELEMENTO_ESTADO = "estado";

    private final Path arquivo;

    public ArquivoXml(String nomeDoArquivo) {
        this.arquivo = Path.of(nomeDoArquivo);
    }

    public void salvar(BaseDados base) {
        try {
            Document doc = novoDocumentBuilder().newDocument();
            Element raiz = doc.createElement(RAIZ);
            raiz.setAttribute("versao", "1");
            Element estado = doc.createElement(ELEMENTO_ESTADO);
            estado.setAttribute("codificacao", "java.io.Serializable+base64");
            estado.setTextContent(Base64.getEncoder().encodeToString(Serializador.serializar(base)));
            raiz.appendChild(estado);
            doc.appendChild(raiz);

            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            t.transform(new DOMSource(doc), new StreamResult(arquivo.toFile()));
        } catch (PersistenciaException e) {
            throw e;
        } catch (Exception e) {
            throw new PersistenciaException(Mensagens.FALHA_AO_GRAVAR);
        }
    }

    /**
     * Vazio quando ainda não há arquivo. Um arquivo ilegível não é descartado em silêncio: é
     * preservado ao lado, com o sufixo ".corrompido", e o aviso vai para a saída de erro.
     */
    public Optional<BaseDados> carregar() {
        File f = arquivo.toFile();
        if (!f.exists()) {
            return Optional.empty();
        }
        try {
            Document doc = novoDocumentBuilder().parse(f);
            String texto = doc.getElementsByTagName(ELEMENTO_ESTADO).item(0).getTextContent().trim();
            byte[] bytes = Base64.getDecoder().decode(texto);
            return Optional.of(Serializador.desserializar(bytes, BaseDados.class));
        } catch (Exception e) {
            isolarArquivoIlegivel();
            return Optional.empty();
        }
    }

    private void isolarArquivoIlegivel() {
        Path destino = Path.of(arquivo + ".corrompido");
        try {
            Files.move(arquivo, destino, StandardCopyOption.REPLACE_EXISTING);
            System.err.println("[bikeshare] " + Mensagens.FALHA_AO_LER + " Arquivo preservado em " + destino
                    + "; iniciando com o sistema vazio.");
        } catch (IOException e) {
            throw new PersistenciaException(Mensagens.FALHA_AO_LER);
        }
    }

    private static DocumentBuilder novoDocumentBuilder() throws Exception {
        DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();
        fabrica.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        fabrica.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        return fabrica.newDocumentBuilder();
    }
}
