package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.CuponDao;
import com.neksbyte.porelgustoweb.model.Cupon;
import com.neksbyte.porelgustoweb.repository.CuponRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public class CuponDaoImpl extends BaseDaoImpl<Cupon, Integer> implements CuponDao {

    private final CuponRepository repo;

    public CuponDaoImpl(CuponRepository repository) {
        super(repository);
        this.repo = repository;
    }

    @Override
    public Optional<Cupon> findByCodigo(String codigo) {
        return repo.findFirstByCodigoIgnoreCase(codigo);
    }

    @Override
    public List<Cupon> findAllRecientes() {
        return repo.findAllByOrderByIdCuponDesc();
    }
}
