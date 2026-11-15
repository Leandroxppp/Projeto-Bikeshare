package br.ufal.ic.p2.bikeshare.excecoes;

/**
 * Catálogo único das mensagens de erro (BS05). Todo comando que trata a mesma situação usa a
 * mesma constante, o que garante mensagens consistentes.
 */
public final class Mensagens {
    private Mensagens() {
    }

    // ---- usuario ----
    public static final String ID_USUARIO_NULO = "Identificacao do usuario nao pode ser nula.";
    public static final String USUARIO_NAO_EXISTE = "Usuario nao existe.";
    public static final String NOME_NULO = "Nome nao pode ser nulo.";
    public static final String EMAIL_NULO = "Email nao pode ser nulo.";
    public static final String EMAIL_INVALIDO = "Email invalido.";
    public static final String EMAIL_JA_CADASTRADO = "Email ja cadastrado.";
    public static final String SENHA_NULA = "Senha nao pode ser nula.";
    public static final String TIPO_INVALIDO = "Tipo invalido.";
    public static final String CPF_NULO = "CPF nao pode ser nulo.";
    public static final String CNPJ_NULO = "CNPJ nao pode ser nulo.";
    public static final String RAZAO_SOCIAL_NULA = "Razao social nao pode ser nula.";
    public static final String USUARIO_NAO_EH_PF = "Usuario nao eh pessoa fisica.";
    public static final String USUARIO_NAO_EH_PJ = "Usuario nao eh pessoa juridica.";
    public static final String ATRIBUTO_NAO_EXISTE = "Atributo nao existe.";
    public static final String ATRIBUTO_NAO_ALTERAVEL = "Atributo nao pode ser alterado.";
    public static final String USUARIO_COM_ALUGUEL = "Nao e possivel remover usuario com aluguel em andamento.";
    public static final String USUARIO_INADIMPLENTE = "Usuario inadimplente.";
    public static final String USUARIO_JA_TEM_ALUGUEL = "Usuario ja possui um aluguel em andamento.";

    // ---- plano ----
    public static final String PLANO_INVALIDO = "Plano invalido.";
    public static final String PLANO_SEM_MENSALIDADE = "Plano nao possui mensalidade.";
    public static final String PLANO_JA_EXISTE = "Plano ja existe.";
    public static final String NOME_PLANO_NULO = "Nome do plano nao pode ser nulo.";
    public static final String MENSALIDADE_NULA = "Mensalidade nao pode ser nula.";
    public static final String MENSALIDADE_NUMERICA = "Mensalidade deve ser numerica.";
    public static final String MENSALIDADE_NEGATIVA = "Mensalidade deve ser nao-negativa.";
    public static final String FRANQUIA_NULA = "Franquia nao pode ser nula.";
    public static final String FRANQUIA_NUMERICA = "Franquia deve ser numerica.";
    public static final String FRANQUIA_NEGATIVA = "Franquia deve ser nao-negativa.";
    public static final String EXCEDENTE_NULO = "Valor do minuto excedente nao pode ser nulo.";
    public static final String EXCEDENTE_NUMERICO = "Valor do minuto excedente deve ser numerico.";
    public static final String EXCEDENTE_NEGATIVO = "Valor do minuto excedente deve ser nao-negativo.";

    // ---- pagamento ----
    public static final String USUARIO_NAO_USA_CARTAO = "Usuario nao usa cartao.";
    public static final String USUARIO_NAO_USA_PIX = "Usuario nao usa pix.";
    public static final String USUARIO_NAO_USA_SALDO = "Usuario nao usa saldo pre-carregado.";
    public static final String NUMERO_CARTAO_NULO = "Numero do cartao nao pode ser nulo.";
    public static final String VALIDADE_CARTAO_NULA = "Validade do cartao nao pode ser nula.";
    public static final String VALIDADE_CARTAO_INVALIDA = "Validade do cartao invalida.";
    public static final String CHAVE_PIX_NULA = "Chave pix nao pode ser nula.";
    public static final String METODO_PAGAMENTO_INVALIDO = "Valor de metodo de pagamento invalido.";
    public static final String METODO_NAO_CADASTRADO = "Metodo de pagamento nao cadastrado.";
    public static final String SALDO_INSUFICIENTE = "Saldo insuficiente.";
    public static final String VALOR_NULO = "Valor nao pode ser nulo.";
    public static final String VALOR_NUMERICO = "Valor deve ser numerico.";
    public static final String VALOR_POSITIVO = "Valor deve ser positivo.";

