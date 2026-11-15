package br.ufal.ic.p2.bikeshare.modelo.penalidade;

import java.math.BigDecimal;

/** Lançada pelo operador por uso indevido do serviço. */
public final class PenalidadeUsoIndevido extends Penalidade {
    private static final long serialVersionUID = 1L;
    public static final String TIPO = "usoIndevido";

    public PenalidadeUsoIndevido(BigDecimal valor, String motivo) {
        super(valor, motivo);
    }

    @Override
    public String getTipo() {
        return TIPO;
    }
}
