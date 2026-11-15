package br.ufal.ic.p2.bikeshare.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Arredondamento e formatação monetária no padrão do sistema ("12,50"). */
public final class Dinheiro {
    public static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2);

    private Dinheiro() {
    }

    public static BigDecimal arredondar(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP);
    }

    public static String formatar(BigDecimal valor) {
        return arredondar(valor).toPlainString().replace('.', ',');
    }
}
