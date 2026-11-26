package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.config.SesionUsuario;
import com.neksbyte.porelgustoweb.dto.CheckoutForm;
import com.neksbyte.porelgustoweb.dto.CompraEnProceso;
import com.neksbyte.porelgustoweb.dto.LineaCarrito;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.model.FormaPago;
import com.neksbyte.porelgustoweb.model.Pedido;
import com.neksbyte.porelgustoweb.model.Usuario;
import com.neksbyte.porelgustoweb.service.CarritoDeCompraService;
import com.neksbyte.porelgustoweb.service.CarritoSesion;
import com.neksbyte.porelgustoweb.service.PedidoService;
import com.neksbyte.porelgustoweb.service.UsuarioService;
import com.neksbyte.porelgustoweb.config.Formatos;
import com.neksbyte.porelgustoweb.dto.ResumenCompra;
import com.neksbyte.porelgustoweb.service.pago.PagoConTarjeta;
import com.neksbyte.porelgustoweb.service.pago.PagoService;
import com.neksbyte.porelgustoweb.service.pago.ResultadoPago;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;

/** Pagar en la página: datos de entrega, forma de pago y confirmación con folio. */
@Controller
public class CheckoutController {
    private static final Logger log = LoggerFactory.getLogger(CheckoutController.class);
    static final String COMPRA = "compraEnProceso";
    static final String PEDIDOS_VISTOS = "pedidosVistos";

    private final CarritoSesion carrito;
    private final CarritoDeCompraService carritoService;
    private final PedidoService pedidos;
    private final PagoService pagos;
    private final UsuarioService usuarios;
    private final SesionUsuario sesion;

    public CheckoutController(CarritoSesion carrito, CarritoDeCompraService carritoService, PedidoService pedidos, PagoService pagos,
                              UsuarioService usuarios, SesionUsuario sesion) {
        this.carrito = carrito;
        this.carritoService = carritoService;
        this.pedidos = pedidos;
        this.pagos = pagos;
        this.usuarios = usuarios;
        this.sesion = sesion;
    }

    /** Precondición: productos disponibles y fecha/horario de entrega asignados. */
    private String validarCarrito(RedirectAttributes flash) {
        if (carrito.isVacio()) {
            flash.addFlashAttribute("mensaje", "Tu carrito está vacío. ¡Sigue viendo opciones!");
            return "redirect:/";
        }
        carritoService.revalidar(carrito);
        if (!carritoService.todoDisponible(carrito)) {
            flash.addFlashAttribute("error", "Hay productos que ya no están disponibles. Quítalos para continuar.");
            return "redirect:/carrito";
        }
        if (!carrito.isConEntrega()) {
            flash.addFlashAttribute("error", "Elige la fecha y el horario de entrega para continuar.");
            return "redirect:/carrito";
        }
        return null;
    }

    @GetMapping("/checkout")
    public String formulario(HttpServletRequest req, Model model, RedirectAttributes flash) {
        String r = validarCarrito(flash);
        if (r != null) return r;
        if (!model.containsAttribute("checkoutForm")) {
            // Si regresó de un pago fallido, conservar lo que ya había escrito.
            if (req.getSession().getAttribute(COMPRA) instanceof CompraEnProceso c && c.getDatos() != null) {
                model.addAttribute("checkoutForm", c.getDatos());
            } else {
                CheckoutForm f = new CheckoutForm();
                f.setMetodoPago(pagos.isDisponible(FormaPago.TARJETA) ? "TARJETA" : pagos.isDisponible(FormaPago.PAYPAL) ? "PAYPAL" : null);
                sesion.usuario(req).ifPresent(u -> {
                    f.setNombreCliente(u.getNombre());
                    f.setCorreoCliente(u.getCorreoElectronico());
                    f.setTelefonoCliente(u.getTelefono());
                });
                model.addAttribute("checkoutForm", f);
            }
        }
        return vista(req, model);
    }

    private String vista(HttpServletRequest req, Model model) {
        Usuario u = sesion.usuario(req).orElse(null);
        model.addAttribute("carrito", carrito);
        model.addAttribute("resumen", carritoService.resumen(carrito, (CheckoutForm) model.getAttribute("checkoutForm")));
        model.addAttribute("direcciones", u == null ? List.of() : usuarios.direcciones(u));
        model.addAttribute("tarjetaDisponible", pagos.isDisponible(FormaPago.TARJETA));
        model.addAttribute("paypalDisponible", pagos.isDisponible(FormaPago.PAYPAL));
        model.addAttribute("permiteQR", carrito.getLineas().stream().anyMatch(LineaCarrito::isPermiteQR));
        return "checkout";
    }

