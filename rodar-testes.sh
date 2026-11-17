#!/bin/bash
# Compila e roda os testes de aceitacao do Bikeshare.
#   ./rodar-testes.sh        -> Milestone 1 (US1 a US9)  [padrao desta entrega]
#   ./rodar-testes.sh 16     -> ate a US16 (Milestone 2)
# Cada usN_1 roda numa JVM propria, logo apos o usN, para exercitar a persistencia entre execucoes.
set -uo pipefail
cd "$(dirname "$0")"
ate="${1:-9}"

rm -rf out && mkdir out
find src -name '*.java' | sort > .fontes.tmp
trap 'rm -f .fontes.tmp bikeshare_data.xml bikeshare_data.xml.corrompido' EXIT
"${JAVAC:-javac}" -encoding UTF-8 -d out -cp lib/easyaccept.jar @.fontes.tmp || exit 1

falhas=0; total=0
for n in $(seq 1 "$ate"); do
  for arq in "tests/us$n.txt" "tests/us${n}_1.txt"; do
    [ -f "$arq" ] || continue
    [ "$arq" = "tests/us$n.txt" ] && rm -f bikeshare_data.xml
    saida=$(java -cp out:lib/easyaccept.jar easyaccept.EasyAccept br.ufal.ic.p2.bikeshare.Facade "$arq" 2>&1)
    resumo=$(echo "$saida" | grep -E "^Test file" | head -1)
    if echo "$resumo" | grep -q "tests OK"; then
      echo "ok   $resumo"; total=$((total + $(echo "$resumo" | grep -oE '[0-9]+ tests' | grep -oE '[0-9]+')))
    else
      falhas=$((falhas+1)); echo "FALHA em $arq"; echo "$saida" | head -60
    fi
  done
done
echo
if [ "$total" -eq 0 ]; then echo "Nenhum teste foi executado (pasta tests/ encontrada?)."; exit 1; fi
[ "$falhas" -eq 0 ] && echo "US1 a US$ate: $total testes, todos passaram." || { echo "$falhas arquivo(s) com falha."; exit 1; }
