package com.neksbyte.porelgustoweb.service;

import com.neksbyte.porelgustoweb.model.EstadoPedido;
import com.neksbyte.porelgustoweb.model.Pedido;
import com.neksbyte.porelgustoweb.model.Visita;
import com.neksbyte.porelgustoweb.repository.PedidoRepository;
import com.neksbyte.porelgustoweb.repository.VisitaRepository;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Registro de páginas vistas y resumen para el panel (visitas, visitantes, de dónde llegan y desde dónde). */
@Service
public class EstadisticasService {
    private final VisitaRepository visitas;
    private final PedidoRepository pedidos;
    private final UbicacionIp ubicacion;
    private final String sal;
    /** Las visitas se guardan en segundo plano para no hacer más lenta la página. */
    private final ExecutorService fondo = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "registro-visitas");
        t.setDaemon(true);
        return t;
    });

    public EstadisticasService(VisitaRepository visitas, PedidoRepository pedidos, UbicacionIp ubicacion,
                               @Value("${app.auth.token-secret:dev}") String sal) {
        this.visitas = visitas;
        this.pedidos = pedidos;
        this.ubicacion = ubicacion;
        this.sal = sal;
    }

    public boolean isUbicacionDisponible() { return ubicacion.isDisponible(); }

    /** Datos de una petición (se copian antes de que termine la petición). */
    public record Pagina(String ruta, String ip, String agente, String referencia, String host, String utmFuente, String utmCampana) { }

    public void registrar(Pagina p) {
        LocalDateTime ahora = LocalDateTime.now();
        fondo.submit(() -> {
            Visita v = new Visita();
            v.setFechaHora(ahora);
            v.setRuta(p.ruta().length() > 200 ? p.ruta().substring(0, 200) : p.ruta());
            v.setVisitante(huella(p.ip(), p.agente(), ahora.toLocalDate()));
            v.setOrigen(origen(p.referencia(), p.host(), p.utmFuente()));
            v.setCampana(Textos.vacio(p.utmCampana()) ? null : recortar(p.utmCampana(), 80));
            UbicacionIp.Lugar l = ubicacion.ubicar(p.ip());
            if (l != null) {
                v.setPais(recortar(l.pais(), 80));
                v.setRegion(recortar(l.region(), 80));
                v.setCiudad(recortar(l.ciudad(), 80));
            }
            v.setDispositivo(dispositivo(p.agente()));
            v.setNavegador(navegador(p.agente()));
            visitas.save(v);
        });
    }

    @PreDestroy
    void detener() { fondo.shutdown(); }

    /** Huella anónima del día: no permite saber la IP ni seguir a la persona entre días. */
    String huella(String ip, String agente, LocalDate dia) {
        try {
            byte[] h = MessageDigest.getInstance("SHA-256").digest((sal + "|" + ip + "|" + agente + "|" + dia).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(h, 0, 16);
        } catch (Exception e) {
            return "?";
        }
    }

    /** De qué sitio llegó: Google, Facebook, WhatsApp… "Directo" si escribió la dirección; "Interno" si navega dentro del sitio. */
    static String origen(String referencia, String host, String utmFuente) {
        if (!Textos.vacio(utmFuente)) return capitalizar(recortar(utmFuente.trim(), 80));
        if (Textos.vacio(referencia)) return "Directo";
        String r;
        try {
            r = java.net.URI.create(referencia.trim()).getHost();
        } catch (IllegalArgumentException e) {
            return "Otro";
        }
        if (r == null) return "Directo";
        r = r.toLowerCase(Locale.ROOT).replaceFirst("^www\\.", "").replaceFirst("^m\\.", "").replaceFirst("^l\\.", "").replaceFirst("^lm\\.", "");
        if (host != null && r.equalsIgnoreCase(host.replaceFirst("^www\\.", ""))) return "Interno";
        if (r.contains("google.")) return "Google";
        if (r.contains("facebook.") || r.equals("fb.com") || r.contains("fb.me")) return "Facebook";
        if (r.contains("instagram.")) return "Instagram";
        if (r.contains("whatsapp.") || r.equals("wa.me")) return "WhatsApp";
        if (r.contains("tiktok.")) return "TikTok";
        if (r.equals("t.co") || r.contains("twitter.") || r.equals("x.com")) return "X (Twitter)";
        if (r.contains("bing.")) return "Bing";
        if (r.contains("yahoo.")) return "Yahoo";
        if (r.contains("duckduckgo.")) return "DuckDuckGo";
        return recortar(r, 80);
    }

    static String dispositivo(String ua) {
        String a = ua == null ? "" : ua.toLowerCase(Locale.ROOT);
        if (a.contains("ipad") || a.contains("tablet")) return "Tableta";
        if (a.contains("mobi") || a.contains("android") || a.contains("iphone")) return "Celular";
        return "Computadora";
    }

    static String navegador(String ua) {
        String a = ua == null ? "" : ua;
        if (a.contains("Edg/")) return "Edge";
        if (a.contains("OPR/") || a.contains("Opera")) return "Opera";
        if (a.contains("SamsungBrowser")) return "Samsung Internet";
        if (a.contains("Firefox/")) return "Firefox";
        if (a.contains("Chrome/") || a.contains("CriOS/")) return "Chrome";
        if (a.contains("Safari/")) return "Safari";
        return "Otro";
    }

    /** Robots y herramientas automáticas no cuentan como visitas. */
    public static boolean esRobot(String ua) {
        if (ua == null || ua.isBlank()) return true;
        String a = ua.toLowerCase(Locale.ROOT);
        return a.matches(".*(bot|crawl|spider|slurp|curl|wget|python|java/|httpclient|headless|lighthouse|preview|facebookexternalhit|whatsapp/).*");
    }

    private static String recortar(String s, int max) { return s == null ? null : s.length() > max ? s.substring(0, max) : s; }

    private static String capitalizar(String s) { return s.isEmpty() ? s : s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1); }

    // ---------------- Resumen para el panel ----------------

    public record Conteo(String nombre, long total, int porcentaje) { }
    public record Dia(LocalDate dia, long vistas, long visitantes, int porcentaje) { }
    public record Resumen(int dias, long vistas, long visitantes, long pedidos, BigDecimal ventas, String conversion,
                          List<Dia> porDia, List<Conteo> paginas, List<Conteo> origenes, List<Conteo> paises,
                          List<Conteo> ciudades, List<Conteo> dispositivos, List<Conteo> navegadores, List<Conteo> campanas) { }

    @Transactional(readOnly = true)
    public Resumen resumen(int dias) {
        int d = Math.max(1, Math.min(dias, 365));
        LocalDate desde = LocalDate.now().minusDays(d - 1L);
        List<Visita> lista = visitas.findByFechaHoraAfter(desde.atStartOfDay());
        // Un visitante = una huella por día.
        long visitantes = lista.stream().map(v -> v.getFechaHora().toLocalDate() + v.getVisitante()).distinct().count();
        // La primera vista de cada visitante en el día dice de dónde llegó.
        Map<String, Visita> entradas = new LinkedHashMap<>();
        lista.stream().sorted(Comparator.comparing(Visita::getFechaHora))
                .forEach(v -> entradas.putIfAbsent(v.getFechaHora().toLocalDate() + v.getVisitante(), v));
        Collection<Visita> unicas = entradas.values();

        Map<LocalDate, List<Visita>> porDia = lista.stream().collect(Collectors.groupingBy(v -> v.getFechaHora().toLocalDate()));
        long maxDia = porDia.values().stream().mapToLong(List::size).max().orElse(1);
        List<Dia> serie = new ArrayList<>();
        for (LocalDate x = desde; !x.isAfter(LocalDate.now()); x = x.plusDays(1)) {
            List<Visita> l = porDia.getOrDefault(x, List.of());
            serie.add(new Dia(x, l.size(), l.stream().map(Visita::getVisitante).distinct().count(), (int) (100 * l.size() / Math.max(1, maxDia))));
        }

        List<Pedido> pedidosPeriodo = pedidos.findAll().stream()
                .filter(o -> o.getFechaHora() != null && !o.getFechaHora().toLocalDate().isBefore(desde))
                .filter(o -> o.getEstado() != EstadoPedido.CANCELADO).toList();
        BigDecimal ventas = pedidosPeriodo.stream().map(Pedido::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        String conversion = visitantes == 0 ? "—" : String.format(Locale.US, "%.1f%%", 100.0 * pedidosPeriodo.size() / visitantes);

        return new Resumen(d, lista.size(), visitantes, pedidosPeriodo.size(), ventas, conversion, serie,
                top(lista, Visita::getRuta, 10),
                // Cada llegada desde un sitio cuenta una vez por visitante y día (aunque ese día llegue desde varios).
                top(lista.stream().filter(v -> !"Interno".equals(v.getOrigen()))
                        .collect(Collectors.toMap(v -> v.getFechaHora().toLocalDate() + v.getVisitante() + v.getOrigen(), v -> v, (x, y) -> x))
                        .values(), Visita::getOrigen, 10),
                top(unicas, v -> v.getPais() == null ? "Sin ubicación" : v.getPais(), 10),
                top(unicas, v -> v.getCiudad() == null ? null
                        : v.getCiudad() + (v.getRegion() == null || v.getRegion().equals(v.getCiudad()) ? "" : ", " + v.getRegion()), 10),
                top(unicas, Visita::getDispositivo, 5),
                top(unicas, Visita::getNavegador, 6),
                top(lista, Visita::getCampana, 8));
    }

    private static List<Conteo> top(Collection<Visita> lista, Function<Visita, String> clave, int max) {
        Map<String, Long> m = lista.stream().map(clave).filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        long total = m.values().stream().mapToLong(Long::longValue).sum();
        return m.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed()).limit(max)
                .map(e -> new Conteo(e.getKey(), e.getValue(), (int) (100 * e.getValue() / Math.max(1, total)))).toList();
    }
}
