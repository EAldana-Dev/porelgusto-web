package com.neksbyte.porelgustoweb.dao;

import com.neksbyte.porelgustoweb.model.ListaDeseoArregloFloral;
import java.util.List;
import java.util.Optional;
import com.neksbyte.porelgustoweb.model.ArregloFloral;
import com.neksbyte.porelgustoweb.model.ListaDeDeseos;

import com.neksbyte.porelgustoweb.model.ListaDeseoArregloFloralId;

public interface ListaDeseoArregloFloralDao extends BaseDao<ListaDeseoArregloFloral, ListaDeseoArregloFloralId> {
    List<ListaDeseoArregloFloral> findByLista(ListaDeDeseos lista);
    List<ListaDeseoArregloFloral> findByArreglo(ArregloFloral arreglo);
    void delete(ListaDeseoArregloFloral entidad);
}
