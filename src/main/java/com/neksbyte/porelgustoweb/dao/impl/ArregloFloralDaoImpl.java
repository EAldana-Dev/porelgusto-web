package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.ArregloFloralDao;
import com.neksbyte.porelgustoweb.model.ArregloFloral;
import com.neksbyte.porelgustoweb.repository.ArregloFloralRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ArregloFloralDaoImpl extends BaseDaoImpl<ArregloFloral, Integer> implements ArregloFloralDao {

    private final ArregloFloralRepository repository;

    public ArregloFloralDaoImpl(ArregloFloralRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Optional<ArregloFloral> findByNombre(String nombreReal) {
        return repository.findByNombre(nombreReal);
    }
}
