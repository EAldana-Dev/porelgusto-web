package com.neksbyte.porelgustoweb.controller;


import com.google.zxing.WriterException;
import com.neksbyte.porelgustoweb.dao.ArregloFloralDao;
import com.neksbyte.porelgustoweb.model.ArregloFloral;
import com.neksbyte.porelgustoweb.service.QrCodeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
public class qrPageController {
    private final ArregloFloralDao dao;
    private final QrCodeService qrCodeService;

    private final String dominio = "localhost:8080";

    public qrPageController(ArregloFloralDao dao, QrCodeService qrCodeService) {
        this.dao = dao;
        this.qrCodeService = qrCodeService;
    }

    @GetMapping("/Catalogo/{nombre}/QR")
    public String crearCodigoQR(@PathVariable String nombre, Model model) throws IOException, WriterException {

        String nombreReal = nombre.replace("-", " ");
        ArregloFloral arreglo = dao.findByNombre(nombreReal)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        String fondo = arreglo.getImagenes() != null
            && !arreglo.getImagenes().isEmpty()
            ? arreglo.getImagenes().get(0).getDireccionImg()
            : null;



        String contenidoQr = dominio+"/Catalogo/" + nombre;
        String qrBase64 = qrCodeService.generarBase64(contenidoQr, 250, 250);

        model.addAttribute("titulo", "QR");
        model.addAttribute("producto", arreglo);

        model.addAttribute("fondo", fondo);

        model.addAttribute("qrBase64", qrBase64);
        model.addAttribute("contenidoQr", contenidoQr);

        return "desingQR";
    }
}
