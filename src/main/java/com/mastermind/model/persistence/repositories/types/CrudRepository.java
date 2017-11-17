package com.mastermind.model.persistence.repositories.types;

import com.mastermind.model.entities.base.Entity;

public interface CrudRepository<T extends Entity> extends ReadOnlyRepository<T> {
    <S extends T> S save(S entity);

    void delete(T entity);
}