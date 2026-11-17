package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.TicketDao;
import com.neksbyte.porelgustoweb.model.Ticket;
import com.neksbyte.porelgustoweb.repository.TicketRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TicketDaoImpl extends BaseDaoImpl<Ticket, Integer> implements TicketDao {

    public TicketDaoImpl(TicketRepository repository) {
        super(repository);
    }
}
