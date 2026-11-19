package com.neksbyte.porelgustoweb.service.impl;

import com.neksbyte.porelgustoweb.dao.ModificacionDao;
import com.neksbyte.porelgustoweb.model.Modificacion;
import com.neksbyte.porelgustoweb.service.ModificacionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ModificacionServiceImpl extends BaseServiceImpl<Modificacion, Integer> implements ModificacionService {

    public ModificacionServiceImpl(ModificacionDao dao) {
        super(dao);
    }
}
