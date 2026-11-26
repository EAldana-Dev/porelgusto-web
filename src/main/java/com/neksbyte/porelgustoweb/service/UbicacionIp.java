package com.neksbyte.porelgustoweb.service;

import com.maxmind.db.Reader;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;

/**
 * País, estado y ciudad aproximados de una IP con la base gratuita "IP to City Lite" de DB-IP
 * (archivo local .mmdb, sin API externa; se descarga con scripts/descargar-geoip.sh).
 * Si el archivo no está, las visitas se registran sin ubicación.
 */
@Component
public class UbicacionIp {
    private static final Logger log = LoggerFactory.getLogger(UbicacionIp.class);
    private final Reader reader;

    public record Lugar(String pais, String region, String ciudad) { }

    public UbicacionIp(@Value("${app.estadisticas.geoip:geoip/dbip-city-lite.mmdb}") String ruta) {
        Reader r = null;
        File f = new File(ruta);
        if (f.isFile()) {
            try {
                r = new Reader(f);
                log.info("Base de ubicaciones por IP cargada: {}", f.getAbsolutePath());
            } catch (IOException e) {
                log.warn("No se pudo abrir la base de ubicaciones {}: {}", ruta, e.getMessage());
            }
        } else {
            log.info("Sin base de ubicaciones por IP ({}): las visitas se guardan sin país ni ciudad.", ruta);
        }
        this.reader = r;
    }

    public boolean isDisponible() { return reader != null; }

    public Lugar ubicar(String ip) {
        if (reader == null || ip == null || ip.isBlank()) return null;
        try {
            InetAddress dir = InetAddress.getByName(ip.trim());
            if (dir.isLoopbackAddress() || dir.isSiteLocalAddress() || dir.isLinkLocalAddress()) return new Lugar("Red local", null, null);
            Map<?, ?> r = reader.get(dir, Map.class);
            if (r == null) return null;
            String pais = nombre(r.get("country"));
            String ciudad = nombre(r.get("city"));
            String region = r.get("subdivisions") instanceof List<?> l && !l.isEmpty() ? nombre(l.get(0)) : null;
            return new Lugar(pais, region, ciudad);
        } catch (Exception e) {
            return null;
        }
    }

    /** Nombre en español si existe, si no en inglés. */
    private static String nombre(Object nodo) {
        if (!(nodo instanceof Map<?, ?> m) || !(m.get("names") instanceof Map<?, ?> n)) return null;
        Object es = n.get("es");
        return String.valueOf(es != null ? es : n.get("en"));
    }

    @PreDestroy
    void cerrar() throws IOException {
        if (reader != null) reader.close();
    }
}
