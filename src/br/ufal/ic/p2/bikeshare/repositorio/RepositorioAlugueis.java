package br.ufal.ic.p2.bikeshare.repositorio;

import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.modelo.aluguel.Aluguel;

import java.util.List;
import java.util.stream.Collectors;

public final class RepositorioAlugueis extends Repositorio<Aluguel> {
    private static final long serialVersionUID = 1L;

    private final GeradorIds ids = new GeradorIds("A");

    public RepositorioAlugueis() {
        super(Mensagens.ID_ALUGUEL_NULO, Mensagens.ALUGUEL_NAO_EXISTE);
    }

    public String proximoId() {
        return ids.proximo();
    }

    public List<Aluguel> doUsuario(String usuarioId) {
        return valores().stream().filter(a -> a.getUsuarioId().equals(usuarioId)).collect(Collectors.toList());
    }
}
