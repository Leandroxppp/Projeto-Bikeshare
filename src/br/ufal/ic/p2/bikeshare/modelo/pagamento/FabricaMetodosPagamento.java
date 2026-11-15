package br.ufal.ic.p2.bikeshare.modelo.pagamento;

import br.ufal.ic.p2.bikeshare.excecoes.DadoInvalidoException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;

/**
 * Cria o método de pagamento pedido. A forma do comando (quantos parâmetros acompanham o tipo)
 * faz parte da validação: cartão pede 2, pix pede 1 e saldo pré-carregado nenhum.
 */
public final class FabricaMetodosPagamento {
    private FabricaMetodosPagamento() {
    }

    public static MetodoPagamento criar(String tipo, SaldoPreCarregado carteira, String... parametros) {
        if (tipo != null) {
            switch (tipo) {
                case "cartao":
                    if (parametros.length == 2) {
                        return new Cartao(parametros[0], parametros[1]);
                    }
                    break;
                case "pix":
                    if (parametros.length == 1) {
                        return new Pix(parametros[0]);
                    }
                    break;
                case "saldoPreCarregado":
                    if (parametros.length == 0) {
                        return carteira;
                    }
                    break;
                default:
                    break;
            }
        }
        throw new DadoInvalidoException(Mensagens.METODO_PAGAMENTO_INVALIDO);
    }
}
