package com.neksbyte.porelgustoweb.service.impl;

import com.neksbyte.porelgustoweb.dao.ContactoDao;
import com.neksbyte.porelgustoweb.dao.TipoUsuarioDao;
import com.neksbyte.porelgustoweb.dao.UsuarioDao;
import com.neksbyte.porelgustoweb.dto.CheckoutForm;
import com.neksbyte.porelgustoweb.dto.PasswordForm;
import com.neksbyte.porelgustoweb.dto.PerfilForm;
import com.neksbyte.porelgustoweb.dto.RegistroForm;
import com.neksbyte.porelgustoweb.exception.NoEncontradoException;
import com.neksbyte.porelgustoweb.exception.ReglaNegocioException;
import com.neksbyte.porelgustoweb.model.*;
import com.neksbyte.porelgustoweb.repository.CodigoRecuperacionRepository;
import com.neksbyte.porelgustoweb.service.CorreoService;
import com.neksbyte.porelgustoweb.service.NotificacionService;
import com.neksbyte.porelgustoweb.service.Textos;
import com.neksbyte.porelgustoweb.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class UsuarioServiceImpl extends BaseServiceImpl<Usuario, Integer> implements UsuarioService {
    private static final int MINUTOS_CODIGO = 15;
    private static final int MAX_INTENTOS = 5;
    private static final String CREDENCIALES = "Usuario o contraseña incorrectos";

    private final UsuarioDao usuarios;
    private final TipoUsuarioDao tipos;
    private final ContactoDao contactos;
    private final CodigoRecuperacionRepository codigos;
    private final PasswordEncoder encoder;
    private final CorreoService correo;
    private final NotificacionService notificaciones;
    private final SecureRandom random = new SecureRandom();

    public UsuarioServiceImpl(UsuarioDao dao, TipoUsuarioDao tipos, ContactoDao contactos, CodigoRecuperacionRepository codigos,
                              PasswordEncoder encoder, CorreoService correo, NotificacionService notificaciones) {
        super(dao);
        this.usuarios = dao;
        this.tipos = tipos;
        this.contactos = contactos;
        this.codigos = codigos;
        this.encoder = encoder;
        this.correo = correo;
        this.notificaciones = notificaciones;
    }

    @Override
    public Usuario registrar(RegistroForm f) {
        String email = f.getCorreo().trim().toLowerCase(Locale.ROOT);
        if (usuarios.existsByCorreo(email)) throw new ReglaNegocioException("Ese correo ya está registrado. ¿Quieres iniciar sesión?");
        Usuario u = new Usuario();
        u.setNombre(f.nombreCompleto());
        u.setCorreoElectronico(email);
        u.setTelefono(Textos.limpio(f.getTelefono()));
        u.setFechaNacimiento(f.getFechaNacimiento());
        u.setPassword(encoder.encode(f.getPassword()));
        u.setFechaRegistro(LocalDate.now());
        u.setActivo(true);
        u.setTipoUsuario(tipos.findById(Permisos.CLIENTE).orElseThrow(() -> new IllegalStateException("Falta el tipo de usuario Cliente")));
        usuarios.save(u);
        correo.enviar(u.getCorreoElectronico(), "Bienvenido a Por el Gusto",
                "Hola " + u.getPrimerNombre() + ",\n\nTu cuenta quedó registrada con el correo " + u.getCorreoElectronico()
                        + ".\nYa puedes guardar tu lista de deseos y seguir tus pedidos.\n\nPor el Gusto · Envío de flores");
        notificaciones.notificar(u, "¡Bienvenido a Por el Gusto!", "Tu cuenta está lista.", "/cuenta", false);
        return u;
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario autenticar(String login, String password) {
        if (Textos.vacio(login) || Textos.vacio(password)) throw new ReglaNegocioException(CREDENCIALES);
        String l = login.trim();
        List<Usuario> candidatos = l.contains("@") ? usuarios.findByCorreo(l).stream().toList() : usuarios.findAllByNombre(l);
        List<Usuario> validos = candidatos.stream()
                .filter(u -> u.isActivo() && u.getPassword() != null && u.getPassword().startsWith("$2")
                        && encoder.matches(password, u.getPassword()))
                .limit(2).toList();
        if (validos.size() != 1) throw new ReglaNegocioException(CREDENCIALES);
        return validos.get(0);
    }

    @Override
    public void actualizarPerfil(Usuario u, PerfilForm f) {
        String email = f.getCorreo().trim().toLowerCase(Locale.ROOT);
        if (!email.equalsIgnoreCase(u.getCorreoElectronico()) && usuarios.existsByCorreo(email)) {
            throw new ReglaNegocioException("Ese correo ya está registrado en otra cuenta");
        }
        u.setNombre(f.getNombre().trim());
        u.setCorreoElectronico(email);
        u.setTelefono(Textos.limpio(f.getTelefono()));
        u.setDireccion(Textos.limpio(f.getDireccion()));
        u.setFechaNacimiento(f.getFechaNacimiento());
        usuarios.save(u);
    }

    @Override
    public void cambiarPassword(Usuario u, PasswordForm f) {
        if (Textos.vacio(f.getActual()) || u.getPassword() == null || !u.getPassword().startsWith("$2")
                || !encoder.matches(f.getActual(), u.getPassword())) {
            throw new ReglaNegocioException("La contraseña actual no es correcta");
        }
        u.setPassword(encoder.encode(f.getNueva()));
        usuarios.save(u);
        correo.enviar(u.getCorreoElectronico(), "Tu contraseña cambió",
                "Hola " + u.getPrimerNombre() + ", tu contraseña se actualizó. Si no fuiste tú, contáctanos.");
    }

    @Override
    public void solicitarCodigo(String email) {
        Usuario u = usuarios.findByCorreo(email == null ? "" : email.trim()).filter(Usuario::isActivo)
                .orElseThrow(() -> new ReglaNegocioException("No hay ninguna cuenta registrada con ese correo"));
        codigos.findTopByUsuarioAndUsadoFalseOrderByFechaHoraDesc(u).ifPresent(c -> c.setUsado(true));
        String codigo = String.format("%06d", random.nextInt(1_000_000));
        CodigoRecuperacion c = new CodigoRecuperacion();
        c.setUsuario(u);
        c.setCodigoHash(encoder.encode(codigo));
        c.setVence(LocalDateTime.now().plusMinutes(MINUTOS_CODIGO));
        c.setFechaHora(LocalDateTime.now());
        codigos.save(c);
        correo.enviar(u.getCorreoElectronico(), "Código para restablecer tu contraseña: " + codigo,
                "Hola " + u.getPrimerNombre() + ",\n\nTu código para restablecer la contraseña es: " + codigo
                        + "\n\nVence en " + MINUTOS_CODIGO + " minutos. Si no lo pediste, ignora este correo.");
    }

    @Override
    public void restablecer(PasswordForm f) {
        Usuario u = usuarios.findByCorreo(f.getCorreo() == null ? "" : f.getCorreo().trim())
                .orElseThrow(() -> new ReglaNegocioException("No hay ninguna cuenta registrada con ese correo"));
        CodigoRecuperacion c = codigos.findTopByUsuarioAndUsadoFalseOrderByFechaHoraDesc(u)
                .orElseThrow(() -> new ReglaNegocioException("VENCIDO:Solicita un código nuevo"));
        if (c.isVencido() || c.getIntentos() >= MAX_INTENTOS) {
            c.setUsado(true);
            throw new ReglaNegocioException("VENCIDO:El código venció. Solicita uno nuevo.");
        }
        if (Textos.vacio(f.getCodigo()) || !encoder.matches(f.getCodigo().trim(), c.getCodigoHash())) {
            c.setIntentos(c.getIntentos() + 1);
            throw new ReglaNegocioException("El código es incorrecto. Te quedan " + (MAX_INTENTOS - c.getIntentos()) + " intentos.");
        }
        c.setUsado(true);
        u.setPassword(encoder.encode(f.getNueva()));
        correo.enviar(u.getCorreoElectronico(), "Contraseña restablecida",
                "Hola " + u.getPrimerNombre() + ", tu contraseña se restableció correctamente.");
    }

    // ---------- Direcciones ----------

    @Override
    @Transactional(readOnly = true)
    public List<Contacto> direcciones(Usuario u) {
        return contactos.findByUsuario(u).stream().filter(c -> c.isActivo() && c.isDireccion()).toList();
    }

    @Override
    public void guardarDireccion(Usuario u, CheckoutForm d) {
        boolean repetida = direcciones(u).stream().anyMatch(c -> c.getCalle().equalsIgnoreCase(d.getCalle().trim())
                && c.getCodigoPostal().equals(d.getCodigoPostal()) && c.getNombre().equalsIgnoreCase(d.getDestinatario().trim()));
        if (repetida) return;
        Contacto c = new Contacto();
        c.setUsuario(u);
        c.setNombre(d.getDestinatario().trim());
        c.setTipoContacto(d.getDestinatario().trim());
        c.setTelefono(Textos.limpio(d.getTelefonoDestinatario()));
        c.setCalle(d.getCalle().trim());
        c.setColonia(Textos.limpio(d.getColonia()));
        c.setCiudad(d.getCiudad().trim());
        c.setCodigoPostal(d.getCodigoPostal());
        c.setReferencias(Textos.limpio(d.getReferencias()));
        c.setActivo(true);
        contactos.save(c);
    }

    @Override
    public void eliminarDireccion(Usuario u, Integer id) {
        contactos.findById(id).filter(c -> c.getUsuario() != null && c.getUsuario().getIdUsuario().equals(u.getIdUsuario()))
                .ifPresent(c -> c.setActivo(false));
    }

    // ---------- Administración ----------

    @Override
    @Transactional(readOnly = true)
    public Usuario porId(Integer id) {
        return usuarios.findById(id).orElseThrow(() -> new NoEncontradoException("El usuario no existe"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> todos() { return usuarios.findAllRecientes(); }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> personal() {
        return usuarios.findByTipos(List.of(Permisos.ADMINISTRADOR, Permisos.EMPLEADO, Permisos.REPARTIDOR)).stream()
                .filter(Usuario::isActivo).sorted(Comparator.comparing(Usuario::getNombre)).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoUsuario> roles() {
        return tipos.findAll().stream().sorted(Comparator.comparing(TipoUsuario::getIdTipoUsuario)).toList();
    }

    @Override
    public void cambiarRol(Usuario admin, Integer idUsuario, Integer idTipoUsuario) {
        Usuario u = porId(idUsuario);
        if (u.getIdUsuario().equals(admin.getIdUsuario())) throw new ReglaNegocioException("No puedes cambiar tu propio rol");
        TipoUsuario t = tipos.findById(idTipoUsuario).orElseThrow(() -> new ReglaNegocioException("Elige un rol válido"));
        u.setTipoUsuario(t);
        notificaciones.notificar(u, "Tu rol cambió", "Ahora tu cuenta es de tipo " + t.getRol() + ".", "/cuenta", true);
    }

    @Override
    public void cambiarEstado(Usuario admin, Integer idUsuario) {
        Usuario u = porId(idUsuario);
        if (u.getIdUsuario().equals(admin.getIdUsuario())) throw new ReglaNegocioException("No puedes desactivar tu propia cuenta");
        u.setActivo(!u.isActivo());
    }
}
