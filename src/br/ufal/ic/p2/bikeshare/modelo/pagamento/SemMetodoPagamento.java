package br.ufal.ic.p2.bikeshare.modelo.pagamento;

import br.ufal.ic.p2.bikeshare.config.Parametros;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.excecoes.OperacaoNaoPermitidaException;

import java.util.Map;

/** Estado inicial: nenhum método cadastrado, logo ninguém retira bicicleta. */
public final class SemMetodoPagamento extends MetodoPagamento {
    private static final long serialVersionUID = 1L;

    @Override
    public String getNome() {
        return "nenhum";
    }

    @Override
    public Map<String, String> getAtributos() {
        return Map.of();
    }

    @Override
    public void autorizarRetirada(Parametros parametros) {
        throw new OperacaoNaoPermitidaException(Mensagens.METODO_NAO_CADASTRADO);
    }
}
