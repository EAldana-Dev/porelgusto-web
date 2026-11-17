package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.service.ContactoService;
import org.springframework.stereotype.Controller;

@Controller
public class ContactoController {

    private final ContactoService service;

    public ContactoController(ContactoService service) {
        this.service = service;
    }

    // Sin rutas ni metodos HTTP por ahora. Se agregaran cuando definamos las API.
}
