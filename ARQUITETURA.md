# Arquitetura do Bikeshare

Pacote raiz: `br.ufal.ic.p2.bikeshare`. Para rodar tudo: `./rodar-testes.sh` (ou `Main`).

## Camadas

| Pacote | Papel |
|---|---|
| `Facade` | Único ponto de entrada (BS01). Só encaminha; comandos que alteram estado passam pelo histórico. |
| `servico` | Um serviço por assunto: usuários, estações, bicicletas, aluguéis, planos, penalidades, relatórios. Validam a ordem das entradas e orquestram o domínio. |
| `modelo` | Regras do domínio, sem conhecer serviços nem arquivos. |
| `repositorio` | Coleções por entidade (`Repositorio<T>`), `BaseDados` (todo o estado) e `ContextoSistema` (estado corrente). |
| `historico` | Desfazer/refazer. |
| `persistencia` | Serialização e arquivo XML. |
| `excecoes` | Hierarquia de erros e catálogo único de mensagens. |
| `config` | `Parametros`: tarifas, franquia, carência, tempo máximo, bateria mínima, penalidade (BS04). |

## Padrões de projeto

| Padrão | Onde | Problema resolvido |
|---|---|---|
| **State** | `EstadoBicicleta` (enum com comportamento) | Disponível / em uso / em manutenção aceitam transições diferentes e recusam as demais com a mensagem certa, sem `if` espalhado. |
| **Strategy** | `Plano` → `PlanoAvulso`, `PlanoAssinante`, `PlanoCustomizado` | O cálculo da tarifa varia por plano; planos novos nascem em tempo de execução sem tocar no serviço. |
| **Template Method** | `Usuario.consultar` / `Bicicleta.consultar` | A base resolve os atributos comuns; cada subtipo trata só os seus (cpf × cnpj/razão social; bateria). |
| **Factory** | `FabricaUsuarios`, `FabricaBicicletas`, `FabricaMetodosPagamento`, `FabricaPenalidades` | Escolha do subtipo e validação de criação num lugar só. |
| **Memento** | `GerenciadorHistorico` | Fotografia serializada do estado antes de cada comando: desfazer, refazer e rollback de comando que falha. |
| **Polimorfismo** | `MetodoPagamento`, `Penalidade`, `Bicicleta`, `Usuario` | Cartão/pix/saldo, tipos de penalidade, adicional da elétrica e PF/PJ sem `instanceof`. |

## Decisões que valem registrar

* **Atomicidade**: todo comando que altera estado tira uma fotografia antes. Se lança exceção, o estado é restaurado (nenhum efeito parcial, nenhum identificador consumido) e o comando não entra no histórico.
* **Zerar é desfazível** e não limpa o histórico; o histórico não é persistido (vive só em memória), como pedem `us16.txt` e `us16_1.txt`.
* **Saldo pré-carregado** pertence ao usuário (`carteira`) e sobrevive à troca de método de pagamento.
* **Mensalidade** é do usuário, não do plano: repetir o plano atual preserva o valor contratado; trocar de plano assume a mensalidade padrão do novo.
* **Faturamento** soma `valorCobrado` das corridas finalizadas pela data da devolução; mensalidades e penalidades não entram.
* **Bicicleta em manutenção** ocupa vaga física, mas não conta como disponível. Elétrica abaixo da bateria mínima também não.
* **Aluguéis referenciam usuário e bicicleta por identificador**, então continuam consultáveis depois que o usuário é removido.

## Persistência

O enunciado fala em XML e, na frase seguinte, exige `java.io.Serializable`. As duas coisas foram atendidas juntas. O arquivo `bikeshare_data.xml` tem duas partes:

* `<resumo>`: usuários, estações, bicicletas e aluguéis em elementos XML legíveis (um elemento por entidade, com os atributos principais). Serve para inspeção; nunca é lido de volta.
* `<estadoSerializado>`: o estado completo, gerado com a serialização nativa do Java (`Serializable`) e embalado em base64. É a fonte da verdade no carregamento.

Se o arquivo estiver ilegível, ele é preservado como `.corrompido` e o sistema começa vazio, com aviso em `stderr`.

## Como rodar os testes

* `./rodar-testes.sh` compila e roda tudo. Cada `usN_1` roda numa JVM separada, logo após `usN`, para exercitar a persistência entre execuções.
* `Main` faz o mesmo: roda cada arquivo de teste numa JVM própria e apaga o arquivo de dados apenas antes de cada `usN` (nunca antes de `usN_1`). Os dois caminhos validam a persistência da mesma forma.
