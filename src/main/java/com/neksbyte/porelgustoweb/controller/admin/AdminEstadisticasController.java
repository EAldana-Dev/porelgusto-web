package com.neksbyte.porelgustoweb.controller.admin;

import com.neksbyte.porelgustoweb.service.EstadisticasService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Estadísticas del sitio: visitas, visitantes, de qué sitio llegan y desde qué ciudad. */
@Controller
public class AdminEstadisticasController {
    private final EstadisticasService estadisticas;

    public AdminEstadisticasController(EstadisticasService estadisticas) {
        this.estadisticas = estadisticas;
    }

    @GetMapping("/admin/estadisticas")
    public String ver(@RequestParam(defaultValue = "30") int dias, Model model) {
        model.addAttribute("r", estadisticas.resumen(dias));
        model.addAttribute("ubicacionDisponible", estadisticas.isUbicacionDisponible());
        model.addAttribute("modulo", "estadisticas");
        return "admin/estadisticas";
    }
}
