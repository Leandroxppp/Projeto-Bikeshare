package br.ufal.ic.p2.bikeshare.repositorio;

import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.modelo.aluguel.Aluguel;

public final class RepositorioAlugueis extends RepositorioComSequencia<Aluguel> {
    private static final long serialVersionUID = 1L;

    public RepositorioAlugueis() {
        super("A", Mensagens.ID_ALUGUEL_NULO, Mensagens.ALUGUEL_NAO_EXISTE);
    }
}
