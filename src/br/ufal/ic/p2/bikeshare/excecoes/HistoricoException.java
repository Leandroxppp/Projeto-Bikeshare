package br.ufal.ic.p2.bikeshare.excecoes;

/** Pedido de desfazer/refazer sem comando disponível. */
public class HistoricoException extends BikeshareException {
    private static final long serialVersionUID = 1L;

    public HistoricoException(String mensagem) {
        super(mensagem);
    }
}
