package com.neksbyte.porelgustoweb.repository;

import com.neksbyte.porelgustoweb.model.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import com.neksbyte.porelgustoweb.model.ArregloFloral;
import java.util.Optional;
import java.util.List;

public interface ImagenRepository extends JpaRepository<Imagen, Integer> {
    List<Imagen> findByArregloFloral(ArregloFloral arregloFloral);
}
