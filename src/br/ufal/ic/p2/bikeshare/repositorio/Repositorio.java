package br.ufal.ic.p2.bikeshare.repositorio;

import br.ufal.ic.p2.bikeshare.excecoes.EntidadeNaoExisteException;
import br.ufal.ic.p2.bikeshare.modelo.Identificavel;
import br.ufal.ic.p2.bikeshare.util.Entrada;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

/** Coleção indexada por identificador, com as mensagens de erro próprias de cada entidade. */
public abstract class Repositorio<T extends Identificavel> implements Serializable {
    private static final long serialVersionUID = 1L;

    private final LinkedHashMap<String, T> itens = new LinkedHashMap<>();
    private final String mensagemIdNulo;
    private final String mensagemNaoExiste;

    protected Repositorio(String mensagemIdNulo, String mensagemNaoExiste) {
        this.mensagemIdNulo = mensagemIdNulo;
        this.mensagemNaoExiste = mensagemNaoExiste;
    }

    /** Valida o identificador e devolve a entidade, ou lança conforme o caso. */
    public final T exigir(String id) {
        Entrada.obrigatorio(id, mensagemIdNulo);
        T item = itens.get(id);
        if (item == null) {
            throw new EntidadeNaoExisteException(mensagemNaoExiste);
        }
        return item;
    }

    public final boolean existe(String id) {
        return id != null && itens.containsKey(id);
    }

    public void adicionar(T item) {
        itens.put(item.getId(), item);
    }

    public final void remover(T item) {
        itens.remove(item.getId());
    }

    /** Cópia da lista, para que quem itera não consiga alterar o repositório. */
    public final List<T> listar() {
        return new ArrayList<>(itens.values());
    }

    protected final Collection<T> valores() {
        return itens.values();
    }
}
