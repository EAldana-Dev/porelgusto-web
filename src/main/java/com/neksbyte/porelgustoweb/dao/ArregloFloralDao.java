package com.neksbyte.porelgustoweb.dao;

import com.neksbyte.porelgustoweb.model.ArregloFloral;
import java.util.Optional;

public interface ArregloFloralDao extends BaseDao<ArregloFloral, Integer> {
    Optional<ArregloFloral> findByNombre(String nombreReal);
}
