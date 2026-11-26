package com.neksbyte.porelgustoweb.dao.impl;

import com.neksbyte.porelgustoweb.dao.ImagenDao;
import com.neksbyte.porelgustoweb.model.Imagen;
import com.neksbyte.porelgustoweb.repository.ImagenRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.neksbyte.porelgustoweb.model.ArregloFloral;


@Repository
public class ImagenDaoImpl extends BaseDaoImpl<Imagen, Integer> implements ImagenDao {

    private final ImagenRepository repo;

    public ImagenDaoImpl(ImagenRepository repository) {
        super(repository);
        this.repo = repository;
    }

    @Override
    public List<Imagen> findByArreglo(ArregloFloral arreglo) {
        return repo.findByArregloFloral(arreglo);
    }
}
