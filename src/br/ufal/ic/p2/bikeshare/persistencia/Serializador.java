package br.ufal.ic.p2.bikeshare.persistencia;

import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.excecoes.PersistenciaException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/** Conversão objeto <-> bytes com a serialização nativa do Java (java.io.Serializable). */
public final class Serializador {
    private Serializador() {
    }

    public static byte[] serializar(Serializable objeto) {
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
             ObjectOutputStream saida = new ObjectOutputStream(bytes)) {
            saida.writeObject(objeto);
            saida.flush();
            return bytes.toByteArray();
        } catch (IOException e) {
            throw new PersistenciaException(Mensagens.FALHA_AO_GRAVAR);
        }
    }

    public static <T> T desserializar(byte[] bytes, Class<T> tipo) {
        try (ObjectInputStream entrada = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return tipo.cast(entrada.readObject());
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new PersistenciaException(Mensagens.FALHA_AO_LER);
        }
    }
}
