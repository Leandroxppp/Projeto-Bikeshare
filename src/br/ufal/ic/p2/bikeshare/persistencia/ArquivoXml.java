package br.ufal.ic.p2.bikeshare.persistencia;

import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.excecoes.PersistenciaException;
import br.ufal.ic.p2.bikeshare.modelo.aluguel.Aluguel;
import br.ufal.ic.p2.bikeshare.modelo.bicicleta.Bicicleta;
import br.ufal.ic.p2.bikeshare.modelo.estacao.Estacao;
import br.ufal.ic.p2.bikeshare.modelo.penalidade.Penalidade;
import br.ufal.ic.p2.bikeshare.modelo.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.repositorio.BaseDados;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

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
 * Grava o estado num arquivo XML com duas partes:
 * <ul>
 *   <li>{@code <resumo>}: usuários, estações, bicicletas e aluguéis em elementos XML legíveis
 *       (somente para consulta/inspeção; nunca é lido de volta);</li>
 *   <li>{@code <estadoSerializado>}: o estado completo, gerado pela serialização nativa do Java
 *       ({@link java.io.Serializable}, exigida pelo enunciado) e embalado em base64. É a fonte
 *       da verdade na hora de carregar.</li>
 * </ul>
 */
public final class ArquivoXml {
    private static final String RAIZ = "bikeshare";
    private static final String ELEMENTO_ESTADO = "estadoSerializado";

    private final Path arquivo;

    public ArquivoXml(String nomeDoArquivo) {
        this.arquivo = Path.of(nomeDoArquivo);
    }

    public void salvar(BaseDados base) {
        try {
            Document doc = novoDocumentBuilder().newDocument();
            Element raiz = doc.createElement(RAIZ);
            raiz.setAttribute("versao", "2");
            raiz.appendChild(montarResumo(doc, base));
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

    /** Visão legível do estado: um elemento por entidade, com os atributos principais. */
    private static Element montarResumo(Document doc, BaseDados base) {
        Element resumo = doc.createElement("resumo");

        Element usuarios = doc.createElement("usuarios");
        for (Usuario u : base.usuarios().listar()) {
            Element e = doc.createElement("usuario");
            e.setAttribute("id", u.getId());
            e.setAttribute("nome", u.getNome());
            e.setAttribute("email", u.getEmail());
            e.setAttribute("tipo", u.getTipo());
            e.setAttribute("estado", u.getEstado().name());
            for (Penalidade p : u.getPenalidades()) {
                Element pen = doc.createElement("penalidade");
                pen.setAttribute("tipo", p.getTipo());
                pen.setAttribute("valor", p.getValor().toPlainString());
                pen.setAttribute("motivo", p.getMotivo());
                e.appendChild(pen);
            }
            usuarios.appendChild(e);
        }
        resumo.appendChild(usuarios);

        Element estacoes = doc.createElement("estacoes");
        for (Estacao s : base.estacoes().listar()) {
            Element e = doc.createElement("estacao");
            e.setAttribute("id", s.getId());
            e.setAttribute("nome", s.getNome());
            e.setAttribute("endereco", s.getEndereco());
            e.setAttribute("capacidade", String.valueOf(s.getCapacidade()));
            e.setAttribute("vagasLivres", String.valueOf(s.getVagasLivres()));
            estacoes.appendChild(e);
        }
        resumo.appendChild(estacoes);

        Element bicicletas = doc.createElement("bicicletas");
        for (Bicicleta b : base.bicicletas().listar()) {
            Element e = doc.createElement("bicicleta");
            e.setAttribute("id", b.getId());
            e.setAttribute("tipo", b.getTipo());
            e.setAttribute("estado", b.getEstado().name());
            bicicletas.appendChild(e);
        }
        resumo.appendChild(bicicletas);

        Element alugueis = doc.createElement("alugueis");
        for (Aluguel a : base.alugueis().listar()) {
            Element e = doc.createElement("aluguel");
            e.setAttribute("id", a.getId());
            e.setAttribute("usuario", a.getUsuarioId());
            e.setAttribute("bicicleta", a.getBicicletaId());
            e.setAttribute("estacaoOrigem", a.getEstacaoOrigemId());
            e.setAttribute("inicio", String.valueOf(a.getInicio()));
            e.setAttribute("estado", a.getEstado().name());
            e.setAttribute("valorCobrado", String.valueOf(a.getValorCobrado()));
            alugueis.appendChild(e);
        }
        resumo.appendChild(alugueis);
        return resumo;
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
        DocumentBuilder construtor = fabrica.newDocumentBuilder();
        construtor.setErrorHandler(new ErrorHandler() { // sem ruído no console; o erro sobe como exceção
            @Override
            public void warning(SAXParseException e) {
                // avisos de parser não interessam
            }

            @Override
            public void error(SAXParseException e) throws SAXParseException {
                throw e;
            }

            @Override
            public void fatalError(SAXParseException e) throws SAXParseException {
                throw e;
            }
        });
        return construtor;
    }
}
