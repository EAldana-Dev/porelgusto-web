package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.service.PedidoService;
import org.springframework.stereotype.Controller;

@Controller
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    // Sin rutas ni metodos HTTP por ahora. Se agregaran cuando definamos las API.
}
