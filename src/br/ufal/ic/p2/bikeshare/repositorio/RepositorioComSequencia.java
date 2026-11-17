package br.ufal.ic.p2.bikeshare.repositorio;

import br.ufal.ic.p2.bikeshare.modelo.Identificavel;

/**
 * Repositório de entidades cujo identificador é gerado pelo sistema. O número só é gasto quando
 * a entidade é de fato adicionada, então uma criação recusada não consome identificador.
 */
public abstract class RepositorioComSequencia<T extends Identificavel> extends Repositorio<T> {
    private static final long serialVersionUID = 1L;

    private final GeradorIds ids;

    protected RepositorioComSequencia(String prefixo, String mensagemIdNulo, String mensagemNaoExiste) {
        super(mensagemIdNulo, mensagemNaoExiste);
        this.ids = new GeradorIds(prefixo);
    }

    public final String proximoId() {
        return ids.proximoDisponivel();
    }

    @Override
    public void adicionar(T item) {
        super.adicionar(item);
        ids.consumir();
    }
}
