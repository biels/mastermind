package com.mastermind.services.players.responses;

import com.mastermind.model.entities.types.Player;

import java.util.ArrayList;

public class CreatePlayerResponse<T extends Player> {
    private boolean success;
    private ArrayList<String> messages = new ArrayList<>();
    private T createdPlayer;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }

    public T getCreatedPlayer() {
        return createdPlayer;
    }

    public void setCreatedPlayer(T createdPlayer) {
        this.createdPlayer = createdPlayer;
    }
}
