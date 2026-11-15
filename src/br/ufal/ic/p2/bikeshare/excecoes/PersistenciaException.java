package br.ufal.ic.p2.bikeshare.excecoes;

/** Falha ao gravar ou ler o estado persistido do sistema. */
public class PersistenciaException extends BikeshareException {
    private static final long serialVersionUID = 1L;

    public PersistenciaException(String mensagem) {
        super(mensagem);
    }
}
