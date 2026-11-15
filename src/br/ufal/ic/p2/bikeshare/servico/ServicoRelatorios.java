package br.ufal.ic.p2.bikeshare.servico;

import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.excecoes.OperacaoNaoPermitidaException;
import br.ufal.ic.p2.bikeshare.modelo.aluguel.Aluguel;
import br.ufal.ic.p2.bikeshare.repositorio.ContextoSistema;
import br.ufal.ic.p2.bikeshare.util.DataHora;
import br.ufal.ic.p2.bikeshare.util.Dinheiro;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Relatório de faturamento (US15); a disponibilidade vem de {@link ServicoEstacoes}. */
public final class ServicoRelatorios {
    private final ContextoSistema contexto;

    public ServicoRelatorios(ContextoSistema contexto) {
        this.contexto = contexto;
    }

    /** Soma o valor das corridas finalizadas cuja devolução cai no período (datas inclusivas). */
    public String totalFaturado(String dataInicial, String dataFinal) {
        LocalDate inicio = DataHora.lerData(dataInicial);
        LocalDate fim = DataHora.lerData(dataFinal);
        if (fim.isBefore(inicio)) {
            throw new OperacaoNaoPermitidaException(Mensagens.DATA_FINAL_ANTERIOR);
        }
        BigDecimal total = Dinheiro.ZERO;
        for (Aluguel aluguel : contexto.base().alugueis().listar()) {
            total = total.add(aluguel.valorFaturadoEntre(inicio, fim));
        }
        return Dinheiro.formatar(total);
    }
}
