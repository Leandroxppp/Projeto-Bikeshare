package br.ufal.ic.p2.bikeshare.modelo.usuario;

/** Situação financeira do usuário; é derivada das penalidades pendentes. */
public enum EstadoUsuario {
    ATIVO("ativo"),
    INADIMPLENTE("inadimplente");

    private final String descricao;

    EstadoUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String descricao() {
        return descricao;
    }
}
