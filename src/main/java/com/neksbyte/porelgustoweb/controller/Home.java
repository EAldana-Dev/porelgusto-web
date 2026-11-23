package com.neksbyte.porelgustoweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class Home {

    @GetMapping("/")
    public String redirigirInicio() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String inicio(Model model) {
        model.addAttribute("titulo", "Inicio");
        return "index";
    }

    
    @GetMapping("/login")
    public String secion(Model model) {
        model.addAttribute("titulo", "Inicio");
        return "secion";
    }

    @GetMapping("/signup")
    public String crearCuenta(Model model) {
        model.addAttribute("titulo", "Crear cuenta");
        return "singup";
    }


    @GetMapping("/catalogo")
    public String catalogo(Model model) {
        model.addAttribute("titulo", "Catalogo");
        return "catalogo";
    }

     @GetMapping("/carrito")
    public String carrito(Model model) {
        model.addAttribute("titulo", "Carrito");
        return "carrito";
    }
    
}
