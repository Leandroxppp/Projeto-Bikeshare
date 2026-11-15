package br.ufal.ic.p2.bikeshare.servico;

import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.modelo.estacao.Estacao;
import br.ufal.ic.p2.bikeshare.repositorio.ContextoSistema;
import br.ufal.ic.p2.bikeshare.util.Entrada;

/** Cadastro de estações e consulta de disponibilidade (US5 e US7). */
public final class ServicoEstacoes {
    private final ContextoSistema contexto;

    public ServicoEstacoes(ContextoSistema contexto) {
        this.contexto = contexto;
    }

    public String criar(String nome, String endereco, String capacidade) {
        Entrada.obrigatorio(nome, Mensagens.NOME_NULO);
        Entrada.obrigatorio(endereco, Mensagens.ENDERECO_NULO);
        int vagas = Entrada.inteiro(capacidade, Mensagens.CAPACIDADE_NULA, Mensagens.CAPACIDADE_NUMERICA);
        Estacao estacao = new Estacao(contexto.base().estacoes().proximoId(), nome, endereco, vagas);
        contexto.base().estacoes().adicionar(estacao);
        return estacao.getId();
    }

    public String getVagasDisponiveis(String estacaoId) {
        return String.valueOf(contexto.base().estacoes().exigir(estacaoId).getVagasLivres());
    }

    public String getBicicletasDisponiveis(String estacaoId) {
        Estacao estacao = contexto.base().estacoes().exigir(estacaoId);
        return String.valueOf(estacao.contarDisponiveis(contexto.parametros()));
    }
}
