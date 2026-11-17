package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.service.ListaDeseoArregloFloralService;
import org.springframework.stereotype.Controller;

@Controller
public class ListaDeseoArregloFloralController {

    private final ListaDeseoArregloFloralService service;

    public ListaDeseoArregloFloralController(ListaDeseoArregloFloralService service) {
        this.service = service;
    }

    // Sin rutas ni metodos HTTP por ahora. Se agregaran cuando definamos las API.
}
