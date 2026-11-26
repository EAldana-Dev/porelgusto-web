package com.neksbyte.porelgustoweb.repository;

import com.neksbyte.porelgustoweb.model.ListaDeDeseos;
import org.springframework.data.jpa.repository.JpaRepository;
import com.neksbyte.porelgustoweb.model.Usuario;
import java.util.Optional;
import java.util.List;

public interface ListaDeDeseosRepository extends JpaRepository<ListaDeDeseos, Integer> {
    Optional<ListaDeDeseos> findFirstByUsuarioOrderByIdListaAsc(Usuario usuario);
}
