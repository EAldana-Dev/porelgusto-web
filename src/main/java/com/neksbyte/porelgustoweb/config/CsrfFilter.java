package com.neksbyte.porelgustoweb.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HexFormat;

/**
 * Protección CSRF: cada formulario POST lleva un token secreto de la sesión
 * (campo oculto "_csrf"). Si no coincide, se rechaza la petición.
 * En las vistas: <input type="hidden" name="_csrf" value="${csrfToken}">
 */
@Component
public class CsrfFilter extends OncePerRequestFilter {
    public static final String TOKEN = "csrfToken";
    private final SecureRandom random = new SecureRandom();

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String ruta = req.getRequestURI().substring(req.getContextPath().length());
        boolean estatico = ruta.startsWith("/css/") || ruta.startsWith("/js/") || ruta.startsWith("/img/")
                || ruta.startsWith("/imagenes/") || ruta.startsWith("/fuentesporelgusto/")
                || ruta.startsWith("/webjars/") || ruta.startsWith("/uploads/") || ruta.startsWith("/favicon");
        // La API JSON (app móvil, telemetría y escaneo de QR) no usa formularios con token.
        boolean api = ruta.startsWith("/api/");
        if (estatico || api) {
            chain.doFilter(req, res);
            return;
        }
        HttpSession s = req.getSession();
        String token = (String) s.getAttribute(TOKEN);
        if (token == null) {
            byte[] b = new byte[24];
            random.nextBytes(b);
            token = HexFormat.of().formatHex(b);
            s.setAttribute(TOKEN, token);
        }
        req.setAttribute(TOKEN, token);
        if ("POST".equalsIgnoreCase(req.getMethod())) {
            String enviado = req.getParameter("_csrf");
            if (enviado == null || !MessageDigest.isEqual(enviado.getBytes(StandardCharsets.UTF_8), token.getBytes(StandardCharsets.UTF_8))) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "La página expiró. Recarga e inténtalo de nuevo.");
                return;
            }
        }
        chain.doFilter(req, res);
    }
}
