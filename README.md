# Bikeshare | Programação 02
O objetivo do BIKESHARE é construir, como projeto da disciplina de Programação 02, a lógica de negócio completa de um sistema de bicicletas compartilhadas, acessada por uma Facade e validada por testes de aceitação automatizados sem qualquer camada de interface gráfica ou de persistência em banco de dados.

<p align="center">
  <img src="https://user-images.githubusercontent.com/91018438/204195385-acc6fcd4-05a7-4f25-87d1-cb7d5cc5c852.png" alt="animated" />
</p>

 ## Sobre o projeto

Assim como o WePayU tinha por propósito construir um sistema de folha de pagamento, o **BIKESHARE** tem por propósito construir um sistema de gerenciamento de um serviço de bicicletas compartilhadas (bike-sharing), no estilo dos sistemas usados em várias cidades brasileiras (ex.: Bike Itaú, Tembici).

O sistema mantém um cadastro de **usuários**, **estações** (pontos de retirada/devolução) e **bicicletas**. Um usuário retira uma bicicleta disponível em uma estação, usa-a por um período (um **aluguel**, também chamado de "corrida") e a devolve em qualquer estação com vaga livre. O sistema deve:

- Controlar a disponibilidade de bicicletas em cada estação (bicicletas presentes × vagas livres);
- Impedir a retirada de bicicletas indisponíveis (em uso, em manutenção) e de estações sem bicicletas;
- Impedir a devolução em estações sem vagas;
- Calcular o valor a cobrar por cada aluguel, de acordo com o **plano** do usuário (avulso ou assinante) e com a duração da corrida;
- Impedir que um usuário **inadimplente** (com pendências financeiras) faça uma nova retirada;
- Impedir que um usuário com um aluguel em andamento inicie um segundo aluguel simultâneo;
- Registrar o histórico de aluguéis de cada usuário e gerar relatórios (faturamento, bicicletas disponíveis/alugadas);
- Permitir desfazer/refazer operações (undo/redo);
- Rodar de forma **totalmente desacoplada de interface gráfica** — assim como no WePayU, só será implementada a lógica de negócio, acessada por uma **Façade** e validada por scripts de teste de aceitação (EasyAccept).

Está dentro do escopo: cadastro, consulta, alteração e remoção de usuários e estações; gestão de bicicletas convencionais e elétricas; retirada e devolução de bicicletas, com cálculo de tarifa; planos de tarifação avulso, assinante e planos customizados criados em tempo de execução; regras de bloqueio por inadimplência, estação cheia ou vazia e bicicleta indisponível; lançamento e conclusão de manutenção; relatórios de disponibilidade e faturamento; desfazer/refazer operações; e persistência do estado do sistema entre execuções.

Está fora do escopo: qualquer interface gráfica ou web, integração com um gateway de pagamento real, geolocalização	e qualquer forma de concorrência.		

## Glossário 

| Termo | Definição |
|---|---|
| **Usuário** | Pessoa cadastrada no sistema, identificada por um identificador único gerado automaticamente. Pode alugar bicicletas. |
| **Estação** | Ponto físico com um número fixo de vagas (`capacidade`), onde bicicletas ficam disponíveis para retirada e podem ser devolvidas. |
| **Bicicleta** | Veículo compartilhado, identificado unicamente, que pertence a uma estação enquanto não está em uso. |
| **Vaga** | Espaço disponível em uma estação para receber uma bicicleta. `vagas livres = capacidade − bicicletas presentes`. |
| **Aluguel (ou corrida)** | Período de uso de uma bicicleta por um usuário, do momento da retirada até a devolução. |
| **Plano** | Modalidade de cobrança contratada pelo usuário: *avulso* (paga por minuto/hora a cada corrida) ou *assinante* (paga mensalidade e tem franquia de minutos gratuitos). |
| **Tarifa** | Valor cobrado por um aluguel, calculado de acordo com o plano do usuário e a duração da corrida. |
| **Penalidade / taxa extra** | Cobrança adicional lançada sobre um usuário (ex.: atraso na devolução, dano à bicicleta, devolução fora da área de cobertura). Análoga à "taxa de serviço" do sindicato no WePayU. |
| **Inadimplência** | Situação de um usuário que possui pendências financeiras não quitadas; nesse estado, não pode iniciar novos aluguéis. |
| **Estado da bicicleta** | Condição operacional da bicicleta: disponível, em uso, em manutenção ou removida. |
| **Estado do aluguel** | Condição de uma corrida: em andamento, finalizada, cancelada ou expirada. |
| **Bicicleta convencional / elétrica** | Os dois tipos fixos de bicicleta suportados pelo sistema; a elétrica possui nível de bateria e cobrança adicional por minuto. |
| **Usuário pessoa física / jurídica** | Os dois tipos fixos de usuário; pessoa física se identifica por CPF, pessoa jurídica por CNPJ/razão social e pode ter fatura consolidada. |
| **Método de pagamento** | Forma pela qual o usuário paga suas cobranças: cartão, Pix ou saldo pré-carregado. |

