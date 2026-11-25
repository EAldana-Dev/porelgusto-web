package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.dao.ArregloFloralDao;
import com.neksbyte.porelgustoweb.model.ArregloFloral;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class CatalogoController {
    private final ArregloFloralDao dao;
    public CatalogoController(ArregloFloralDao dao) {
        this.dao = dao;
    }

    @GetMapping("/Catalogo")
    public String inicio(Model model) {

    List<ArregloFloral> arreglos = dao.findAll();
    model.addAttribute("arreglo", arreglos);

    model.addAttribute("titulo", "Catalogo");

    return "catalogo";
    }
    @GetMapping("/Catalogo/{nombre}")
    public String detallesProducto(@PathVariable String nombre, HttpServletRequest request, Model model) {

        String nombreReal = nombre.replace("-", " ");

        ArregloFloral arreglo = dao.findByNombre(nombreReal)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        String slug = arreglo.getNombre().trim().replaceAll("\\s+", "-");
        model.addAttribute("urlProducto", request.getContextPath() + "/Catalogo/" + slug);

        model.addAttribute("producto", arreglo);

        return "detalleProducto";
    }

}
