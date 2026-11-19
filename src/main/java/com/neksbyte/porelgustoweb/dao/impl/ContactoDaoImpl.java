package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.ContactoDao;
import com.neksbyte.porelgustoweb.model.Contacto;
import com.neksbyte.porelgustoweb.repository.ContactoRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ContactoDaoImpl extends BaseDaoImpl<Contacto, Integer> implements ContactoDao {

    public ContactoDaoImpl(ContactoRepository repository) {
        super(repository);
    }
}
