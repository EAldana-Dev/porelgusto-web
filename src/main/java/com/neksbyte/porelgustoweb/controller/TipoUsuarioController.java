package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.service.TipoUsuarioService;
import org.springframework.stereotype.Controller;

@Controller
public class TipoUsuarioController {

    private final TipoUsuarioService service;

    public TipoUsuarioController(TipoUsuarioService service) {
        this.service = service;
    }

    // Sin rutas ni metodos HTTP por ahora. Se agregaran cuando definamos las API.
}
