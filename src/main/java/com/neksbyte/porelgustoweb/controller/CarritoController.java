package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.config.Formatos;
import com.neksbyte.porelgustoweb.config.TiendaConfig;
import com.neksbyte.porelgustoweb.dto.LineaCarrito;
import com.neksbyte.porelgustoweb.dto.ResumenCompra;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.service.CarritoDeCompraService;
import com.neksbyte.porelgustoweb.service.CarritoSesion;
import com.neksbyte.porelgustoweb.service.Textos;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

/** Carrito de compras (en la sesión) y "Continuar por WhatsApp". */
@Controller
public class CarritoController {
    private final CarritoSesion carrito;
    private final CarritoDeCompraService servicio;
    private final TiendaConfig tienda;

    public CarritoController(CarritoSesion carrito, CarritoDeCompraService servicio, TiendaConfig tienda) {
        this.carrito = carrito;
        this.servicio = servicio;
        this.tienda = tienda;
    }

    @GetMapping("/carrito")
    public String carrito(Model model) {
        model.addAttribute("avisos", servicio.revalidar(carrito));
        model.addAttribute("carrito", carrito);
        model.addAttribute("resumen", servicio.resumen(carrito));
        model.addAttribute("fechas", servicio.fechasDisponibles());
        model.addAttribute("horarios", CarritoDeCompraService.HORARIOS);
        model.addAttribute("horariosHoy", servicio.horariosDe(LocalDate.now()));
        model.addAttribute("hoy", LocalDate.now());
        model.addAttribute("todoDisponible", servicio.todoDisponible(carrito));
        return "carrito";
    }

    /** Rutas anteriores del carrito. */
    @GetMapping("/MiCarrito")
    public String carritoAnterior() { return "redirect:/carrito"; }

    @GetMapping("/compra")
    public String compraAnterior() { return "redirect:/checkout"; }

    @PostMapping("/carrito/agregar")
    public String agregar(@RequestParam Integer productoId, @RequestParam(defaultValue = "1") int cantidad,
                          @RequestParam(required = false) List<Integer> opciones, @RequestParam(required = false) String nota,
                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                          @RequestParam(required = false) String horario, @RequestParam(required = false) String sku,
                          @RequestParam(defaultValue = "false") boolean comprarAhora, RedirectAttributes flash) {
        String volver = Textos.vacio(sku) ? "/catalogo" : "/producto/" + sku;
        try {
            List<String> noDisponibles = servicio.agregar(carrito, productoId, cantidad, opciones, nota);
            if (fecha != null && !Textos.vacio(horario)) {
                try {
                    servicio.asignarEntrega(carrito, fecha, horario);
                } catch (ReglaNegocioException e) {
                    flash.addFlashAttribute("error", e.getMessage());
                }
            }
            LineaCarrito ultima = carrito.getLineas().stream().filter(l -> l.getIdArreglo().equals(productoId))
                    .reduce((a, b) -> b).orElseThrow();
            String msg = "Agregaste " + ultima.getNombre() + " a tu carrito. Llevas " + carrito.getCantidad()
                    + (carrito.getCantidad() == 1 ? " producto" : " productos") + " por " + Formatos.dinero(servicio.subtotal(carrito)) + ".";
            if (!noDisponibles.isEmpty()) msg += " Estas opciones no están disponibles y no se agregaron: " + String.join(", ", noDisponibles) + ".";
            flash.addFlashAttribute("mensaje", msg);
            flash.addFlashAttribute("agregadoAlCarrito", true);
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
            return "redirect:" + volver;
        }
        return comprarAhora ? "redirect:/carrito" : "redirect:" + volver;
    }