    // ---- penalidade ----
    public static final String TIPO_PENALIDADE_INVALIDO = "Tipo de penalidade invalido.";
    public static final String MOTIVO_NULO = "Motivo nao pode ser nulo.";

    // ---- estacao ----
    public static final String ID_ESTACAO_NULO = "Identificacao da estacao nao pode ser nula.";
    public static final String ESTACAO_NAO_EXISTE = "Estacao nao existe.";
    public static final String ENDERECO_NULO = "Endereco nao pode ser nulo.";
    public static final String CAPACIDADE_NULA = "Capacidade nao pode ser nula.";
    public static final String CAPACIDADE_NUMERICA = "Capacidade deve ser numerica.";
    public static final String CAPACIDADE_POSITIVA = "Capacidade deve ser positiva.";
    public static final String ESTACAO_SEM_VAGAS = "Estacao sem vagas disponiveis.";
    public static final String ESTACAO_CHEIA = "Estacao cheia.";

    // ---- bicicleta ----
    public static final String ID_BICICLETA_NULO = "Identificacao da bicicleta nao pode ser nula.";
    public static final String BICICLETA_NAO_EXISTE = "Bicicleta nao existe.";
    public static final String BICICLETA_JA_EXISTE = "Bicicleta ja existe.";
    public static final String BICICLETA_SEM_BATERIA = "Bicicleta nao possui bateria.";
    public static final String BATERIA_NULA = "Nivel de bateria nao pode ser nulo.";
    public static final String BATERIA_NUMERICA = "Nivel de bateria deve ser numerico.";
    public static final String BATERIA_FAIXA = "Nivel de bateria deve estar entre 0 e 100.";
    public static final String BATERIA_INSUFICIENTE = "Nivel de bateria insuficiente.";
    public static final String BICICLETA_NAO_NESTA_ESTACAO = "Bicicleta nao esta nesta estacao.";
    public static final String BICICLETA_INDISPONIVEL = "Bicicleta nao esta disponivel.";
    public static final String BICICLETA_JA_EM_MANUTENCAO = "Bicicleta ja esta em manutencao.";
    public static final String BICICLETA_NAO_EM_MANUTENCAO = "Bicicleta nao esta em manutencao.";
    public static final String DEFEITO_EM_BICICLETA_EM_USO = "Nao e possivel lancar defeito em bicicleta em uso.";
    public static final String DESCRICAO_NULA = "Descricao nao pode ser nula.";

    // ---- aluguel ----
    public static final String ID_ALUGUEL_NULO = "Identificacao do aluguel nao pode ser nula.";
    public static final String ALUGUEL_NAO_EXISTE = "Aluguel nao existe.";
    public static final String ALUGUEL_EM_ANDAMENTO = "Aluguel ainda esta em andamento.";
    public static final String ALUGUEL_NAO_EM_ANDAMENTO = "Aluguel nao esta em andamento.";
    public static final String DEVOLUCAO_ANTERIOR_A_RETIRADA = "Data de devolucao anterior a data de retirada.";
    public static final String DATA_NULA = "Data nao pode ser nula.";
    public static final String DATA_INVALIDA = "Data invalida.";
    public static final String DATA_FINAL_ANTERIOR = "Data final anterior a data inicial.";

    // ---- historico / persistencia ----
    public static final String NADA_A_DESFAZER = "Nao ha comando a desfazer.";
    public static final String NADA_A_REFAZER = "Nao ha comando a refazer.";
    public static final String FALHA_AO_GRAVAR = "Nao foi possivel gravar os dados do sistema.";
    public static final String FALHA_AO_LER = "Nao foi possivel ler os dados do sistema.";
}
