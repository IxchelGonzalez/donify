package Modelo;

import java.sql.Date;

public class EntregaSalida {

    public static final String ESTADO_PENDIENTE = "Pendiente";
    public static final String ESTADO_ENTREGADA = "Entregada";

    private int idEntregaSalida;
    private int idBeneficiario;
    private int idAsociacion;
    private Date fechaEntrega;
    private String estado;

    public EntregaSalida() {
    }

    public EntregaSalida(int idEntregaSalida, int idBeneficiario, int idAsociacion, Date fechaEntrega, String estado) {
        this.idEntregaSalida = idEntregaSalida;
        this.idBeneficiario = idBeneficiario;
        this.idAsociacion = idAsociacion;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
    }

    public EntregaSalida(int idBeneficiario, int idAsociacion, Date fechaEntrega) {
        this.idBeneficiario = idBeneficiario;
        this.idAsociacion = idAsociacion;
        this.fechaEntrega = fechaEntrega;
        this.estado = ESTADO_PENDIENTE;
    }

    public int getIdEntregaSalida() {
        return idEntregaSalida;
    }

    public void setIdEntregaSalida(int idEntregaSalida) {
        this.idEntregaSalida = idEntregaSalida;
    }

    public int getIdBeneficiario() {
        return idBeneficiario;
    }

    public void setIdBeneficiario(int idBeneficiario) {
        this.idBeneficiario = idBeneficiario;
    }

    public int getIdAsociacion() {
        return idAsociacion;
    }

    public void setIdAsociacion(int idAsociacion) {
        this.idAsociacion = idAsociacion;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
