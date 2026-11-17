package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.service.ImagenService;
import org.springframework.stereotype.Controller;

@Controller
public class ImagenController {

    private final ImagenService service;

    public ImagenController(ImagenService service) {
        this.service = service;
    }

    // Sin rutas ni metodos HTTP por ahora. Se agregaran cuando definamos las API.
}
