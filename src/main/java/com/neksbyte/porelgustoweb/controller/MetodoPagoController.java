package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.service.MetodoPagoService;
import org.springframework.stereotype.Controller;

@Controller
public class MetodoPagoController {

    private final MetodoPagoService service;

    public MetodoPagoController(MetodoPagoService service) {
        this.service = service;
    }

    // Sin rutas ni metodos HTTP por ahora. Se agregaran cuando definamos las API.
}
