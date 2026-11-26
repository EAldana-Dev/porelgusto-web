package com.neksbyte.porelgustoweb.controller.admin;

import com.neksbyte.porelgustoweb.config.SesionUsuario;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.model.Permisos;
import com.neksbyte.porelgustoweb.model.TipoUsuario;
import com.neksbyte.porelgustoweb.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Usuarios, roles (TipoUsuario) y permisos de acceso al panel. */
@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuariosController {
    private final UsuarioService usuarios;
    private final SesionUsuario sesion;

    public AdminUsuariosController(UsuarioService usuarios, SesionUsuario sesion) {
        this.usuarios = usuarios;
        this.sesion = sesion;
    }

    @GetMapping
    public String lista(Model model) {
        List<TipoUsuario> roles = usuarios.roles();
        Map<Integer, List<String>> permisos = new LinkedHashMap<>();
        Map<Integer, String> descripciones = new LinkedHashMap<>();
        for (TipoUsuario t : roles) {
            permisos.put(t.getIdTipoUsuario(), Permisos.de(t.getIdTipoUsuario()));
            descripciones.put(t.getIdTipoUsuario(), Permisos.descripcion(t.getIdTipoUsuario()));
        }
        model.addAttribute("usuarios", usuarios.todos());
        model.addAttribute("roles", roles);
        model.addAttribute("permisos", permisos);
        model.addAttribute("descripciones", descripciones);
        model.addAttribute("modulo", "usuarios");
        return "admin/usuarios";
    }

    @PostMapping("/{id}/rol")
    public String rol(@PathVariable Integer id, @RequestParam Integer rol, HttpServletRequest req, RedirectAttributes flash) {
        try {
            usuarios.cambiarRol(sesion.requerido(req), id, rol);
            flash.addFlashAttribute("mensaje", "Rol actualizado.");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/{id}/estado")
    public String estado(@PathVariable Integer id, HttpServletRequest req, RedirectAttributes flash) {
        try {
            usuarios.cambiarEstado(sesion.requerido(req), id);
            flash.addFlashAttribute("mensaje", "Estado de la cuenta actualizado.");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }
}
