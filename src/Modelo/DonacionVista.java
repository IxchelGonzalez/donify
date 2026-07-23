package Modelo;

import java.sql.Date;

public class DonacionVista {

    private int idDonacionIngreso;
    private int idDonacionDetalle;
    private int idDonador;
    private String nombreDonador;
    private int idAsociacion;
    private String nombreAsociacion;
    private int idPrenda;
    private String prenda;
    private int cantidad;
    private Date fechaDonacion;

    public DonacionVista() {
    }

    public DonacionVista(int idDonacionIngreso, int idDonacionDetalle, int idDonador, String nombreDonador,
            int idAsociacion, String nombreAsociacion, int idPrenda, String prenda, int cantidad,
            Date fechaDonacion) {
        this.idDonacionIngreso = idDonacionIngreso;
        this.idDonacionDetalle = idDonacionDetalle;
        this.idDonador = idDonador;
        this.nombreDonador = nombreDonador;
        this.idAsociacion = idAsociacion;
        this.nombreAsociacion = nombreAsociacion;
        this.idPrenda = idPrenda;
        this.prenda = prenda;
        this.cantidad = cantidad;
        this.fechaDonacion = fechaDonacion;
    }

    public int getIdDonacionIngreso() {
        return idDonacionIngreso;
    }

    public void setIdDonacionIngreso(int idDonacionIngreso) {
        this.idDonacionIngreso = idDonacionIngreso;
    }

    public int getIdDonacionDetalle() {
        return idDonacionDetalle;
    }

    public void setIdDonacionDetalle(int idDonacionDetalle) {
        this.idDonacionDetalle = idDonacionDetalle;
    }

    public int getIdDonador() {
        return idDonador;
    }

    public void setIdDonador(int idDonador) {
        this.idDonador = idDonador;
    }

    public String getNombreDonador() {
        return nombreDonador;
    }

    public void setNombreDonador(String nombreDonador) {
        this.nombreDonador = nombreDonador;
    }

    public int getIdAsociacion() {
        return idAsociacion;
    }

    public void setIdAsociacion(int idAsociacion) {
        this.idAsociacion = idAsociacion;
    }

    public String getNombreAsociacion() {
        return nombreAsociacion;
    }

    public void setNombreAsociacion(String nombreAsociacion) {
        this.nombreAsociacion = nombreAsociacion;
    }

    public int getIdPrenda() {
        return idPrenda;
    }

    public void setIdPrenda(int idPrenda) {
        this.idPrenda = idPrenda;
    }

    public String getPrenda() {
        return prenda;
    }

    public void setPrenda(String prenda) {
        this.prenda = prenda;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFechaDonacion() {
        return fechaDonacion;
    }

    public void setFechaDonacion(Date fechaDonacion) {
        this.fechaDonacion = fechaDonacion;
    }
}
