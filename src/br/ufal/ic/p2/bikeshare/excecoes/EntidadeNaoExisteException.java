package br.ufal.ic.p2.bikeshare.excecoes;

/** Identificador que não corresponde a nenhum usuário, estação, bicicleta, aluguel ou plano. */
public class EntidadeNaoExisteException extends BikeshareException {
    private static final long serialVersionUID = 1L;

    public EntidadeNaoExisteException(String mensagem) {
        super(mensagem);
    }
}
