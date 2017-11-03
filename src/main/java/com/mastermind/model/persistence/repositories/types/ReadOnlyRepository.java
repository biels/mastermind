package com.mastermind.model.persistence.repositories.types;

import com.mastermind.model.entities.base.Entity;

import java.util.List;
import java.util.Optional;

public interface ReadOnlyRepository<T extends Entity> extends Repository<T>{
    Optional<T> findOne(Long primaryKey);
    List<T> findAll();
    Long count();
    boolean exists(Long primaryKey);
}
