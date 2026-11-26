package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.config.SesionUsuario;
import com.neksbyte.porelgustoweb.dto.PasswordForm;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.model.Usuario;
import com.neksbyte.porelgustoweb.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Inicio y cierre de sesión, y recuperación de contraseña con código por correo. */
@Controller
public class Sesion {
    private final UsuarioService usuarios;
    private final SesionUsuario sesion;

    public Sesion(UsuarioService usuarios, SesionUsuario sesion) {
        this.usuarios = usuarios;
        this.sesion = sesion;
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String next, Model model) {
        model.addAttribute("next", next);
        return "auth/login";
    }

    @PostMapping("/login")
    public String iniciarSesion(@RequestParam(required = false) String usuario,
                                @RequestParam(required = false) String password,
                                @RequestParam(required = false) String next,
                                HttpServletRequest req, Model model, RedirectAttributes flash) {
        try {
            Usuario u = usuarios.autenticar(usuario, password);
            sesion.iniciar(req, u);
            flash.addFlashAttribute("mensaje", "¡Hola, " + u.getPrimerNombre() + "!");
            if (next != null && next.startsWith("/") && !next.startsWith("//")) return "redirect:" + next;
            return u.isPersonal() ? "redirect:/admin" : "redirect:/";
        } catch (ReglaNegocioException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("login", usuario);
            model.addAttribute("next", next);
            return "auth/login";
        }
    }

    @PostMapping("/logout")
    public String salir(HttpServletRequest req, RedirectAttributes flash) {
        sesion.cerrar(req);
        flash.addFlashAttribute("mensaje", "Cerraste sesión. ¡Vuelve pronto!");
        return "redirect:/";
    }

    // ---------------- Recuperar acceso ----------------

    @GetMapping("/recuperar")
    public String recuperar() { return "auth/recuperar"; }

    @PostMapping("/recuperar")
    public String enviarCodigo(@RequestParam String correo, Model model, RedirectAttributes flash) {
        try {
            usuarios.solicitarCodigo(correo);
            flash.addFlashAttribute("mensaje", "Te enviamos un código de 6 dígitos a " + correo.trim() + ". Vence en 15 minutos.");
            flash.addFlashAttribute("correo", correo.trim());
            return "redirect:/restablecer";
        } catch (ReglaNegocioException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("correo", correo);
            return "auth/recuperar";
        }
    }

    @GetMapping("/restablecer")
    public String restablecer(Model model) {
        if (!model.containsAttribute("passwordForm")) {
            PasswordForm f = new PasswordForm();
            f.setCorreo((String) model.getAttribute("correo"));
            model.addAttribute("passwordForm", f);
        }
        return "auth/restablecer";
    }

    @PostMapping("/restablecer")
    public String cambiar(@Valid @ModelAttribute("passwordForm") PasswordForm form, BindingResult errores, Model model,
                          RedirectAttributes flash) {
        if (errores.hasErrors()) return "auth/restablecer";
        try {
            usuarios.restablecer(form);
            flash.addFlashAttribute("mensaje", "Contraseña restablecida. Ya puedes iniciar sesión.");
            return "redirect:/login";
        } catch (ReglaNegocioException e) {
            String msg = e.getMessage();
            if (msg.startsWith("VENCIDO:")) {
                model.addAttribute("vencido", true);
                msg = msg.substring(8);
            }
            model.addAttribute("error", msg);
            return "auth/restablecer";
        }
    }
}
