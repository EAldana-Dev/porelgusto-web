package com.neksbyte.porelgustoweb.service;

import com.neksbyte.porelgustoweb.config.TiendaConfig;
import com.neksbyte.porelgustoweb.model.ConfiguracionTienda;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Costo de envío por distancia en línea recta desde la florería:
 * dentro de la CDMX, hasta 10 km $100, hasta 15 km $150, hasta 20 km $200 y gratis desde $1,500;
 * a más de 20 km o fuera de la CDMX siempre se cobra $200 (no aplica el envío gratis).
 */
@Service
public class EnvioService {
    private final ConfiguracionService configuracion;
    private final Geocodificador geo;
    private final TiendaConfig tienda;

    public EnvioService(ConfiguracionService configuracion, Geocodificador geo, TiendaConfig tienda) {
        this.configuracion = configuracion;
        this.geo = geo;
        this.tienda = tienda;
    }

    /** Resultado del cálculo para mostrar y cobrar. */
    public record Cotizacion(BigDecimal costo, Double km, boolean enCdmx, boolean ubicada, boolean gratis, String detalle) { }

    /** Códigos postales de la Ciudad de México: 01000 a 16999. */
    public static boolean esCdmx(String cp) {
        if (cp == null || !cp.trim().matches("\\d{5}")) return false;
        int n = Integer.parseInt(cp.trim());
        return n >= 1000 && n <= 16999;
    }

    public Cotizacion cotizar(String calle, String colonia, String ciudad, String codigoPostal, BigDecimal compra) {
        boolean cdmx = esCdmx(codigoPostal);
        Double km = null;
        Optional<double[]> destino = Textos.vacio(codigoPostal) ? Optional.empty() : geo.ubicar(calle, colonia, ciudad, codigoPostal);
        ConfiguracionTienda c = configuracion.actual();
        if (destino.isPresent() && c.getOrigenLatitud() != null && c.getOrigenLongitud() != null) {
            km = Math.round(distanciaKm(c.getOrigenLatitud(), c.getOrigenLongitud(), destino.get()[0], destino.get()[1]) * 10) / 10.0;
        }
        return calcular(km, cdmx, compra);
    }

    /** Reglas de cobro (separadas para poder probarlas sin el mapa). */
    public Cotizacion calcular(Double km, boolean cdmx, BigDecimal compra) {
        BigDecimal maximo = tienda.getEnvioTarifa20();
        if (!cdmx) return new Cotizacion(maximo, km, false, km != null, false, "Fuera de la Ciudad de México");
        if (km == null) {
            return new Cotizacion(maximo, null, true, false, false, "No pudimos ubicar la dirección en el mapa; se cobra la tarifa máxima");
        }
        if (km > tienda.getEnvioKm3()) return new Cotizacion(maximo, km, true, true, false, "Más de " + tienda.getEnvioKm3() + " km");
        if (compra != null && compra.compareTo(tienda.getEnvioGratisDesde()) >= 0) {
            return new Cotizacion(BigDecimal.ZERO, km, true, true, true, "Envío gratis");
        }
        BigDecimal costo = km <= tienda.getEnvioKm1() ? tienda.getEnvioTarifa10()
                : km <= tienda.getEnvioKm2() ? tienda.getEnvioTarifa15() : tienda.getEnvioTarifa20();
        return new Cotizacion(costo, km, true, true, false, "A " + km + " km de la florería");
    }

    /** Distancia en línea recta (fórmula del haversine). */
    public static double distanciaKm(double lat1, double lon1, double lat2, double lon2) {
        double r = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 2 * r * Math.asin(Math.sqrt(a));
    }
}