    @PostMapping("/checkout")
    public String pagar(@Valid @ModelAttribute("checkoutForm") CheckoutForm form, BindingResult errores,
                        HttpServletRequest req, Model model, RedirectAttributes flash) {
        String r = validarCarrito(flash);
        if (r != null) return r;
        FormaPago forma = null;
        try {
            forma = FormaPago.valueOf(String.valueOf(form.getMetodoPago()));
            if (forma == FormaPago.WHATSAPP) forma = null;
        } catch (IllegalArgumentException ignored) {
            // forma queda null
        }
        if (forma != null && !pagos.isDisponible(forma)) forma = null;
        if (forma == null) errores.rejectValue("metodoPago", "invalido", "Elige un método de pago");
        ResumenCompra resumen = carritoService.resumen(carrito, form);
        if (forma == FormaPago.TARJETA && pagos.estrategia(FormaPago.TARJETA).isConfigurado()
                && resumen.getTotal().compareTo(PagoConTarjeta.MINIMO_MXN) < 0) {
            errores.rejectValue("metodoPago", "minimo", "El pago con tarjeta requiere un total mínimo de $10. Elige PayPal.");
        }
        if (errores.hasErrors()) {
            model.addAttribute("error", "Revisa los datos marcados para continuar.");
            return vista(req, model);
        }
        Usuario u = sesion.usuario(req).orElse(null);
        if (u != null && form.isGuardarDireccion()) usuarios.guardarDireccion(u, form);
        CompraEnProceso compra = new CompraEnProceso();
        compra.setDatos(form);
        compra.setForma(forma);
        compra.setLineas(new ArrayList<>(carrito.getLineas()));
        compra.setResumen(resumen);
        compra.setFechaEntrega(carrito.getFechaEntrega());
        compra.setHorarioEntrega(carrito.getHorarioEntrega());
        req.getSession().setAttribute(COMPRA, compra);
        try {
            String destino = pagos.estrategia(forma).iniciar(compra, ServletUriComponentsBuilder.fromCurrentContextPath().toUriString());
            return "redirect:" + destino;
        } catch (IllegalStateException e) {
            flash.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        }
    }

    /** Costo de envío y total según la dirección que se está escribiendo (lo usa la página de pago). */
    @GetMapping("/checkout/envio")
    @ResponseBody
    public Map<String, Object> cotizarEnvio(@ModelAttribute CheckoutForm direccion) {
        ResumenCompra r = carritoService.resumen(carrito, direccion);
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("calculado", r.isEnvioCalculado());
        res.put("envio", r.isEnvioCalculado() ? (r.getEnvio().signum() == 0 ? "Gratis" : Formatos.dinero(r.getEnvio())) : "Por calcular");
        res.put("detalle", r.getEnvioDetalle());
        res.put("iva", Formatos.dinero(r.getIva()));
        res.put("total", Formatos.dinero(r.getTotal()));
        return res;
    }

    // ---------------- Pasarelas ----------------

    private void soloSimulado(FormaPago forma) {
        if (!pagos.isSimulacionPermitida() || pagos.estrategia(forma).isConfigurado()) {
            throw new com.neksbyte.porelgustoweb.exception.NoEncontradoException("Página no encontrada");
        }
    }

    private CompraEnProceso compra(HttpSession s) {
        if (s.getAttribute(COMPRA) instanceof CompraEnProceso c && !c.isVencida()) return c;
        throw new ReglaNegocioException("Tu sesión de pago expiró. Vuelve a intentarlo desde el carrito.");
    }

    @GetMapping("/pago/tarjeta")
    public String tarjeta(HttpSession s, Model model) {
        soloSimulado(FormaPago.TARJETA);
        model.addAttribute("compra", compra(s));
        return "pago-tarjeta";
    }

