package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.config.SesionUsuario;
import com.neksbyte.porelgustoweb.dto.FiltroCatalogo;
import com.neksbyte.porelgustoweb.exception.NoEncontradoException;
import com.neksbyte.porelgustoweb.model.ArregloFloral;
import com.neksbyte.porelgustoweb.model.Usuario;
import com.neksbyte.porelgustoweb.service.ArregloFloralService;
import com.neksbyte.porelgustoweb.service.CarritoDeCompraService;
import com.neksbyte.porelgustoweb.service.ListaDeDeseosService;
import com.neksbyte.porelgustoweb.service.QrCodeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;

/** Catálogo con buscador y filtros, ficha del producto y su código QR. */
@Controller
public class CatalogoController {
    private final ArregloFloralService catalogo;
    private final ListaDeDeseosService deseos;
    private final CarritoDeCompraService carritoService;
    private final QrCodeService qr;
    private final SesionUsuario sesion;

    public CatalogoController(ArregloFloralService catalogo, ListaDeDeseosService deseos, CarritoDeCompraService carritoService,
                              QrCodeService qr, SesionUsuario sesion) {
        this.catalogo = catalogo;
        this.deseos = deseos;
        this.carritoService = carritoService;
        this.qr = qr;
        this.sesion = sesion;
    }

    @GetMapping("/catalogo")
    public String catalogo(@ModelAttribute("filtro") FiltroCatalogo filtro, HttpServletRequest req, HttpServletResponse res, Model model) {
        model.addAttribute("resultados", catalogo.buscar(filtro));
        model.addAttribute("categorias", catalogo.categoriasMenu());
        model.addAttribute("colecciones", catalogo.colecciones());
        model.addAttribute("etiquetas", catalogo.etiquetas());
        model.addAttribute("deseos", deseos.skus(sesion.usuario(req).orElse(null)));
        if (filtro.getCategoria() != null && !filtro.getCategoria().isBlank()) {
            model.addAttribute("categoriaActual", catalogo.categoriaPorSlug(filtro.getCategoria()));
            Preferencias.registrarCategoria(req, res, filtro.getCategoria());
        }
        if (filtro.getColeccion() != null && !filtro.getColeccion().isBlank()) {
            model.addAttribute("coleccionActual", catalogo.categoriaPorSlug(filtro.getColeccion()));
        }
        return "catalogo";
    }

    /** Rutas anteriores del catálogo (/Catalogo y /Catalogo/{nombre-del-arreglo}). */
    @GetMapping("/Catalogo")
    public String catalogoAnterior() { return "redirect:/catalogo"; }

    @GetMapping("/Catalogo/{nombre}")
    public String detalleAnterior(@PathVariable String nombre) {
        ArregloFloral p = catalogo.findAll().stream().filter(a -> a.getNombre().equalsIgnoreCase(nombre.replace("-", " ")))
                .findFirst().orElseThrow(() -> new NoEncontradoException("No encontramos el producto"));
        return "redirect:/producto/" + p.getSku();
    }

    @GetMapping("/producto/{sku}")
    public String producto(@PathVariable String sku, HttpServletRequest req, HttpServletResponse res, Model model) {
        ArregloFloral p = catalogo.porSku(sku);
        Usuario u = sesion.usuario(req).orElse(null);
        if (!p.isActivo() && (u == null || !u.isPersonal())) throw new NoEncontradoException("Este producto ya no está en el catálogo");
        catalogo.registrarVista(p);
        Preferencias.registrarCategoria(req, res, p.getCategoriaSlug());
        Preferencias.registrarVisto(req, res, p.getSku());
        model.addAttribute("v", catalogo.vista(p));
        model.addAttribute("p", p);
        model.addAttribute("categoria", catalogo.categoriaPorSlug(p.getCategoriaSlug()));
        model.addAttribute("opciones", catalogo.opcionesPara(p));
        model.addAttribute("relacionados", catalogo.relacionados(p, 4));
        model.addAttribute("enDeseos", deseos.skus(u).contains(p.getSku()));
        model.addAttribute("fechas", carritoService.fechasDisponibles());
        model.addAttribute("horarios", CarritoDeCompraService.HORARIOS);
        model.addAttribute("horariosHoy", carritoService.horariosDe(LocalDate.now()));
        model.addAttribute("hoy", LocalDate.now());
        return "producto";
    }

    /** QR que lleva directo a la ficha del producto (acceso directo con la cámara del celular). */
    @GetMapping(value = "/producto/{sku}/qr.png", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> qrProducto(@PathVariable String sku) {
        ArregloFloral p = catalogo.porSku(sku);
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/producto/" + p.getSku()).toUriString();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qr.png(url, 360));
    }
}
