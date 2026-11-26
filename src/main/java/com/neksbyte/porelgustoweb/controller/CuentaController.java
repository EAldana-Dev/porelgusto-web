package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.config.SesionUsuario;
import com.neksbyte.porelgustoweb.dto.PasswordForm;
import com.neksbyte.porelgustoweb.dto.PerfilForm;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.model.ArregloFloral;
import com.neksbyte.porelgustoweb.model.Usuario;
import com.neksbyte.porelgustoweb.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Mi cuenta: perfil, contraseña, direcciones, pedidos, lista de deseos y notificaciones. */
@Controller
public class CuentaController {
    private final UsuarioService usuarios;
    private final PedidoService pedidos;
    private final ListaDeDeseosService deseos;
    private final NotificacionService notificaciones;
    private final ArregloFloralService catalogo;
    private final CarritoDeCompraService carritoService;
    private final CarritoSesion carrito;
    private final SesionUsuario sesion;

    public CuentaController(UsuarioService usuarios, PedidoService pedidos, ListaDeDeseosService deseos, NotificacionService notificaciones,
                            ArregloFloralService catalogo, CarritoDeCompraService carritoService, CarritoSesion carrito, SesionUsuario sesion) {
        this.usuarios = usuarios;
        this.pedidos = pedidos;
        this.deseos = deseos;
        this.notificaciones = notificaciones;
        this.catalogo = catalogo;
        this.carritoService = carritoService;
        this.carrito = carrito;
        this.sesion = sesion;
    }

    @GetMapping("/cuenta")
    public String cuenta(HttpServletRequest req, Model model) {
        Usuario u = sesion.requerido(req);
        if (!model.containsAttribute("perfilForm")) {
            PerfilForm f = new PerfilForm();
            f.setNombre(u.getNombre());
            f.setCorreo(u.getCorreoElectronico());
            f.setTelefono(u.getTelefono());
            f.setDireccion(u.getDireccion());
            f.setFechaNacimiento(u.getFechaNacimiento());
            model.addAttribute("perfilForm", f);
        }
        if (!model.containsAttribute("passwordForm")) model.addAttribute("passwordForm", new PasswordForm());
        model.addAttribute("direcciones", usuarios.direcciones(u));
        model.addAttribute("seccion", "perfil");
        return "cuenta/perfil";
    }

    @PostMapping("/cuenta")
    public String guardar(@Valid @ModelAttribute("perfilForm") PerfilForm form, BindingResult errores, HttpServletRequest req,
                          Model model, RedirectAttributes flash) {
        Usuario u = sesion.requerido(req);
        if (!errores.hasErrors()) {
            try {
                usuarios.actualizarPerfil(u, form);
                flash.addFlashAttribute("mensaje", "Cambios guardados.");
                return "redirect:/cuenta";
            } catch (ReglaNegocioException e) {
                errores.rejectValue("correo", "duplicado", e.getMessage());
            }
        }
        model.addAttribute("passwordForm", new PasswordForm());
        model.addAttribute("direcciones", usuarios.direcciones(u));
        model.addAttribute("seccion", "perfil");
        model.addAttribute("error", "No se guardaron los cambios. Revisa los campos marcados.");
        return "cuenta/perfil";
    }

