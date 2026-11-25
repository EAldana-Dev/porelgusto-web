package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.dto.auth.AuthResponse;
import com.neksbyte.porelgustoweb.dto.auth.RegistroRequest;
import com.neksbyte.porelgustoweb.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class RegistroWeb {
    private final AuthService authService;

    public RegistroWeb(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public String crearCuenta(@Valid @ModelAttribute RegistroRequest request,
                              BindingResult bindingResult,
                              HttpSession session,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Revisa los datos. El correo debe ser válido y la contraseña tener al menos 8 caracteres.");
            return "singup";
        }
        try {
            AuthResponse respuesta = authService.register(request);
            session.setAttribute("authToken", respuesta.token());
            session.setAttribute("authTokenType", respuesta.tokenType());
            session.setAttribute("authUsuario", respuesta.usuario());
            return "redirect:/home";
        } catch (ResponseStatusException exception) {
            if (exception.getStatusCode() == HttpStatus.CONFLICT) {
                model.addAttribute("error", "Ese correo ya está registrado.");
            } else {
                model.addAttribute("error", "No se pudo crear la cuenta. Inténtalo de nuevo.");
            }
            return "singup";
        }
    }
}
