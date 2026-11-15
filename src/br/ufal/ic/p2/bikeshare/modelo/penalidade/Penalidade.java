package br.ufal.ic.p2.bikeshare.modelo.penalidade;

import java.io.Serializable;
import java.math.BigDecimal;

/** Cobrança adicional lançada sobre um usuário; enquanto existir, ele fica inadimplente. */
public abstract class Penalidade implements Serializable {
    private static final long serialVersionUID = 1L;

    private final BigDecimal valor;
    private final String motivo;

    protected Penalidade(BigDecimal valor, String motivo) {
        this.valor = valor;
        this.motivo = motivo;
    }

    public final BigDecimal getValor() {
        return valor;
    }

    public final String getMotivo() {
        return motivo;
    }

    public abstract String getTipo();
}
