package br.ufal.ic.p2.bikeshare.modelo.usuario;

import br.ufal.ic.p2.bikeshare.excecoes.DadoInvalidoException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.modelo.plano.Plano;

/** Cria o subtipo de usuário coerente com o tipo informado e com os dados fornecidos. */
public final class FabricaUsuarios {
    private FabricaUsuarios() {
    }

    public static Usuario pessoaFisica(String id, String nome, String email, String senha, String tipo,
                                       String cpf, Plano planoInicial) {
        exigirTipo(PessoaFisica.TIPO, tipo);
        return new PessoaFisica(id, nome, email, senha, planoInicial, cpf);
    }

    public static Usuario pessoaJuridica(String id, String nome, String email, String senha, String tipo,
                                         String cnpj, String razaoSocial, Plano planoInicial) {
        exigirTipo(PessoaJuridica.TIPO, tipo);
        return new PessoaJuridica(id, nome, email, senha, planoInicial, cnpj, razaoSocial);
    }

    private static void exigirTipo(String esperado, String informado) {
        if (!esperado.equals(informado)) {
            throw new DadoInvalidoException(Mensagens.TIPO_INVALIDO);
        }
    }
}
