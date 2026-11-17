package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.service.CuponService;
import org.springframework.stereotype.Controller;

@Controller
public class CuponController {

    private final CuponService service;

    public CuponController(CuponService service) {
        this.service = service;
    }

    // Sin rutas ni metodos HTTP por ahora. Se agregaran cuando definamos las API.
}
