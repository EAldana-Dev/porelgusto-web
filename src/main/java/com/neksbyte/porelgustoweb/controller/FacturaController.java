package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.service.FacturaService;
import org.springframework.stereotype.Controller;

@Controller
public class FacturaController {

    private final FacturaService service;

    public FacturaController(FacturaService service) {
        this.service = service;
    }

    // Sin rutas ni metodos HTTP por ahora. Se agregaran cuando definamos las API.
}
