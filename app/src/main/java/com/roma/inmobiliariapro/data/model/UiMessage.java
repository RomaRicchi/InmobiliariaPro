package com.roma.inmobiliariapro.data.model;

public class UiMessage {
    public String title;
    public String message;
    public Status status;

    public UiMessage(String title, String message, Status status) {
        this.title = title;
        this.message = message;
        this.status = status;
    }
}
