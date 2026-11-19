package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.ContenidoCarritoDao;
import com.neksbyte.porelgustoweb.model.ContenidoCarrito;
import com.neksbyte.porelgustoweb.repository.ContenidoCarritoRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ContenidoCarritoDaoImpl extends BaseDaoImpl<ContenidoCarrito, Integer> implements ContenidoCarritoDao {

    public ContenidoCarritoDaoImpl(ContenidoCarritoRepository repository) {
        super(repository);
    }
}
