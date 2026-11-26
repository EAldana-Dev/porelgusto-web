package com.neksbyte.porelgustoweb.service;

import com.neksbyte.porelgustoweb.dao.UsuarioDao;
import com.neksbyte.porelgustoweb.model.Notificacion;
import com.neksbyte.porelgustoweb.model.Permisos;
import com.neksbyte.porelgustoweb.model.Usuario;
import com.neksbyte.porelgustoweb.repository.NotificacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/** Notificaciones dentro del sitio (campana del menú), con copia opcional por correo. */
@Service
@Transactional
public class NotificacionService {
    private final NotificacionRepository notificaciones;
    private final UsuarioDao usuarios;
    private final CorreoService correo;

    public NotificacionService(NotificacionRepository notificaciones, UsuarioDao usuarios, CorreoService correo) {
        this.notificaciones = notificaciones;
        this.usuarios = usuarios;
        this.correo = correo;
    }

    public void notificar(Usuario usuario, String titulo, String cuerpo, String liga, boolean tambienCorreo) {
        if (usuario == null) return;
        Notificacion n = new Notificacion();
        n.setUsuario(usuario);
        n.setTitulo(titulo.length() > 150 ? titulo.substring(0, 150) : titulo);
        n.setCuerpo(cuerpo == null ? null : cuerpo.length() > 500 ? cuerpo.substring(0, 500) : cuerpo);
        n.setLiga(liga);
        n.setFechaHora(LocalDateTime.now());
        notificaciones.save(n);
        if (tambienCorreo) correo.enviar(usuario.getCorreoElectronico(), titulo, cuerpo == null ? titulo : cuerpo);
    }

    /** Avisa a todos los administradores (por ejemplo, un pedido nuevo o una falla en la entrega). */
    public void notificarAdmins(String titulo, String cuerpo, String liga) {
        usuarios.findByTipos(List.of(Permisos.ADMINISTRADOR)).stream().filter(Usuario::isActivo)
                .forEach(a -> notificar(a, titulo, cuerpo, liga, true));
    }

    @Transactional(readOnly = true)
    public List<Notificacion> recientes(Usuario u) { return notificaciones.findTop30ByUsuarioOrderByFechaHoraDesc(u); }

    @Transactional(readOnly = true)
    public long pendientes(Usuario u) { return u == null ? 0 : notificaciones.contarPendientes(u); }

    public void marcarTodas(Usuario u) { recientes(u).forEach(n -> n.setLeida(true)); }

    /** Marca una como leída y regresa su liga (solo si es del usuario). */
    public String abrir(Usuario u, Integer id) {
        return notificaciones.findById(id)
                .filter(n -> n.getUsuario().getIdUsuario().equals(u.getIdUsuario()))
                .map(n -> { n.setLeida(true); return n.getLiga(); })
                .orElse(null);
    }
}
