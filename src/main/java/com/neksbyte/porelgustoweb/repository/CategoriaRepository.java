package com.neksbyte.porelgustoweb.repository;

import com.neksbyte.porelgustoweb.model.Categoria;
import com.neksbyte.porelgustoweb.model.TipoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    List<Categoria> findByTipoOrderByOrdenAscNombreAsc(TipoCategoria tipo);
    Optional<Categoria> findBySlug(String slug);
    Optional<Categoria> findFirstByNombreIgnoreCase(String nombre);
}
