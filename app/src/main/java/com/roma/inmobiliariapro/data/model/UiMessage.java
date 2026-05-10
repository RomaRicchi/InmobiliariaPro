package com.roma.inmobiliariapro.data.model;

public class UiMessage {
    public String title;
    public String message;
    public boolean success;

    public UiMessage(String title, String message, boolean success) {
        this.title = title;
        this.message = message;
        this.success = success;
    }
}
