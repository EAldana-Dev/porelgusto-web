package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.config.SesionUsuario;
import com.neksbyte.porelgustoweb.dto.RegistroForm;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Registro de clientes desde el sitio. */
@Controller
public class RegistroWeb {
    private final UsuarioService usuarios;
    private final SesionUsuario sesion;

    public RegistroWeb(UsuarioService usuarios, SesionUsuario sesion) {
        this.usuarios = usuarios;
        this.sesion = sesion;
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("registroForm", new RegistroForm());
        return "auth/registro";
    }

    /** Ruta anterior del registro. */
    @GetMapping("/signup")
    public String signup() { return "redirect:/registro"; }

    @PostMapping("/registro")
    public String crearCuenta(@Valid @ModelAttribute("registroForm") RegistroForm form, BindingResult errores,
                              HttpServletRequest req, Model model, RedirectAttributes flash) {
        if (errores.hasErrors()) {
            model.addAttribute("error", "Datos incorrectos. Revisa los campos marcados.");
            return "auth/registro";
        }
        try {
            Usuario u = usuarios.registrar(form);
            sesion.iniciar(req, u);
            flash.addFlashAttribute("mensaje", "¡Bienvenido, " + u.getPrimerNombre() + "! Tu cuenta quedó registrada.");
            return "redirect:/";
        } catch (ReglaNegocioException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/registro";
        }
    }
}
