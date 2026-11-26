package com.neksbyte.porelgustoweb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Ubica una dirección en el mapa (latitud y longitud) con Nominatim de OpenStreetMap, gratuito y sin llave.
 * Guarda los resultados en memoria y espera 1 segundo entre consultas, como pide su política de uso.
 */
@Component
public class Geocodificador {
    private static final Logger log = LoggerFactory.getLogger(Geocodificador.class);
    private final RestClient http;
    private final String url;
    private final boolean activo;
    private final Map<String, Optional<double[]>> cache = new ConcurrentHashMap<>();
    private long ultimaConsulta;

    public Geocodificador(@Value("${app.envio.geocodificador.url:https://nominatim.openstreetmap.org/search}") String url,
                          @Value("${app.envio.geocodificador.activo:true}") boolean activo) {
        SimpleClientHttpRequestFactory f = new SimpleClientHttpRequestFactory();
        f.setConnectTimeout(4000);
        f.setReadTimeout(6000);
        this.http = RestClient.builder().requestFactory(f).defaultHeader("User-Agent", "PorElGusto-Web/1.0").build();
        this.url = url;
        this.activo = activo;
    }

    /** Búsqueda libre ("San Bernabé, Azcapotzalco, 02830 CDMX"). */
    public Optional<double[]> ubicar(String direccion) {
        return consultar("q|" + direccion, UriComponentsBuilder.fromUriString(url).queryParam("q", direccion));
    }

    /** Búsqueda por partes de una dirección de entrega en México; si no la encuentra, usa el código postal. */
    public Optional<double[]> ubicar(String calle, String colonia, String ciudad, String codigoPostal) {
        String c = calle == null ? "" : calle.trim();
        Optional<double[]> r = consultar("d|" + c + "|" + codigoPostal + "|" + colonia,
                UriComponentsBuilder.fromUriString(url).queryParam("street", c)
                        .queryParam("city", Textos.vacio(ciudad) ? "Ciudad de México" : ciudad.trim())
                        .queryParam("postalcode", codigoPostal));
        if (r.isPresent()) return r;
        if (!Textos.vacio(colonia)) {
            r = ubicar(colonia.trim() + ", " + codigoPostal + ", Ciudad de México");
            if (r.isPresent()) return r;
        }
        return consultar("cp|" + codigoPostal, UriComponentsBuilder.fromUriString(url).queryParam("postalcode", codigoPostal));
    }

    private Optional<double[]> consultar(String clave, UriComponentsBuilder b) {
        if (!activo) return Optional.empty();
        Optional<double[]> guardado = cache.get(clave);
        if (guardado != null) return guardado;
        URI uri = b.queryParam("format", "json").queryParam("limit", 1).queryParam("countrycodes", "mx")
                .encode().build().toUri();
        try {
            esperarTurno();
            List<?> r = http.get().uri(uri).retrieve().body(List.class);
            Optional<double[]> res = r == null || r.isEmpty() ? Optional.empty() : Optional.of(new double[]{
                    Double.parseDouble(String.valueOf(((Map<?, ?>) r.get(0)).get("lat"))),
                    Double.parseDouble(String.valueOf(((Map<?, ?>) r.get(0)).get("lon")))});
            cache.put(clave, res);
            return res;
        } catch (RuntimeException e) {
            log.warn("No se pudo ubicar la dirección en el mapa: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private synchronized void esperarTurno() {
        long espera = 1000 - (System.currentTimeMillis() - ultimaConsulta);
        if (espera > 0) {
            try { Thread.sleep(espera); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        ultimaConsulta = System.currentTimeMillis();
    }
}
