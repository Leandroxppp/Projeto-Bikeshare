package br.ufal.ic.p2.bikeshare.repositorio;

import br.ufal.ic.p2.bikeshare.config.Parametros;

/**
 * Ponto único de acesso ao estado corrente. Desfazer, refazer e zerar trocam a {@link BaseDados}
 * inteira; por isso os serviços guardam o contexto, nunca a base.
 */
public final class ContextoSistema {
    private final Parametros parametros;
    private BaseDados base;

    public ContextoSistema(Parametros parametros, BaseDados base) {
        this.parametros = parametros;
        this.base = base;
    }

    public Parametros parametros() {
        return parametros;
    }

    public BaseDados base() {
        return base;
    }

    public void substituirBase(BaseDados novaBase) {
        this.base = novaBase;
    }
}
