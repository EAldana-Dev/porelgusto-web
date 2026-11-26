package com.neksbyte.porelgustoweb.repository;

import com.neksbyte.porelgustoweb.model.ListaDeseoArregloFloral;
import com.neksbyte.porelgustoweb.model.ListaDeseoArregloFloralId;
import org.springframework.data.jpa.repository.JpaRepository;
import com.neksbyte.porelgustoweb.model.ListaDeDeseos;
import com.neksbyte.porelgustoweb.model.ArregloFloral;
import java.util.Optional;
import java.util.List;

public interface ListaDeseoArregloFloralRepository extends JpaRepository<ListaDeseoArregloFloral, ListaDeseoArregloFloralId> {
    List<ListaDeseoArregloFloral> findByListaDeDeseos(ListaDeDeseos lista);
    List<ListaDeseoArregloFloral> findByArregloFloral(ArregloFloral arreglo);
}
