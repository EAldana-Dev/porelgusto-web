package com.neksbyte.porelgustoweb.controller.admin;

import com.neksbyte.porelgustoweb.config.SesionUsuario;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.service.SoporteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Atención de solicitudes de soporte. */
@Controller
@RequestMapping("/admin/soporte")
public class AdminSoporteController {
    private final SoporteService soporte;
    private final SesionUsuario sesion;

    public AdminSoporteController(SoporteService soporte, SesionUsuario sesion) {
        this.soporte = soporte;
        this.sesion = sesion;
    }

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("solicitudes", soporte.todas());
        model.addAttribute("modulo", "soporte");
        return "admin/soporte";
    }

    @GetMapping("/{id}")
    public String solicitud(@PathVariable Integer id, Model model) {
        model.addAttribute("solicitud", soporte.porId(id));
        model.addAttribute("modulo", "soporte");
        return "admin/soporte-solicitud";
    }

    @PostMapping("/{id}/responder")
    public String responder(@PathVariable Integer id, @RequestParam String mensaje, HttpServletRequest req, RedirectAttributes flash) {
        try {
            soporte.responder(id, sesion.requerido(req), mensaje);
            flash.addFlashAttribute("mensaje", "Respuesta enviada al cliente por correo.");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/soporte/" + id;
    }

    @PostMapping("/{id}/tomar")
    public String tomar(@PathVariable Integer id, HttpServletRequest req) {
        soporte.asignar(id, sesion.requerido(req));
        return "redirect:/admin/soporte/" + id;
    }

    @PostMapping("/{id}/cerrar")
    public String cerrar(@PathVariable Integer id, RedirectAttributes flash) {
        soporte.cerrar(id);
        flash.addFlashAttribute("mensaje", "Solicitud cerrada.");
        return "redirect:/admin/soporte/" + id;
    }
}
