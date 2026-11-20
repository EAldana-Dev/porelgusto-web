package com.neksbyte.porelgustoweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import jakarta.servlet.http.HttpSession;
import com.neksbyte.porelgustoweb.dto.auth.AuthResponse;
import com.neksbyte.porelgustoweb.dto.auth.LoginRequest;
import com.neksbyte.porelgustoweb.service.AuthService;

@Controller
public class Sesion {
    private final AuthService authService;

    public Sesion(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public String iniciarSesion(@RequestParam(required = false) String usuario,
                                @RequestParam(required = false) String password,
                                HttpSession session,
                                Model model) {
        if (usuario == null || usuario.isBlank() || password == null || password.isBlank()) {
            model.addAttribute("error", "Escribe tu usuario y contraseña.");
            return "secion";
        }

        try {
            AuthResponse respuesta = authService.login(new LoginRequest(usuario, password));
            session.setAttribute("authToken", respuesta.token());
            session.setAttribute("authTokenType", respuesta.tokenType());
            session.setAttribute("authUsuario", respuesta.usuario());
            return "redirect:/home";
        } catch (ResponseStatusException exception) {
            model.addAttribute("error", "Usuario o contraseña incorrectos.");
            return "secion";
        }
    }
}
