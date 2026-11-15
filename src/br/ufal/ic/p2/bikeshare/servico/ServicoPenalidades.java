package br.ufal.ic.p2.bikeshare.servico;

import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.modelo.penalidade.FabricaPenalidades;
import br.ufal.ic.p2.bikeshare.modelo.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.repositorio.ContextoSistema;
import br.ufal.ic.p2.bikeshare.util.Entrada;

import java.math.BigDecimal;

/** Lançamento manual de penalidades (US13). */
public final class ServicoPenalidades {
    private final ContextoSistema contexto;

    public ServicoPenalidades(ContextoSistema contexto) {
        this.contexto = contexto;
    }

    public void lancar(String usuarioId, String tipo, String valor, String motivo) {
        Usuario usuario = contexto.base().usuarios().exigir(usuarioId);
        FabricaPenalidades.Criador criador = FabricaPenalidades.paraTipo(tipo);
        BigDecimal valorPenalidade = Entrada.decimalPositivo(valor, Mensagens.VALOR_NULO, Mensagens.VALOR_NUMERICO,
                Mensagens.VALOR_POSITIVO);
        Entrada.obrigatorio(motivo, Mensagens.MOTIVO_NULO);
        usuario.adicionarPenalidade(criador.criar(valorPenalidade, motivo));
    }
}
