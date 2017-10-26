package com.mastermind.model.persistence.repositories.impl.inmemory;

import com.mastermind.model.entities.base.Entity;
import com.mastermind.model.persistence.repositories.types.CrudRepository;

import java.util.List;

public abstract class CrudRepositoryInMemoryImpl<T extends Entity> implements CrudRepository<T>{
    protected List<T> collection;
    Long lastAutoIncremental = 0L;

    private Long getNextAutoIncremental(){
        return lastAutoIncremental++;
    }

    @Override
    public <S extends T> S save(S entity) {
        Long id = entity.getId();
        if(id == null)id = getNextAutoIncremental();
        entity.setId(id);
        T findResult = findOne(id);
        if (findResult != null){
            collection.remove(findResult);
        }
        boolean addded = collection.add(entity);
        return (addded ? entity : null);
    }

    @Override
    public T findOne(Long primaryKey) {
        for (T e : collection){
            Long id = e.getId();
            if (id.equals(primaryKey))return e;
        }
        return null;
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
        collection.remove(findOne(entity.getId()));
    }

    @Override
    public boolean exists(Long primaryKey) {
        return false;
    }
}
