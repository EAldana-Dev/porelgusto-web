package com.neksbyte.porelgustoweb.repository;

import com.neksbyte.porelgustoweb.model.Visita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitaRepository extends JpaRepository<Visita, Long> {
    List<Visita> findByFechaHoraAfter(LocalDateTime desde);
}
