package com.neksbyte.porelgustoweb.service;

import com.neksbyte.porelgustoweb.dto.CheckoutForm;
import com.neksbyte.porelgustoweb.dto.PasswordForm;
import com.neksbyte.porelgustoweb.dto.PerfilForm;
import com.neksbyte.porelgustoweb.dto.RegistroForm;
import com.neksbyte.porelgustoweb.model.Contacto;
import com.neksbyte.porelgustoweb.model.TipoUsuario;
import com.neksbyte.porelgustoweb.model.Usuario;

import java.util.List;

public interface UsuarioService extends BaseService<Usuario, Integer> {
    Usuario registrar(RegistroForm form);
    /** Correo (o nombre) + contraseña. Mensaje genérico para no revelar qué dato falló. */
    Usuario autenticar(String login, String password);
    void actualizarPerfil(Usuario u, PerfilForm form);
    void cambiarPassword(Usuario u, PasswordForm form);
    /** Envía por correo un código de 6 dígitos válido por 15 minutos. */
    void solicitarCodigo(String correo);
    /** Valida el código y cambia la contraseña ("VENCIDO:" al inicio del mensaje si hay que pedir otro). */
    void restablecer(PasswordForm form);

    // ---------- Direcciones guardadas (tabla Contacto) ----------
    List<Contacto> direcciones(Usuario u);
    void guardarDireccion(Usuario u, CheckoutForm datos);
    void eliminarDireccion(Usuario u, Integer idContacto);

    // ---------- Administración ----------
    Usuario porId(Integer id);
    List<Usuario> todos();
    /** Administradores, empleados y repartidores (para asignar pedidos). */
    List<Usuario> personal();
    List<TipoUsuario> roles();
    void cambiarRol(Usuario admin, Integer idUsuario, Integer idTipoUsuario);
    void cambiarEstado(Usuario admin, Integer idUsuario);
}
