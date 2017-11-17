package com.mastermind.model.persistence.repositories.impl.inmemory;

import com.mastermind.model.entities.base.Entity;
import com.mastermind.model.persistence.repositories.types.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class CrudRepositoryInMemoryImpl<T extends Entity> implements CrudRepository<T> {
    List<T> collection = new ArrayList<>();
    private Long lastAutoIncremental = 0L;

    private Long getNextAutoIncremental() {
        return lastAutoIncremental++;
    }

    @Override
    public <S extends T> S save(S entity) {
        Long id = entity.getId();
        if (id == null) {
            id = getNextAutoIncremental();
            entity.setId(id);
        }
        Optional<T> findResult = findOne(id);
        findResult.ifPresent(t -> collection.remove(t));
        boolean added = collection.add(entity);
        return (added ? entity : null);
    }

    @Override
    public Optional<T> findOne(Long primaryKey) {
        return collection.stream()
                .filter(t -> t.getId() == primaryKey)
                .findFirst();
    }

    @Override
    public List<T> findAll() {
        return collection;
    }

    @Override
    public Long count() {
        return (long) collection.size();
    }

    @Override
    public void delete(T entity) {
        //This will be implemented using ids instead of object references in SQL
        Optional<T> foundEntity = findOne(entity.getId());
        foundEntity.ifPresent(t -> collection.remove(t));
    }

    @Override
    public boolean exists(Long primaryKey) {
        return false;
    }
}
