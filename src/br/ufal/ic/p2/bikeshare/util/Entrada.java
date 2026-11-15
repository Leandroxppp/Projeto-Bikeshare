package br.ufal.ic.p2.bikeshare.util;

import br.ufal.ic.p2.bikeshare.excecoes.DadoInvalidoException;
import br.ufal.ic.p2.bikeshare.excecoes.DadoObrigatorioException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Pattern;

/** Validação e conversão de parâmetros textuais recebidos pela Facade. */
public final class Entrada {
    private static final Pattern INTEIRO = Pattern.compile("-?\\d+");
    private static final Pattern DECIMAL = Pattern.compile("-?\\d+([.,]\\d+)?");

    private Entrada() {
    }

    public static boolean vazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    /** Devolve o próprio valor se houver conteúdo; senão lança com a mensagem informada. */
    public static String obrigatorio(String valor, String mensagemNulo) {
        if (vazio(valor)) {
            throw new DadoObrigatorioException(mensagemNulo);
        }
        return valor;
    }

    public static int inteiro(String valor, String mensagemNulo, String mensagemNaoNumerico) {
        obrigatorio(valor, mensagemNulo);
        String texto = valor.trim();
        if (!INTEIRO.matcher(texto).matches()) {
            throw new DadoInvalidoException(mensagemNaoNumerico);
        }
        try {
            return new BigInteger(texto).intValueExact();
        } catch (ArithmeticException e) {
            throw new DadoInvalidoException(mensagemNaoNumerico);
        }
    }

    /** Inteiro maior ou igual a zero. */
    public static int inteiroNaoNegativo(String valor, String nulo, String naoNumerico, String negativo) {
        int numero = inteiro(valor, nulo, naoNumerico);
        if (numero < 0) {
            throw new DadoInvalidoException(negativo);
        }
        return numero;
    }

    /** Decimal maior ou igual a zero. */
    public static BigDecimal decimalNaoNegativo(String valor, String nulo, String naoNumerico, String negativo) {
        BigDecimal numero = decimal(valor, nulo, naoNumerico);
        if (numero.signum() < 0) {
            throw new DadoInvalidoException(negativo);
        }
        return numero;
    }

    /** Decimal estritamente positivo. */
    public static BigDecimal decimalPositivo(String valor, String nulo, String naoNumerico, String naoPositivo) {
        BigDecimal numero = decimal(valor, nulo, naoNumerico);
        if (numero.signum() <= 0) {
            throw new DadoInvalidoException(naoPositivo);
        }
        return numero;
    }

    /** Aceita vírgula ou ponto como separador decimal. */
    public static BigDecimal decimal(String valor, String mensagemNulo, String mensagemNaoNumerico) {
        obrigatorio(valor, mensagemNulo);
        String texto = valor.trim();
        if (!DECIMAL.matcher(texto).matches()) {
            throw new DadoInvalidoException(mensagemNaoNumerico);
        }
        return new BigDecimal(texto.replace(',', '.'));
    }
}
