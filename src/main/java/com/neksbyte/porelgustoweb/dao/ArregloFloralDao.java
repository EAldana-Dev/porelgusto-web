package com.neksbyte.porelgustoweb.dao;

import com.neksbyte.porelgustoweb.model.ArregloFloral;
import java.util.List;

import java.util.Optional;

public interface ArregloFloralDao extends BaseDao<ArregloFloral, Integer> {
    Optional<ArregloFloral> findByNombre(String nombreReal);
    Optional<ArregloFloral> findBySku(String sku);
    /** Todos, los más recientes primero (panel de administración). */
    List<ArregloFloral> findAllRecientes();
}
