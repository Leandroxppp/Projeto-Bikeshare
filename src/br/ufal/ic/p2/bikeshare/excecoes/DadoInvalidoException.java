package br.ufal.ic.p2.bikeshare.excecoes;

/** Parâmetro presente, mas com formato ou valor inaceitável. */
public class DadoInvalidoException extends BikeshareException {
    private static final long serialVersionUID = 1L;

    public DadoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
