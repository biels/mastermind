package com.mastermind.model.entities.types;

public class HumanPlayer extends Player {
    private String password;

    public HumanPlayer() {
        this("human");
    }
    public HumanPlayer(String name) {
        this(name, "default");
    }



    public HumanPlayer(String name, String password) {
        super(name);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    protected void deserializeSpecific(String specific) {
        password = specific;
    }

    @Override
    protected String serializeSpecific() {
        return password;
    }
}
