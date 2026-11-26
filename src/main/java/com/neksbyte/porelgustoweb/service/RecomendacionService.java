package com.neksbyte.porelgustoweb.service;

import com.neksbyte.porelgustoweb.dao.ArregloFloralDao;
import com.neksbyte.porelgustoweb.dto.ProductoVista;
import com.neksbyte.porelgustoweb.model.ArregloFloral;
import com.neksbyte.porelgustoweb.model.TipoCategoria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * "Recomendados para ti": combina temporada, novedad, popularidad, ventas y,
 * si el usuario aceptó cookies, las categorías que más visita.
 */
@Service
@Transactional(readOnly = true)
public class RecomendacionService {
    private final ArregloFloralDao arreglos;
    private final ArregloFloralService catalogo;

    public RecomendacionService(ArregloFloralDao arreglos, ArregloFloralService catalogo) {
        this.arreglos = arreglos;
        this.catalogo = catalogo;
    }

    /**
     * @param categoriasPreferidas slugs de categorías vistas (vacío si no aceptó cookies)
     * @param vistos               SKUs que ya vio (se muestran al final)
     */
    public List<ProductoVista> recomendar(List<String> categoriasPreferidas, Set<String> vistos, int max) {
        List<ArregloFloral> disponibles = arreglos.findAll().stream().filter(p -> p.isDisponible() && !p.isPrueba()).toList();
        long maxVistas = Math.max(1, disponibles.stream().mapToLong(ArregloFloral::getVistas).max().orElse(1));
        int maxVentas = Math.max(1, disponibles.stream().mapToInt(ArregloFloral::getVentas).max().orElse(1));
        Map<ArregloFloral, Double> puntos = new HashMap<>();
        for (ArregloFloral p : disponibles) {
            double temporada = "temporada".equals(p.getCategoriaSlug())
                    || p.getColecciones().stream().anyMatch(c -> c.getTipo() == TipoCategoria.TEMPORADA && c.isVigente()) ? 1 : 0;
            long dias = p.getFechaAlta() == null ? 365 : Duration.between(p.getFechaAlta(), LocalDateTime.now()).toDays();
            double novedad = Math.max(0, 1 - dias / 60.0);
            double popularidad = (double) p.getVistas() / maxVistas;
            double ventas = (double) p.getVentas() / maxVentas;
            int idx = categoriasPreferidas.indexOf(p.getCategoriaSlug());
            double preferencia = idx < 0 ? 0 : 1.0 - idx * 0.25;
            double total = 1.2 * temporada + 0.8 * novedad + 1.0 * popularidad + 1.5 * ventas + 2.0 * Math.max(0, preferencia);
            if (vistos.contains(p.getSku())) total -= 1.5;
            // Pequeña variación estable por día para que la portada no sea siempre igual.
            total += (Math.abs((p.getSku() + LocalDateTime.now().getDayOfYear()).hashCode()) % 100) / 400.0;
            puntos.put(p, total);
        }
        return catalogo.vistas(puntos.entrySet().stream()
                .sorted(Map.Entry.<ArregloFloral, Double>comparingByValue().reversed())
                .limit(max).map(Map.Entry::getKey).toList());
    }

    /** Más vendidos (si aún no hay ventas, los más vistos). */
    public List<ProductoVista> masVendidos(int max) {
        return catalogo.vistas(arreglos.findAll().stream().filter(p -> p.isDisponible() && !p.isPrueba())
                .sorted(Comparator.comparingInt(ArregloFloral::getVentas).reversed()
                        .thenComparing(Comparator.comparingLong(ArregloFloral::getVistas).reversed()))
                .limit(max).toList());
    }
}
