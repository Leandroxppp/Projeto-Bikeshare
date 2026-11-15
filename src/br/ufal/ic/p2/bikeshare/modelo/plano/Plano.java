package br.ufal.ic.p2.bikeshare.modelo.plano;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;

/** Estratégia de tarifação: cada plano sabe calcular o valor de uma corrida pela duração. */
public abstract class Plano implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String nome;

    protected Plano(String nome) {
        this.nome = nome;
    }

    public final String getNome() {
        return nome;
    }

    /** Valor da corrida (sem o adicional da bicicleta elétrica). */
    public abstract BigDecimal calcularTarifa(long minutos);

    /** Mensalidade assumida ao aderir ao plano; vazio quando o plano não tem mensalidade. */
    public abstract Optional<BigDecimal> getMensalidadePadrao();

    @Override
    public final boolean equals(Object outro) {
        return outro instanceof Plano && nome.equals(((Plano) outro).nome);
    }

    @Override
    public final int hashCode() {
        return nome.hashCode();
    }
}
