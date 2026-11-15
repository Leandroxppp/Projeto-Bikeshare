package br.ufal.ic.p2.bikeshare.modelo.penalidade;

import java.math.BigDecimal;

/** Aplicada automaticamente quando um aluguel expira (valor fixo configurado). */
public final class PenalidadeAtraso extends Penalidade {
    private static final long serialVersionUID = 1L;
    public static final String TIPO = "atraso";

    public PenalidadeAtraso(BigDecimal valor, String motivo) {
        super(valor, motivo);
    }

    @Override
    public String getTipo() {
        return TIPO;
    }
}
