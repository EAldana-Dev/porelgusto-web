package com.neksbyte.porelgustoweb.repository;

import com.neksbyte.porelgustoweb.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
}
