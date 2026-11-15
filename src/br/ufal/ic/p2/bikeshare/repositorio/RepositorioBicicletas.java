package br.ufal.ic.p2.bikeshare.repositorio;

import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.modelo.bicicleta.Bicicleta;

public final class RepositorioBicicletas extends Repositorio<Bicicleta> {
    private static final long serialVersionUID = 1L;

    public RepositorioBicicletas() {
        super(Mensagens.ID_BICICLETA_NULO, Mensagens.BICICLETA_NAO_EXISTE);
    }
}
