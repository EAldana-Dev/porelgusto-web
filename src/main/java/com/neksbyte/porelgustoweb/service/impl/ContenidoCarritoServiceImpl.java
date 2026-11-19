package com.neksbyte.porelgustoweb.service.impl;

import com.neksbyte.porelgustoweb.dao.ContenidoCarritoDao;
import com.neksbyte.porelgustoweb.model.ContenidoCarrito;
import com.neksbyte.porelgustoweb.service.ContenidoCarritoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContenidoCarritoServiceImpl extends BaseServiceImpl<ContenidoCarrito, Integer> implements ContenidoCarritoService {

    public ContenidoCarritoServiceImpl(ContenidoCarritoDao dao) {
        super(dao);
    }
}
