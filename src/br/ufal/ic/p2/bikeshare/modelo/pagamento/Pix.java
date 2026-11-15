package br.ufal.ic.p2.bikeshare.modelo.pagamento;

import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.util.Entrada;

import java.util.Map;

public final class Pix extends MetodoPagamento {
    private static final long serialVersionUID = 1L;

    private final String chave;

    public Pix(String chave) {
        this.chave = Entrada.obrigatorio(chave, Mensagens.CHAVE_PIX_NULA);
    }

    @Override
    public String getNome() {
        return "pix";
    }

    @Override
    public Map<String, String> getAtributos() {
        return Map.of(AtributoPagamento.CHAVE_PIX.chave(), chave);
    }
}
