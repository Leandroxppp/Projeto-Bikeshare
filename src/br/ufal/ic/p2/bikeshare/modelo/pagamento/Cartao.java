package br.ufal.ic.p2.bikeshare.modelo.pagamento;

import br.ufal.ic.p2.bikeshare.excecoes.DadoInvalidoException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.util.Entrada;

import java.util.Map;
import java.util.regex.Pattern;

public final class Cartao extends MetodoPagamento {
    private static final long serialVersionUID = 1L;
    private static final Pattern VALIDADE = Pattern.compile("(0[1-9]|1[0-2])/\\d{4}");

    private final String numero;
    private final String validade;

    public Cartao(String numero, String validade) {
        this.numero = Entrada.obrigatorio(numero, Mensagens.NUMERO_CARTAO_NULO);
        Entrada.obrigatorio(validade, Mensagens.VALIDADE_CARTAO_NULA);
        if (!VALIDADE.matcher(validade.trim()).matches()) {
            throw new DadoInvalidoException(Mensagens.VALIDADE_CARTAO_INVALIDA);
        }
        this.validade = validade.trim();
    }

    @Override
    public String getNome() {
        return "cartao";
    }

    @Override
    public Map<String, String> getAtributos() {
        return Map.of(AtributoPagamento.NUMERO_CARTAO.chave(), numero,
                AtributoPagamento.VALIDADE_CARTAO.chave(), validade);
    }
}
