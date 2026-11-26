package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.config.Formatos;
import com.neksbyte.porelgustoweb.config.SesionUsuario;
import com.neksbyte.porelgustoweb.dto.FiltroCatalogo;
import com.neksbyte.porelgustoweb.exception.NoEncontradoException;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.model.Pedido;
import com.neksbyte.porelgustoweb.model.Usuario;
import com.neksbyte.porelgustoweb.service.ArregloFloralService;
import com.neksbyte.porelgustoweb.service.PedidoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * API JSON de la tienda: sugerencias del buscador, ubicación del repartidor (telemetría),
 * rastreo en vivo y entrega confirmada escaneando el QR del pedido.
 */
@RestController
@RequestMapping("/api")
public class TiendaApiController {
    private final ArregloFloralService catalogo;
    private final PedidoService pedidos;
    private final SesionUsuario sesion;

    public TiendaApiController(ArregloFloralService catalogo, PedidoService pedidos, SesionUsuario sesion) {
        this.catalogo = catalogo;
        this.pedidos = pedidos;
        this.sesion = sesion;
    }

    /** Sugerencias mientras se escribe en el buscador. */
    @GetMapping("/productos")
    public List<Map<String, Object>> buscar(@RequestParam(defaultValue = "") String q) {
        if (q.trim().length() < 2) return List.of();
        FiltroCatalogo f = new FiltroCatalogo();
        f.setQ(q);
        return catalogo.buscar(f).stream().limit(6).map(v -> Map.<String, Object>of(
                "sku", v.getProducto().getSku(),
                "nombre", v.getProducto().getNombre(),
                "categoria", v.getProducto().getCategoria(),
                "precio", Formatos.dinero(v.getPrecioFinal()),
                "imagen", v.getProducto().getImagenUrl(),
                "disponible", v.getProducto().isDisponible())).toList();
    }

    /** El repartidor envía su ubicación. Requiere sesión del personal. */
    @PostMapping("/telemetria")
    public ResponseEntity<Map<String, Object>> telemetria(@RequestBody Map<String, Object> body, HttpServletRequest req) {
        Usuario u = sesion.usuario(req).filter(Usuario::isPersonal).orElse(null);
        if (u == null) return error(HttpStatus.UNAUTHORIZED, "Inicia sesión como empleado");
        try {
            Pedido o = pedidos.actualizarUbicacion(String.valueOf(body.get("tracking")),
                    ((Number) body.get("latitud")).doubleValue(), ((Number) body.get("longitud")).doubleValue(), u);
            return ResponseEntity.ok(Map.of("ok", true, "eta", Formatos.fechaHora(o.getHoraEstimada())));
        } catch (ReglaNegocioException | NoEncontradoException e) {
            return error(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (ClassCastException | NullPointerException e) {
            return error(HttpStatus.BAD_REQUEST, "Datos inválidos");
        }
    }

    /** Estado del envío para actualizar el mapa del seguimiento sin recargar. */
    @GetMapping("/rastreo/{guia}")
    public ResponseEntity<Map<String, Object>> rastreo(@PathVariable String guia) {
        try {
            Pedido o = pedidos.porGuia(guia);
            Map<String, Object> r = new LinkedHashMap<>();
            r.put("estado", o.getEstado().getEtiqueta());
            r.put("latitud", o.getLatitud());
            r.put("longitud", o.getLongitud());
            r.put("actualizado", o.getUbicacionActualizada() == null ? null : Formatos.hace(o.getUbicacionActualizada()));
            r.put("eta", Formatos.fechaHora(o.getHoraEstimada()));
            return ResponseEntity.ok(r);
        } catch (NoEncontradoException e) {
            return error(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /** El repartidor escanea el QR del pedido para confirmar la entrega. */
    @PostMapping("/qr/{token}/entrega")
    public ResponseEntity<Map<String, Object>> entregaQR(@PathVariable String token, @RequestBody(required = false) Map<String, Object> body,
                                                         HttpServletRequest req) {
        Usuario u = sesion.usuario(req).filter(Usuario::isPersonal).orElse(null);
        if (u == null) return error(HttpStatus.UNAUTHORIZED, "Inicia sesión como empleado");
        try {
            String recibio = body == null || body.get("recibio") == null ? null : String.valueOf(body.get("recibio"));
            Pedido o = pedidos.verificarEntregaQR(token, recibio, u);
            return ResponseEntity.ok(Map.of("ok", true, "folio", o.getFolio(), "estado", o.getEstado().getEtiqueta()));
        } catch (ReglaNegocioException | NoEncontradoException e) {
            return error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private static ResponseEntity<Map<String, Object>> error(HttpStatus status, String msg) {
        return ResponseEntity.status(status).body(Map.of("ok", false, "error", msg));
    }
}
