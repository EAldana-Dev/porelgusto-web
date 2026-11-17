package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.service.ModificacionService;
import org.springframework.stereotype.Controller;

@Controller
public class ModificacionController {

    private final ModificacionService service;

    public ModificacionController(ModificacionService service) {
        this.service = service;
    }

    // Sin rutas ni metodos HTTP por ahora. Se agregaran cuando definamos las API.
}
