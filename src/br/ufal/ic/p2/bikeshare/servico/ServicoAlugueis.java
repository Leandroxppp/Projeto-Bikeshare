package br.ufal.ic.p2.bikeshare.servico;

import br.ufal.ic.p2.bikeshare.config.Parametros;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.excecoes.OperacaoNaoPermitidaException;
import br.ufal.ic.p2.bikeshare.modelo.aluguel.Aluguel;
import br.ufal.ic.p2.bikeshare.modelo.bicicleta.Bicicleta;
import br.ufal.ic.p2.bikeshare.modelo.estacao.Estacao;
import br.ufal.ic.p2.bikeshare.modelo.penalidade.PenalidadeAtraso;
import br.ufal.ic.p2.bikeshare.modelo.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.repositorio.BaseDados;
import br.ufal.ic.p2.bikeshare.repositorio.ContextoSistema;
import br.ufal.ic.p2.bikeshare.util.DataHora;
import br.ufal.ic.p2.bikeshare.util.Dinheiro;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Retirada (US8) e devolução com tarifação (US9); consulta de atributos do aluguel. */
public final class ServicoAlugueis {
    private final ContextoSistema contexto;

    public ServicoAlugueis(ContextoSistema contexto) {
        this.contexto = contexto;
    }

    public String retirar(String usuarioId, String estacaoId, String bicicletaId, String data) {
        BaseDados base = contexto.base();
        Parametros parametros = contexto.parametros();

        Usuario usuario = base.usuarios().exigir(usuarioId);
        Estacao estacao = base.estacoes().exigir(estacaoId);
        Bicicleta bicicleta = base.bicicletas().exigir(bicicletaId);
        LocalDateTime inicio = DataHora.lerDataHora(data);

        usuario.exigirApto();
        if (!estacao.contem(bicicleta)) {
            throw new OperacaoNaoPermitidaException(Mensagens.BICICLETA_NAO_NESTA_ESTACAO);
        }
        bicicleta.exigirDisponivel();
        if (!bicicleta.temBateriaSuficiente(parametros)) {
            throw new OperacaoNaoPermitidaException(Mensagens.BATERIA_INSUFICIENTE);
        }
        usuario.autorizarRetirada(parametros);

        Aluguel aluguel = new Aluguel(base.alugueis().proximoId(), usuario.getId(), bicicleta.getId(),
                estacao.getId(), inicio);
        estacao.liberar(bicicleta);
        bicicleta.retirar();
        base.alugueis().adicionar(aluguel);
        usuario.iniciarAluguel(aluguel.getId());
        return aluguel.getId();
    }

    public void devolver(String aluguelId, String estacaoId, String data) {
        BaseDados base = contexto.base();
        Parametros parametros = contexto.parametros();

        Aluguel aluguel = base.alugueis().exigir(aluguelId);
        Estacao destino = base.estacoes().exigir(estacaoId);
        LocalDateTime fim = DataHora.lerDataHora(data);

        aluguel.exigirEmAndamento();
        if (fim.isBefore(aluguel.getInicio())) {
            throw new OperacaoNaoPermitidaException(Mensagens.DEVOLUCAO_ANTERIOR_A_RETIRADA);
        }
        if (destino.estaCheia()) {
            throw new OperacaoNaoPermitidaException(Mensagens.ESTACAO_CHEIA);
        }

        Usuario usuario = base.usuarios().exigir(aluguel.getUsuarioId());
        Bicicleta bicicleta = base.bicicletas().exigir(aluguel.getBicicletaId());
        long minutos = aluguel.minutosAte(fim);

        if (minutos > parametros.tempoMaximoMinutos()) {
            expirar(aluguel, usuario, bicicleta, destino, fim, parametros);
        } else if (destino.getId().equals(aluguel.getEstacaoOrigemId()) && minutos <= parametros.carenciaMinutos()) {
            cancelar(aluguel, usuario, bicicleta, destino, fim);
        } else {
            finalizar(aluguel, usuario, bicicleta, destino, fim, minutos, parametros);
        }
    }

    private void finalizar(Aluguel aluguel, Usuario usuario, Bicicleta bicicleta, Estacao destino,
                           LocalDateTime fim, long minutos, Parametros parametros) {
        BigDecimal adicional = bicicleta.getAdicionalPorMinuto(parametros).multiply(BigDecimal.valueOf(minutos));
        BigDecimal valor = Dinheiro.arredondar(usuario.calcularTarifa(minutos).add(adicional));
        usuario.cobrar(valor); // pode recusar por saldo insuficiente, antes de qualquer alteração
        bicicleta.devolver();
        destino.receber(bicicleta);
        aluguel.finalizar(fim, destino.getId(), valor);
        usuario.encerrarAluguel();
    }

    private void cancelar(Aluguel aluguel, Usuario usuario, Bicicleta bicicleta, Estacao destino, LocalDateTime fim) {
        bicicleta.devolver();
        destino.receber(bicicleta);
        aluguel.cancelar(fim, destino.getId());
        usuario.encerrarAluguel();
    }

    private void expirar(Aluguel aluguel, Usuario usuario, Bicicleta bicicleta, Estacao destino,
                         LocalDateTime fim, Parametros parametros) {
        bicicleta.devolverComExpiracao();
        destino.receber(bicicleta);
        aluguel.expirar(fim, destino.getId());
        usuario.encerrarAluguel();
        usuario.adicionarPenalidade(new PenalidadeAtraso(parametros.valorPenalidadeAtraso(),
                "Aluguel " + aluguel.getId() + " excedeu o tempo maximo"));
    }

    public String consultar(String aluguelId, String atributo) {
        return contexto.base().alugueis().exigir(aluguelId).consultar(atributo);
    }
}
