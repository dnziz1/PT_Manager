package com.example.pt_app;

public class ChatMessage {
    private String sender;
    private String messageText;
    private String timestamp;

    public ChatMessage(String sender, String messageText, String timestamp) {
        this.sender = sender;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getTimestamp() {
        return timestamp;
    }
}

