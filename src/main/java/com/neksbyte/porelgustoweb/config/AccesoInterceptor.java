package com.neksbyte.porelgustoweb.config;

import com.neksbyte.porelgustoweb.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Optional;

/**
 * Validación de roles de acceso: /cuenta requiere sesión y /admin requiere
 * el permiso del módulo (DASHBOARD, PEDIDOS, PRODUCTOS...). Ver model.Permisos.
 */
@Component
public class AccesoInterceptor implements HandlerInterceptor {
    private final SesionUsuario sesion;
    private final TiendaConfig tienda;

    public AccesoInterceptor(SesionUsuario sesion, TiendaConfig tienda) {
        this.sesion = sesion;
        this.tienda = tienda;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        String ruta = req.getRequestURI().substring(req.getContextPath().length());
        boolean swagger = ruta.startsWith("/swagger-ui") || ruta.startsWith("/v3/api-docs");
        boolean requiereSesion = ruta.startsWith("/cuenta") || ruta.startsWith("/admin") || swagger;
        if (!requiereSesion) return true;
        Optional<Usuario> u = sesion.usuario(req);
        if (u.isEmpty()) {
            String destino = ruta + (req.getQueryString() == null ? "" : "?" + req.getQueryString());
            res.sendRedirect(req.getContextPath() + "/login?next=" + URLEncoder.encode(destino, StandardCharsets.UTF_8));
            return false;
        }
        if (swagger && !u.get().tienePermiso("CONFIGURACION")) {
            res.sendError(403);
            return false;
        }
        if (ruta.startsWith("/admin")) {
            String[] partes = ruta.split("/");
            String modulo = partes.length > 2 ? partes[2].toUpperCase(Locale.ROOT) : "DASHBOARD";
            if (modulo.equals("CUPONES")) modulo = "PROMOCIONES";
            if (modulo.equals("CORREOS")) modulo = "CONFIGURACION";
            if (!com.neksbyte.porelgustoweb.model.Permisos.MODULOS.contains(modulo)) modulo = "DASHBOARD";
            if (!u.get().tienePermiso(modulo)) {
                req.setAttribute("jakarta.servlet.error.message", "No tienes permiso para entrar a esta sección.");
                res.sendError(403);
                return false;
            }
        }
        return true;
    }
}
