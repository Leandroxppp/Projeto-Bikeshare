package br.ufal.ic.p2.bikeshare.excecoes;

/** Tentativa de cadastrar algo que já existe (email, bicicleta, plano). */
public class EntidadeDuplicadaException extends BikeshareException {
    private static final long serialVersionUID = 1L;

    public EntidadeDuplicadaException(String mensagem) {
        super(mensagem);
    }
}
