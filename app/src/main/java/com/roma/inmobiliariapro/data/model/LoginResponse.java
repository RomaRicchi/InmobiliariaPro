package com.roma.inmobiliariapro.data.model;

public class LoginResponse {
    private String token;
    // Agrega otros campos que devuelva tu API, por ejemplo:
    // private String nombre;
    // private String email;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
