package com.neksbyte.porelgustoweb.service.impl;

import com.neksbyte.porelgustoweb.dao.PedidoDao;
import com.neksbyte.porelgustoweb.model.Pedido;
import com.neksbyte.porelgustoweb.service.PedidoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PedidoServiceImpl extends BaseServiceImpl<Pedido, Integer> implements PedidoService {

    public PedidoServiceImpl(PedidoDao dao) {
        super(dao);
    }
}
