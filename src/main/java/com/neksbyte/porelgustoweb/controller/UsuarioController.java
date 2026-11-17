package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.service.UsuarioService;
import org.springframework.stereotype.Controller;

@Controller
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // Sin rutas ni metodos HTTP por ahora. Se agregaran cuando definamos las API.
}
