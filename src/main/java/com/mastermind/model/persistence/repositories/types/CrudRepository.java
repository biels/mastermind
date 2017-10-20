package com.mastermind.model.persistence.repositories.types;

import com.mastermind.model.entities.base.Entity;

import java.util.List;

public interface CrudRepository<T extends Entity> extends Repository<T> {
    <S extends T> S save(S entity);
    T findOne(Long primaryKey);
    List<T> findAll();
    Long count();
    void delete(T entity);
    boolean exists(Long primaryKey);
}