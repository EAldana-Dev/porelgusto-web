package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.dao.ArregloFloralDao;
import com.neksbyte.porelgustoweb.dao.ContenidoCarritoDao;
import com.neksbyte.porelgustoweb.model.ArregloFloral;
import com.neksbyte.porelgustoweb.model.ContenidoCarrito;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CarritoController {
    private final ContenidoCarritoDao contenidoDao;
    private final ArregloFloralDao arregloDao;

    public CarritoController(ContenidoCarritoDao contenidoDao, ArregloFloralDao arregloDao) {
        this.contenidoDao = contenidoDao;
        this.arregloDao = arregloDao;
    }

    @GetMapping("/MiCarrito")
    public String carrito(Model model) {

        // por ahora se usa el carrito 1 porque todavia no hay sesion del usuario
        Integer idCarrito = 1;

        List<ContenidoCarrito> productos = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (ContenidoCarrito contenido : contenidoDao.findAll()) {
            if (contenido.getCarritoDeCompra().getIdCarritoDeCompra().equals(idCarrito)) {

                // se vuelve a buscar el arreglo para tener su nombre e imagenes
                ArregloFloral arreglo = arregloDao.findById(contenido.getArregloFloral().getIdArregloFloral()).orElse(null);
                contenido.setArregloFloral(arreglo);

                productos.add(contenido);

                BigDecimal subtotal = contenido.getPrecioUnitario().multiply(new BigDecimal(contenido.getCantidad()));
                total = total.add(subtotal);
            }
        }

        model.addAttribute("productos", productos);
        model.addAttribute("total", total);
        model.addAttribute("titulo", "Carrito | Por el Gusto");

        return "carrito";
    }

    @GetMapping("/compra")
    public String compra(Model model) {
        model.addAttribute("titulo", "Compra | Por el Gusto");
        return "compra";
    }
        @PostMapping("/MiCarrito/buy")
    public String procesarCompra(Model model, @RequestParam int productoId){
        return "redirect:/MiCarrito";
    }
}
