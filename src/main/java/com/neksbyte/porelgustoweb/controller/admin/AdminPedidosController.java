package com.neksbyte.porelgustoweb.controller.admin;

import com.neksbyte.porelgustoweb.config.SesionUsuario;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.config.Formatos;
import com.neksbyte.porelgustoweb.model.ContenidoCarrito;
import com.neksbyte.porelgustoweb.model.EstadoPedido;
import com.neksbyte.porelgustoweb.model.Pedido;
import com.neksbyte.porelgustoweb.model.Usuario;
import com.neksbyte.porelgustoweb.service.PedidoService;
import com.neksbyte.porelgustoweb.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

/** Pedidos: lista, detalle, asignación y avance de etapas (administrador, empleado y repartidor). */
@Controller
@RequestMapping("/admin/pedidos")
public class AdminPedidosController {
    private final PedidoService pedidos;
    private final UsuarioService usuarios;
    private final SesionUsuario sesion;

    public AdminPedidosController(PedidoService pedidos, UsuarioService usuarios, SesionUsuario sesion) {
        this.pedidos = pedidos;
        this.usuarios = usuarios;
        this.sesion = sesion;
    }

    @GetMapping
    public String lista(@RequestParam(required = false) String estado, @RequestParam(defaultValue = "false") boolean mios,
                        HttpServletRequest req, Model model) {
        Usuario u = sesion.requerido(req);
        EstadoPedido filtro = null;
        try {
            if (estado != null && !estado.isBlank()) filtro = EstadoPedido.valueOf(estado);
        } catch (IllegalArgumentException ignored) {
            // filtro inválido: se muestran todos
        }
        // El personal que no es administrador solo ve los pedidos que tiene asignados.
        boolean todos = u.isAdmin() && !mios;
        List<Pedido> lista = todos ? pedidos.todos(filtro) : pedidos.asignadosA(u);
        if (filtro != null && !todos) {
            EstadoPedido f = filtro;
            lista = lista.stream().filter(o -> o.getEstado() == f).toList();
        }
        model.addAttribute("pedidos", lista);
        model.addAttribute("estados", EstadoPedido.values());
        model.addAttribute("estado", filtro);
        model.addAttribute("mios", mios);
        model.addAttribute("modulo", "pedidos");
        return "admin/pedidos";
    }

    @GetMapping("/{folio}")
    public String detalle(@PathVariable String folio, HttpServletRequest req, Model model) {
        Usuario u = sesion.requerido(req);
        Pedido o = pedidos.porFolio(folio);
        if (!u.isAdmin() && (o.getEmpleado() == null || !o.getEmpleado().equals(u))) {
            throw new ReglaNegocioException("Este pedido está asignado a otra persona");
        }
        model.addAttribute("pedido", o);
        model.addAttribute("siguiente", o.getEstado().siguiente());
        model.addAttribute("empleados", usuarios.personal());
        String mensaje = mensajeParaEquipo(o, ServletUriComponentsBuilder.fromCurrentContextPath().toUriString());
        String texto = java.net.URLEncoder.encode(mensaje, java.nio.charset.StandardCharsets.UTF_8).replace("+", "%20");
        model.addAttribute("mensajeEquipo", mensaje);
        model.addAttribute("compartirLibre", "https://wa.me/?text=" + texto);
        model.addAttribute("compartirEquipo", usuarios.personal().stream()
                .filter(e -> telefonoWhatsapp(e.getTelefono()) != null)
                .map(e -> java.util.Map.of("nombre", e.getNombre(), "rol", e.getRol(),
                        "liga", "https://wa.me/" + telefonoWhatsapp(e.getTelefono()) + "?text=" + texto))
                .toList());
        model.addAttribute("modulo", "pedidos");
        return "admin/pedido";
    }