    @PostMapping("/carrito/cantidad")
    public String cantidad(@RequestParam String idLinea, @RequestParam int cantidad, RedirectAttributes flash) {
        try {
            servicio.cambiarCantidad(carrito, idLinea, cantidad);
            if (carrito.isVacio()) return vacio(flash);
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/carrito";
    }

    @PostMapping("/carrito/quitar")
    public String quitar(@RequestParam String idLinea, RedirectAttributes flash) {
        try {
            servicio.quitar(carrito, idLinea);
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
            return "redirect:/carrito";
        }
        if (carrito.isVacio()) return vacio(flash);
        flash.addFlashAttribute("mensaje", "Quitamos el producto de tu carrito.");
        return "redirect:/carrito";
    }

    /** Si el carrito queda vacío, se avisa y se lleva al inicio. */
    private String vacio(RedirectAttributes flash) {
        carrito.vaciar();
        flash.addFlashAttribute("mensaje", "Tu carrito está vacío. ¡Sigue viendo opciones!");
        return "redirect:/";
    }

    @PostMapping("/carrito/cupon")
    public String cupon(@RequestParam(required = false) String codigo, RedirectAttributes flash) {
        try {
            servicio.aplicarCupon(carrito, codigo);
            flash.addFlashAttribute("mensaje", "Cupón aplicado.");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/carrito";
    }

    @PostMapping("/carrito/cupon/quitar")
    public String quitarCupon() {
        carrito.setCodigoCupon(null);
        return "redirect:/carrito";
    }

    @PostMapping("/carrito/entrega")
    public String entrega(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                          @RequestParam(required = false) String horario, RedirectAttributes flash) {
        try {
            servicio.asignarEntrega(carrito, fecha, horario);
            flash.addFlashAttribute("mensaje", "Entrega programada para el " + Formatos.fechaLarga(fecha) + " de " + horario + ".");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/carrito";
    }

    /** Arma el mensaje con el carrito y abre WhatsApp (si se atiende en este horario). */
    @GetMapping("/carrito/whatsapp")
    public String whatsapp(RedirectAttributes flash) {
        if (carrito.isVacio()) return vacio(flash);
        servicio.revalidar(carrito);
        if (!servicio.todoDisponible(carrito)) {
            flash.addFlashAttribute("error", "Quita los productos no disponibles antes de continuar.");
            return "redirect:/carrito";
        }
        if (!carrito.isConEntrega()) {
            flash.addFlashAttribute("error", "Elige la fecha y el horario de entrega antes de continuar.");
            return "redirect:/carrito";
        }
        if (!tienda.isWhatsappDisponible()) return "whatsapp-no-disponible";
        ResumenCompra r = servicio.resumen(carrito);
        StringBuilder sb = new StringBuilder("Hola Por el Gusto 🌸\n\nQuiero hacer este pedido:\n");
        for (LineaCarrito l : carrito.getLineas()) {
            sb.append("\n💐 ").append(l.getCantidad()).append(" × ").append(l.getNombre()).append(" (").append(l.getSku()).append(") - ")
              .append(Formatos.dinero(l.getSubtotal()));
            if (!l.getNombresOpciones().isEmpty()) sb.append("\n   Personalización: ").append(l.getOpcionesTexto());
            if (l.getNota() != null) sb.append("\n   Nota: ").append(l.getNota());
        }
        sb.append("\n\n📅 Entrega: ").append(Formatos.fechaLarga(carrito.getFechaEntrega())).append(", ").append(carrito.getHorarioEntrega());
        sb.append("\n\nSubtotal: ").append(Formatos.dinero(r.getSubtotal()));
        if (r.getDescuento().signum() > 0) sb.append("\nDescuento (").append(r.getCupon()).append("): -").append(Formatos.dinero(r.getDescuento()));
        sb.append("\nEnvío: ").append(r.isEnvioCalculado() ? (r.getEnvio().signum() == 0 ? "Gratis" : Formatos.dinero(r.getEnvio()))
                : "por calcular con la dirección (desde " + Formatos.dinero(tienda.getEnvioTarifa10()) + ")");
        sb.append("\nTotal: ").append(Formatos.dinero(r.getTotal())).append(r.isEnvioCalculado() ? "" : " + envío");
        sb.append("\n\n¿Me ayudan a completar la compra?");
        return "redirect:https://wa.me/" + tienda.getWhatsapp() + "?text=" + URLEncoder.encode(sb.toString(), StandardCharsets.UTF_8).replace("+", "%20");
    }
}
