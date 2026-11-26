package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.ContenidoCarritoDao;
import com.neksbyte.porelgustoweb.model.ContenidoCarrito;
import com.neksbyte.porelgustoweb.repository.ContenidoCarritoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.neksbyte.porelgustoweb.model.ArregloFloral;


@Repository
public class ContenidoCarritoDaoImpl extends BaseDaoImpl<ContenidoCarrito, Integer> implements ContenidoCarritoDao {

    private final ContenidoCarritoRepository repo;

    public ContenidoCarritoDaoImpl(ContenidoCarritoRepository repository) {
        super(repository);
        this.repo = repository;
    }

    @Override
    public boolean existsByArreglo(ArregloFloral arreglo) {
        return repo.existsByArregloFloral(arreglo);
    }
}
