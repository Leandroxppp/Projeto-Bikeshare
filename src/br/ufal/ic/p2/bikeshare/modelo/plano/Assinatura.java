package br.ufal.ic.p2.bikeshare.modelo.plano;

import br.ufal.ic.p2.bikeshare.util.Dinheiro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;

/** Plano em vigor para um usuário, com a mensalidade que ele contratou (se o plano tiver). */
public final class Assinatura implements Serializable {
    private static final long serialVersionUID = 1L;

    private Plano plano;
    private BigDecimal mensalidade; // nulo enquanto o plano não tem mensalidade

    public Assinatura(Plano planoInicial) {
        this.plano = planoInicial;
    }

    /** Adesão com a mensalidade escolhida (nula para planos sem mensalidade). */
    public void contratar(Plano novoPlano, BigDecimal mensalidadeContratada) {
        this.plano = novoPlano;
        this.mensalidade = mensalidadeContratada == null ? null : Dinheiro.arredondar(mensalidadeContratada);
    }

    /** Troca de plano; repetir o plano atual preserva a mensalidade já contratada. */
    public void trocarPara(Plano novoPlano) {
        if (!novoPlano.equals(plano)) {
            contratar(novoPlano, novoPlano.getMensalidadePadrao().orElse(null));
        }
    }

    public String getNomePlano() {
        return plano.getNome();
    }

    public Optional<BigDecimal> getMensalidade() {
        return Optional.ofNullable(mensalidade);
    }

    public BigDecimal calcularTarifa(long minutos) {
        return plano.calcularTarifa(minutos);
    }
}
