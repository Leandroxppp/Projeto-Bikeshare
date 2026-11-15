package br.ufal.ic.p2.bikeshare.modelo.estacao;

import br.ufal.ic.p2.bikeshare.config.Parametros;
import br.ufal.ic.p2.bikeshare.excecoes.DadoInvalidoException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.excecoes.OperacaoNaoPermitidaException;
import br.ufal.ic.p2.bikeshare.modelo.Identificavel;
import br.ufal.ic.p2.bikeshare.modelo.bicicleta.Bicicleta;
import br.ufal.ic.p2.bikeshare.util.Entrada;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Ponto de retirada e devolução. Bicicletas em manutenção continuam ocupando vaga física, mas
 * só as aptas contam como disponíveis.
 */
public final class Estacao implements Identificavel, Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String nome;
    private final String endereco;
    private final int capacidade;
    private final ArrayList<Bicicleta> bicicletas = new ArrayList<>();

    public Estacao(String id, String nome, String endereco, int capacidade) {
        this.id = id;
        this.nome = Entrada.obrigatorio(nome, Mensagens.NOME_NULO);
        this.endereco = Entrada.obrigatorio(endereco, Mensagens.ENDERECO_NULO);
        if (capacidade <= 0) {
            throw new DadoInvalidoException(Mensagens.CAPACIDADE_POSITIVA);
        }
        this.capacidade = capacidade;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public int getVagasLivres() {
        return capacidade - bicicletas.size();
    }

    public boolean estaCheia() {
        return getVagasLivres() <= 0;
    }

    public boolean contem(Bicicleta bicicleta) {
        return bicicletas.contains(bicicleta);
    }

    public long contarDisponiveis(Parametros parametros) {
        return bicicletas.stream().filter(b -> b.estaDisponivelParaRetirada(parametros)).count();
    }

    public void receber(Bicicleta bicicleta) {
        if (estaCheia()) {
            throw new OperacaoNaoPermitidaException(Mensagens.ESTACAO_CHEIA);
        }
        bicicletas.add(bicicleta);
    }

    public void liberar(Bicicleta bicicleta) {
        bicicletas.remove(bicicleta);
    }
}
