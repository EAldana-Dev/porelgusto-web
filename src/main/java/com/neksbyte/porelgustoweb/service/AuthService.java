package com.neksbyte.porelgustoweb.service;

import com.neksbyte.porelgustoweb.dto.auth.AuthResponse;
import com.neksbyte.porelgustoweb.dto.auth.LoginRequest;
import com.neksbyte.porelgustoweb.dto.auth.RegistroRequest;
import com.neksbyte.porelgustoweb.model.TipoUsuario;
import com.neksbyte.porelgustoweb.model.Usuario;
import com.neksbyte.porelgustoweb.repository.TipoUsuarioRepository;
import com.neksbyte.porelgustoweb.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class AuthService {
    private final UsuarioRepository usuarios;
    private final TipoUsuarioRepository tiposUsuario;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final int defaultUserTypeId;

    public AuthService(UsuarioRepository usuarios, TipoUsuarioRepository tiposUsuario,
                       PasswordEncoder passwordEncoder, TokenService tokenService,
                       @Value("${app.auth.default-user-type-id:1}") int defaultUserTypeId) {
        this.usuarios = usuarios;
        this.tiposUsuario = tiposUsuario;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.defaultUserTypeId = defaultUserTypeId;
    }

    @Transactional
    public AuthResponse register(RegistroRequest request) {
        String email = request.correoElectronico().trim();
        if (usuarios.existsByCorreoElectronicoIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }
        TipoUsuario tipo = tiposUsuario.findById(defaultUserTypeId).orElseGet(() -> {
            TipoUsuario tipoPredeterminado = new TipoUsuario();
            tipoPredeterminado.setIdTipoUsuario(defaultUserTypeId);
            tipoPredeterminado.setRol("CLIENTE");
            return tiposUsuario.save(tipoPredeterminado);
        });
        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre().trim());
        usuario.setDireccion(request.direccion());
        usuario.setCorreoElectronico(email);
        usuario.setTelefono(request.telefono());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.setFechaRegistro(LocalDate.now());
        usuario.setTipoUsuario(tipo);
        return response(usuarios.save(usuario));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarios.findByCorreoElectronicoIgnoreCase(request.correoElectronico().trim())
                .filter(cuenta -> cuenta.getPassword() != null
                        && passwordEncoder.matches(request.password(), cuenta.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));
        return response(usuario);
    }

    @Transactional(readOnly = true)
    public AuthResponse loginPorNombre(String nombre, String password) {
        List<Usuario> coincidencias = usuarios.findAllByNombreIgnoreCase(nombre.trim()).stream()
                .filter(usuario -> usuario.getPassword() != null
                        && passwordEncoder.matches(password, usuario.getPassword()))
                .limit(2)
                .toList();
        if (coincidencias.size() != 1) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }
        return response(coincidencias.get(0));
    }

    private AuthResponse response(Usuario usuario) {
        return new AuthResponse(tokenService.issue(usuario), "Bearer", tokenService.getExpirationSeconds(),
                new AuthResponse.UsuarioResumen(usuario.getIdUsuario(), usuario.getNombre(),
                        usuario.getCorreoElectronico(), usuario.getTipoUsuario().getIdTipoUsuario()));
    }
}
