package com.neksbyte.porelgustoweb.repository;

import com.neksbyte.porelgustoweb.model.EstadoSoporte;
import com.neksbyte.porelgustoweb.model.SolicitudSoporte;
import com.neksbyte.porelgustoweb.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudSoporteRepository extends JpaRepository<SolicitudSoporte, Integer> {
    List<SolicitudSoporte> findByUsuarioOrderByFechaHoraDesc(Usuario usuario);
    List<SolicitudSoporte> findAllByOrderByFechaHoraDesc();
    long countByEstado(EstadoSoporte estado);
}
