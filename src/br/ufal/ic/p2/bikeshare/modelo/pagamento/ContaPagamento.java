package br.ufal.ic.p2.bikeshare.modelo.pagamento;

import br.ufal.ic.p2.bikeshare.config.Parametros;
import br.ufal.ic.p2.bikeshare.excecoes.AtributoException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Tudo o que envolve pagar: o método escolhido e a carteira de saldo. A carteira é permanente,
 * então o saldo sobrevive quando o usuário troca para cartão ou pix e depois volta.
 */
public final class ContaPagamento implements Serializable {
    private static final long serialVersionUID = 1L;

    private final SaldoPreCarregado carteira = new SaldoPreCarregado();
    private MetodoPagamento metodo = new SemMetodoPagamento();

    public void alterarMetodo(String tipo, String... parametros) {
        this.metodo = FabricaMetodosPagamento.criar(tipo, carteira, parametros);
    }

    public String getNomeDoMetodo() {
        return metodo.getNome();
    }

    public boolean aceitaRecarga() {
        return metodo.aceitaRecarga();
    }

    public void recarregar(BigDecimal valor) {
        if (!aceitaRecarga()) {
            throw new AtributoException(Mensagens.USUARIO_NAO_USA_SALDO);
        }
        carteira.creditar(valor);
    }

    public void autorizarRetirada(Parametros parametros) {
        metodo.autorizarRetirada(parametros);
    }

    public void cobrar(BigDecimal valor) {
        metodo.cobrar(valor);
    }

    /** Valor de um atributo que só existe para certos métodos (cartão, pix, saldo). */
    public String consultar(AtributoPagamento atributo) {
        String valor = metodo.getAtributos().get(atributo.chave());
        if (valor == null) {
            throw new AtributoException(atributo.mensagemQuandoNaoUsa());
        }
        return valor;
    }
}
