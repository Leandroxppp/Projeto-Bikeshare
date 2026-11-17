package br.ufal.ic.p2.bikeshare.excecoes;

/** Arquivo de configuração ilegível ou com parâmetro inválido: o sistema não inicia. */
public class ConfiguracaoException extends BikeshareException {
    private static final long serialVersionUID = 1L;

    public ConfiguracaoException(String mensagem) {
        super(mensagem);
    }
}
