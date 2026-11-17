# Compila e roda os testes de aceitacao do Bikeshare no Windows PowerShell.
#   .\rodar-testes.ps1        -> Milestone 1 (US1 a US9)  [padrao desta entrega]
#   .\rodar-testes.ps1 16     -> ate a US16 (Milestone 2)
# Cada usN_1 roda numa JVM propria, logo apos o usN, para exercitar a persistencia entre execucoes.
param([int]$Ate = 9)
Set-Location $PSScriptRoot

Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue
New-Item -ItemType Directory out | Out-Null
$fontes = Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d out -cp lib/easyaccept.jar $fontes
if ($LASTEXITCODE -ne 0) { exit 1 }

$falhas = 0; $total = 0
foreach ($n in 1..$Ate) {
  foreach ($arq in @("tests/us$n.txt", "tests/us${n}_1.txt")) {
    if (-not (Test-Path $arq)) { continue }
    if ($arq -eq "tests/us$n.txt") { Remove-Item bikeshare_data.xml -ErrorAction SilentlyContinue }
    $saida = java "-Dfile.encoding=UTF-8" -cp "out;lib/easyaccept.jar" easyaccept.EasyAccept br.ufal.ic.p2.bikeshare.Facade $arq 2>&1 | Out-String
    $linha = "$($saida -split "`n" | Select-String '^Test file' | Select-Object -First 1)"
    if ($linha -match "(\d+) tests OK") { Write-Host "ok   $linha" -ForegroundColor Green; $total += [int]$Matches[1] }
    else { $falhas++; Write-Host "FALHA em $arq" -ForegroundColor Red; Write-Host $saida }
  }
}
Remove-Item bikeshare_data.xml, bikeshare_data.xml.corrompido -ErrorAction SilentlyContinue
Write-Host ""
if ($total -eq 0) { Write-Host "Nenhum teste foi executado (pasta tests/ encontrada?)." -ForegroundColor Red; exit 1 }
if ($falhas -eq 0) { Write-Host "US1 a US${Ate}: $total testes, todos passaram." -ForegroundColor Green }
else { Write-Host "$falhas arquivo(s) com falha." -ForegroundColor Red; exit 1 }
