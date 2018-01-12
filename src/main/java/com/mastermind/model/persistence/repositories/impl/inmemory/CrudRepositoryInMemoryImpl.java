package com.mastermind.model.persistence.repositories.impl.inmemory;

import com.mastermind.model.entities.base.Entity;
import com.mastermind.model.persistence.repositories.types.CrudRepository;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class CrudRepositoryInMemoryImpl<T extends Entity> implements CrudRepository<T> {
    List<T> collection = new ArrayList<>();
    private Long lastAutoIncremental = 0L;


    private Long getNextAutoIncremental() {
        return lastAutoIncremental++;
    }

    public CrudRepositoryInMemoryImpl() {
        if(isPersistent())readFromFile();
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
        if(isPersistent())flushToFile();
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

    public abstract String serialize(T entity);
    public abstract T deserialize(String serialized);

    private String getFileName() {
        return getClass().getSimpleName() + ".txt";
    }
    protected abstract boolean isPersistent();
    public void readFromFile(){
        String filename = getFileName();
        if(!Files.exists(Paths.get(filename))) return;
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            lastAutoIncremental = Long.parseLong(lines.get(0));
            collection.clear();
            IntStream.range(1, lines.size()).forEach(i -> collection.add(deserialize(lines.get(i))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void flushToFile(){
        List<String> lines = new ArrayList<>();
        lines.add(Long.toString(lastAutoIncremental));
        collection.parallelStream()
                .map(this::serialize)
                .forEach(lines::add);
        try {
            String str = getFileName();
            if(Files.exists(Paths.get(str))) Files.delete(Paths.get(str));
            Files.write(Paths.get(str), lines, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
