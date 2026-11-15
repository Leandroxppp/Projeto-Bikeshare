package br.ufal.ic.p2.bikeshare.excecoes;

/** Atributo inexistente, não alterável ou não aplicável ao subtipo do objeto consultado. */
public class AtributoException extends BikeshareException {
    private static final long serialVersionUID = 1L;

    public AtributoException(String mensagem) {
        super(mensagem);
    }
}
