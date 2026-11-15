package br.ufal.ic.p2.bikeshare.modelo.plano;

import java.math.BigDecimal;
import java.util.Optional;

/** Paga-se por minuto de cada corrida, sem franquia e sem mensalidade. */
public final class PlanoAvulso extends Plano {
    private static final long serialVersionUID = 1L;
    public static final String NOME = "avulso";

    private final BigDecimal valorMinuto;

    public PlanoAvulso(BigDecimal valorMinuto) {
        super(NOME);
        this.valorMinuto = valorMinuto;
    }

    @Override
    public BigDecimal calcularTarifa(long minutos) {
        return valorMinuto.multiply(BigDecimal.valueOf(minutos));
    }

    @Override
    public Optional<BigDecimal> getMensalidadePadrao() {
        return Optional.empty();
    }
}
