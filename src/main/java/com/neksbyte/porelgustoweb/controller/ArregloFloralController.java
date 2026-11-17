package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.service.ArregloFloralService;
import org.springframework.stereotype.Controller;

@Controller
public class ArregloFloralController {

    private final ArregloFloralService service;

    public ArregloFloralController(ArregloFloralService service) {
        this.service = service;
    }

    // Sin rutas ni metodos HTTP por ahora. Se agregaran cuando definamos las API.
}