    @PostMapping("/{folio}/estado")
    public String estado(@PathVariable String folio, @RequestParam String estado, @RequestParam(required = false) String nota,
                         @RequestParam(required = false) String repartidor, @RequestParam(required = false) String telefono,
                         @RequestParam(required = false) String recibio, HttpServletRequest req, RedirectAttributes flash) {
        try {
            EstadoPedido nuevo = EstadoPedido.valueOf(estado);
            pedidos.actualizarEstado(folio, nuevo, sesion.requerido(req), nota, repartidor, telefono, recibio);
            flash.addFlashAttribute("mensaje", "Estado actualizado a «" + pedidos.porFolio(folio).getEstado().getEtiqueta() + "». Avisamos al cliente.");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", "Elige un estado válido");
        }
        return "redirect:/admin/pedidos/" + folio;
    }

    @PostMapping("/{folio}/fallo")
    public String fallo(@PathVariable String folio, @RequestParam String descripcion, HttpServletRequest req, RedirectAttributes flash) {
        try {
            pedidos.reportarFallo(folio, sesion.requerido(req), descripcion);
            flash.addFlashAttribute("mensaje", "Incidencia registrada. Notificamos al cliente y a los administradores.");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/pedidos/" + folio;
    }

    @PostMapping("/{folio}/asignar")
    public String asignar(@PathVariable String folio, @RequestParam(required = false) Integer empleadoId, HttpServletRequest req,
                          RedirectAttributes flash) {
        try {
            pedidos.asignar(folio, empleadoId, sesion.requerido(req));
            flash.addFlashAttribute("mensaje", empleadoId == null ? "Pedido sin asignar." : "Pedido asignado.");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/pedidos/" + folio;
    }

    /**
     * Mensaje para mandar el pedido a un empleado por WhatsApp: solo lo necesario para prepararlo y entregarlo
     * (sin precios, forma de pago ni datos de quien compró).
     */
    static String mensajeParaEquipo(Pedido o, String base) {
        StringBuilder sb = new StringBuilder("🌸 *Pedido " + o.getFolio() + "* · Por el Gusto\n");
        sb.append("\n📅 Entrega: ").append(Formatos.fechaLarga(o.getFechaEntrega()));
        if (o.getHorarioEntrega() != null) sb.append(", ").append(o.getHorarioEntrega());
        sb.append("\n👤 Recibe: ").append(o.getDestinatario());
        if (o.getTelefonoDestinatario() != null) sb.append(" · Tel. ").append(o.getTelefonoDestinatario());
        sb.append("\n📍 ").append(o.getDireccionEntrega());
        if (o.getInstruccionesEspeciales() != null) sb.append("\n   Referencias: ").append(o.getInstruccionesEspeciales());
        if (o.getDireccionEntrega() != null) {
            sb.append("\n   Mapa: https://www.google.com/maps/search/?api=1&query=")
              .append(java.net.URLEncoder.encode(o.getDireccionEntrega(), java.nio.charset.StandardCharsets.UTF_8));
        }
        sb.append("\n\n💐 Productos:");
        for (ContenidoCarrito i : o.getItems()) {
            sb.append("\n• ").append(i.getCantidad()).append(" × ").append(i.getArregloFloral().getNombre())
              .append(" (").append(i.getArregloFloral().getSku()).append(")");
            if (i.getPersonalizacion() != null) sb.append("\n   Personalización: ").append(i.getPersonalizacion().replaceAll(" \\(\\+\\$\\d+\\)", ""));
            if (i.getNota() != null) sb.append("\n   Nota: ").append(i.getNota());
        }
        if (o.isConDedicatoria()) {
            sb.append("\n\n💌 Dedicatoria con QR (imprimir en la tarjeta): ").append(base).append("/qr/").append(o.getTokenQr()).append(".png");
        }
        sb.append("\n\nDetalle: ").append(base).append("/admin/pedidos/").append(o.getFolio());
        return sb.toString();
    }

    /** Número para wa.me: 10 dígitos de México se completan con 52. */
    static String telefonoWhatsapp(String tel) {
        if (tel == null) return null;
        String d = tel.replaceAll("\\D", "");
        if (d.length() == 10) return "52" + d;
        return d.length() >= 11 ? d : null;
    }
}
