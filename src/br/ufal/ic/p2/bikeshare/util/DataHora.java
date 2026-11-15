package br.ufal.ic.p2.bikeshare.util;

import br.ufal.ic.p2.bikeshare.excecoes.DadoInvalidoException;
import br.ufal.ic.p2.bikeshare.excecoes.Mensagens;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Conversão entre o texto "d/M/yyyy [H:mm]" usado nos scripts e as classes java.time. */
public final class DataHora {
    private static final Pattern DATA_HORA = Pattern.compile("(\\d{1,2})/(\\d{1,2})/(\\d{4}) (\\d{1,2}):(\\d{2})");
    private static final Pattern DATA = Pattern.compile("(\\d{1,2})/(\\d{1,2})/(\\d{4})");

    private DataHora() {
    }

    public static LocalDateTime lerDataHora(String texto) {
        Entrada.obrigatorio(texto, Mensagens.DATA_NULA);
        Matcher m = DATA_HORA.matcher(texto.trim());
        if (!m.matches()) {
            throw new DadoInvalidoException(Mensagens.DATA_INVALIDA);
        }
        try {
            return LocalDateTime.of(num(m, 3), num(m, 2), num(m, 1), num(m, 4), num(m, 5));
        } catch (DateTimeException e) {
            throw new DadoInvalidoException(Mensagens.DATA_INVALIDA);
        }
    }

    public static LocalDate lerData(String texto) {
        Entrada.obrigatorio(texto, Mensagens.DATA_NULA);
        Matcher m = DATA.matcher(texto.trim());
        if (!m.matches()) {
            throw new DadoInvalidoException(Mensagens.DATA_INVALIDA);
        }
        try {
            return LocalDate.of(num(m, 3), num(m, 2), num(m, 1));
        } catch (DateTimeException e) {
            throw new DadoInvalidoException(Mensagens.DATA_INVALIDA);
        }
    }

    /** Ex.: 10/3/2025 8:00 (dia e mês sem zero à esquerda, hora sem zero à esquerda). */
    public static String formatar(LocalDateTime dataHora) {
        return dataHora.getDayOfMonth() + "/" + dataHora.getMonthValue() + "/" + dataHora.getYear()
                + " " + dataHora.getHour() + ":" + String.format("%02d", dataHora.getMinute());
    }

    private static int num(Matcher m, int grupo) {
        return Integer.parseInt(m.group(grupo));
    }
}
