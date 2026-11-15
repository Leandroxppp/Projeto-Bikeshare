package br.ufal.ic.p2.bikeshare.excecoes;

/** Parâmetro obrigatório ausente, nulo ou composto só de espaços. */
public class DadoObrigatorioException extends BikeshareException {
    private static final long serialVersionUID = 1L;

    public DadoObrigatorioException(String mensagem) {
        super(mensagem);
    }
}
