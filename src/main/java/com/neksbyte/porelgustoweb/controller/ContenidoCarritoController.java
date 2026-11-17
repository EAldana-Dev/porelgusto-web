package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.service.ContenidoCarritoService;
import org.springframework.stereotype.Controller;

@Controller
public class ContenidoCarritoController {

    private final ContenidoCarritoService service;

    public ContenidoCarritoController(ContenidoCarritoService service) {
        this.service = service;
    }

    // Sin rutas ni metodos HTTP por ahora. Se agregaran cuando definamos las API.
}
