package br.ufal.ic.p2.bikeshare;

import br.ufal.ic.p2.bikeshare.config.Parametros;
import br.ufal.ic.p2.bikeshare.persistencia.ArquivoXml;
import br.ufal.ic.p2.bikeshare.repositorio.BaseDados;
import br.ufal.ic.p2.bikeshare.repositorio.ContextoSistema;
import br.ufal.ic.p2.bikeshare.servico.ServicoAlugueis;
import br.ufal.ic.p2.bikeshare.servico.ServicoBicicletas;
import br.ufal.ic.p2.bikeshare.servico.ServicoEstacoes;
import br.ufal.ic.p2.bikeshare.servico.ServicoUsuarios;

import java.nio.file.Path;

/**
 * Único ponto de entrada da lógica de negócio (BS01). Não decide nada: encaminha cada comando
 * ao serviço responsável. As operações do Milestone 2 (US10 a US16) continuam como no stub
 * original e serão implementadas na próxima etapa.
 */
public class Facade {
    private static final String AINDA_NAO_IMPLEMENTADO = "Ainda não implementado.";
    private static final String ARQUIVO_DE_DADOS = "bikeshare_data.xml";
    private static final String ARQUIVO_DE_CONFIGURACAO = "bikeshare.properties";

    private final Parametros parametros = Parametros.carregar(Path.of(ARQUIVO_DE_CONFIGURACAO));
    private final ArquivoXml arquivo = new ArquivoXml(ARQUIVO_DE_DADOS);
    private final ContextoSistema contexto;

    private final ServicoUsuarios usuarios;
    private final ServicoEstacoes estacoes;
    private final ServicoBicicletas bicicletas;
    private final ServicoAlugueis alugueis;

    public Facade() {
        BaseDados inicial = arquivo.carregar().orElseGet(() -> new BaseDados(parametros));
        this.contexto = new ContextoSistema(parametros, inicial);
        this.usuarios = new ServicoUsuarios(contexto);
        this.estacoes = new ServicoEstacoes(contexto);
        this.bicicletas = new ServicoBicicletas(contexto);
        this.alugueis = new ServicoAlugueis(contexto);
    }

    // =====================================================================
    // US1 - Cadastro de usuario
    // =====================================================================

    public String criarUsuario(String nome, String email, String senha, String tipo, String cpfOuCnpj) {
        return usuarios.criarPessoaFisica(nome, email, senha, tipo, cpfOuCnpj, null, null);
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cnpj, String razaoSocial) {
        return usuarios.criarPessoaJuridica(nome, email, senha, tipo, cnpj, razaoSocial, null, null);
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cpf,
                               String plano, String mensalidade) {
        return usuarios.criarPessoaFisica(nome, email, senha, tipo, cpf, plano, mensalidade);
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cnpj,
                               String razaoSocial, String plano, String mensalidade) {
        return usuarios.criarPessoaJuridica(nome, email, senha, tipo, cnpj, razaoSocial, plano, mensalidade);
    }

    // =====================================================================
    // US2 - Consulta de usuario
    // =====================================================================

    public String getAtributoUsuario(String usuario, String atributo) {
        return usuarios.consultar(usuario, atributo);
    }

    public String getUsuarioPorEmail(String email) {
        return usuarios.buscarIdPorEmail(email);
    }

    // =====================================================================
    // US3 - Alteracao de usuario (inclui metodo de pagamento)
    // =====================================================================

    public void alterarUsuario(String usuario, String atributo, String valor) {
        usuarios.alterar(usuario, atributo, valor);
    }

    public void alterarUsuario(String usuario, String atributo, String valor1, String chave) {
        usuarios.alterarMetodoPagamento(usuario, atributo, valor1, chave);
    }

    public void alterarUsuario(String usuario, String atributo, String valor1, String numero, String validade) {
        usuarios.alterarMetodoPagamento(usuario, atributo, valor1, numero, validade);
    }

    // =====================================================================
    // US4 - Remocao de usuario
    // =====================================================================

    public void removerUsuario(String usuario) {
        usuarios.remover(usuario);
    }

    // =====================================================================
    // US5 - Cadastro de estacao
    // =====================================================================

    public String criarEstacao(String nome, String endereco, String capacidade) {
        return estacoes.criar(nome, endereco, capacidade);
    }

    // =====================================================================
    // US6 - Gestao de bicicletas na estacao
    // =====================================================================

    public void adicionarBicicleta(String estacao, String bicicleta, String tipo) {
        bicicletas.adicionar(estacao, bicicleta, tipo, null);
    }

    public void adicionarBicicleta(String estacao, String bicicleta, String tipo, String nivelBateria) {
        bicicletas.adicionar(estacao, bicicleta, tipo, nivelBateria);
    }

    public void removerBicicleta(String estacao, String bicicleta) {
        bicicletas.remover(estacao, bicicleta);
    }

    public String getAtributoBicicleta(String bicicleta, String atributo) {
        return bicicletas.consultar(bicicleta, atributo);
    }

    // =====================================================================
    // US7 - Consulta de disponibilidade
    // =====================================================================

    public String getBicicletasDisponiveis(String estacao) {
        return estacoes.getBicicletasDisponiveis(estacao);
    }

    public String getVagasDisponiveis(String estacao) {
        return estacoes.getVagasDisponiveis(estacao);
    }

    public String getEstadoBicicleta(String bicicleta) {
        return bicicletas.getEstado(bicicleta);
    }

    // =====================================================================
    // US8 - Retirada de bicicleta
    // =====================================================================

    public String retirarBicicleta(String usuario, String estacao, String bicicleta, String data) {
        return alugueis.retirar(usuario, estacao, bicicleta, data);
    }

    // =====================================================================
    // US9 - Devolucao de bicicleta
    // =====================================================================

    public void devolverBicicleta(String aluguel, String estacao, String data) {
        alugueis.devolver(aluguel, estacao, data);
    }

    // =====================================================================
    // US10 - Consulta de historico de alugueis
    // =====================================================================

    public String getHistoricoAlugueis(String usuario) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    public String getHistoricoAlugueis(String usuario, String atributo) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    /** Já necessário no Milestone 1: os testes da US8 e da US9 consultam o aluguel. */
    public String getAtributoAluguel(String aluguel, String atributo) {
        return alugueis.consultar(aluguel, atributo);
    }

    // =====================================================================
    // US11/US12 - Planos e tarifacao / criacao de novos planos
    // =====================================================================

    public String criarPlano(String nome, String franquiaMinutos, String valorMinutoExcedente, String mensalidade) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US13 - Regras de bloqueio / lancamento manual de penalidade
    // =====================================================================

    public void lancaPenalidade(String usuario, String tipo, String valor, String motivo) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US14 - Lancamento e conclusao de manutencao
    // =====================================================================

    public void lancaDefeito(String bicicleta, String descricao) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    public void concluiManutencao(String bicicleta) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US15 - Relatorios
    // =====================================================================

    public String getTotalFaturado(String dataInicial, String dataFinal) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US16 - Undo/redo
    // =====================================================================

    public void undo() {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    public void redo() {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // Ciclo de vida do sistema
    // =====================================================================

    public void zerarSistema() {
        contexto.substituirBase(new BaseDados(parametros));
    }

    public void encerrarSistema() {
        arquivo.salvar(contexto.base());
    }
}
