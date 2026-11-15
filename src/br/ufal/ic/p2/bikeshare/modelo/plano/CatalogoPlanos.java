package br.ufal.ic.p2.bikeshare.modelo.plano;

import br.ufal.ic.p2.bikeshare.config.Parametros;
import br.ufal.ic.p2.bikeshare.excecoes.DadoInvalidoException;
import br.ufal.ic.p2.bikeshare.excecoes.EntidadeDuplicadaException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;

import java.io.Serializable;
import java.util.LinkedHashMap;

/** Planos disponíveis: os dois padrão (avulso, assinante) e os criados em tempo de execução. */
public final class CatalogoPlanos implements Serializable {
    private static final long serialVersionUID = 1L;

    private final LinkedHashMap<String, Plano> planos = new LinkedHashMap<>();
    private final Plano padrao;

    public CatalogoPlanos(Parametros parametros) {
        this.padrao = new PlanoAvulso(parametros.valorMinutoAvulso());
        registrarInterno(padrao);
        registrarInterno(new PlanoAssinante(parametros.franquiaAssinanteMinutos(),
                parametros.valorMinutoExcedenteAssinante(), parametros.mensalidadePadraoAssinante()));
    }

    /** Plano atribuído a todo usuário recém-cadastrado. */
    public Plano getPadrao() {
        return padrao;
    }

    public boolean existe(String nome) {
        return nome != null && planos.containsKey(nome);
    }

    public Plano exigir(String nome) {
        if (!existe(nome)) {
            throw new DadoInvalidoException(Mensagens.PLANO_INVALIDO);
        }
        return planos.get(nome);
    }

    public void registrar(Plano plano) {
        if (existe(plano.getNome())) {
            throw new EntidadeDuplicadaException(Mensagens.PLANO_JA_EXISTE);
        }
        registrarInterno(plano);
    }

    private void registrarInterno(Plano plano) {
        planos.put(plano.getNome(), plano);
    }
}
