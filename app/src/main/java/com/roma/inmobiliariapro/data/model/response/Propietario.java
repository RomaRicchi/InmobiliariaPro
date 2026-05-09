package com.roma.inmobiliariapro.data.model.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Propietario implements Serializable {
    @SerializedName("idPropietario")
    private int id;
    
    @SerializedName("nombre")
    private String nombre;
    
    @SerializedName("apellido")
    private String apellido;
    
    @SerializedName("dni")
    private String dni;
    
    @SerializedName("telefono")
    private String telefono;
    
    @SerializedName("email")
    private String email;
    
//    @SerializedName("clave")
//    private String clave;

    // Getters y Setters
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
//    public String getClave() { return clave; }
//    public void setClave(String clave) { this.clave = clave; }

    public String getFullName() {
        return nombre + " " + apellido;
    }
}
