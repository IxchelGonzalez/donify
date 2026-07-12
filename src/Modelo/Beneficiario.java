package Modelo;

import java.sql.Date;

public class Beneficiario {

    private int idBeneficiario;
    private String nombre;
    private String sexo;
    private Date fechaUltimaRecepcion;

    public Beneficiario() {
    }

    public Beneficiario(int idBeneficiario, String nombre, String sexo, Date fechaUltimaRecepcion) {
        this.idBeneficiario = idBeneficiario;
        this.nombre = nombre;
        this.sexo = sexo;
        this.fechaUltimaRecepcion = fechaUltimaRecepcion;
    }

    public Beneficiario(String nombre, String sexo, Date fechaUltimaRecepcion) {
        this.nombre = nombre;
        this.sexo = sexo;
        this.fechaUltimaRecepcion = fechaUltimaRecepcion;
    }

    public int getIdBeneficiario() {
        return idBeneficiario;
    }

    public void setIdBeneficiario(int idBeneficiario) {
        this.idBeneficiario = idBeneficiario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Date getFechaUltimaRecepcion() {
        return fechaUltimaRecepcion;
    }

    public void setFechaUltimaRecepcion(Date fechaUltimaRecepcion) {
        this.fechaUltimaRecepcion = fechaUltimaRecepcion;
    }
    
    @Override
public String toString() {
    return nombre;
}
}