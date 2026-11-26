package com.neksbyte.porelgustoweb.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.*;

/** Preferencias de navegación en cookies (solo si el usuario aceptó cookies). */
final class Preferencias {
    private static final int MAX_CATEGORIAS = 4;
    private static final int MAX_VISTOS = 12;

    private Preferencias() { }

    static boolean aceptadas(HttpServletRequest req) {
        return "si".equals(GlobalAdvice.leerCookie(req, CookieController.CONSENTIMIENTO));
    }

    static List<String> categorias(HttpServletRequest req) { return lista(req, CookieController.PREFERENCIAS); }

    static Set<String> vistos(HttpServletRequest req) { return new HashSet<>(lista(req, CookieController.VISTOS)); }

    private static List<String> lista(HttpServletRequest req, String nombre) {
        if (!aceptadas(req)) return List.of();
        String v = GlobalAdvice.leerCookie(req, nombre);
        if (v == null || v.isBlank()) return List.of();
        return Arrays.stream(v.split("\\.")).filter(s -> s.matches("[A-Za-z0-9-]{1,40}")).toList();
    }

    /** Registra una categoría visitada (la más reciente primero). */
    static void registrarCategoria(HttpServletRequest req, HttpServletResponse res, String slug) {
        if (!aceptadas(req) || slug == null || slug.isBlank()) return;
        LinkedList<String> l = new LinkedList<>(categorias(req));
        l.remove(slug);
        l.addFirst(slug);
        while (l.size() > MAX_CATEGORIAS) l.removeLast();
        res.addCookie(CookieController.cookie(CookieController.PREFERENCIAS, String.join(".", l), 60 * 60 * 24 * 90));
    }

    static void registrarVisto(HttpServletRequest req, HttpServletResponse res, String sku) {
        if (!aceptadas(req) || sku == null) return;
        LinkedList<String> l = new LinkedList<>(lista(req, CookieController.VISTOS));
        l.remove(sku);
        l.addFirst(sku);
        while (l.size() > MAX_VISTOS) l.removeLast();
        res.addCookie(CookieController.cookie(CookieController.VISTOS, String.join(".", l), 60 * 60 * 24 * 90));
    }
}
