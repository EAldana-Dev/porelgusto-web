package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.dto.FiltroCatalogo;
import com.neksbyte.porelgustoweb.dto.ProductoVista;
import com.neksbyte.porelgustoweb.model.Promocion;
import com.neksbyte.porelgustoweb.service.ArregloFloralService;
import com.neksbyte.porelgustoweb.service.PromocionService;
import com.neksbyte.porelgustoweb.service.RecomendacionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;
import java.util.Map;

/** Página principal: oferta destacada, más vendidos, recomendaciones, colecciones y categorías. */
@Controller
public class Home {
    private final RecomendacionService recomendaciones;
    private final ArregloFloralService catalogo;
    private final PromocionService promociones;

    public Home(RecomendacionService recomendaciones, ArregloFloralService catalogo, PromocionService promociones) {
        this.recomendaciones = recomendaciones;
        this.catalogo = catalogo;
        this.promociones = promociones;
    }

    @GetMapping("/")
    public String inicio(HttpServletRequest req, Model model) {
        boolean personalizado = Preferencias.aceptadas(req) && !Preferencias.categorias(req).isEmpty();
        model.addAttribute("titulo", "Inicio");
        model.addAttribute("recomendados", recomendaciones.recomendar(Preferencias.categorias(req), Preferencias.vistos(req), 8));
        model.addAttribute("personalizado", personalizado);
        model.addAttribute("masVendidos", recomendaciones.masVendidos(3));
        model.addAttribute("colecciones", catalogo.colecciones());
        Promocion destacada = promociones.vigentes().stream().findFirst().orElse(null);
        model.addAttribute("ofertaDestacada", destacada);
        // Portada de cada categoría y colección: la foto de su primer producto disponible.
        Map<String, String> portadas = new LinkedHashMap<>();
        for (ProductoVista v : catalogo.buscar(new FiltroCatalogo())) {
            portadas.putIfAbsent(v.getProducto().getCategoriaSlug(), v.getProducto().getImagenUrl());
            v.getProducto().getColecciones().forEach(c -> portadas.putIfAbsent("col-" + c.getSlug(), v.getProducto().getImagenUrl()));
        }
        model.addAttribute("portadas", portadas);
        return "index";
    }

    /** Ruta anterior de la página de inicio. */
    @GetMapping("/home")
    public String redirigirInicio() {
        return "redirect:/";
    }
}
