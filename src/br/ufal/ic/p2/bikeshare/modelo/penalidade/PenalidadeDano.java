package br.ufal.ic.p2.bikeshare.modelo.penalidade;

import java.math.BigDecimal;

/** Lançada pelo operador por dano à bicicleta. */
public final class PenalidadeDano extends Penalidade {
    private static final long serialVersionUID = 1L;
    public static final String TIPO = "dano";

    public PenalidadeDano(BigDecimal valor, String motivo) {
        super(valor, motivo);
    }

    @Override
    public String getTipo() {
        return TIPO;
    }
}
