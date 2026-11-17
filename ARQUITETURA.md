# Arquitetura do Bikeshare

Pacote raiz: `br.ufal.ic.p2.bikeshare`. **Estado desta entrega: Milestone 1 (US1 a US9).**
Para compilar e rodar os testes: `./rodar-testes.sh` (Linux/macOS) ou `.\rodar-testes.ps1` (Windows).

## Camadas

| Pacote | Papel |
|---|---|
| `Facade` | Único ponto de entrada (BS01). Só encaminha para os serviços. |
| `servico` | Um serviço por assunto (usuários, estações, bicicletas, aluguéis). Validam a ordem das entradas e orquestram o domínio. |
| `modelo` | Regras do domínio, sem conhecer serviços nem arquivos. |
| `repositorio` | Coleções por entidade, `BaseDados` (todo o estado) e `ContextoSistema` (estado corrente). |
| `persistencia` | Serialização e arquivo XML. |
| `excecoes` | Hierarquia de erros e catálogo único de mensagens (BS05). |
| `config` | `Parametros`: tarifas, franquia, carência, tempo máximo, bateria mínima, penalidade (BS04). |

## Padrões de projeto

| Padrão | Onde | Problema resolvido |
|---|---|---|
| **State** | `EstadoBicicleta` (enum) | Disponível / em uso / em manutenção declaram, cada um, o que aceitam e recusam. |
| **Strategy** | `Plano` → `PlanoAvulso`, `PlanoAssinante` | O cálculo da tarifa varia por plano. |
| **Template Method** | `Usuario.consultar`, `Bicicleta.consultar` | A base resolve os atributos comuns; cada subtipo trata só os seus (cpf × cnpj/razão social; bateria). |
| **Factory** | `FabricaUsuarios`, `FabricaBicicletas`, `FabricaMetodosPagamento` | Escolha do subtipo e validação de criação num lugar só. |
| **Composição** | `Usuario` = `Assinatura` + `ContaPagamento` + `Pendencias` | Plano, pagamento e penalidades têm dono próprio; `Usuario` cuida de identificação e aluguel. |
| **Polimorfismo** | `MetodoPagamento`, `Bicicleta`, `Usuario` | Cartão/pix/saldo, adicional da elétrica e PF/PJ sem `instanceof`. |

## Decisões que valem registrar

* **Parâmetros configuráveis (BS04)**: `bikeshare.properties` sobrescreve tarifa, franquia, carência, tempo máximo, bateria mínima, penalidade e saldo mínimo, sem recompilar. Sem o arquivo valem os padrões do enunciado; um valor inválido impede o sistema de iniciar (`ConfiguracaoException`).
* **Serviços atômicos**: toda validação acontece antes de qualquer alteração, e o identificador (U1, E1, A1...) só é consumido quando a entidade é de fato adicionada. Uma operação recusada não deixa efeito parcial nem buraco na numeração.
* **Saldo pré-carregado** pertence ao usuário (`ContaPagamento`) e sobrevive à troca de método de pagamento.
* **Mensalidade** é do usuário, não do plano: repetir o plano atual preserva o valor contratado; trocar de plano assume a mensalidade padrão do novo.
* **Bicicleta em manutenção** ocupa vaga física, mas não conta como disponível. Elétrica abaixo da bateria mínima também não.
* **Aluguéis referenciam usuário e bicicleta por identificador**, então continuam consultáveis depois que o usuário é removido.
* **Aluguel expirado** (acima do tempo máximo): sem tarifa, bicicleta vai para manutenção e o usuário recebe uma penalidade de atraso, ficando inadimplente.

## Persistência

O enunciado fala em XML e, na frase seguinte, exige `java.io.Serializable`. As duas coisas foram atendidas juntas. O arquivo `bikeshare_data.xml` tem duas partes:

* `<resumo>`: usuários (com suas penalidades), estações, bicicletas e aluguéis em elementos XML legíveis. Serve para inspeção; nunca é lido de volta.
* `<estadoSerializado>`: o estado completo, gerado com a serialização nativa do Java (`Serializable`) e embalado em base64. É a fonte da verdade no carregamento.

Se o arquivo estiver ilegível, ele é preservado como `.corrompido` e o sistema começa vazio, com aviso em `stderr`.

## Fica para o Milestone 2 (US10 a US16)

Na `Facade` estas operações seguem como no stub original (`UnsupportedOperationException`): histórico de aluguéis, criação de planos, lançamento de penalidades, manutenção (defeito/conclusão), relatório de faturamento e undo/redo. O `getAtributoAluguel` já está pronto porque os testes da US8 e da US9 o usam.

## Como rodar os testes

* `./rodar-testes.sh` / `.\rodar-testes.ps1` compilam e rodam US1 a US9 (padrão desta entrega). Passando `16`, rodam até a US16. Cada `usN_1` roda numa JVM própria, logo após `usN`, para exercitar a persistência entre execuções.
* A classe `Main` do monitor roda todos os arquivos numa só JVM; nesta entrega, US10 a US16 falham por serem do Milestone 2.
