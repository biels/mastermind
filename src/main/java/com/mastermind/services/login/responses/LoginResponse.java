package com.mastermind.services.login.responses;

import java.util.ArrayList;

public class LoginResponse {
    private boolean success;
    private ArrayList<String> messages = new ArrayList<>();

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
}
