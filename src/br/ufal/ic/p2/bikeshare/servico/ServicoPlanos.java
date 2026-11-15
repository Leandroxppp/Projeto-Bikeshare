package br.ufal.ic.p2.bikeshare.servico;

import br.ufal.ic.p2.bikeshare.excecoes.EntidadeDuplicadaException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.modelo.plano.CatalogoPlanos;
import br.ufal.ic.p2.bikeshare.modelo.plano.PlanoCustomizado;
import br.ufal.ic.p2.bikeshare.repositorio.ContextoSistema;
import br.ufal.ic.p2.bikeshare.util.Entrada;

import java.math.BigDecimal;

/** Criação de planos de tarifação em tempo de execução (US12). */
public final class ServicoPlanos {
    private final ContextoSistema contexto;

    public ServicoPlanos(ContextoSistema contexto) {
        this.contexto = contexto;
    }

    public String criar(String nome, String franquiaMinutos, String valorMinutoExcedente, String mensalidade) {
        Entrada.obrigatorio(nome, Mensagens.NOME_PLANO_NULO);
        CatalogoPlanos catalogo = contexto.base().planos();
        if (catalogo.existe(nome)) {
            throw new EntidadeDuplicadaException(Mensagens.PLANO_JA_EXISTE);
        }
        int franquia = Entrada.inteiroNaoNegativo(franquiaMinutos, Mensagens.FRANQUIA_NULA,
                Mensagens.FRANQUIA_NUMERICA, Mensagens.FRANQUIA_NEGATIVA);
        BigDecimal excedente = Entrada.decimalNaoNegativo(valorMinutoExcedente, Mensagens.EXCEDENTE_NULO,
                Mensagens.EXCEDENTE_NUMERICO, Mensagens.EXCEDENTE_NEGATIVO);
        BigDecimal valorMensalidade = Entrada.decimalNaoNegativo(mensalidade, Mensagens.MENSALIDADE_NULA,
                Mensagens.MENSALIDADE_NUMERICA, Mensagens.MENSALIDADE_NEGATIVA);
        catalogo.registrar(new PlanoCustomizado(nome, franquia, excedente, valorMensalidade));
        return nome;
    }
}
