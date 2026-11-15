package br.ufal.ic.p2.bikeshare.modelo.plano;

import java.math.BigDecimal;

/** Plano criado pelo operador em tempo de execução. */
public final class PlanoCustomizado extends PlanoComFranquia {
    private static final long serialVersionUID = 1L;

    public PlanoCustomizado(String nome, long franquiaMinutos, BigDecimal valorMinutoExcedente,
                            BigDecimal mensalidade) {
        super(nome, franquiaMinutos, valorMinutoExcedente, mensalidade);
    }
}
