package br.ufal.ic.p2.bikeshare.modelo.usuario;

import br.ufal.ic.p2.bikeshare.excecoes.AtributoException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.modelo.plano.Plano;
import br.ufal.ic.p2.bikeshare.util.Entrada;

public final class PessoaFisica extends Usuario {
    private static final long serialVersionUID = 1L;
    public static final String TIPO = "pessoaFisica";

    private final String cpf;

    public PessoaFisica(String id, String nome, String email, String senha, Plano planoInicial, String cpf) {
        super(id, nome, email, senha, planoInicial);
        this.cpf = Entrada.obrigatorio(cpf, Mensagens.CPF_NULO);
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    protected String consultarAtributoEspecifico(String atributo) {
        switch (atributo) {
            case "cpf":
                return cpf;
            case "cnpj":
            case "razaoSocial":
                throw new AtributoException(Mensagens.USUARIO_NAO_EH_PJ);
            default:
                throw new AtributoException(Mensagens.ATRIBUTO_NAO_EXISTE);
        }
    }
}
