package br.ufal.ic.p2.bikeshare.repositorio;

import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.modelo.bicicleta.Bicicleta;
import br.ufal.ic.p2.bikeshare.modelo.estacao.Estacao;

import java.util.Optional;

public final class RepositorioEstacoes extends RepositorioComSequencia<Estacao> {
    private static final long serialVersionUID = 1L;

    public RepositorioEstacoes() {
        super("E", Mensagens.ID_ESTACAO_NULO, Mensagens.ESTACAO_NAO_EXISTE);
    }

    /** Estação onde a bicicleta está agora; vazio se estiver em uso (fora de qualquer estação). */
    public Optional<Estacao> localizar(Bicicleta bicicleta) {
        for (Estacao e : valores()) {
            if (e.contem(bicicleta)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }
}
