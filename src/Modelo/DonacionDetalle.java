package Modelo;

public class DonacionDetalle {

    private int idDonacionDetalle;
    private int idDonacionIngreso;
    private int idPrenda;
    private int cantidad;

    public DonacionDetalle() {
    }

    //constructor
    public DonacionDetalle(int idDonacionDetalle, int idDonacionIngreso, int idPrenda, int cantidad) {
        this.idDonacionDetalle = idDonacionDetalle;
        this.idDonacionIngreso = idDonacionIngreso;
        this.idPrenda = idPrenda;
        this.cantidad = cantidad;
    }

    public DonacionDetalle(int idDonacionIngreso, int idPrenda, int cantidad) {
        this.idDonacionIngreso = idDonacionIngreso;
        this.idPrenda = idPrenda;
        this.cantidad = cantidad;
    }

    //getters y setters 
    public int getIdDonacionDetalle() {
        return idDonacionDetalle;
    }

    public void setIdDonacionDetalle(int idDonacionDetalle) {
        this.idDonacionDetalle = idDonacionDetalle;
    }

    public int getIdDonacionIngreso() {
        return idDonacionIngreso;
    }

    public void setIdDonacionIngreso(int idDonacionIngreso) {
        this.idDonacionIngreso = idDonacionIngreso;
    }

    public int getIdPrenda() {
        return idPrenda;
    }

    public void setIdPrenda(int idPrenda) {
        this.idPrenda = idPrenda;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
