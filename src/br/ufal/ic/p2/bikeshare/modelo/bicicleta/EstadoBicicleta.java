package br.ufal.ic.p2.bikeshare.modelo.bicicleta;

import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.excecoes.OperacaoNaoPermitidaException;

/**
 * Padrão State: cada estado conhece as transições que aceita e recusa as demais com a mensagem
 * apropriada. Removida não é estado: a bicicleta simplesmente deixa de existir.
 */
public enum EstadoBicicleta {
    DISPONIVEL("disponivel") {
        @Override
        public EstadoBicicleta aoRetirar() {
            return EM_USO;
        }

        @Override
        public EstadoBicicleta aoLancarDefeito() {
            return EM_MANUTENCAO;
        }

        @Override
        public void exigirRemovivel() {
            // bicicleta disponível pode ser removida
        }
    },
    EM_USO("emUso") {
        @Override
        public EstadoBicicleta aoDevolver() {
            return DISPONIVEL;
        }

        @Override
        public EstadoBicicleta aoDevolverComExpiracao() {
            return EM_MANUTENCAO;
        }

        @Override
        public EstadoBicicleta aoLancarDefeito() {
            throw new OperacaoNaoPermitidaException(Mensagens.DEFEITO_EM_BICICLETA_EM_USO);
        }
    },
    EM_MANUTENCAO("emManutencao") {
        @Override
        public EstadoBicicleta aoLancarDefeito() {
            throw new OperacaoNaoPermitidaException(Mensagens.BICICLETA_JA_EM_MANUTENCAO);
        }

        @Override
        public EstadoBicicleta aoConcluirManutencao() {
            return DISPONIVEL;
        }
    };

    private final String descricao;

    EstadoBicicleta(String descricao) {
        this.descricao = descricao;
    }

    public String descricao() {
        return descricao;
    }

    public EstadoBicicleta aoRetirar() {
        throw new OperacaoNaoPermitidaException(Mensagens.BICICLETA_INDISPONIVEL);
    }

    public EstadoBicicleta aoDevolver() {
        throw new OperacaoNaoPermitidaException(Mensagens.BICICLETA_INDISPONIVEL);
    }

    public EstadoBicicleta aoDevolverComExpiracao() {
        throw new OperacaoNaoPermitidaException(Mensagens.BICICLETA_INDISPONIVEL);
    }

    public EstadoBicicleta aoLancarDefeito() {
        throw new OperacaoNaoPermitidaException(Mensagens.BICICLETA_INDISPONIVEL);
    }

    public EstadoBicicleta aoConcluirManutencao() {
        throw new OperacaoNaoPermitidaException(Mensagens.BICICLETA_NAO_EM_MANUTENCAO);
    }

    public void exigirRemovivel() {
        throw new OperacaoNaoPermitidaException(Mensagens.BICICLETA_INDISPONIVEL);
    }
}
