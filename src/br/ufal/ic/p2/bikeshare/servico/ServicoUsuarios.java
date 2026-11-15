package br.ufal.ic.p2.bikeshare.servico;

import br.ufal.ic.p2.bikeshare.excecoes.AtributoException;
import br.ufal.ic.p2.bikeshare.excecoes.DadoInvalidoException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.modelo.plano.CatalogoPlanos;
import br.ufal.ic.p2.bikeshare.modelo.plano.Plano;
import br.ufal.ic.p2.bikeshare.modelo.usuario.FabricaUsuarios;
import br.ufal.ic.p2.bikeshare.modelo.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.repositorio.ContextoSistema;
import br.ufal.ic.p2.bikeshare.repositorio.RepositorioUsuarios;
import br.ufal.ic.p2.bikeshare.util.Entrada;

/** Cadastro, consulta, alteração e remoção de usuários (US1 a US4). */
public final class ServicoUsuarios {
    private final ContextoSistema contexto;

    public ServicoUsuarios(ContextoSistema contexto) {
        this.contexto = contexto;
    }

    /** Plano e mensalidade nulos significam "não informados": o usuário entra no plano avulso. */
    public String criarPessoaFisica(String nome, String email, String senha, String tipo, String cpf,
                                    String plano, String mensalidade) {
        CatalogoPlanos catalogo = contexto.base().planos();
        Usuario usuario = FabricaUsuarios.pessoaFisica(usuarios().proximoId(), nome, email, senha, tipo, cpf,
                catalogo.getPadrao());
        return cadastrar(usuario, plano, mensalidade);
    }

    public String criarPessoaJuridica(String nome, String email, String senha, String tipo, String cnpj,
                                      String razaoSocial, String plano, String mensalidade) {
        CatalogoPlanos catalogo = contexto.base().planos();
        Usuario usuario = FabricaUsuarios.pessoaJuridica(usuarios().proximoId(), nome, email, senha, tipo, cnpj,
                razaoSocial, catalogo.getPadrao());
        return cadastrar(usuario, plano, mensalidade);
    }

    private String cadastrar(Usuario usuario, String nomePlano, String mensalidade) {
        if (nomePlano != null) {
            contratarPlanoInicial(usuario, nomePlano, mensalidade);
        }
        usuarios().adicionar(usuario);
        return usuario.getId();
    }

    private void contratarPlanoInicial(Usuario usuario, String nomePlano, String mensalidade) {
        Plano plano = contexto.base().planos().exigir(nomePlano);
        if (plano.getMensalidadePadrao().isEmpty()) {
            if (!Entrada.vazio(mensalidade)) {
                throw new DadoInvalidoException(Mensagens.PLANO_SEM_MENSALIDADE);
            }
            usuario.contratarPlano(plano, null);
            return;
        }
        usuario.contratarPlano(plano, Entrada.decimalNaoNegativo(mensalidade, Mensagens.MENSALIDADE_NULA,
                Mensagens.MENSALIDADE_NUMERICA, Mensagens.MENSALIDADE_NEGATIVA));
    }

    public String consultar(String usuarioId, String atributo) {
        return usuarios().exigir(usuarioId).consultar(atributo);
    }

    public String buscarIdPorEmail(String email) {
        Entrada.obrigatorio(email, Mensagens.EMAIL_NULO);
        return usuarios().buscarPorEmail(email).getId();
    }

    /** Alteração simples de atributo (nome, email, senha, plano, saldo ou saldoPreCarregado). */
    public void alterar(String usuarioId, String atributo, String valor) {
        Usuario usuario = usuarios().exigir(usuarioId);
        if (atributo == null) {
            throw new AtributoException(Mensagens.ATRIBUTO_NAO_ALTERAVEL);
        }
        switch (atributo) {
            case "nome":
                usuario.setNome(valor);
                break;
            case "email":
                String email = Usuario.validarEmail(valor);
                usuarios().exigirEmailLivre(email, usuario);
                usuario.setEmail(email);
                break;
            case "senha":
                usuario.setSenha(valor);
                break;
            case "plano":
                usuario.trocarPlano(contexto.base().planos().exigir(valor));
                break;
            case "saldo":
                recarregar(usuario, valor);
                break;
            case "metodoPagamento":
                usuario.alterarMetodoPagamento(valor);
                break;
            default:
                throw new AtributoException(Mensagens.ATRIBUTO_NAO_ALTERAVEL);
        }
    }

    /** Alteração do método de pagamento com os dados que ele exige (pix: 1; cartão: 2). */
    public void alterarMetodoPagamento(String usuarioId, String atributo, String tipo, String... dados) {
        Usuario usuario = usuarios().exigir(usuarioId);
        if (!"metodoPagamento".equals(atributo)) {
            throw new AtributoException(Mensagens.ATRIBUTO_NAO_ALTERAVEL);
        }
        usuario.alterarMetodoPagamento(tipo, dados);
    }

    private void recarregar(Usuario usuario, String valorTexto) {
        if (!usuario.aceitaRecarga()) {
            throw new AtributoException(Mensagens.USUARIO_NAO_USA_SALDO);
        }
        usuario.recarregar(Entrada.decimalPositivo(valorTexto, Mensagens.VALOR_NULO, Mensagens.VALOR_NUMERICO,
                Mensagens.VALOR_POSITIVO));
    }

    public void remover(String usuarioId) {
        Usuario usuario = usuarios().exigir(usuarioId);
        usuario.exigirSemAluguelAtivo();
        usuarios().remover(usuario);
    }

    private RepositorioUsuarios usuarios() {
        return contexto.base().usuarios();
    }
}
