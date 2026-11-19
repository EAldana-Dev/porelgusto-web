package com.neksbyte.porelgustoweb.service.impl;

import com.neksbyte.porelgustoweb.dao.UsuarioDao;
import com.neksbyte.porelgustoweb.model.Usuario;
import com.neksbyte.porelgustoweb.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioServiceImpl extends BaseServiceImpl<Usuario, Integer> implements UsuarioService {

    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioDao dao, PasswordEncoder passwordEncoder) {
        super(dao);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario save(Usuario usuario) {
        if (usuario.getPassword() != null && !usuario.getPassword().startsWith("$2a$")
                && !usuario.getPassword().startsWith("$2b$") && !usuario.getPassword().startsWith("$2y$")) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        return super.save(usuario);
    }
}
