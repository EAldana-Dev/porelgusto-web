package com.neksbyte.porelgustoweb.service;

import java.text.Normalizer;
import java.util.Locale;

/** Utilidades de texto: quitar acentos (para buscar) y generar slugs para URLs. */
public final class Textos {
    private Textos() { }

    /** "Orquídeas Rosas" -> "orquideas rosas" */
    public static String normalizar(String s) {
        if (s == null) return "";
        return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT).trim();
    }

    /** "Día de las Madres" -> "dia-de-las-madres" */
    public static String slug(String s) {
        return normalizar(s).replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
    }

    public static boolean vacio(String s) { return s == null || s.isBlank(); }

    /** Texto recortado o null si está vacío. */
    public static String limpio(String s) { return vacio(s) ? null : s.trim(); }
}