    @PostMapping("/cuenta/password")
    public String password(@Valid @ModelAttribute("passwordForm") PasswordForm form, BindingResult errores, HttpServletRequest req,
                           RedirectAttributes flash) {
        Usuario u = sesion.requerido(req);
        if (errores.hasErrors()) {
            flash.addFlashAttribute("org.springframework.validation.BindingResult.passwordForm", errores);
            flash.addFlashAttribute("passwordForm", form);
            flash.addFlashAttribute("error", "No se cambió la contraseña. Revisa los campos.");
            return "redirect:/cuenta#password";
        }
        try {
            usuarios.cambiarPassword(u, form);
            flash.addFlashAttribute("mensaje", "Tu contraseña se actualizó.");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cuenta#password";
    }

    @PostMapping("/cuenta/direcciones/{id}/eliminar")
    public String eliminarDireccion(@PathVariable Integer id, HttpServletRequest req, RedirectAttributes flash) {
        usuarios.eliminarDireccion(sesion.requerido(req), id);
        flash.addFlashAttribute("mensaje", "Dirección eliminada.");
        return "redirect:/cuenta#direcciones";
    }

    @GetMapping("/cuenta/pedidos")
    public String pedidos(HttpServletRequest req, Model model) {
        model.addAttribute("pedidos", pedidos.deCliente(sesion.requerido(req)));
        model.addAttribute("seccion", "pedidos");
        return "cuenta/pedidos";
    }

    // ---------------- Lista de deseos ----------------

    @GetMapping("/cuenta/deseos")
    public String deseos(HttpServletRequest req, Model model) {
        model.addAttribute("deseos", catalogo.vistas(deseos.productosDe(sesion.requerido(req))));
        model.addAttribute("seccion", "deseos");
        return "cuenta/deseos";
    }

    /** Agregar o quitar de la lista desde el catálogo o la ficha (pide iniciar sesión si no la hay). */
    @PostMapping("/deseos/{sku}")
    public String alternarDeseo(@PathVariable String sku, @RequestParam(defaultValue = "agregar") String accion,
                                HttpServletRequest req, RedirectAttributes flash) {
        Usuario u = sesion.usuario(req).orElse(null);
        ArregloFloral p = catalogo.porSku(sku);
        if (u == null) {
            flash.addFlashAttribute("mensaje", "Inicia sesión para guardar productos en tu lista de deseos.");
            return "redirect:/login?next=/producto/" + p.getSku();
        }
        if ("quitar".equals(accion)) {
            deseos.quitar(u, p);
            flash.addFlashAttribute("mensaje", "Se borró el artículo de tu lista de deseos correctamente.");
        } else {
            boolean nuevo = deseos.agregar(u, p);
            flash.addFlashAttribute("mensaje", nuevo ? "Guardamos " + p.getNombre() + " en tu lista de deseos." : "Ya estaba en tu lista de deseos.");
        }
        return "redirect:" + GlobalAdvice.regreso(req);
    }

    /** Desde la lista de deseos: pasar el producto al carrito. */
    @PostMapping("/cuenta/deseos/{sku}/carrito")
    public String deseoAlCarrito(@PathVariable String sku, RedirectAttributes flash) {
        try {
            carritoService.agregar(carrito, catalogo.porSku(sku).getIdArregloFloral(), 1, null, null);
            flash.addFlashAttribute("mensaje", "Agregado a tu carrito. Llevas " + carrito.getCantidad() + " productos.");
            flash.addFlashAttribute("agregadoAlCarrito", true);
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cuenta/deseos";
    }

    // ---------------- Notificaciones ----------------

    @GetMapping("/cuenta/notificaciones")
    public String notificaciones(HttpServletRequest req, Model model) {
        model.addAttribute("notificaciones", notificaciones.recientes(sesion.requerido(req)));
        model.addAttribute("seccion", "notificaciones");
        return "cuenta/notificaciones";
    }

    @GetMapping("/cuenta/notificaciones/{id}")
    public String abrirNotificacion(@PathVariable Integer id, HttpServletRequest req) {
        String liga = notificaciones.abrir(sesion.requerido(req), id);
        return "redirect:" + (liga == null || !liga.startsWith("/") || liga.startsWith("//") ? "/cuenta/notificaciones" : liga);
    }

    @PostMapping("/cuenta/notificaciones/leidas")
    public String marcarLeidas(HttpServletRequest req) {
        notificaciones.marcarTodas(sesion.requerido(req));
        return "redirect:/cuenta/notificaciones";
    }
}
