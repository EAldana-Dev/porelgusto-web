package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.dao.ArregloFloralDao;
import com.neksbyte.porelgustoweb.model.ArregloFloral;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Controller
public class Home {
    private final ArregloFloralDao arreglos;

    public Home(ArregloFloralDao arreglos) {
        this.arreglos = arreglos;
    }

    @GetMapping("/")
    public String redirigirInicio() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String inicio(Model model) {
        model.addAttribute("titulo", "Inicio");
        List<ArregloFloral> temporada = arreglos.findAll().stream()
                .sorted(Comparator.comparing(ArregloFloral::getIdArregloFloral))
                .limit(6)
                .toList();
        model.addAttribute("temporada", temporada);
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
