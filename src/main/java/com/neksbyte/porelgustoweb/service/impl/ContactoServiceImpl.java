package com.neksbyte.porelgustoweb.service.impl;

import com.neksbyte.porelgustoweb.dao.ContactoDao;
import com.neksbyte.porelgustoweb.model.Contacto;
import com.neksbyte.porelgustoweb.service.ContactoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContactoServiceImpl extends BaseServiceImpl<Contacto, Integer> implements ContactoService {

    public ContactoServiceImpl(ContactoDao dao) {
        super(dao);
    }
}
