package br.ufal.ic.p2.bikeshare.repositorio;

import java.io.Serializable;

/**
 * Sequência de identificadores (U1, U2, ...). Faz parte do estado: nunca reaproveita número.
 * O próximo id pode ser consultado sem ser gasto; só {@link #consumir()} avança a sequência,
 * o que permite recusar um cadastro sem deixar buraco na numeração.
 */
public final class GeradorIds implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String prefixo;
    private long ultimo;

    public GeradorIds(String prefixo) {
        this.prefixo = prefixo;
    }

    public String proximoDisponivel() {
        return prefixo + (ultimo + 1);
    }

    public void consumir() {
        ultimo++;
    }
}
