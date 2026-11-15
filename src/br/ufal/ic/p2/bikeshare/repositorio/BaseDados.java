package br.ufal.ic.p2.bikeshare.repositorio;

import br.ufal.ic.p2.bikeshare.config.Parametros;
import br.ufal.ic.p2.bikeshare.modelo.plano.CatalogoPlanos;

import java.io.Serializable;

/**
 * Todo o estado do sistema num único grafo serializável. É ele que é gravado em disco no
 * encerramento e fotografado pelo histórico de desfazer/refazer.
 */
public final class BaseDados implements Serializable {
    private static final long serialVersionUID = 1L;

    private final RepositorioUsuarios usuarios = new RepositorioUsuarios();
    private final RepositorioEstacoes estacoes = new RepositorioEstacoes();
    private final RepositorioBicicletas bicicletas = new RepositorioBicicletas();
    private final RepositorioAlugueis alugueis = new RepositorioAlugueis();
    private final CatalogoPlanos planos;

    public BaseDados(Parametros parametros) {
        this.planos = new CatalogoPlanos(parametros);
    }

    public RepositorioUsuarios usuarios() {
        return usuarios;
    }

    public RepositorioEstacoes estacoes() {
        return estacoes;
    }

    public RepositorioBicicletas bicicletas() {
        return bicicletas;
    }

    public RepositorioAlugueis alugueis() {
        return alugueis;
    }

    public CatalogoPlanos planos() {
        return planos;
    }
}
