package Modelo;

import java.sql.Date;

public class EntregaSalida {

    private int idEntregaSalida;
    private int idBeneficiario;
    private int idAsociacion;
    private Date fechaEntrega;

    //constructor
    public EntregaSalida() {
    }

    public EntregaSalida(int idEntregaSalida, int idBeneficiario, int idAsociacion, Date fechaEntrega) {
        this.idEntregaSalida = idEntregaSalida;
        this.idBeneficiario = idBeneficiario;
        this.idAsociacion = idAsociacion;
        this.fechaEntrega = fechaEntrega;
    }

    public EntregaSalida(int idBeneficiario, int idAsociacion, Date fechaEntrega) {
        this.idBeneficiario = idBeneficiario;
        this.idAsociacion = idAsociacion;
        this.fechaEntrega = fechaEntrega;
    }

    //getters y setters 
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
}
