package br.ufal.ic.p2.bikeshare.modelo.pagamento;

import br.ufal.ic.p2.bikeshare.config.Parametros;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.excecoes.OperacaoNaoPermitidaException;
import br.ufal.ic.p2.bikeshare.util.Dinheiro;

import java.math.BigDecimal;
import java.util.Map;

/** Carteira com crédito. O saldo pertence ao usuário e sobrevive à troca de método. */
public final class SaldoPreCarregado extends MetodoPagamento {
    private static final long serialVersionUID = 1L;

    private BigDecimal saldo = Dinheiro.ZERO;

    @Override
    public String getNome() {
        return "saldoPreCarregado";
    }

    @Override
    public Map<String, String> getAtributos() {
        return Map.of(AtributoPagamento.SALDO.chave(), Dinheiro.formatar(saldo));
    }

    @Override
    public void autorizarRetirada(Parametros parametros) {
        if (saldo.compareTo(parametros.saldoMinimoParaRetirada()) < 0) {
            throw new OperacaoNaoPermitidaException(Mensagens.SALDO_INSUFICIENTE);
        }
    }

    @Override
    public void cobrar(BigDecimal valor) {
        if (saldo.compareTo(valor) < 0) {
            throw new OperacaoNaoPermitidaException(Mensagens.SALDO_INSUFICIENTE);
        }
        saldo = Dinheiro.arredondar(saldo.subtract(valor));
    }

    @Override
    public boolean aceitaRecarga() {
        return true;
    }

    public void creditar(BigDecimal valor) {
        saldo = Dinheiro.arredondar(saldo.add(valor));
    }
}
