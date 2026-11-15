package br.ufal.ic.p2.bikeshare;

import br.ufal.ic.p2.bikeshare.config.Parametros;
import br.ufal.ic.p2.bikeshare.historico.GerenciadorHistorico;
import br.ufal.ic.p2.bikeshare.persistencia.ArquivoXml;
import br.ufal.ic.p2.bikeshare.repositorio.BaseDados;
import br.ufal.ic.p2.bikeshare.repositorio.ContextoSistema;
import br.ufal.ic.p2.bikeshare.servico.ServicoAlugueis;
import br.ufal.ic.p2.bikeshare.servico.ServicoBicicletas;
import br.ufal.ic.p2.bikeshare.servico.ServicoEstacoes;
import br.ufal.ic.p2.bikeshare.servico.ServicoPenalidades;
import br.ufal.ic.p2.bikeshare.servico.ServicoPlanos;
import br.ufal.ic.p2.bikeshare.servico.ServicoRelatorios;
import br.ufal.ic.p2.bikeshare.servico.ServicoUsuarios;

/**
 * Único ponto de entrada da lógica de negócio (BS01). Não decide nada: encaminha cada comando
 * ao serviço responsável. Comandos que alteram o estado passam pelo histórico, que os torna
 * atômicos e desfazíveis; consultas vão direto ao serviço.
 */
public class Facade {
    private static final String ARQUIVO_DE_DADOS = "bikeshare_data.xml";

    private final Parametros parametros = Parametros.padrao();
    private final ArquivoXml arquivo = new ArquivoXml(ARQUIVO_DE_DADOS);
    private final ContextoSistema contexto;
    private final GerenciadorHistorico historico;

    private final ServicoUsuarios usuarios;
    private final ServicoEstacoes estacoes;
    private final ServicoBicicletas bicicletas;
    private final ServicoAlugueis alugueis;
    private final ServicoPlanos planos;
    private final ServicoPenalidades penalidades;
    private final ServicoRelatorios relatorios;

    public Facade() {
        BaseDados inicial = arquivo.carregar().orElseGet(() -> new BaseDados(parametros));
        this.contexto = new ContextoSistema(parametros, inicial);
        this.historico = new GerenciadorHistorico(contexto);
        this.usuarios = new ServicoUsuarios(contexto);
        this.estacoes = new ServicoEstacoes(contexto);
        this.bicicletas = new ServicoBicicletas(contexto);
        this.alugueis = new ServicoAlugueis(contexto);
        this.planos = new ServicoPlanos(contexto);
        this.penalidades = new ServicoPenalidades(contexto);
        this.relatorios = new ServicoRelatorios(contexto);
    }

    // =====================================================================
    // US1 - Cadastro de usuario
    // =====================================================================

    public String criarUsuario(String nome, String email, String senha, String tipo, String cpfOuCnpj) {
        return historico.executar(() -> usuarios.criarPessoaFisica(nome, email, senha, tipo, cpfOuCnpj, null, null));
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cnpj, String razaoSocial) {
        return historico.executar(() ->
                usuarios.criarPessoaJuridica(nome, email, senha, tipo, cnpj, razaoSocial, null, null));
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cpf,
                               String plano, String mensalidade) {
        return historico.executar(() ->
                usuarios.criarPessoaFisica(nome, email, senha, tipo, cpf, plano, mensalidade));
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cnpj,
                               String razaoSocial, String plano, String mensalidade) {
        return historico.executar(() ->
                usuarios.criarPessoaJuridica(nome, email, senha, tipo, cnpj, razaoSocial, plano, mensalidade));
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
        historico.executar(() -> usuarios.alterar(usuario, atributo, valor));
    }

    public void alterarUsuario(String usuario, String atributo, String valor1, String chave) {
        historico.executar(() -> usuarios.alterarMetodoPagamento(usuario, atributo, valor1, chave));
    }

    public void alterarUsuario(String usuario, String atributo, String valor1, String numero, String validade) {
        historico.executar(() -> usuarios.alterarMetodoPagamento(usuario, atributo, valor1, numero, validade));
    }

    // =====================================================================
    // US4 - Remocao de usuario
    // =====================================================================

    public void removerUsuario(String usuario) {
        historico.executar(() -> usuarios.remover(usuario));
    }

    // =====================================================================
    // US5 - Cadastro de estacao
    // =====================================================================

    public String criarEstacao(String nome, String endereco, String capacidade) {
        return historico.executar(() -> estacoes.criar(nome, endereco, capacidade));
    }

    // =====================================================================
    // US6 - Gestao de bicicletas na estacao
    // =====================================================================

    public void adicionarBicicleta(String estacao, String bicicleta, String tipo) {
        historico.executar(() -> bicicletas.adicionar(estacao, bicicleta, tipo, null));
    }

    public void adicionarBicicleta(String estacao, String bicicleta, String tipo, String nivelBateria) {
        historico.executar(() -> bicicletas.adicionar(estacao, bicicleta, tipo, nivelBateria));
    }

    public void removerBicicleta(String estacao, String bicicleta) {
        historico.executar(() -> bicicletas.remover(estacao, bicicleta));
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
        return historico.executar(() -> alugueis.retirar(usuario, estacao, bicicleta, data));
    }

    // =====================================================================
    // US9 - Devolucao de bicicleta
    // =====================================================================

    public void devolverBicicleta(String aluguel, String estacao, String data) {
        historico.executar(() -> alugueis.devolver(aluguel, estacao, data));
    }

    // =====================================================================
    // US10 - Consulta de historico de alugueis
    // =====================================================================

    public String getHistoricoAlugueis(String usuario) {
        return alugueis.historico(usuario, null);
    }

    public String getHistoricoAlugueis(String usuario, String atributo) {
        return alugueis.historico(usuario, atributo);
    }

    public String getAtributoAluguel(String aluguel, String atributo) {
        return alugueis.consultar(aluguel, atributo);
    }

    // =====================================================================
    // US11/US12 - Planos e tarifacao / criacao de novos planos
    // =====================================================================

    public String criarPlano(String nome, String franquiaMinutos, String valorMinutoExcedente, String mensalidade) {
        return historico.executar(() -> planos.criar(nome, franquiaMinutos, valorMinutoExcedente, mensalidade));
    }

    // =====================================================================
    // US13 - Regras de bloqueio / lancamento manual de penalidade
    // =====================================================================

    public void lancaPenalidade(String usuario, String tipo, String valor, String motivo) {
        historico.executar(() -> penalidades.lancar(usuario, tipo, valor, motivo));
    }

    // =====================================================================
    // US14 - Lancamento e conclusao de manutencao
    // =====================================================================

    public void lancaDefeito(String bicicleta, String descricao) {
        historico.executar(() -> bicicletas.lancarDefeito(bicicleta, descricao));
    }

    public void concluiManutencao(String bicicleta) {
        historico.executar(() -> bicicletas.concluirManutencao(bicicleta));
    }

    // =====================================================================
    // US15 - Relatorios
    // =====================================================================

    public String getTotalFaturado(String dataInicial, String dataFinal) {
        return relatorios.totalFaturado(dataInicial, dataFinal);
    }

    // =====================================================================
    // US16 - Undo/redo
    // =====================================================================

    public void undo() {
        historico.desfazer();
    }

    public void redo() {
        historico.refazer();
    }

    // =====================================================================
    // Ciclo de vida do sistema
    // =====================================================================

    public void zerarSistema() {
        historico.executar(() -> contexto.substituirBase(new BaseDados(parametros)));
    }

    public void encerrarSistema() {
        arquivo.salvar(contexto.base());
    }
}
