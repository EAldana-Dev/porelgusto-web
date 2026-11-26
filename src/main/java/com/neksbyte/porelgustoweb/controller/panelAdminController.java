package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.config.SesionUsuario;
import com.neksbyte.porelgustoweb.model.EstadoPedido;
import com.neksbyte.porelgustoweb.model.Pedido;
import com.neksbyte.porelgustoweb.model.Usuario;
import com.neksbyte.porelgustoweb.service.ArregloFloralService;
import com.neksbyte.porelgustoweb.service.EstadisticasService;
import com.neksbyte.porelgustoweb.service.PedidoService;
import com.neksbyte.porelgustoweb.service.SoporteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

/** Tablero del panel: pedidos por etapa, ventas, entregas de hoy, inventario bajo y soporte. */
@Controller
public class panelAdminController {
    private final PedidoService pedidos;
    private final ArregloFloralService catalogo;
    private final SoporteService soporte;
    private final EstadisticasService estadisticas;
    private final SesionUsuario sesion;

    public panelAdminController(PedidoService pedidos, ArregloFloralService catalogo, SoporteService soporte,
                                EstadisticasService estadisticas, SesionUsuario sesion) {
        this.pedidos = pedidos;
        this.catalogo = catalogo;
        this.soporte = soporte;
        this.estadisticas = estadisticas;
        this.sesion = sesion;
    }

    @GetMapping("/admin")
    public String tablero(HttpServletRequest req, Model model) {
        Usuario u = sesion.requerido(req);
        List<Pedido> todos = u.isAdmin() ? pedidos.todos(null) : pedidos.asignadosA(u);
        model.addAttribute("recientes", todos.stream().limit(8).toList());
        model.addAttribute("hoy", todos.stream().filter(o -> LocalDate.now().equals(o.getFechaEntrega()) && !o.getEstado().esFinal()).toList());
        if (u.tienePermiso("ESTADISTICAS")) model.addAttribute("visitantesHoy", estadisticas.resumen(1).visitantes());
        model.addAttribute("pendientes", pedidos.contar(EstadoPedido.PENDIENTE));
        model.addAttribute("preparando", pedidos.contar(EstadoPedido.PREPARANDO));
        model.addAttribute("enCamino", pedidos.contar(EstadoPedido.EN_CAMINO));
        model.addAttribute("entregados", pedidos.contar(EstadoPedido.ENTREGADO));
        model.addAttribute("totalVendido", pedidos.totalVendido());
        model.addAttribute("bajoInventario", catalogo.bajoInventario());
        model.addAttribute("solicitudesAbiertas", soporte.abiertas());
        model.addAttribute("modulo", "tablero");
        return "admin/tablero";
    }

    /** Ruta anterior del panel. */
    @GetMapping("/panelAdmin")
    public String goToPanelAdmin() {
        return "redirect:/admin";
    }
}
