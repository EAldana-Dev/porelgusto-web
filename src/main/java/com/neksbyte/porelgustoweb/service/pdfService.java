package com.neksbyte.porelgustoweb.service;

import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class pdfService {

    public String normalizarNombreArchivo(String nombreBase) {
        if (nombreBase == null || nombreBase.isBlank()) {
            return "codigo-qr.pdf";
        }
        String limpio = nombreBase.trim().replaceAll("[^a-zA-Z0-9-_]", "-");
        return limpio.toLowerCase().endsWith(".pdf") ? limpio : limpio + ".pdf";
    }

    public byte[] validarContenidoPdf(byte[] pdfBytes) {
        Objects.requireNonNull(pdfBytes, "El contenido del PDF no puede ser nulo");
        if (pdfBytes.length == 0) {
            throw new IllegalArgumentException("El contenido del PDF está vacío");
        }
        return pdfBytes;
    }
}
