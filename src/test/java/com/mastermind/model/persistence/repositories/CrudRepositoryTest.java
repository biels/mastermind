package com.mastermind.model.persistence.repositories;

import com.mastermind.model.entities.base.Entity;
import com.mastermind.model.persistence.repositories.types.CrudRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public abstract class CrudRepositoryTest<T extends CrudRepository> extends RepositoryTest<T> {

    protected abstract List<Entity> getSampleEntities();

    @Test
    void save() {
        List<Entity> testEntities = getSampleEntities();
        // Save one
        component.save(testEntities.get(0));
        assertEquals(1, component.findAll().size());
        // Save the rest
        saveAllSampleEntities();
        // Since 0 gets saved twice
        assertEquals(testEntities.size() + 1, component.findAll().size());
    }

    @Test
    void saveFound() {
        List<Entity> testEntities = getSampleEntities();
        // Save one
        Entity e0 = component.save(testEntities.get(0));
        component.save((Entity) component.findOne(e0.getId()).get());
        assertEquals(1, component.findAll().size());

    }

    protected void saveAllSampleEntities() {
        getSampleEntities().forEach(e -> component.save(e));
    }

    @Test
    void count() {
        saveAllSampleEntities();
        assertEquals(getSampleEntities().size(), component.count().intValue());
    }

    @Test
    void exists() {
        List<Entity> testEntities = getSampleEntities();
        Entity e0 = testEntities.get(0);
        Entity e1 = testEntities.get(1);
        assertFalse(component.exists(e0.getId()));
        component.save(e0);
        assertTrue(component.exists(e0.getId()));
        assertFalse(component.exists(e1.getId()));
        component.save(e1);
        assertTrue(component.exists(e1.getId()));
    }

    @Test
    void findOne() {
        List<Entity> testEntities = getSampleEntities();
        Entity e0 = testEntities.get(0);
        Entity e1 = testEntities.get(1);
        assertFalse(component.findOne(e0.getId()).isPresent());
        component.save(e0);
        assertTrue(component.findOne(e0.getId()).isPresent());
        assertFalse(component.findOne(e1.getId()).isPresent());
        component.save(e1);
        assertTrue(component.findOne(e1.getId()).isPresent());
    }

    @Test
    void findAll() {
        List all = component.findAll();
        assertNotNull(all);
        assertEquals(0, all.size());
        saveAllSampleEntities();
        assertEquals(getSampleEntities().size(), all.size());
    }
}
