package br.ufal.ic.p2.bikeshare.modelo.usuario;

import br.ufal.ic.p2.bikeshare.config.Parametros;
import br.ufal.ic.p2.bikeshare.excecoes.AtributoException;
import br.ufal.ic.p2.bikeshare.excecoes.DadoInvalidoException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;
import br.ufal.ic.p2.bikeshare.excecoes.OperacaoNaoPermitidaException;
import br.ufal.ic.p2.bikeshare.modelo.Identificavel;
import br.ufal.ic.p2.bikeshare.modelo.pagamento.AtributoPagamento;
import br.ufal.ic.p2.bikeshare.modelo.pagamento.FabricaMetodosPagamento;
import br.ufal.ic.p2.bikeshare.modelo.pagamento.MetodoPagamento;
import br.ufal.ic.p2.bikeshare.modelo.pagamento.SaldoPreCarregado;
import br.ufal.ic.p2.bikeshare.modelo.pagamento.SemMetodoPagamento;
import br.ufal.ic.p2.bikeshare.modelo.penalidade.Penalidade;
import br.ufal.ic.p2.bikeshare.modelo.plano.Plano;
import br.ufal.ic.p2.bikeshare.util.Dinheiro;
import br.ufal.ic.p2.bikeshare.util.Entrada;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Usuário do sistema. Pessoa física e jurídica compartilham tudo (plano, pagamento, aluguel,
 * penalidades) e diferem apenas nos dados de identificação, tratados por cada subclasse.
 */
public abstract class Usuario implements Identificavel, Serializable {
    private static final long serialVersionUID = 1L;
    private static final Pattern EMAIL = Pattern.compile("[^@\\s]+@[^@\\s]+");

    private final String id;
    private String nome;
    private String email;
    private String senha;
    private Plano plano;
    private BigDecimal mensalidade; // nulo enquanto o plano não tem mensalidade
    private MetodoPagamento metodoPagamento = new SemMetodoPagamento();
    private final SaldoPreCarregado carteira = new SaldoPreCarregado();
    private String aluguelAtivoId;
    private final ArrayList<Penalidade> penalidades = new ArrayList<>();

    protected Usuario(String id, String nome, String email, String senha, Plano planoInicial) {
        this.id = id;
        setNome(nome);
        setEmail(email);
        setSenha(senha);
        this.plano = planoInicial;
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

    /** Adesão inicial, com a mensalidade contratada (nula para planos sem mensalidade). */
    public final void contratarPlano(Plano novoPlano, BigDecimal mensalidadeContratada) {
        this.plano = novoPlano;
        this.mensalidade = mensalidadeContratada == null ? null : Dinheiro.arredondar(mensalidadeContratada);
    }

    /** Troca de plano; repetir o plano atual preserva a mensalidade já contratada. */
    public final void trocarPlano(Plano novoPlano) {
        if (!novoPlano.equals(plano)) {
            contratarPlano(novoPlano, novoPlano.getMensalidadePadrao().orElse(null));
        }
    }

    public final BigDecimal calcularTarifa(long minutos) {
        return plano.calcularTarifa(minutos);
    }

    // ---------------------------------------------------------------- pagamento

    public final void alterarMetodoPagamento(String tipo, String... parametros) {
        this.metodoPagamento = FabricaMetodosPagamento.criar(tipo, carteira, parametros);
    }

    public final boolean aceitaRecarga() {
        return metodoPagamento.aceitaRecarga();
    }

    public final void recarregar(BigDecimal valor) {
        if (!aceitaRecarga()) {
            throw new AtributoException(Mensagens.USUARIO_NAO_USA_SALDO);
        }
        carteira.creditar(valor);
    }

    public final void cobrar(BigDecimal valor) {
        metodoPagamento.cobrar(valor);
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

    public final void autorizarRetirada(Parametros parametros) {
        metodoPagamento.autorizarRetirada(parametros);
    }

    public final void exigirSemAluguelAtivo() {
        if (temAluguelAtivo()) {
            throw new OperacaoNaoPermitidaException(Mensagens.USUARIO_COM_ALUGUEL);
        }
    }

    // ---------------------------------------------------------------- penalidades

    public final void adicionarPenalidade(Penalidade penalidade) {
        penalidades.add(penalidade);
    }

    public final BigDecimal getTotalPendencias() {
        BigDecimal total = Dinheiro.ZERO;
        for (Penalidade p : penalidades) {
            total = total.add(p.getValor());
        }
        return Dinheiro.arredondar(total);
    }

    public final boolean isInadimplente() {
        return getTotalPendencias().signum() > 0;
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
                return plano.getNome();
            case "mensalidade":
                return consultarMensalidade();
            case "metodoPagamento":
                return metodoPagamento.getNome();
            case "aluguelAtivo":
                return aluguelAtivoId == null ? "nenhum" : aluguelAtivoId;
            case "totalPendencias":
                return Dinheiro.formatar(getTotalPendencias());
            default:
                Optional<AtributoPagamento> pagamento = AtributoPagamento.da(atributo);
                if (pagamento.isPresent()) {
                    return consultarPagamento(pagamento.get());
                }
                return consultarAtributoEspecifico(atributo);
        }
    }

    private String consultarMensalidade() {
        if (mensalidade == null) {
            throw new AtributoException(Mensagens.PLANO_SEM_MENSALIDADE);
        }
        return Dinheiro.formatar(mensalidade);
    }

    private String consultarPagamento(AtributoPagamento atributo) {
        String valor = metodoPagamento.getAtributos().get(atributo.chave());
        if (valor == null) {
            throw new AtributoException(atributo.mensagemQuandoNaoUsa());
        }
        return valor;
    }
}
