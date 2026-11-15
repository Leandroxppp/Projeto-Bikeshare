#!/bin/bash
# Compila todos os fontes e roda os testes de aceitacao (us1 a us16). Cada usN_1 roda numa JVM
# separada, logo depois do usN, para exercitar a persistencia entre execucoes.
set -uo pipefail
cd "$(dirname "$0")"

rm -rf out && mkdir out
find src -name '*.java' | sort > .fontes.tmp
trap 'rm -f .fontes.tmp bikeshare_data.xml bikeshare_data.xml.corrompido' EXIT
"${JAVAC:-javac}" -encoding UTF-8 -d out -cp lib/easyaccept.jar @.fontes.tmp || exit 1

falhas=0
for n in $(seq 1 16); do
  for arq in "tests/us$n.txt" "tests/us${n}_1.txt"; do
    [ -f "$arq" ] || continue
    [ "$arq" = "tests/us$n.txt" ] && rm -f bikeshare_data.xml
    saida=$(java -cp out:lib/easyaccept.jar easyaccept.EasyAccept br.ufal.ic.p2.bikeshare.Facade "$arq" 2>&1)
    resumo=$(echo "$saida" | grep -E "^Test file" | head -1)
    if echo "$resumo" | grep -q "tests OK"; then
      echo "ok   $resumo"
    else
      falhas=$((falhas+1)); echo "FALHA em $arq"; echo "$saida" | head -60
    fi
  done
done
echo
[ "$falhas" -eq 0 ] && echo "Todos os testes passaram." || { echo "$falhas arquivo(s) com falha."; exit 1; }
