package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.ImagenDao;
import com.neksbyte.porelgustoweb.model.Imagen;
import com.neksbyte.porelgustoweb.repository.ImagenRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ImagenDaoImpl extends BaseDaoImpl<Imagen, Integer> implements ImagenDao {

    public ImagenDaoImpl(ImagenRepository repository) {
        super(repository);
    }
}
