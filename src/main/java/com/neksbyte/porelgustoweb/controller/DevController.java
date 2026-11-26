package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.service.CorreoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** Correos que envió la tienda (los últimos 50). Solo administradores: ver AccesoInterceptor. */
@Controller
public class DevController {
    private final CorreoService correos;

    public DevController(CorreoService correos) {
        this.correos = correos;
    }

    @GetMapping("/admin/correos")
    public String correos(Model model) {
        model.addAttribute("correos", correos.recientes());
        model.addAttribute("envioReal", correos.isEnvioReal());
        model.addAttribute("modulo", "correos");
        return "admin/correos";
    }

    /** Ruta anterior: ahora está dentro del panel. */
    @GetMapping("/dev/correos")
    public String anterior() { return "redirect:/admin/correos"; }
}
