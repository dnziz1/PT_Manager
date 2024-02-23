package com.soft.myapplication;
public class ChatMessage {
    private String message;
    private boolean isSentByUser; // Indicator to differentiate between received and sent messages

    public ChatMessage(String message, boolean isSentByUser) {
        this.message = message;
        this.isSentByUser = isSentByUser;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSentByUser() {
        return isSentByUser;
    }
}
