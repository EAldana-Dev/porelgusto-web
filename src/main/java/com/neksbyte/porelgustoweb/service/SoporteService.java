package com.neksbyte.porelgustoweb.service;

import com.neksbyte.porelgustoweb.dto.SoporteForm;
import com.neksbyte.porelgustoweb.exception.NoEncontradoException;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.model.EstadoSoporte;
import com.neksbyte.porelgustoweb.model.SolicitudSoporte;
import com.neksbyte.porelgustoweb.model.Usuario;
import com.neksbyte.porelgustoweb.repository.SolicitudSoporteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/** Solicitudes de soporte entre clientes y el personal de la tienda. */
@Service
@Transactional
public class SoporteService {
    private final SolicitudSoporteRepository solicitudes;
    private final NotificacionService notificaciones;
    private final CorreoService correo;

    public SoporteService(SolicitudSoporteRepository solicitudes, NotificacionService notificaciones, CorreoService correo) {
        this.solicitudes = solicitudes;
        this.notificaciones = notificaciones;
        this.correo = correo;
    }

    public SolicitudSoporte abrir(SoporteForm f, Usuario usuario) {
        SolicitudSoporte s = new SolicitudSoporte();
        s.setUsuario(usuario);
        s.setNombreContacto(f.getNombre().trim());
        s.setCorreoContacto(f.getCorreo().trim().toLowerCase(Locale.ROOT));
        s.setAsunto(f.getAsunto().trim());
        s.setFolioPedido(Textos.vacio(f.getFolio()) ? null : f.getFolio().trim().toUpperCase(Locale.ROOT));
        s.setFechaHora(LocalDateTime.now());
        s.setFechaLimite(LocalDateTime.now().plusHours(24));
        s.agregarMensaje(usuario, s.getNombreContacto(), f.getMensaje().trim(), false);
        solicitudes.save(s);
        correo.enviar(s.getCorreoContacto(), "Recibimos tu solicitud de soporte",
                "Hola " + s.getNombreContacto() + ", recibimos tu mensaje sobre «" + s.getAsunto() + "». Te responderemos en menos de 24 horas.");
        notificaciones.notificarAdmins("Nueva solicitud de soporte", s.getAsunto() + " · " + s.getNombreContacto(), "/admin/soporte/" + s.getIdSolicitud());
        return s;
    }

    public void responder(Integer id, Usuario autor, String texto) {
        if (Textos.vacio(texto)) throw new ReglaNegocioException("Escribe un mensaje");
        SolicitudSoporte s = porId(id);
        if (s.getEstado() == EstadoSoporte.CERRADO) throw new ReglaNegocioException("La solicitud está cerrada");
        boolean personal = autor.isPersonal() && !s.isDe(autor);
        s.agregarMensaje(autor, autor.getNombre(), texto.trim(), personal);
        if (personal) {
            if (s.getAgente() == null) s.asignarA(autor);
            correo.enviar(s.getCorreoContacto(), "Respuesta a tu solicitud: " + s.getAsunto(), texto.trim());
            notificaciones.notificar(s.getUsuario(), "Respondimos tu solicitud", s.getAsunto(), "/soporte/" + s.getIdSolicitud(), false);
        } else if (s.getAgente() != null) {
            notificaciones.notificar(s.getAgente(), "Nuevo mensaje en soporte", s.getAsunto(), "/admin/soporte/" + s.getIdSolicitud(), false);
        }
    }

    public void cerrar(Integer id) { porId(id).setEstado(EstadoSoporte.CERRADO); }

    public void asignar(Integer id, Usuario agente) { porId(id).asignarA(agente); }

    @Transactional(readOnly = true)
    public SolicitudSoporte porId(Integer id) {
        return solicitudes.findById(id).orElseThrow(() -> new NoEncontradoException("La solicitud no existe"));
    }

    @Transactional(readOnly = true)
    public List<SolicitudSoporte> deUsuario(Usuario u) { return solicitudes.findByUsuarioOrderByFechaHoraDesc(u); }

    @Transactional(readOnly = true)
    public List<SolicitudSoporte> todas() { return solicitudes.findAllByOrderByFechaHoraDesc(); }

    @Transactional(readOnly = true)
    public long abiertas() { return solicitudes.countByEstado(EstadoSoporte.ABIERTO); }
}
