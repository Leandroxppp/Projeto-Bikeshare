package br.ufal.ic.p2.bikeshare.excecoes;

/** Regra de negócio que bloqueia a operação (inadimplência, estação cheia, estado inadequado...). */
public class OperacaoNaoPermitidaException extends BikeshareException {
    private static final long serialVersionUID = 1L;

    public OperacaoNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}
