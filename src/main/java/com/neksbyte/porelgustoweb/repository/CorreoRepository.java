package com.neksbyte.porelgustoweb.repository;

import com.neksbyte.porelgustoweb.model.Correo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CorreoRepository extends JpaRepository<Correo, Integer> {
    List<Correo> findTop50ByOrderByFechaHoraDescIdCorreoDesc();
}
