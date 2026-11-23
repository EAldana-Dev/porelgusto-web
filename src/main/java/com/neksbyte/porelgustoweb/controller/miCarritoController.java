package com.neksbyte.porelgustoweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class miCarritoController {

    @GetMapping("/MiCarrito")
    public String intoCarrito(Model model){
        model.addAttribute("titulo", "Mi Carrito | Por el Gusto");
        return "carrito";
    }

    @PostMapping("/MiCarrito/buy")
    public String procesarCompra(Model model,
                                 @RequestParam int productoId
    ){

        return "redirect:/MiCarrito";
    }
}
