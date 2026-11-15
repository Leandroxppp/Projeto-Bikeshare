package br.ufal.ic.p2.bikeshare.modelo.plano;

import java.math.BigDecimal;

/** Plano de assinatura padrão do sistema. */
public final class PlanoAssinante extends PlanoComFranquia {
    private static final long serialVersionUID = 1L;
    public static final String NOME = "assinante";

    public PlanoAssinante(long franquiaMinutos, BigDecimal valorMinutoExcedente, BigDecimal mensalidadePadrao) {
        super(NOME, franquiaMinutos, valorMinutoExcedente, mensalidadePadrao);
    }
}
