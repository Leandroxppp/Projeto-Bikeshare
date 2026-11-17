package br.ufal.ic.p2.bikeshare.modelo.aluguel;

import br.ufal.ic.p2.bikeshare.excecoes.AtributoException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.excecoes.OperacaoNaoPermitidaException;
import br.ufal.ic.p2.bikeshare.modelo.Identificavel;
import br.ufal.ic.p2.bikeshare.util.DataHora;
import br.ufal.ic.p2.bikeshare.util.Dinheiro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Corrida de um usuário com uma bicicleta. Referencia usuário, bicicleta e estações por
 * identificador: o aluguel continua consultável mesmo depois que o usuário é removido.
 */
public final class Aluguel implements Identificavel, Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String usuarioId;
    private final String bicicletaId;
    private final String estacaoOrigemId;
    private final LocalDateTime inicio;

    private EstadoAluguel estado = EstadoAluguel.EM_ANDAMENTO;
    private LocalDateTime fim;
    private String estacaoDestinoId;
    private BigDecimal valorCobrado = Dinheiro.ZERO;

    public Aluguel(String id, String usuarioId, String bicicletaId, String estacaoOrigemId, LocalDateTime inicio) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.bicicletaId = bicicletaId;
        this.estacaoOrigemId = estacaoOrigemId;
        this.inicio = inicio;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public String getBicicletaId() {
        return bicicletaId;
    }

    public String getEstacaoOrigemId() {
        return estacaoOrigemId;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public EstadoAluguel getEstado() {
        return estado;
    }

    public BigDecimal getValorCobrado() {
        return valorCobrado;
    }

    public void exigirEmAndamento() {
        if (estado != EstadoAluguel.EM_ANDAMENTO) {
            throw new OperacaoNaoPermitidaException(Mensagens.ALUGUEL_NAO_EM_ANDAMENTO);
        }
    }

    public long minutosAte(LocalDateTime momento) {
        return Duration.between(inicio, momento).toMinutes();
    }

    public void finalizar(LocalDateTime momento, String estacaoDestino, BigDecimal valor) {
        concluir(EstadoAluguel.FINALIZADO, momento, estacaoDestino, valor);
    }

    /** Devolução na mesma estação dentro da carência: nada é cobrado. */
    public void cancelar(LocalDateTime momento, String estacaoDestino) {
        concluir(EstadoAluguel.CANCELADO, momento, estacaoDestino, Dinheiro.ZERO);
    }

    /** Tempo máximo excedido: sem tarifa (a penalidade é lançada no usuário). */
    public void expirar(LocalDateTime momento, String estacaoDestino) {
        concluir(EstadoAluguel.EXPIRADO, momento, estacaoDestino, Dinheiro.ZERO);
    }

    private void concluir(EstadoAluguel novoEstado, LocalDateTime momento, String estacaoDestino, BigDecimal valor) {
        exigirEmAndamento();
        this.estado = novoEstado;
        this.fim = momento;
        this.estacaoDestinoId = estacaoDestino;
        this.valorCobrado = Dinheiro.arredondar(valor);
    }

    public String consultar(String atributo) {
        if (atributo == null) {
            throw new AtributoException(Mensagens.ATRIBUTO_NAO_EXISTE);
        }
        switch (atributo) {
            case "estado":
                return estado.descricao();
            case "usuario":
                return usuarioId;
            case "bicicleta":
                return bicicletaId;
            case "estacaoOrigem":
                return estacaoOrigemId;
            case "inicio":
                return DataHora.formatar(inicio);
            case "fim":
                exigirConcluido();
                return DataHora.formatar(fim);
            case "estacaoDestino":
                exigirConcluido();
                return estacaoDestinoId;
            case "valorCobrado":
                exigirConcluido();
                return Dinheiro.formatar(valorCobrado);
            default:
                throw new AtributoException(Mensagens.ATRIBUTO_NAO_EXISTE);
        }
    }

    private void exigirConcluido() {
        if (!estado.isConcluido()) {
            throw new AtributoException(Mensagens.ALUGUEL_EM_ANDAMENTO);
        }
    }
}
