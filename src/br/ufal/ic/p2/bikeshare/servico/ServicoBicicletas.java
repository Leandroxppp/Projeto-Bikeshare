package br.ufal.ic.p2.bikeshare.servico;

import br.ufal.ic.p2.bikeshare.excecoes.EntidadeDuplicadaException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.excecoes.OperacaoNaoPermitidaException;
import br.ufal.ic.p2.bikeshare.modelo.bicicleta.Bicicleta;
import br.ufal.ic.p2.bikeshare.modelo.bicicleta.FabricaBicicletas;
import br.ufal.ic.p2.bikeshare.modelo.estacao.Estacao;
import br.ufal.ic.p2.bikeshare.repositorio.BaseDados;
import br.ufal.ic.p2.bikeshare.repositorio.ContextoSistema;
import br.ufal.ic.p2.bikeshare.util.Entrada;

/** Gestão de bicicletas (US6), estado (US7) e manutenção (US14). */
public final class ServicoBicicletas {
    private final ContextoSistema contexto;

    public ServicoBicicletas(ContextoSistema contexto) {
        this.contexto = contexto;
    }

    /** {@code nivelBateria} é nulo quando o comando não informa bateria. */
    public void adicionar(String estacaoId, String bicicletaId, String tipo, String nivelBateria) {
        BaseDados base = contexto.base();
        Estacao estacao = base.estacoes().exigir(estacaoId);
        Entrada.obrigatorio(bicicletaId, Mensagens.ID_BICICLETA_NULO);
        if (base.bicicletas().existe(bicicletaId)) {
            throw new EntidadeDuplicadaException(Mensagens.BICICLETA_JA_EXISTE);
        }
        Bicicleta bicicleta = FabricaBicicletas.criar(bicicletaId, tipo, nivelBateria);
        if (estacao.estaCheia()) {
            throw new OperacaoNaoPermitidaException(Mensagens.ESTACAO_SEM_VAGAS);
        }
        estacao.receber(bicicleta);
        base.bicicletas().adicionar(bicicleta);
    }

    public void remover(String estacaoId, String bicicletaId) {
        BaseDados base = contexto.base();
        Estacao estacao = base.estacoes().exigir(estacaoId);
        Bicicleta bicicleta = base.bicicletas().exigir(bicicletaId);
        if (!estacao.contem(bicicleta)) {
            throw new OperacaoNaoPermitidaException(Mensagens.BICICLETA_NAO_NESTA_ESTACAO);
        }
        bicicleta.exigirRemovivel();
        estacao.liberar(bicicleta);
        base.bicicletas().remover(bicicleta);
    }

    public String consultar(String bicicletaId, String atributo) {
        BaseDados base = contexto.base();
        Bicicleta bicicleta = base.bicicletas().exigir(bicicletaId);
        if ("estacao".equals(atributo)) {
            return base.estacoes().localizar(bicicleta).map(Estacao::getId).orElse("nenhuma");
        }
        return bicicleta.consultar(atributo);
    }

    public String getEstado(String bicicletaId) {
        return contexto.base().bicicletas().exigir(bicicletaId).getEstado().descricao();
    }

    public void lancarDefeito(String bicicletaId, String descricao) {
        Bicicleta bicicleta = contexto.base().bicicletas().exigir(bicicletaId);
        Entrada.obrigatorio(descricao, Mensagens.DESCRICAO_NULA);
        bicicleta.lancarDefeito();
    }

    public void concluirManutencao(String bicicletaId) {
        contexto.base().bicicletas().exigir(bicicletaId).concluirManutencao();
    }
}
