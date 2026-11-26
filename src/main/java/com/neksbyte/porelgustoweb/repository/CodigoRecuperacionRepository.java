package com.neksbyte.porelgustoweb.repository;

import com.neksbyte.porelgustoweb.model.CodigoRecuperacion;
import com.neksbyte.porelgustoweb.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodigoRecuperacionRepository extends JpaRepository<CodigoRecuperacion, Integer> {
    Optional<CodigoRecuperacion> findTopByUsuarioAndUsadoFalseOrderByFechaHoraDesc(Usuario usuario);
}
