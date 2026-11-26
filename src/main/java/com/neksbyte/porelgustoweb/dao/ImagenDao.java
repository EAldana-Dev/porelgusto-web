package com.neksbyte.porelgustoweb.dao;

import com.neksbyte.porelgustoweb.model.Imagen;
import java.util.List;
import java.util.Optional;
import com.neksbyte.porelgustoweb.model.ArregloFloral;


public interface ImagenDao extends BaseDao<Imagen, Integer> {
    List<Imagen> findByArreglo(ArregloFloral arreglo);
}
