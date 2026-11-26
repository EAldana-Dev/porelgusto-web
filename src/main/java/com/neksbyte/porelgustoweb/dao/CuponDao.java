package com.neksbyte.porelgustoweb.dao;

import com.neksbyte.porelgustoweb.model.Cupon;
import java.util.List;
import java.util.Optional;


public interface CuponDao extends BaseDao<Cupon, Integer> {
    Optional<Cupon> findByCodigo(String codigo);
    List<Cupon> findAllRecientes();
}
