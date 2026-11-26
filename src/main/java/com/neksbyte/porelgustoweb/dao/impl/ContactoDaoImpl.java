package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.ContactoDao;
import com.neksbyte.porelgustoweb.model.Contacto;
import com.neksbyte.porelgustoweb.repository.ContactoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.neksbyte.porelgustoweb.model.Usuario;


@Repository
public class ContactoDaoImpl extends BaseDaoImpl<Contacto, Integer> implements ContactoDao {

    private final ContactoRepository repo;

    public ContactoDaoImpl(ContactoRepository repository) {
        super(repository);
        this.repo = repository;
    }

    @Override
    public List<Contacto> findByUsuario(Usuario usuario) {
        return repo.findByUsuarioOrderByIdContactoDesc(usuario);
    }
}
