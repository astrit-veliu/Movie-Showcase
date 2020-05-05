package com.av.movieshowcase.data.local.model;

public class EventSender {
    private String message;

    public EventSender(String message ) {
        this.message =message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
