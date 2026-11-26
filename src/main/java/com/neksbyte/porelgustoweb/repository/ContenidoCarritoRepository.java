package com.neksbyte.porelgustoweb.repository;

import com.neksbyte.porelgustoweb.model.ContenidoCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import com.neksbyte.porelgustoweb.model.ArregloFloral;
import java.util.Optional;
import java.util.List;

public interface ContenidoCarritoRepository extends JpaRepository<ContenidoCarrito, Integer> {
    boolean existsByArregloFloral(ArregloFloral arregloFloral);
}
