package com.mastermind.model.entities.base;

public abstract class Entity {
    private Long id = null;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
