package com.neksbyte.porelgustoweb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalTime;

/** Datos de la tienda leídos de application.properties (también se usan en las vistas como ${tienda}). */
@Component
public class TiendaConfig {
    @Value("${app.tienda.correo}") private String correo;
    @Value("${app.tienda.telefono}") private String telefono;
    @Value("${app.whatsapp.numero}") private String whatsapp;
    @Value("${app.whatsapp.hora-inicio:8}") private int whatsappDesde;
    @Value("${app.whatsapp.hora-fin:21}") private int whatsappHasta;
    @Value("${app.pedido.iva:0.16}") private BigDecimal iva;
    @Value("${app.pedido.envio-gratis-desde:1500}") private BigDecimal envioGratisDesde;
    @Value("${app.envio.km1:10}") private int envioKm1;
    @Value("${app.envio.km2:15}") private int envioKm2;
    @Value("${app.envio.km3:20}") private int envioKm3;
    @Value("${app.envio.tarifa10:100}") private BigDecimal envioTarifa10;
    @Value("${app.envio.tarifa15:150}") private BigDecimal envioTarifa15;
    @Value("${app.envio.tarifa20:200}") private BigDecimal envioTarifa20;
    @Value("${app.uploads.dir:uploads}") private String uploadsDir;

    /** WhatsApp se atiende solo en horario de la tienda. */
    public boolean isWhatsappDisponible() {
        int hora = LocalTime.now().getHour();
        return hora >= whatsappDesde && hora < whatsappHasta;
    }

    public String getCorreo() { return correo; }
    public String getTelefono() { return telefono; }
    public String getWhatsapp() { return whatsapp; }
    public int getWhatsappDesde() { return whatsappDesde; }
    public int getWhatsappHasta() { return whatsappHasta; }
    public BigDecimal getIva() { return iva; }
    public BigDecimal getEnvioGratisDesde() { return envioGratisDesde; }
    public int getEnvioKm1() { return envioKm1; }
    public int getEnvioKm2() { return envioKm2; }
    public int getEnvioKm3() { return envioKm3; }
    public BigDecimal getEnvioTarifa10() { return envioTarifa10; }
    public BigDecimal getEnvioTarifa15() { return envioTarifa15; }
    public BigDecimal getEnvioTarifa20() { return envioTarifa20; }
    public String getUploadsDir() { return uploadsDir; }
}
