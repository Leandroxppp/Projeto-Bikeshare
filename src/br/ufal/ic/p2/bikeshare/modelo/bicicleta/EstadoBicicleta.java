package br.ufal.ic.p2.bikeshare.modelo.bicicleta;

import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.excecoes.OperacaoNaoPermitidaException;

/**
 * Padrão State: cada estado declara explicitamente o que aceita e o que recusa (o compilador
 * obriga cada constante a tratar todas as transições). Removida não é estado: a bicicleta
 * simplesmente deixa de existir.
 */
public enum EstadoBicicleta {
    DISPONIVEL("disponivel") {
        @Override
        public EstadoBicicleta aoRetirar() {
            return EM_USO;
        }

        @Override
        public EstadoBicicleta aoDevolver() {
            throw indisponivel();
        }

        @Override
        public EstadoBicicleta aoDevolverComExpiracao() {
            throw indisponivel();
        }

        @Override
        public void exigirDisponivel() {
            // é exatamente o estado exigido
        }
    },
    EM_USO("emUso") {
        @Override
        public EstadoBicicleta aoRetirar() {
            throw indisponivel();
        }

        @Override
        public EstadoBicicleta aoDevolver() {
            return DISPONIVEL;
        }

        @Override
        public EstadoBicicleta aoDevolverComExpiracao() {
            return EM_MANUTENCAO;
        }

        @Override
        public void exigirDisponivel() {
            throw indisponivel();
        }
    },
    EM_MANUTENCAO("emManutencao") {
        @Override
        public EstadoBicicleta aoRetirar() {
            throw indisponivel();
        }

        @Override
        public EstadoBicicleta aoDevolver() {
            throw indisponivel();
        }

        @Override
        public EstadoBicicleta aoDevolverComExpiracao() {
            throw indisponivel();
        }

        @Override
        public void exigirDisponivel() {
            throw indisponivel();
        }
    };

    private final String descricao;

    EstadoBicicleta(String descricao) {
        this.descricao = descricao;
    }

    public String descricao() {
        return descricao;
    }

    /** Estado seguinte quando um usuário retira a bicicleta. */
    public abstract EstadoBicicleta aoRetirar();

    /** Estado seguinte numa devolução normal. */
    public abstract EstadoBicicleta aoDevolver();

    /** Estado seguinte numa devolução fora do tempo máximo (a bicicleta vai para manutenção). */
    public abstract EstadoBicicleta aoDevolverComExpiracao();

    /** Falha se a bicicleta não puder ser retirada nem removida neste estado. */
    public abstract void exigirDisponivel();

    private static OperacaoNaoPermitidaException indisponivel() {
        return new OperacaoNaoPermitidaException(Mensagens.BICICLETA_INDISPONIVEL);
    }
}
