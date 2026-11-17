package com.neksbyte.porelgustoweb.service.impl;

import com.neksbyte.porelgustoweb.dao.BaseDao;
import com.neksbyte.porelgustoweb.service.BaseService;

import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<T, ID> implements BaseService<T, ID> {

    protected final BaseDao<T, ID> dao;

    protected BaseServiceImpl(BaseDao<T, ID> dao) {
        this.dao = dao;
    }

    @Override
    public List<T> findAll() {
        return dao.findAll();
    }

    @Override
    public Optional<T> findById(ID id) {
        return dao.findById(id);
    }

    @Override
    public T save(T entity) {
        return dao.save(entity);
    }

    @Override
    public boolean existsById(ID id) {
        return dao.existsById(id);
    }

    @Override
    public void deleteById(ID id) {
        dao.deleteById(id);
    }
}
