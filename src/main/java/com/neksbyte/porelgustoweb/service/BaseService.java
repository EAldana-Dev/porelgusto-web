package com.neksbyte.porelgustoweb.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID id);
    T save(T entity);
    boolean existsById(ID id);
    void deleteById(ID id);
}
