import easyaccept.EasyAccept;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Roda todos os testes de aceitação (us1 a us16), igual ao {@code rodar-testes.sh}:
 * <ul>
 *   <li>cada arquivo roda numa JVM separada, para que {@code usN_1} realmente leia o que
 *       {@code usN} gravou em disco (teste de persistência entre execuções);</li>
 *   <li>o arquivo de dados é apagado antes de cada {@code usN}, nunca antes de {@code usN_1}.</li>
 * </ul>
 * Uso interno: {@code java Main <arquivo.txt>} roda um único arquivo de teste na JVM atual.
 */
public class Main {
    private static final String FACADE = "br.ufal.ic.p2.bikeshare.Facade";
    private static final String ARQUIVO_DE_DADOS = "bikeshare_data.xml";

    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            EasyAccept.main(new String[]{FACADE, args[0]});
            return;
        }

        int falhas = 0;
        for (int n = 1; n <= 16; n++) {
            for (String arquivo : new String[]{"tests/us" + n + ".txt", "tests/us" + n + "_1.txt"}) {
                if (!new File(arquivo).isFile()) {
                    continue;
                }
                if (arquivo.equals("tests/us" + n + ".txt")) {
                    new File(ARQUIVO_DE_DADOS).delete();
                }
                String saida = rodarEmNovaJvm(arquivo);
                String resumo = saida.lines().filter(l -> l.startsWith("Test file")).findFirst().orElse("");
                if (resumo.contains("tests OK")) {
                    System.out.println("ok   " + resumo);
                } else {
                    falhas++;
                    System.out.println("FALHA em " + arquivo);
                    saida.lines().limit(60).forEach(System.out::println);
                }
            }
        }
        new File(ARQUIVO_DE_DADOS).delete();
        new File(ARQUIVO_DE_DADOS + ".corrompido").delete();

        System.out.println();
        System.out.println(falhas == 0 ? "Todos os testes passaram." : falhas + " arquivo(s) com falha.");
        System.exit(falhas == 0 ? 0 : 1);
    }

    private static String rodarEmNovaJvm(String arquivo) throws Exception {
        String java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        List<String> comando = new ArrayList<>(List.of(
                java, "-cp", System.getProperty("java.class.path"), "Main", arquivo));
        Process processo = new ProcessBuilder(comando).redirectErrorStream(true).start();
        StringBuilder saida = new StringBuilder();
        try (BufferedReader leitor = new BufferedReader(
                new InputStreamReader(processo.getInputStream(), StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                saida.append(linha).append('\n');
            }
        }
        processo.waitFor();
        return saida.toString();
    }
}
