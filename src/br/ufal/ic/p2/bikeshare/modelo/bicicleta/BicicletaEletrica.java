package br.ufal.ic.p2.bikeshare.modelo.bicicleta;

import br.ufal.ic.p2.bikeshare.config.Parametros;
import br.ufal.ic.p2.bikeshare.excecoes.DadoInvalidoException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;

import java.math.BigDecimal;

public final class BicicletaEletrica extends Bicicleta {
    private static final long serialVersionUID = 1L;
    public static final String TIPO = "eletrica";

    private final int nivelBateria;

    public BicicletaEletrica(String id, int nivelBateria) {
        super(id);
        if (nivelBateria < 0 || nivelBateria > 100) {
            throw new DadoInvalidoException(Mensagens.BATERIA_FAIXA);
        }
        this.nivelBateria = nivelBateria;
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public boolean temBateriaSuficiente(Parametros parametros) {
        return nivelBateria >= parametros.bateriaMinimaPercentual();
    }

    @Override
    public BigDecimal getAdicionalPorMinuto(Parametros parametros) {
        return parametros.adicionalMinutoEletrica();
    }

    @Override
    protected String consultarAtributoEspecifico(String atributo) {
        if ("nivelBateria".equals(atributo)) {
            return String.valueOf(nivelBateria);
        }
        throw atributoInexistente();
    }
}
