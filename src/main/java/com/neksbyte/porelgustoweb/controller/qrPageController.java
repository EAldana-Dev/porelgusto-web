package com.neksbyte.porelgustoweb.controller;

import com.google.zxing.WriterException;
import com.neksbyte.porelgustoweb.model.ArregloFloral;
import com.neksbyte.porelgustoweb.service.ArregloFloralService;
import com.neksbyte.porelgustoweb.service.QrCodeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

/**
 * Códigos QR: tarjeta con QR y foto de un producto (para imprimir o guardar en PDF) y
 * generador del panel para productos, colecciones o cualquier liga.
 */
@Controller
public class qrPageController {
    private final ArregloFloralService catalogo;
    private final QrCodeService qrCodeService;

    public qrPageController(ArregloFloralService catalogo, QrCodeService qrCodeService) {
        this.catalogo = catalogo;
        this.qrCodeService = qrCodeService;
    }

    /** Tarjeta para imprimir: foto del arreglo de fondo y QR que abre su ficha. */
    @GetMapping("/producto/{sku}/tarjeta-qr")
    public String crearCodigoQR(@PathVariable String sku, Model model) throws IOException, WriterException {
        ArregloFloral arreglo = catalogo.porSku(sku);
        String contenidoQr = ServletUriComponentsBuilder.fromCurrentContextPath().path("/producto/" + arreglo.getSku()).toUriString();
        model.addAttribute("titulo", "Tarjeta QR");
        model.addAttribute("producto", arreglo);
        model.addAttribute("fondo", arreglo.getImagenUrl());
        model.addAttribute("qrBase64", qrCodeService.generarBase64(contenidoQr, 250, 250));
        model.addAttribute("contenidoQr", contenidoQr);
        return "desingQR";
    }

    /** Ruta anterior de la tarjeta QR. */
    @GetMapping("/Catalogo/{nombre}/QR")
    public String rutaAnterior(@PathVariable String nombre) {
        ArregloFloral p = catalogo.findAll().stream().filter(a -> a.getNombre().equalsIgnoreCase(nombre.replace("-", " ")))
                .findFirst().orElseThrow(() -> new com.neksbyte.porelgustoweb.exception.NoEncontradoException("No encontramos el producto"));
        return "redirect:/producto/" + p.getSku() + "/tarjeta-qr";
    }

    @GetMapping("/admin/qr")
    public String generador(@RequestParam(required = false) String texto, @RequestParam(defaultValue = "360") int tamano, Model model) {
        model.addAttribute("texto", texto);
        model.addAttribute("tamano", tamano);
        model.addAttribute("productos", catalogo.todosLosProductos());
        model.addAttribute("colecciones", catalogo.colecciones());
        model.addAttribute("modulo", "qr");
        return "admin/qr";
    }

    @GetMapping(value = "/admin/qr/imagen", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> imagen(@RequestParam String texto, @RequestParam(defaultValue = "360") int tamano,
                                         @RequestParam(defaultValue = "false") boolean descargar) {
        String contenido = texto.length() > 1000 ? texto.substring(0, 1000) : texto;
        ResponseEntity.BodyBuilder r = ResponseEntity.ok().contentType(MediaType.IMAGE_PNG);
        if (descargar) r.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"qr-porelgusto.png\"");
        return r.body(qrCodeService.png(contenido, tamano));
    }
}
