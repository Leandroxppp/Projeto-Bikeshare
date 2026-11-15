package br.ufal.ic.p2.bikeshare.modelo.bicicleta;

import br.ufal.ic.p2.bikeshare.excecoes.AtributoException;
import br.ufal.ic.p2.bikeshare.excecoes.DadoInvalidoException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.util.Entrada;

/** Cria a bicicleta do tipo pedido; o parâmetro de bateria é nulo quando o comando não o traz. */
public final class FabricaBicicletas {
    private FabricaBicicletas() {
    }

    public static Bicicleta criar(String id, String tipo, String nivelBateria) {
        if (BicicletaConvencional.TIPO.equals(tipo)) {
            if (nivelBateria != null) {
                throw new AtributoException(Mensagens.BICICLETA_SEM_BATERIA);
            }
            return new BicicletaConvencional(id);
        }
        if (BicicletaEletrica.TIPO.equals(tipo)) {
            int nivel = Entrada.inteiro(nivelBateria, Mensagens.BATERIA_NULA, Mensagens.BATERIA_NUMERICA);
            return new BicicletaEletrica(id, nivel);
        }
        throw new DadoInvalidoException(Mensagens.TIPO_INVALIDO);
    }
}
