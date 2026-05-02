package com.roma.inmobiliariapro.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Pago implements Serializable {
    @SerializedName("idPago")
    private int id;
    private String fechaPago;
    @SerializedName("monto")
    private double importe;
    private String detalle;
    private boolean estado;
    @SerializedName("idContrato")
    private int contratoId;
    private Contrato contrato;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFechaPago() { return fechaPago; }
    public void setFechaPago(String fechaPago) { this.fechaPago = fechaPago; }
    public double getImporte() { return importe; }
    public void setImporte(double importe) { this.importe = importe; }
    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
    public int getContratoId() { return contratoId; }
    public void setContratoId(int contratoId) { this.contratoId = contratoId; }
    public Contrato getContrato() { return contrato; }
    public void setContrato(Contrato contrato) { this.contrato = contrato; }
}
