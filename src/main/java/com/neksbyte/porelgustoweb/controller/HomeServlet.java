package com.neksbyte.porelgustoweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeServlet {

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

    
}
