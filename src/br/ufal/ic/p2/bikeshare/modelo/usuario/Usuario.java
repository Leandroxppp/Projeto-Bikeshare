package br.ufal.ic.p2.bikeshare.modelo.usuario;

import br.ufal.ic.p2.bikeshare.config.Parametros;
import br.ufal.ic.p2.bikeshare.excecoes.AtributoException;
import br.ufal.ic.p2.bikeshare.excecoes.DadoInvalidoException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.excecoes.OperacaoNaoPermitidaException;
import br.ufal.ic.p2.bikeshare.modelo.Identificavel;
import br.ufal.ic.p2.bikeshare.modelo.pagamento.AtributoPagamento;
import br.ufal.ic.p2.bikeshare.modelo.pagamento.ContaPagamento;
import br.ufal.ic.p2.bikeshare.modelo.penalidade.Pendencias;
import br.ufal.ic.p2.bikeshare.modelo.penalidade.Penalidade;
import br.ufal.ic.p2.bikeshare.modelo.plano.Assinatura;
import br.ufal.ic.p2.bikeshare.modelo.plano.Plano;
import br.ufal.ic.p2.bikeshare.util.Dinheiro;
import br.ufal.ic.p2.bikeshare.util.Entrada;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Usuário do sistema: identificação, aluguel em curso e consulta de atributos. Plano, pagamento
 * e pendências vivem em objetos próprios. Pessoa física e jurídica diferem só nos dados de
 * identificação, tratados por cada subclasse.
 */
public abstract class Usuario implements Identificavel, Serializable {
    private static final long serialVersionUID = 1L;
    private static final Pattern EMAIL = Pattern.compile("[^@\\s]+@[^@\\s]+");

    private final String id;
    private String nome;
    private String email;
    private String senha;
    private String aluguelAtivoId;
    private final Assinatura assinatura;
    private final ContaPagamento conta = new ContaPagamento();
    private final Pendencias pendencias = new Pendencias();

    protected Usuario(String id, String nome, String email, String senha, Plano planoInicial) {
        this.id = id;
        setNome(nome);
        setEmail(email);
        setSenha(senha);
        this.assinatura = new Assinatura(planoInicial);
    }

    // ---------------------------------------------------------------- validação

    public static String validarEmail(String email) {
        Entrada.obrigatorio(email, Mensagens.EMAIL_NULO);
        if (!EMAIL.matcher(email.trim()).matches()) {
            throw new DadoInvalidoException(Mensagens.EMAIL_INVALIDO);
        }
        return email.trim();
    }

    // ---------------------------------------------------------------- dados básicos

    @Override
    public final String getId() {
        return id;
    }

    public final String getNome() {
        return nome;
    }

    public final void setNome(String nome) {
        this.nome = Entrada.obrigatorio(nome, Mensagens.NOME_NULO);
    }

    public final String getEmail() {
        return email;
    }

    public final void setEmail(String email) {
        this.email = validarEmail(email);
    }

    public final void setSenha(String senha) {
        this.senha = Entrada.obrigatorio(senha, Mensagens.SENHA_NULA);
    }

    // ---------------------------------------------------------------- plano

    public final void contratarPlano(Plano plano, BigDecimal mensalidadeContratada) {
        assinatura.contratar(plano, mensalidadeContratada);
    }

    public final void trocarPlano(Plano novoPlano) {
        assinatura.trocarPara(novoPlano);
    }

    public final BigDecimal calcularTarifa(long minutos) {
        return assinatura.calcularTarifa(minutos);
    }

    // ---------------------------------------------------------------- pagamento

    public final void alterarMetodoPagamento(String tipo, String... parametros) {
        conta.alterarMetodo(tipo, parametros);
    }

    public final boolean aceitaRecarga() {
        return conta.aceitaRecarga();
    }

    public final void recarregar(BigDecimal valor) {
        conta.recarregar(valor);
    }

    public final void cobrar(BigDecimal valor) {
        conta.cobrar(valor);
    }

    public final void autorizarRetirada(Parametros parametros) {
        conta.autorizarRetirada(parametros);
    }

    // ---------------------------------------------------------------- aluguel

    public final boolean temAluguelAtivo() {
        return aluguelAtivoId != null;
    }

    public final void iniciarAluguel(String aluguelId) {
        this.aluguelAtivoId = aluguelId;
    }

    public final void encerrarAluguel() {
        this.aluguelAtivoId = null;
    }

    /** Bloqueios que dependem só do usuário; a inadimplência tem precedência. */
    public final void exigirApto() {
        if (isInadimplente()) {
            throw new OperacaoNaoPermitidaException(Mensagens.USUARIO_INADIMPLENTE);
        }
        if (temAluguelAtivo()) {
            throw new OperacaoNaoPermitidaException(Mensagens.USUARIO_JA_TEM_ALUGUEL);
        }
    }

    public final void exigirSemAluguelAtivo() {
        if (temAluguelAtivo()) {
            throw new OperacaoNaoPermitidaException(Mensagens.USUARIO_COM_ALUGUEL);
        }
    }

    // ---------------------------------------------------------------- penalidades

    public final void adicionarPenalidade(Penalidade penalidade) {
        pendencias.adicionar(penalidade);
    }

    public final List<Penalidade> getPenalidades() {
        return pendencias.listar();
    }

    public final boolean isInadimplente() {
        return pendencias.existem();
    }

    public final EstadoUsuario getEstado() {
        return isInadimplente() ? EstadoUsuario.INADIMPLENTE : EstadoUsuario.ATIVO;
    }

    // ---------------------------------------------------------------- consulta

    public abstract String getTipo();

    /** Atributos exclusivos do subtipo (documento e, para pessoa jurídica, razão social). */
    protected abstract String consultarAtributoEspecifico(String atributo);

    public final String consultar(String atributo) {
        if (atributo == null) {
            throw new AtributoException(Mensagens.ATRIBUTO_NAO_EXISTE);
        }
        switch (atributo) {
            case "nome":
                return nome;
            case "email":
                return email;
            case "senha":
                return senha;
            case "tipo":
                return getTipo();
            case "estado":
                return getEstado().descricao();
            case "plano":
                return assinatura.getNomePlano();
            case "mensalidade":
                return assinatura.getMensalidade().map(Dinheiro::formatar)
                        .orElseThrow(() -> new AtributoException(Mensagens.PLANO_SEM_MENSALIDADE));
            case "metodoPagamento":
                return conta.getNomeDoMetodo();
            case "aluguelAtivo":
                return aluguelAtivoId == null ? "nenhum" : aluguelAtivoId;
            case "totalPendencias":
                return Dinheiro.formatar(pendencias.getTotal());
            default:
                Optional<AtributoPagamento> pagamento = AtributoPagamento.da(atributo);
                if (pagamento.isPresent()) {
                    return conta.consultar(pagamento.get());
                }
                return consultarAtributoEspecifico(atributo);
        }
    }
}
