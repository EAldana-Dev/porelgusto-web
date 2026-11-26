package com.neksbyte.porelgustoweb.config;

import com.neksbyte.porelgustoweb.dao.UsuarioDao;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.util.Optional;

/** Usuario con sesión iniciada (en la sesión solo se guarda su id). */
@Component
public class SesionUsuario {
    public static final String USUARIO_ID = "usuarioId";
    public static final String ATRIBUTO = "usuarioActual";

    private final UsuarioDao usuarios;

    public SesionUsuario(UsuarioDao usuarios) {
        this.usuarios = usuarios;
    }

    /** Usuario actual, cargado una vez por petición. */
    public Optional<Usuario> usuario(HttpServletRequest req) {
        Object cache = req.getAttribute(ATRIBUTO);
        if (cache instanceof Usuario u) return Optional.of(u);
        HttpSession s = req.getSession(false);
        if (s == null || !(s.getAttribute(USUARIO_ID) instanceof Integer id)) return Optional.empty();
        Optional<Usuario> u = usuarios.findById(id).filter(Usuario::isActivo);
        if (u.isEmpty()) s.removeAttribute(USUARIO_ID);
        u.ifPresent(x -> req.setAttribute(ATRIBUTO, x));
        return u;
    }

    public Usuario requerido(HttpServletRequest req) {
        return usuario(req).orElseThrow(() -> new ReglaNegocioException("Inicia sesión para continuar"));
    }

    /** Inicia sesión (cambia el id de sesión para evitar fijación de sesión). */
    public void iniciar(HttpServletRequest req, Usuario u) {
        req.getSession(true);
        req.changeSessionId();
        req.getSession().setAttribute(USUARIO_ID, u.getIdUsuario());
        req.setAttribute(ATRIBUTO, u);
    }

    public void cerrar(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s != null) s.invalidate();
    }
}
