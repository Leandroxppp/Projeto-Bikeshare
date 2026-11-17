package br.ufal.ic.p2.bikeshare.modelo.plano;

import br.ufal.ic.p2.bikeshare.config.Parametros;
import br.ufal.ic.p2.bikeshare.excecoes.DadoInvalidoException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;

import java.io.Serializable;
import java.util.LinkedHashMap;

/** Planos disponíveis. Por ora, os dois padrão do sistema: avulso e assinante. */
public final class CatalogoPlanos implements Serializable {
    private static final long serialVersionUID = 1L;

    private final LinkedHashMap<String, Plano> planos = new LinkedHashMap<>();
    private final Plano padrao;

    public CatalogoPlanos(Parametros parametros) {
        this.padrao = new PlanoAvulso(parametros.valorMinutoAvulso());
        registrar(padrao);
        registrar(new PlanoAssinante(parametros.franquiaAssinanteMinutos(),
                parametros.valorMinutoExcedenteAssinante(), parametros.mensalidadePadraoAssinante()));
    }

    /** Plano atribuído a todo usuário recém-cadastrado. */
    public Plano getPadrao() {
        return padrao;
    }

    public Plano exigir(String nome) {
        Plano plano = nome == null ? null : planos.get(nome);
        if (plano == null) {
            throw new DadoInvalidoException(Mensagens.PLANO_INVALIDO);
        }
        return plano;
    }

    private void registrar(Plano plano) {
        planos.put(plano.getNome(), plano);
    }
}
