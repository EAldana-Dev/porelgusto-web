package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.service.ListaDeDeseosService;
import org.springframework.stereotype.Controller;

@Controller
public class ListaDeDeseosController {

    private final ListaDeDeseosService service;

    public ListaDeDeseosController(ListaDeDeseosService service) {
        this.service = service;
    }

    // Sin rutas ni metodos HTTP por ahora. Se agregaran cuando definamos las API.
}
