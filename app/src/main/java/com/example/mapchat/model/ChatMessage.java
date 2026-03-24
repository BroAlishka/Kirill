package com.example.mapchat.model;

public class ChatMessage {
    private final String authorLogin;
    private final String authorName;
    private final String text;
    private final long timestamp;

    public ChatMessage(String authorLogin, String authorName, String text, long timestamp) {
        this.authorLogin = authorLogin;
        this.authorName = authorName;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getAuthorLogin() {
        return authorLogin;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
