package com.roma.inmobiliariapro.data.model.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Inquilino implements Serializable {
    @SerializedName("idInquilino")
    private int id;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String email;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
