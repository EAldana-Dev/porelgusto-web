package com.neksbyte.porelgustoweb.repository;

import com.neksbyte.porelgustoweb.model.ArregloFloral;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface ArregloFloralRepository extends JpaRepository<ArregloFloral, Integer> {

    Optional<ArregloFloral> findByNombre(String nombre);
    Optional<ArregloFloral> findBySku(String sku);
    List<ArregloFloral> findAllByOrderByIdArregloFloralDesc();
}
