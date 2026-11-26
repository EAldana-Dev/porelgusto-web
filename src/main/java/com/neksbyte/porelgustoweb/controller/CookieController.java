package com.neksbyte.porelgustoweb.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Aviso de cookies: aceptar guarda preferencias para "Recomendados para ti". */
@Controller
public class CookieController {
    public static final String CONSENTIMIENTO = "pg_cookies";
    public static final String PREFERENCIAS = "pg_prefs";
    public static final String VISTOS = "pg_vistos";

    @PostMapping("/cookies")
    public String decidir(@RequestParam String decision, HttpServletRequest req, HttpServletResponse res) {
        boolean acepta = "aceptar".equals(decision);
        res.addCookie(cookie(CONSENTIMIENTO, acepta ? "si" : "no", 60 * 60 * 24 * 365));
        if (!acepta) {
            res.addCookie(cookie(PREFERENCIAS, "", 0));
            res.addCookie(cookie(VISTOS, "", 0));
        }
        return "redirect:" + GlobalAdvice.regreso(req);
    }

    static Cookie cookie(String nombre, String valor, int segundos) {
        Cookie c = new Cookie(nombre, valor);
        c.setPath("/");
        c.setMaxAge(segundos);
        c.setHttpOnly(true);
        c.setAttribute("SameSite", "Lax");
        return c;
    }
}
