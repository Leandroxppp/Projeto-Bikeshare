package br.ufal.ic.p2.bikeshare.modelo.pagamento;

import br.ufal.ic.p2.bikeshare.config.Parametros;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Forma de pagamento do usuário. Os ganchos têm comportamento padrão neutro (cartão e pix são
 * cobrados fora do sistema); quem restringe a retirada ou controla saldo os sobrescreve.
 */
public abstract class MetodoPagamento implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Nome exposto no atributo metodoPagamento. */
    public abstract String getNome();

    /** Atributos consultáveis próprios deste método, por chave. */
    public abstract Map<String, String> getAtributos();

    /** Valida se o usuário pode iniciar uma corrida com este método. */
    public void autorizarRetirada(Parametros parametros) {
    }

    /** Efetiva a cobrança de uma corrida finalizada. */
    public void cobrar(BigDecimal valor) {
    }

    public boolean aceitaRecarga() {
        return false;
    }
}
