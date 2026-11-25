package com.neksbyte.porelgustoweb.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class QrCodeService {

    private static final String URL_LOGO_POR_DEFECTO =
        "https://static.wixstatic.com/media/1e29ba_ba03290225344134b5fbf298417327b0~mv2.png/v1/crop/x_2,y_0,w_284,h_284/fill/w_210,h_210,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/lol.png";

    private static final String FORMATO_IMAGEN = "PNG";
    private static final String CODIFICACION_TEXTO = "UTF-8";
    private static final int MARGEN_QR = 0;
    private static final double PROPORCION_LOGO = 0.20;
    private static final int RELLENO_LOGO = 4;
    private static final int TIMEOUT_MS = 5000;

    private final QRCodeWriter qrCodeWriter = new QRCodeWriter();

    public byte[] generarPngBytes(String contenido, int ancho, int alto) throws WriterException, IOException {
        return generarPngBytesConLogo(contenido, ancho, alto, URL_LOGO_POR_DEFECTO);
    }

    public byte[] generarPngBytesSinLogo(String contenido, int ancho, int alto) throws WriterException, IOException {
        return generarPngBytesConLogo(contenido, ancho, alto, null);
    }

    public String generarBase64(String contenido, int ancho, int alto) throws WriterException, IOException {
        byte[] pngBytes = generarPngBytes(contenido, ancho, alto);
        return Base64.getEncoder().encodeToString(pngBytes);
    }


    private byte[] generarPngBytesConLogo(String contenido, int ancho, int alto, String urlLogo)
        throws WriterException, IOException {

        BufferedImage imagenQr = crearImagenQrBase(contenido, ancho, alto);
        BufferedImage logo = descargarLogo(urlLogo);

        if (logo != null) {
            incrustarLogoCentrado(imagenQr, logo);
        }

        return convertirBufferedImageAPng(imagenQr);
    }

    // --- 1. Matriz y QR base a color (ARGB) ---

    private BufferedImage crearImagenQrBase(String contenido, int ancho, int alto) throws WriterException {
        Map<EncodeHintType, Object> opciones = crearOpcionesQr();
        BitMatrix bitMatrix = qrCodeWriter.encode(contenido, BarcodeFormat.QR_CODE, ancho, alto, opciones);

        BufferedImage qrBinario = MatrixToImageWriter.toBufferedImage(bitMatrix);
        BufferedImage qrArgb = new BufferedImage(
            qrBinario.getWidth(),
            qrBinario.getHeight(),
            BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2d = qrArgb.createGraphics();
        try {
            g2d.drawImage(qrBinario, 0, 0, null);
        } finally {
            g2d.dispose();
        }
        return qrArgb;
    }

    private Map<EncodeHintType, Object> crearOpcionesQr() {
        Map<EncodeHintType, Object> opciones = new HashMap<>();
        opciones.put(EncodeHintType.CHARACTER_SET, CODIFICACION_TEXTO);
        opciones.put(EncodeHintType.MARGIN, MARGEN_QR);
        opciones.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        return opciones;
    }


    private BufferedImage descargarLogo(String urlLogo) {
        if (urlLogo == null || urlLogo.isBlank()) {
            return null;
        }
        try {
            URLConnection conexion = URI.create(urlLogo).toURL().openConnection();
            conexion.setConnectTimeout(TIMEOUT_MS);
            conexion.setReadTimeout(TIMEOUT_MS);
            conexion.setRequestProperty("User-Agent", "Mozilla/5.0");

            try (InputStream stream = conexion.getInputStream()) {
                return ImageIO.read(stream);
            }
        } catch (Exception e) {
            return null;
        }
    }

    private void incrustarLogoCentrado(BufferedImage imagenQr, BufferedImage logo) {
        int tamanoLogo = calcularTamanoLogo(imagenQr);
        Point posicion = calcularPosicionCentrada(imagenQr, tamanoLogo);
        BufferedImage logoEscalado = escalarLogo(logo, tamanoLogo);

        Graphics2D g2d = imagenQr.createGraphics();
        try {
            configurarRenderizado(g2d);
            dibujarRespaldoBlanco(g2d, posicion, tamanoLogo);
            g2d.drawImage(logoEscalado, posicion.x, posicion.y, null);
        } finally {
            g2d.dispose();
        }
    }

    private int calcularTamanoLogo(BufferedImage imagenQr) {
        int ladoMenor = Math.min(imagenQr.getWidth(), imagenQr.getHeight());
        return (int) (ladoMenor * PROPORCION_LOGO);
    }

    private Point calcularPosicionCentrada(BufferedImage imagenQr, int tamanoLogo) {
        int x = (imagenQr.getWidth() - tamanoLogo) / 2;
        int y = (imagenQr.getHeight() - tamanoLogo) / 2;
        return new Point(x, y);
    }

    private BufferedImage escalarLogo(BufferedImage logo, int tamano) {
        BufferedImage escalada = new BufferedImage(tamano, tamano, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = escalada.createGraphics();
        try {
            configurarRenderizado(g2d);
            g2d.drawImage(logo, 0, 0, tamano, tamano, null);
        } finally {
            g2d.dispose();
        }
        return escalada;
    }

    private void configurarRenderizado(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    private void dibujarRespaldoBlanco(Graphics2D g2d, Point posicion, int tamanoLogo) {
        int x = posicion.x - RELLENO_LOGO;
        int y = posicion.y - RELLENO_LOGO;
        int lado = tamanoLogo + (RELLENO_LOGO * 2);

        g2d.setColor(Color.WHITE);
        g2d.fill(new RoundRectangle2D.Float(x, y, lado, lado, 1, 1));
    }


    private byte[] convertirBufferedImageAPng(BufferedImage imagen) throws IOException {
        try (ByteArrayOutputStream salidaStream = new ByteArrayOutputStream()) {
            ImageIO.write(imagen, FORMATO_IMAGEN, salidaStream);
            return salidaStream.toByteArray();
        }
    }
}