---

---

## Estado da entrega

**Milestone 1 (US1 a US9)** concluído: 834 verificações dos testes de aceitação passando, 0 falhas.

| Milestone | User Stories | Situação |
|---|---|---|
| 1 | US1 a US9 (usuários, estações, bicicletas, aluguel, tarifa, pagamento, persistência) | Implementado e testado |
| 2 | US10 a US16 (histórico, criação de planos, penalidades, manutenção, faturamento, undo/redo) | Pendente: os métodos correspondentes da `Facade` ainda lançam `UnsupportedOperationException` |

Por isso, rodar US10 a US16 hoje **falha de propósito**. Isso é esperado nesta entrega.

---

## Como executar

### Requisitos

- **JDK 17 ou superior** (precisa do compilador `javac`, não só do JRE). O projeto usa `record`, então Java 11 não compila. Testado com JDK 21.
- Nenhuma outra dependência: o `easyaccept.jar` já está em `lib/`.

Confira com:

```bash
java -version
javac -version
```

> **Rode sempre a partir da pasta raiz do projeto** (a que contém `src/`, `tests/` e `bikeshare.properties`). O sistema procura `bikeshare.properties` e grava `bikeshare_data.xml` no diretório atual. Em outra pasta, os parâmetros do `.properties` são ignorados (valem os padrões) e a persistência vai parar em outro lugar. Os scripts já entram na pasta certa sozinhos.

### Forma recomendada: script

Linux / macOS:

```bash
chmod +x rodar-testes.sh   # só na primeira vez
./rodar-testes.sh          # US1 a US9 (Milestone 1)
./rodar-testes.sh 16       # US1 a US16 (Milestone 2, quando existir)
```

Windows (PowerShell):

```powershell
.\rodar-testes.ps1         # US1 a US9
.\rodar-testes.ps1 16      # US1 a US16
```

Se o PowerShell bloquear o script, libere só para a sessão atual:

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
```

O script:

1. apaga `out/` e compila todos os `.java` de `src/`;
2. para cada US, roda `usN.txt` e, em seguida, `usN_1.txt` **em outra JVM**, sem apagar o arquivo de persistência entre os dois;
3. apaga `bikeshare_data.xml` antes de cada `usN.txt`, para começar de um estado limpo;
4. imprime `ok` ou `FALHA` por arquivo e o total ao final;
5. remove os arquivos temporários.

### Saída esperada (US1 a US9)

```text
ok   Test file tests/us1.txt: 71 tests OK
ok   Test file tests/us1_1.txt: 14 tests OK
ok   Test file tests/us2.txt: 46 tests OK
ok   Test file tests/us3.txt: 129 tests OK
ok   Test file tests/us3_1.txt: 17 tests OK
ok   Test file tests/us4.txt: 37 tests OK
ok   Test file tests/us4_1.txt: 15 tests OK
ok   Test file tests/us5.txt: 42 tests OK
ok   Test file tests/us5_1.txt: 11 tests OK
ok   Test file tests/us6.txt: 74 tests OK
ok   Test file tests/us6_1.txt: 14 tests OK
ok   Test file tests/us7.txt: 39 tests OK
ok   Test file tests/us8.txt: 106 tests OK
ok   Test file tests/us8_1.txt: 17 tests OK
ok   Test file tests/us9.txt: 180 tests OK
ok   Test file tests/us9_1.txt: 22 tests OK

