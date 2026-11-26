package com.neksbyte.porelgustoweb.controller.admin;

import com.neksbyte.porelgustoweb.config.SesionUsuario;
import com.neksbyte.porelgustoweb.config.TiendaConfig;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.service.ConfiguracionService;
import com.neksbyte.porelgustoweb.service.pago.PagoConPayPal;
import com.neksbyte.porelgustoweb.service.pago.PagoConTarjeta;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Configuración de la tienda: a qué cuentas de Stripe y PayPal llegan los pagos y punto de partida de los envíos. */
@Controller
@RequestMapping("/admin/configuracion")
public class AdminConfiguracionController {
    private final ConfiguracionService configuracion;
    private final PagoConTarjeta tarjeta;
    private final PagoConPayPal paypal;
    private final TiendaConfig tienda;
    private final com.neksbyte.porelgustoweb.service.pago.PagoService pagos;
    private final SesionUsuario sesion;

    public AdminConfiguracionController(ConfiguracionService configuracion, PagoConTarjeta tarjeta, PagoConPayPal paypal,
                                        TiendaConfig tienda, com.neksbyte.porelgustoweb.service.pago.PagoService pagos, SesionUsuario sesion) {
        this.configuracion = configuracion;
        this.tarjeta = tarjeta;
        this.paypal = paypal;
        this.tienda = tienda;
        this.pagos = pagos;
        this.sesion = sesion;
    }

    @GetMapping
    public String ver(Model model) {
        model.addAttribute("config", configuracion.actual());
        model.addAttribute("stripeMascara", ConfiguracionService.enmascarar(configuracion.stripeSecreta()));
        model.addAttribute("paypalIdMascara", ConfiguracionService.enmascarar(configuracion.paypalClientId()));
        model.addAttribute("tarjetaSimulada", tarjeta.isSimulado());
        model.addAttribute("simulacionPermitida", pagos.isSimulacionPermitida());
        model.addAttribute("tarjetaPrueba", tarjeta.isModoPrueba());
        model.addAttribute("paypalSimulado", paypal.isSimulado());
        model.addAttribute("paypalPrueba", paypal.isModoPrueba());
        model.addAttribute("llaveIlegible", configuracion.isLlaveIlegible());
        model.addAttribute("modulo", "configuracion");
        return "admin/configuracion";
    }

    @PostMapping("/pagos")
    public String pagos(@RequestParam(required = false) String stripe, @RequestParam(defaultValue = "false") boolean borrarStripe,
                        @RequestParam(required = false) String paypalId, @RequestParam(required = false) String paypalSecreta,
                        @RequestParam(defaultValue = "false") boolean paypalProduccion, @RequestParam(defaultValue = "false") boolean borrarPaypal,
                        HttpServletRequest req, RedirectAttributes flash) {
        try {
            configuracion.guardarPagos(stripe, borrarStripe, paypalId, paypalSecreta, paypalProduccion, borrarPaypal, sesion.requerido(req));
            flash.addFlashAttribute("mensaje", "Datos de pago guardados. Usa «Probar conexión» para revisar que funcionen.");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/configuracion";
    }

    @PostMapping("/probar/{pasarela}")
    public String probar(@PathVariable String pasarela, RedirectAttributes flash) {
        try {
            String r = "stripe".equals(pasarela) ? tarjeta.probarConexion() : paypal.probarConexion();
            flash.addFlashAttribute("mensaje", ("stripe".equals(pasarela) ? "Stripe: " : "PayPal: ") + r);
        } catch (IllegalStateException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/configuracion";
    }

    @PostMapping("/origen")
    public String origen(@RequestParam String direccion, @RequestParam(required = false) Double latitud,
                         @RequestParam(required = false) Double longitud, HttpServletRequest req, RedirectAttributes flash) {
        try {
            configuracion.guardarOrigen(direccion, latitud, longitud, sesion.requerido(req));
            flash.addFlashAttribute("mensaje", "Punto de partida actualizado. Los envíos se calculan desde ahí (" + tienda.getEnvioKm1() + ", "
                    + tienda.getEnvioKm2() + " y " + tienda.getEnvioKm3() + " km).");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/configuracion#envios";
    }
}
