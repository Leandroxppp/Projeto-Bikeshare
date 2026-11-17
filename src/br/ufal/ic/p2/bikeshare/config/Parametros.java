package br.ufal.ic.p2.bikeshare.config;

import br.ufal.ic.p2.bikeshare.excecoes.ConfiguracaoException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Parâmetros de tarifação e limites de negócio (BS04). Podem ser alterados sem recompilar: o
 * arquivo {@code bikeshare.properties} sobrescreve qualquer valor. Sem o arquivo (ou sem uma
 * chave), valem os valores definidos pelo enunciado em {@link #padrao()}.
 */
public record Parametros(
        BigDecimal valorMinutoAvulso,
        long franquiaAssinanteMinutos,
        BigDecimal valorMinutoExcedenteAssinante,
        BigDecimal mensalidadePadraoAssinante,
        BigDecimal adicionalMinutoEletrica,
        long carenciaMinutos,
        long tempoMaximoMinutos,
        int bateriaMinimaPercentual,
        BigDecimal valorPenalidadeAtraso,
        BigDecimal saldoMinimoParaRetirada) {

    static final String VALOR_MINUTO_AVULSO = "avulso.valorMinuto";
    static final String FRANQUIA_ASSINANTE = "assinante.franquiaMinutos";
    static final String EXCEDENTE_ASSINANTE = "assinante.valorMinutoExcedente";
    static final String MENSALIDADE_ASSINANTE = "assinante.mensalidadePadrao";
    static final String ADICIONAL_ELETRICA = "eletrica.adicionalPorMinuto";
    static final String BATERIA_MINIMA = "eletrica.bateriaMinimaPercentual";
    static final String CARENCIA = "aluguel.carenciaMinutos";
    static final String TEMPO_MAXIMO = "aluguel.tempoMaximoMinutos";
    static final String PENALIDADE_ATRASO = "penalidade.atraso.valor";
    static final String SALDO_MINIMO = "pagamento.saldoMinimoParaRetirada";

    /** Valores definidos pelo enunciado e pelos testes de aceitação. */
    public static Parametros padrao() {
        return new Parametros(
                new BigDecimal("0.50"),
                60,
                new BigDecimal("0.30"),
                new BigDecimal("30.00"),
                new BigDecimal("0.20"),
                2,
                24 * 60,
                15,
                new BigDecimal("10.00"),
                new BigDecimal("0.01"));
    }

    /**
     * Lê o arquivo de configuração, se existir. Um valor ilegível ou fora da faixa não é
     * ignorado: o sistema recusa iniciar, para que ninguém cobre com uma tarifa errada sem saber.
     */
    public static Parametros carregar(Path arquivo) {
        Parametros base = padrao();
        if (!Files.isRegularFile(arquivo)) {
            return base;
        }
        Properties p = new Properties();
        try (Reader leitor = Files.newBufferedReader(arquivo, StandardCharsets.UTF_8)) {
            p.load(leitor);
        } catch (IOException e) {
            throw new ConfiguracaoException(Mensagens.CONFIGURACAO_ILEGIVEL);
        }
        return new Parametros(
                dinheiro(p, VALOR_MINUTO_AVULSO, base.valorMinutoAvulso),
                inteiro(p, FRANQUIA_ASSINANTE, base.franquiaAssinanteMinutos, 0, Long.MAX_VALUE),
                dinheiro(p, EXCEDENTE_ASSINANTE, base.valorMinutoExcedenteAssinante),
                dinheiro(p, MENSALIDADE_ASSINANTE, base.mensalidadePadraoAssinante),
                dinheiro(p, ADICIONAL_ELETRICA, base.adicionalMinutoEletrica),
                inteiro(p, CARENCIA, base.carenciaMinutos, 0, Long.MAX_VALUE),
                inteiro(p, TEMPO_MAXIMO, base.tempoMaximoMinutos, 1, Long.MAX_VALUE),
                (int) inteiro(p, BATERIA_MINIMA, base.bateriaMinimaPercentual, 0, 100),
                dinheiro(p, PENALIDADE_ATRASO, base.valorPenalidadeAtraso),
                dinheiro(p, SALDO_MINIMO, base.saldoMinimoParaRetirada));
    }

    private static BigDecimal dinheiro(Properties p, String chave, BigDecimal padrao) {
        String texto = p.getProperty(chave);
        if (texto == null) {
            return padrao;
        }
        try {
            BigDecimal valor = new BigDecimal(texto.trim());
            if (valor.signum() < 0) {
                throw new ConfiguracaoException(Mensagens.parametroInvalido(chave));
            }
            return valor;
        } catch (NumberFormatException e) {
            throw new ConfiguracaoException(Mensagens.parametroInvalido(chave));
        }
    }

    private static long inteiro(Properties p, String chave, long padrao, long minimo, long maximo) {
        String texto = p.getProperty(chave);
        if (texto == null) {
            return padrao;
        }
        try {
            long valor = Long.parseLong(texto.trim());
            if (valor < minimo || valor > maximo) {
                throw new ConfiguracaoException(Mensagens.parametroInvalido(chave));
            }
            return valor;
        } catch (NumberFormatException e) {
            throw new ConfiguracaoException(Mensagens.parametroInvalido(chave));
        }
    }
}
