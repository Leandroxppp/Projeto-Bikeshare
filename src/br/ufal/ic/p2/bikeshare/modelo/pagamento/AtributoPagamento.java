package br.ufal.ic.p2.bikeshare.modelo.pagamento;

import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;

import java.util.Optional;

/** Atributos de usuário que só existem para um método de pagamento específico. */
public enum AtributoPagamento {
    NUMERO_CARTAO("numeroCartao", Mensagens.USUARIO_NAO_USA_CARTAO),
    VALIDADE_CARTAO("validadeCartao", Mensagens.USUARIO_NAO_USA_CARTAO),
    CHAVE_PIX("chavePix", Mensagens.USUARIO_NAO_USA_PIX),
    SALDO("saldo", Mensagens.USUARIO_NAO_USA_SALDO);

    private final String chave;
    private final String mensagemQuandoNaoUsa;

    AtributoPagamento(String chave, String mensagemQuandoNaoUsa) {
        this.chave = chave;
        this.mensagemQuandoNaoUsa = mensagemQuandoNaoUsa;
    }

    public String chave() {
        return chave;
    }

    public String mensagemQuandoNaoUsa() {
        return mensagemQuandoNaoUsa;
    }

    public static Optional<AtributoPagamento> da(String nome) {
        for (AtributoPagamento a : values()) {
            if (a.chave.equals(nome)) {
                return Optional.of(a);
            }
        }
        return Optional.empty();
    }
}
