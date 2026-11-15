package br.ufal.ic.p2.bikeshare.modelo.aluguel;

/** Ciclo de vida de uma corrida. Só EM_ANDAMENTO ainda não tem fim, destino e valor. */
public enum EstadoAluguel {
    EM_ANDAMENTO("emAndamento", false),
    FINALIZADO("finalizado", true),
    CANCELADO("cancelado", true),
    EXPIRADO("expirado", true);

    private final String descricao;
    private final boolean concluido;

    EstadoAluguel(String descricao, boolean concluido) {
        this.descricao = descricao;
        this.concluido = concluido;
    }

    public String descricao() {
        return descricao;
    }

    public boolean isConcluido() {
        return concluido;
    }
}
