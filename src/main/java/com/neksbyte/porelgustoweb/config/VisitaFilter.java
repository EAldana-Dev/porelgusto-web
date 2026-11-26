package com.neksbyte.porelgustoweb.config;

import com.neksbyte.porelgustoweb.service.EstadisticasService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/** Cuenta las páginas vistas de la tienda (no el panel, la API, imágenes ni robots). */
@Component
public class VisitaFilter extends OncePerRequestFilter {
    private final EstadisticasService estadisticas;

    public VisitaFilter(EstadisticasService estadisticas) {
        this.estadisticas = estadisticas;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        chain.doFilter(req, res);
        try {
            if (!"GET".equals(req.getMethod()) || res.getStatus() != 200) return;
            String ruta = req.getRequestURI().substring(req.getContextPath().length());
            if (!esPagina(ruta)) return;
            String accept = req.getHeader("Accept");
            String ua = req.getHeader("User-Agent");
            if (accept == null || !accept.contains("text/html") || EstadisticasService.esRobot(ua)) return;
            String ip = req.getHeader("X-Forwarded-For");
            ip = ip == null || ip.isBlank() ? req.getRemoteAddr() : ip.split(",")[0].trim();
            estadisticas.registrar(new EstadisticasService.Pagina(ruta, ip, ua, req.getHeader("Referer"), req.getServerName(),
                    req.getParameter("utm_source"), req.getParameter("utm_campaign")));
        } catch (RuntimeException ignored) {
            // Las estadísticas nunca deben romper la página.
        }
    }

    static boolean esPagina(String r) {
        if (r.startsWith("/admin") || r.startsWith("/api/") || r.startsWith("/dev/") || r.startsWith("/h2-console")
                || r.startsWith("/css/") || r.startsWith("/js/") || r.startsWith("/img/") || r.startsWith("/imagenes/")
                || r.startsWith("/webjars/") || r.startsWith("/uploads/") || r.startsWith("/error") || r.startsWith("/pago/")
                || r.startsWith("/swagger-ui") || r.startsWith("/v3/api-docs")) return false;
        int punto = r.lastIndexOf('.');
        return punto < 0 || r.lastIndexOf('/') > punto;
    }
}
