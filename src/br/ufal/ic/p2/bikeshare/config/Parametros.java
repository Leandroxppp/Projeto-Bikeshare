package br.ufal.ic.p2.bikeshare.config;

import java.math.BigDecimal;

/**
 * Parâmetros de tarifação e limites de negócio (BS04). Nada disso fica embutido na lógica:
 * os serviços recebem uma instância e os planos nascem a partir dela.
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
}
