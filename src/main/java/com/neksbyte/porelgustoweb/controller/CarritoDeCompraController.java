package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.service.CarritoDeCompraService;
import org.springframework.stereotype.Controller;

@Controller
public class CarritoDeCompraController {

    private final CarritoDeCompraService service;

    public CarritoDeCompraController(CarritoDeCompraService service) {
        this.service = service;
    }

    // Sin rutas ni metodos HTTP por ahora. Se agregaran cuando definamos las API.
}
