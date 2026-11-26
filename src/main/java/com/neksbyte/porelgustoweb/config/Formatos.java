package com.neksbyte.porelgustoweb.config;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Locale;

/** Funciones para las vistas JSP (taglib "pg", ver WEB-INF/pg.tld): dinero, fechas en español, etc. */
public final class Formatos {
    private static final Locale MX = Locale.forLanguageTag("es-MX");
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("d MMM yyyy", MX);
    private static final DateTimeFormatter FECHA_LARGA = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", MX);
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm", MX);

    private Formatos() { }

    /** 1250 -> "$1,250" ; 99.5 -> "$99.50" */
    public static String dinero(BigDecimal v) {
        if (v == null) return "$0";
        BigDecimal n = v.setScale(2, RoundingMode.HALF_UP);
        DecimalFormat f = new DecimalFormat(n.stripTrailingZeros().scale() <= 0 ? "#,##0" : "#,##0.00", DecimalFormatSymbols.getInstance(Locale.US));
        return "$" + f.format(n);
    }

    public static String fecha(Object t) {
        if (t == null) return "";
        return t instanceof LocalDateTime ldt ? ldt.format(FECHA) : FECHA.format((TemporalAccessor) t);
    }

    /** "Martes 25 de noviembre" */
    public static String fechaLarga(Object t) {
        if (t == null) return "";
        String s = t instanceof LocalDateTime ldt ? ldt.format(FECHA_LARGA) : FECHA_LARGA.format((TemporalAccessor) t);
        return s.substring(0, 1).toUpperCase(MX) + s.substring(1);
    }

    public static String fechaHora(LocalDateTime t) { return t == null ? "" : t.format(FECHA_HORA); }

    /** Para inputs type=date o datetime-local. */
    public static String iso(Object t) {
        if (t == null) return "";
        if (t instanceof LocalDateTime ldt) return ldt.withNano(0).withSecond(0).toString();
        return t.toString();
    }

    /** "hace 5 min", "hace 2 h", "hace 3 días" */
    public static String hace(LocalDateTime t) {
        if (t == null) return "";
        long min = Duration.between(t, LocalDateTime.now()).toMinutes();
        if (min < 1) return "justo ahora";
        if (min < 60) return "hace " + min + " min";
        if (min < 60 * 24) return "hace " + (min / 60) + " h";
        long dias = min / (60 * 24);
        return dias == 1 ? "ayer" : dias < 30 ? "hace " + dias + " días" : fecha(t);
    }

    public static String url(String s) { return s == null ? "" : URLEncoder.encode(s, StandardCharsets.UTF_8); }

    public static boolean contiene(Collection<?> c, Object o) { return c != null && o != null && c.contains(o); }

    public static boolean esHoy(LocalDate d) { return d != null && d.equals(LocalDate.now()); }
}
