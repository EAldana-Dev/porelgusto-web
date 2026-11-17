package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.service.TicketService;
import org.springframework.stereotype.Controller;

@Controller
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    // Sin rutas ni metodos HTTP por ahora. Se agregaran cuando definamos las API.
}
