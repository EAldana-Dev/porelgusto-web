package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.config.SesionUsuario;
import com.neksbyte.porelgustoweb.dto.SoporteForm;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.model.SolicitudSoporte;
import com.neksbyte.porelgustoweb.model.Usuario;
import com.neksbyte.porelgustoweb.service.SoporteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/** Soporte y contacto: medios de contacto, preguntas frecuentes y solicitudes de ayuda. */
@Controller
public class SoporteController {
    private final SoporteService soporte;
    private final SesionUsuario sesion;

    public SoporteController(SoporteService soporte, SesionUsuario sesion) {
        this.soporte = soporte;
        this.sesion = sesion;
    }

    @GetMapping("/soporte")
    public String soporte(HttpServletRequest req, Model model) {
        Usuario u = sesion.usuario(req).orElse(null);
        if (!model.containsAttribute("soporteForm")) {
            SoporteForm f = new SoporteForm();
            if (u != null) {
                f.setNombre(u.getNombre());
                f.setCorreo(u.getCorreoElectronico());
            }
            model.addAttribute("soporteForm", f);
        }
        model.addAttribute("solicitudes", u == null ? List.of() : soporte.deUsuario(u));
        return "soporte";
    }

    @PostMapping("/soporte")
    public String abrir(@Valid @ModelAttribute("soporteForm") SoporteForm form, BindingResult errores, HttpServletRequest req,
                        Model model, RedirectAttributes flash) {
        Usuario u = sesion.usuario(req).orElse(null);
        if (errores.hasErrors()) {
            model.addAttribute("solicitudes", u == null ? List.of() : soporte.deUsuario(u));
            model.addAttribute("error", "Revisa los campos marcados.");
            return "soporte";
        }
        SolicitudSoporte s = soporte.abrir(form, u);
        flash.addFlashAttribute("mensaje", "Recibimos tu mensaje. Te responderemos por correo en menos de 24 horas.");
        return u == null ? "redirect:/soporte" : "redirect:/soporte/" + s.getIdSolicitud();
    }

    @GetMapping("/soporte/{id}")
    public String solicitud(@PathVariable Integer id, HttpServletRequest req, Model model) {
        Usuario u = sesion.usuario(req).orElse(null);
        if (u == null) return "redirect:/login?next=/soporte/" + id;
        SolicitudSoporte s = soporte.porId(id);
        if (!s.isDe(u)) return u.tienePermiso("SOPORTE") ? "redirect:/admin/soporte/" + id : "redirect:/soporte";
        model.addAttribute("solicitud", s);
        return "soporte-solicitud";
    }

    @PostMapping("/soporte/{id}/responder")
    public String responder(@PathVariable Integer id, @RequestParam String mensaje, HttpServletRequest req, RedirectAttributes flash) {
        Usuario u = sesion.requerido(req);
        SolicitudSoporte s = soporte.porId(id);
        if (!s.isDe(u)) return "redirect:/soporte";
        try {
            soporte.responder(id, u, mensaje);
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/soporte/" + id;
    }
}
