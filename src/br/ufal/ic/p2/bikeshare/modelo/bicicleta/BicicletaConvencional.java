package br.ufal.ic.p2.bikeshare.modelo.bicicleta;

import br.ufal.ic.p2.bikeshare.config.Parametros;
import br.ufal.ic.p2.bikeshare.excecoes.AtributoException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.util.Dinheiro;

import java.math.BigDecimal;

public final class BicicletaConvencional extends Bicicleta {
    private static final long serialVersionUID = 1L;
    public static final String TIPO = "convencional";

    public BicicletaConvencional(String id) {
        super(id);
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public boolean temBateriaSuficiente(Parametros parametros) {
        return true;
    }

    @Override
    public BigDecimal getAdicionalPorMinuto(Parametros parametros) {
        return Dinheiro.ZERO;
    }

    @Override
    protected String consultarAtributoEspecifico(String atributo) {
        if ("nivelBateria".equals(atributo)) {
            throw new AtributoException(Mensagens.BICICLETA_SEM_BATERIA);
        }
        throw atributoInexistente();
    }
}
