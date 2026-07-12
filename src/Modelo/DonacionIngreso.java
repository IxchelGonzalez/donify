package Modelo;

import java.sql.Date;

public class DonacionIngreso {

    private int idDonacionIngreso;
    private int idDonador;
    private int idAsociacion;
    private Date fechaDonacion;

    public DonacionIngreso() {
    }

    public DonacionIngreso(int idDonacionIngreso, int idDonador, int idAsociacion, Date fechaDonacion) {
        this.idDonacionIngreso = idDonacionIngreso;
        this.idDonador = idDonador;
        this.idAsociacion = idAsociacion;
        this.fechaDonacion = fechaDonacion;
    }

    public DonacionIngreso(int idDonador, int idAsociacion, Date fechaDonacion) {
        this.idDonador = idDonador;
        this.idAsociacion = idAsociacion;
        this.fechaDonacion = fechaDonacion;
    }

    public int getIdDonacionIngreso() {
        return idDonacionIngreso;
    }

    public void setIdDonacionIngreso(int idDonacionIngreso) {
        this.idDonacionIngreso = idDonacionIngreso;
    }

    public int getIdDonador() {
        return idDonador;
    }

    public void setIdDonador(int idDonador) {
        this.idDonador = idDonador;
    }

    public int getIdAsociacion() {
        return idAsociacion;
    }

    public void setIdAsociacion(int idAsociacion) {
        this.idAsociacion = idAsociacion;
    }

    public Date getFechaDonacion() {
        return fechaDonacion;
    }

    public void setFechaDonacion(Date fechaDonacion) {
        this.fechaDonacion = fechaDonacion;
    }
}