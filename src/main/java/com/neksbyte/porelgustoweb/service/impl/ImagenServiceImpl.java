package com.neksbyte.porelgustoweb.service.impl;

import com.neksbyte.porelgustoweb.dao.ImagenDao;
import com.neksbyte.porelgustoweb.model.Imagen;
import com.neksbyte.porelgustoweb.service.ImagenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ImagenServiceImpl extends BaseServiceImpl<Imagen, Integer> implements ImagenService {

    public ImagenServiceImpl(ImagenDao dao) {
        super(dao);
    }
}
