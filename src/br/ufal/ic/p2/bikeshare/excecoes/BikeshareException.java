package br.ufal.ic.p2.bikeshare.excecoes;

/**
 * Raiz de todas as falhas de negócio do sistema. É não verificada para que as camadas de
 * domínio não precisem propagar {@code throws} em cada assinatura; a Facade deixa a exceção
 * chegar ao chamador (o EasyAccept compara a mensagem). Nenhuma falha é engolida.
 */
public abstract class BikeshareException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    protected BikeshareException(String mensagem) {
        super(mensagem);
    }
}
