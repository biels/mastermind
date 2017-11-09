package com.mastermind.model.entities.types;

public class HumanPlayer extends Player {
    private String password;

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
}
