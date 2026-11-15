package br.ufal.ic.p2.bikeshare.historico;

import br.ufal.ic.p2.bikeshare.excecoes.HistoricoException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.persistencia.Serializador;
import br.ufal.ic.p2.bikeshare.repositorio.BaseDados;
import br.ufal.ic.p2.bikeshare.repositorio.ContextoSistema;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Supplier;

/**
 * Desfazer/refazer com o padrão Memento: antes de cada comando que altera o estado, uma
 * fotografia (bytes serializados) da base é guardada. Só comandos bem-sucedidos entram no
 * histórico; se o comando falha, o estado é restaurado, o que também garante que um comando
 * recusado não consome identificador nem deixa efeito parcial. O histórico vive só em memória.
 */
public final class GerenciadorHistorico {
    private final ContextoSistema contexto;
    private final Deque<byte[]> aDesfazer = new ArrayDeque<>();
    private final Deque<byte[]> aRefazer = new ArrayDeque<>();

    public GerenciadorHistorico(ContextoSistema contexto) {
        this.contexto = contexto;
    }

    public <T> T executar(Supplier<T> comando) {
        byte[] antes = fotografar();
        T resultado;
        try {
            resultado = comando.get();
        } catch (RuntimeException e) {
            restaurar(antes);
            throw e;
        }
        aDesfazer.push(antes);
        aRefazer.clear();
        return resultado;
    }

    public void executar(Runnable comando) {
        executar(() -> {
            comando.run();
            return null;
        });
    }

    public void desfazer() {
        if (aDesfazer.isEmpty()) {
            throw new HistoricoException(Mensagens.NADA_A_DESFAZER);
        }
        aRefazer.push(fotografar());
        restaurar(aDesfazer.pop());
    }

    public void refazer() {
        if (aRefazer.isEmpty()) {
            throw new HistoricoException(Mensagens.NADA_A_REFAZER);
        }
        aDesfazer.push(fotografar());
        restaurar(aRefazer.pop());
    }

    private byte[] fotografar() {
        return Serializador.serializar(contexto.base());
    }

    private void restaurar(byte[] fotografia) {
        contexto.substituirBase(Serializador.desserializar(fotografia, BaseDados.class));
    }
}