    @PostMapping("/pago/tarjeta")
    public String tarjetaEnviar(@RequestParam Map<String, String> datos, HttpServletRequest req, Model model, RedirectAttributes flash) {
        soloSimulado(FormaPago.TARJETA);
        CompraEnProceso c = compra(req.getSession());
        ResultadoPago res = pagos.estrategia(FormaPago.TARJETA).confirmar(c, datos);
        if (!res.isAprobado()) {
            model.addAttribute("compra", c);
            model.addAttribute("errorPago", res.getMensaje());
            model.addAttribute("titular", datos.get("titular"));
            return "pago-tarjeta";
        }
        return finalizar(c, res, req, flash);
    }

    @GetMapping("/pago/paypal-simulado")
    public String paypalSimulado(HttpSession s, Model model) {
        soloSimulado(FormaPago.PAYPAL);
        model.addAttribute("compra", compra(s));
        return "pago-paypal";
    }

    @PostMapping("/pago/paypal-simulado")
    public String paypalSimuladoEnviar(@RequestParam Map<String, String> datos, HttpServletRequest req, RedirectAttributes flash) {
        soloSimulado(FormaPago.PAYPAL);
        CompraEnProceso c = compra(req.getSession());
        ResultadoPago res = pagos.estrategia(FormaPago.PAYPAL).confirmar(c, datos);
        if (!res.isAprobado()) {
            flash.addFlashAttribute("error", res.getMensaje() + ". Puedes intentarlo de nuevo o elegir otro método.");
            return "redirect:/checkout";
        }
        return finalizar(c, res, req, flash);
    }

    @GetMapping("/pago/stripe/exito")
    public String stripeExito(@RequestParam Map<String, String> datos, HttpServletRequest req, RedirectAttributes flash) {
        return regresoPasarela(FormaPago.TARJETA, datos, req, flash);
    }

    @GetMapping("/pago/paypal/exito")
    public String paypalExito(@RequestParam Map<String, String> datos, HttpServletRequest req, RedirectAttributes flash) {
        return regresoPasarela(FormaPago.PAYPAL, datos, req, flash);
    }

    private String regresoPasarela(FormaPago forma, Map<String, String> datos, HttpServletRequest req, RedirectAttributes flash) {
        CompraEnProceso c = compra(req.getSession());
        ResultadoPago res = pagos.estrategia(forma).confirmar(c, datos);
        if (!res.isAprobado()) {
            flash.addFlashAttribute("error", res.getMensaje() + ". No registramos tu pedido; puedes volver a intentarlo.");
            return "redirect:/checkout";
        }
        return finalizar(c, res, req, flash);
    }

    @GetMapping("/pago/cancelado")
    public String cancelado(RedirectAttributes flash) {
        flash.addFlashAttribute("error", "Cancelaste el pago. Tu carrito sigue guardado.");
        return "redirect:/checkout";
    }

    /** Registra el pedido; si ya no hay inventario después de cobrar, se devuelve el dinero. */
    private String finalizar(CompraEnProceso c, ResultadoPago res, HttpServletRequest req, RedirectAttributes flash) {
        Usuario u = sesion.usuario(req).orElse(null);
        Pedido o;
        try {
            o = pedidos.registrarCompra(c, u, res);
        } catch (ReglaNegocioException e) {
            if (res != null && res.isAprobado()) {
                boolean devuelto = pagos.estrategia(c.getForma()).reembolsar(res.getTransaccion(), c.getTotal());
                log.warn("Pedido no registrado tras cobro {}: {} (devolución {})", res.getTransaccion(), e.getMessage(), devuelto);
                flash.addFlashAttribute("error", e.getMessage() + ". " + (devuelto ? "Te devolvimos el cobro." : "Contáctanos para devolverte el cobro."));
            } else {
                flash.addFlashAttribute("error", e.getMessage());
            }
            return "redirect:/carrito";
        }
        carrito.vaciar();
        HttpSession s = req.getSession();
        s.removeAttribute(COMPRA);
        permitirVer(s, o.getFolio());
        return "redirect:/pedido/" + o.getFolio() + "/confirmacion";
    }

    /** Quien compró sin cuenta (o buscó con folio y correo) puede ver su pedido en esta sesión. */
    @SuppressWarnings("unchecked")
    static void permitirVer(HttpSession s, String folio) {
        Set<String> vistos = (Set<String>) s.getAttribute(PEDIDOS_VISTOS);
        if (vistos == null) {
            vistos = new HashSet<>();
            s.setAttribute(PEDIDOS_VISTOS, vistos);
        }
        vistos.add(folio);
    }
}