US1 a US9: 834 testes, todos passaram.
```

### Execução manual

Linux / macOS:

```bash
rm -rf out && mkdir out
javac -encoding UTF-8 -d out -cp lib/easyaccept.jar $(find src -name '*.java')

# um arquivo de teste
java -cp "out:lib/easyaccept.jar" easyaccept.EasyAccept br.ufal.ic.p2.bikeshare.Facade tests/us1.txt
```

Windows (PowerShell):

```powershell
Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue
New-Item -ItemType Directory out | Out-Null
javac -encoding UTF-8 -d out -cp lib/easyaccept.jar (Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName })

java -cp "out;lib/easyaccept.jar" easyaccept.EasyAccept br.ufal.ic.p2.bikeshare.Facade tests/us1.txt
```

### Rodando pela IDE

A classe `src/Main.java` chama o EasyAccept para todos os arquivos `tests/us*.txt`, em uma única JVM. Nesta entrega, as US10 a US16 aparecem como falha, por serem do Milestone 2. Para validar o Milestone 1, prefira o script.

### Problemas comuns

| Sintoma | Causa provável | Solução |
|---|---|---|
| `javac: command not found` | Só o JRE está instalado | Instalar um JDK 17+ |
| `records are not supported` | JDK anterior ao 16 | Atualizar para JDK 17+ |
| Falha só em `usN_1.txt` | O arquivo de persistência foi apagado entre `usN` e `usN_1` | Rodar `usN` e `usN_1` em sequência, sem apagar `bikeshare_data.xml` |
| Caracteres estranhos nas mensagens | Codificação do terminal | Compilar com `-encoding UTF-8` e rodar com `-Dfile.encoding=UTF-8` |
| Alterações em `bikeshare.properties` não surtem efeito | Comando executado fora da raiz do projeto | Rodar da raiz (ou usar o script) |
| `ConfiguracaoException` ao iniciar | Valor inválido em `bikeshare.properties` | Corrigir ou remover a chave |

---

## Persistência nos testes

Os arquivos `usN_1.txt` verificam que o estado criado em `usN.txt` sobreviveu ao encerramento da JVM. Por isso:

- `usN_1.txt` deve rodar **logo depois** de `usN.txt`, em **outra JVM**;
- o arquivo `bikeshare_data.xml`, criado na pasta onde o comando é executado, **não pode ser apagado** entre os dois;
- antes de começar outra US, o arquivo pode ser removido para garantir estado limpo (o script já faz isso).

O arquivo está no `.gitignore`. Se ficar ilegível, é preservado como `bikeshare_data.xml.corrompido` e o sistema começa vazio, com aviso em `stderr`.

---

## Configuração

Os parâmetros de tarifação e limites ficam em `bikeshare.properties` (valor por minuto, franquia do assinante, adicional da elétrica, carência, tempo máximo, penalidade, saldo mínimo). Podem ser alterados **sem recompilar**. Se uma chave for removida, vale o padrão do enunciado.

---

## Estrutura do projeto

```text
.
├── README.md
├── ARQUITETURA.md            # padrões de projeto e decisões
├── bikeshare.properties      # parâmetros de tarifação e limites
├── rodar-testes.sh / .ps1    # compila e roda a suíte
├── lib/easyaccept.jar
├── tests/                    # usN.txt e usN_1.txt (EasyAccept)
├── relatorio/                # reservada ao relatório final
└── src/
    ├── Main.java             # roda todos os testes numa só JVM
    └── br/ufal/ic/p2/bikeshare/
        ├── Facade.java       # único ponto de entrada; só encaminha
        ├── servico/          # um serviço por assunto
        ├── modelo/           # regras do domínio
        ├── repositorio/      # coleções e estado do sistema
        ├── persistencia/     # serialização e arquivo XML
        ├── excecoes/         # hierarquia de erros e catálogo de mensagens
        ├── config/           # leitura de bikeshare.properties
        └── util/
```

Arquitetura, padrões usados (State, Strategy, Template Method, Factory, composição) e decisões de projeto estão em [ARQUITETURA.md](ARQUITETURA.md).
