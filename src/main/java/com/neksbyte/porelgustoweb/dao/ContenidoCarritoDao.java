package com.neksbyte.porelgustoweb.dao;

import com.neksbyte.porelgustoweb.model.ContenidoCarrito;
import java.util.List;
import java.util.Optional;
import com.neksbyte.porelgustoweb.model.ArregloFloral;


public interface ContenidoCarritoDao extends BaseDao<ContenidoCarrito, Integer> {
    boolean existsByArreglo(ArregloFloral arreglo);
}
