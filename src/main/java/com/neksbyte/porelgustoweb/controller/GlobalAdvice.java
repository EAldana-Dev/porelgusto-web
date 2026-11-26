package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.config.SesionUsuario;
import com.neksbyte.porelgustoweb.config.TiendaConfig;
import com.neksbyte.porelgustoweb.exception.NoEncontradoException;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.model.Categoria;
import com.neksbyte.porelgustoweb.model.Usuario;
import com.neksbyte.porelgustoweb.service.ArregloFloralService;
import com.neksbyte.porelgustoweb.service.CarritoSesion;
import com.neksbyte.porelgustoweb.service.NotificacionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

/** Datos que usan todas las vistas (menú, carrito, usuario) y manejo de errores de las páginas. */
@ControllerAdvice(annotations = Controller.class)
public class GlobalAdvice {
    private final SesionUsuario sesion;
    private final CarritoSesion carrito;
    private final ArregloFloralService catalogo;
    private final NotificacionService notificaciones;
    private final TiendaConfig tienda;

    public GlobalAdvice(SesionUsuario sesion, CarritoSesion carrito, ArregloFloralService catalogo,
                        NotificacionService notificaciones, TiendaConfig tienda) {
        this.sesion = sesion;
        this.carrito = carrito;
        this.catalogo = catalogo;
        this.notificaciones = notificaciones;
        this.tienda = tienda;
    }

    @ModelAttribute
    public void comunes(HttpServletRequest req, Model model) {
        Usuario u = sesion.usuario(req).orElse(null);
        model.addAttribute("usuarioActual", u);
        model.addAttribute("notifPendientes", notificaciones.pendientes(u));
        model.addAttribute("carritoCantidad", carrito.getCantidad());
        model.addAttribute("tienda", tienda);
        model.addAttribute("rutaActual", req.getRequestURI());
        model.addAttribute("cookiesDecididas", leerCookie(req, CookieController.CONSENTIMIENTO) != null);
    }

    @ModelAttribute("categoriasMenu")
    public List<Categoria> categoriasMenu() { return catalogo.categoriasMenu(); }

    static String leerCookie(HttpServletRequest req, String nombre) {
        if (req.getCookies() == null) return null;
        return Arrays.stream(req.getCookies()).filter(c -> c.getName().equals(nombre)).map(Cookie::getValue).findFirst().orElse(null);
    }

    @ExceptionHandler(NoEncontradoException.class)
    public String noEncontrado(NoEncontradoException e, HttpServletRequest req, HttpServletResponse res, Model model) {
        res.setStatus(404);
        comunes(req, model);
        model.addAttribute("categoriasMenu", catalogo.categoriasMenu());
        model.addAttribute("status", 404);
        model.addAttribute("message", e.getMessage());
        return "error";
    }

    /** Errores de negocio no atrapados: se regresa a la página anterior con el mensaje. */
    @ExceptionHandler(ReglaNegocioException.class)
    public String reglaNegocio(ReglaNegocioException e, HttpServletRequest req, RedirectAttributes flash) {
        flash.addFlashAttribute("error", e.getMessage());
        return "redirect:" + regreso(req);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String archivoGrande(HttpServletRequest req, RedirectAttributes flash) {
        flash.addFlashAttribute("error", "La imagen es demasiado grande (máximo 5 MB).");
        return "redirect:" + regreso(req);
    }

    /** Página anterior, solo si es de este mismo sitio. */
    static String regreso(HttpServletRequest req) {
        String ref = req.getHeader("Referer");
        String base = req.getScheme() + "://" + req.getServerName() + (req.getServerPort() == 80 || req.getServerPort() == 443 ? "" : ":" + req.getServerPort());
        if (ref != null && ref.startsWith(base + "/")) return ref.substring(base.length());
        return "/";
    }
}
