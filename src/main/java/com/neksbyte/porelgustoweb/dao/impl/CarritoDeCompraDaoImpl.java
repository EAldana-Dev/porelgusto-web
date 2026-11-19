package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.CarritoDeCompraDao;
import com.neksbyte.porelgustoweb.model.CarritoDeCompra;
import com.neksbyte.porelgustoweb.repository.CarritoDeCompraRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CarritoDeCompraDaoImpl extends BaseDaoImpl<CarritoDeCompra, Integer> implements CarritoDeCompraDao {

    public CarritoDeCompraDaoImpl(CarritoDeCompraRepository repository) {
        super(repository);
    }
}
