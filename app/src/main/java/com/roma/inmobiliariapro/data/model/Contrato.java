package com.roma.inmobiliariapro.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Contrato implements Serializable {
    @SerializedName("idContrato")
    private int id;
    private String fechaInicio;
    @SerializedName("fechaFinalizacion")
    private String fechaFin;
    private double montoAlquiler;
    private boolean estado;
    @SerializedName("idInquilino")
    private int inquilinoId;
    @SerializedName("idInmueble")
    private int inmuebleId;
    private Inquilino inquilino;
    private Inmueble inmueble;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }
    public double getMontoAlquiler() { return montoAlquiler; }
    public void setMontoAlquiler(double montoAlquiler) { this.montoAlquiler = montoAlquiler; }
    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
    public int getInquilinoId() { return inquilinoId; }
    public void setInquilinoId(int inquilinoId) { this.inquilinoId = inquilinoId; }
    public int getInmuebleId() { return inmuebleId; }
    public void setInmuebleId(int inmuebleId) { this.inmuebleId = inmuebleId; }
    public Inquilino getInquilino() { return inquilino; }
    public void setInquilino(Inquilino inquilino) { this.inquilino = inquilino; }
    public Inmueble getInmueble() { return inmueble; }
    public void setInmueble(Inmueble inmueble) { this.inmueble = inmueble; }
}
