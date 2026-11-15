package br.ufal.ic.p2.bikeshare.modelo.penalidade;

import br.ufal.ic.p2.bikeshare.excecoes.DadoInvalidoException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;

import java.math.BigDecimal;

/** Penalidades que o operador pode lançar manualmente (a de atraso é sempre automática). */
public final class FabricaPenalidades {
    /** Construtor de penalidade já resolvido pelo tipo, à espera de valor e motivo. */
    @FunctionalInterface
    public interface Criador {
        Penalidade criar(BigDecimal valor, String motivo);
    }

    private FabricaPenalidades() {
    }

    public static Criador paraTipo(String tipo) {
        if (PenalidadeDano.TIPO.equals(tipo)) {
            return PenalidadeDano::new;
        }
        if (PenalidadeUsoIndevido.TIPO.equals(tipo)) {
            return PenalidadeUsoIndevido::new;
        }
        throw new DadoInvalidoException(Mensagens.TIPO_PENALIDADE_INVALIDO);
    }
}
