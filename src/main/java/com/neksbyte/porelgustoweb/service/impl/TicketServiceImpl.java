package com.neksbyte.porelgustoweb.service.impl;

import com.neksbyte.porelgustoweb.dao.TicketDao;
import com.neksbyte.porelgustoweb.model.Ticket;
import com.neksbyte.porelgustoweb.service.TicketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TicketServiceImpl extends BaseServiceImpl<Ticket, Integer> implements TicketService {

    public TicketServiceImpl(TicketDao dao) {
        super(dao);
    }
}
