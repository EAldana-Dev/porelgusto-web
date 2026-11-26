package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.config.SesionUsuario;
import com.neksbyte.porelgustoweb.dto.FacturaForm;
import com.neksbyte.porelgustoweb.exception.NoEncontradoException;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.model.EstadoPedido;
import com.neksbyte.porelgustoweb.model.Pedido;
import com.neksbyte.porelgustoweb.model.Usuario;
import com.neksbyte.porelgustoweb.service.PedidoService;
import com.neksbyte.porelgustoweb.service.QrCodeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Set;

/** Consultar el pedido (con sesión o con folio + correo), ticket, factura y dedicatoria con QR. */
@Controller
public class SeguimientoController {
    /** Usos del CFDI más comunes para personas y empresas. */
    static final List<String> USOS_CFDI = List.of("G01 Adquisición de mercancías", "G03 Gastos en general",
            "D10 Pagos por servicios educativos", "S01 Sin efectos fiscales");

    private final PedidoService pedidos;
    private final QrCodeService qr;
    private final SesionUsuario sesion;

    public SeguimientoController(PedidoService pedidos, QrCodeService qr, SesionUsuario sesion) {
        this.pedidos = pedidos;
        this.qr = qr;
        this.sesion = sesion;
    }

    @GetMapping("/seguimiento")
    public String seguimiento(@RequestParam(required = false) String folio, Model model) {
        model.addAttribute("folio", folio);
        return "seguimiento";
    }

    @PostMapping("/seguimiento")
    public String buscar(@RequestParam String folio, @RequestParam String correo, HttpSession s, RedirectAttributes flash) {
        try {
            Pedido o = pedidos.porFolio(folio);
            String correoPedido = o.getCorreoCliente() != null ? o.getCorreoCliente()
                    : o.getCarritoDeCompra() != null && o.getCarritoDeCompra().getUsuario() != null
                    ? o.getCarritoDeCompra().getUsuario().getCorreoElectronico() : null;
            if (correoPedido == null || !correoPedido.equalsIgnoreCase(correo.trim())) throw new NoEncontradoException("x");
            CheckoutController.permitirVer(s, o.getFolio());
            return "redirect:/pedido/" + o.getFolio();
        } catch (NoEncontradoException e) {
            flash.addFlashAttribute("error", "No hay pedidos con ese folio y correo. Revisa los datos.");
            flash.addFlashAttribute("folioBuscado", folio);
            return "redirect:/seguimiento";
        }
    }

    @SuppressWarnings("unchecked")
    private Pedido permitido(String folio, HttpServletRequest req) {
        Pedido o = pedidos.porFolio(folio);
        Usuario u = sesion.usuario(req).orElse(null);
        Set<String> vistos = (Set<String>) req.getSession().getAttribute(CheckoutController.PEDIDOS_VISTOS);
        boolean ok = pedidos.puedeVer(o, u) || (vistos != null && vistos.contains(o.getFolio()));
        return ok ? o : null;
    }

    @GetMapping("/pedido/{folio}")
    public String detalle(@PathVariable String folio, HttpServletRequest req, Model model) {
        Pedido o = permitido(folio, req);
        if (o == null) return "redirect:/seguimiento?folio=" + folio;
        model.addAttribute("pedido", o);
        model.addAttribute("etapas", EstadoPedido.etapas());
        if (!model.containsAttribute("facturaForm")) model.addAttribute("facturaForm", new FacturaForm());
        model.addAttribute("usosCfdi", USOS_CFDI);
        return "pedido";
    }

    @GetMapping("/pedido/{folio}/confirmacion")
    public String confirmacion(@PathVariable String folio, HttpServletRequest req, Model model) {
        Pedido o = permitido(folio, req);
        if (o == null) return "redirect:/seguimiento?folio=" + folio;
        model.addAttribute("pedido", o);
        return "confirmacion";
    }

    /** Ticket digital / comprobante de pago para imprimir o guardar como PDF. */
    @GetMapping("/pedido/{folio}/ticket")
    public String ticket(@PathVariable String folio, HttpServletRequest req, Model model) {
        Pedido o = permitido(folio, req);
        if (o == null) return "redirect:/seguimiento?folio=" + folio;
        model.addAttribute("pedido", o);
        return "ticket";
    }

    /** Solicitud de factura con los datos fiscales del cliente (tabla Factura). */
    @PostMapping("/pedido/{folio}/factura")
    public String factura(@PathVariable String folio, @Valid @ModelAttribute("facturaForm") FacturaForm form, BindingResult errores,
                          HttpServletRequest req, RedirectAttributes flash) {
        Pedido o = permitido(folio, req);
        if (o == null) return "redirect:/seguimiento?folio=" + folio;
        if (errores.hasErrors()) {
            flash.addFlashAttribute("org.springframework.validation.BindingResult.facturaForm", errores);
            flash.addFlashAttribute("facturaForm", form);
            flash.addFlashAttribute("error", "Revisa tus datos fiscales.");
            return "redirect:/pedido/" + o.getFolio() + "#factura";
        }
        try {
            pedidos.solicitarFactura(o.getFolio(), form);
            flash.addFlashAttribute("mensaje", "Recibimos tu solicitud de factura. Te la enviaremos por correo en un máximo de 72 horas.");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/pedido/" + o.getFolio() + "#factura";
    }

    /** Página pública que abre quien recibe las flores al escanear el QR. */
    @GetMapping("/qr/{token}")
    public String dedicatoria(@PathVariable String token, Model model) {
        model.addAttribute("pedido", pedidos.porTokenQr(token));
        return "dedicatoria";
    }

    @GetMapping(value = "/qr/{token}.png", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> qrImagen(@PathVariable String token) {
        Pedido o = pedidos.porTokenQr(token);
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/qr/" + o.getTokenQr()).toUriString();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qr.png(url, 360));
    }
}
