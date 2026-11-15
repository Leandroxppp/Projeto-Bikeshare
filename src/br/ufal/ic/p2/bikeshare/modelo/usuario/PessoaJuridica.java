package br.ufal.ic.p2.bikeshare.modelo.usuario;

import br.ufal.ic.p2.bikeshare.excecoes.AtributoException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.modelo.plano.Plano;
import br.ufal.ic.p2.bikeshare.util.Entrada;

public final class PessoaJuridica extends Usuario {
    private static final long serialVersionUID = 1L;
    public static final String TIPO = "pessoaJuridica";

    private final String cnpj;
    private final String razaoSocial;

    public PessoaJuridica(String id, String nome, String email, String senha, Plano planoInicial,
                          String cnpj, String razaoSocial) {
        super(id, nome, email, senha, planoInicial);
        this.cnpj = Entrada.obrigatorio(cnpj, Mensagens.CNPJ_NULO);
        this.razaoSocial = Entrada.obrigatorio(razaoSocial, Mensagens.RAZAO_SOCIAL_NULA);
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    protected String consultarAtributoEspecifico(String atributo) {
        switch (atributo) {
            case "cnpj":
                return cnpj;
            case "razaoSocial":
                return razaoSocial;
            case "cpf":
                throw new AtributoException(Mensagens.USUARIO_NAO_EH_PF);
            default:
                throw new AtributoException(Mensagens.ATRIBUTO_NAO_EXISTE);
        }
    }
}
