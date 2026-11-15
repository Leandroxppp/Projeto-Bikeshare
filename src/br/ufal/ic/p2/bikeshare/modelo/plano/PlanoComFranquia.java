package br.ufal.ic.p2.bikeshare.modelo.plano;

import java.math.BigDecimal;
import java.util.Optional;

/** Base dos planos com mensalidade: minutos gratuitos (franquia) e preço do minuto excedente. */
public abstract class PlanoComFranquia extends Plano {
    private static final long serialVersionUID = 1L;

    private final long franquiaMinutos;
    private final BigDecimal valorMinutoExcedente;
    private final BigDecimal mensalidade;

    protected PlanoComFranquia(String nome, long franquiaMinutos, BigDecimal valorMinutoExcedente,
                               BigDecimal mensalidade) {
        super(nome);
        this.franquiaMinutos = franquiaMinutos;
        this.valorMinutoExcedente = valorMinutoExcedente;
        this.mensalidade = mensalidade;
    }

    @Override
    public BigDecimal calcularTarifa(long minutos) {
        long excedente = Math.max(0, minutos - franquiaMinutos);
        return valorMinutoExcedente.multiply(BigDecimal.valueOf(excedente));
    }

    @Override
    public Optional<BigDecimal> getMensalidadePadrao() {
        return Optional.of(mensalidade);
    }
}
