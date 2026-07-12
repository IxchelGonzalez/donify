package Modelo;

public class EntregaDetalle {

    private int idEntregaDetalle;
    private int idEntregaSalida;
    private int idPrenda;
    private int cantidad;

    public EntregaDetalle() {
    }

    public EntregaDetalle(int idEntregaDetalle, int idEntregaSalida, int idPrenda, int cantidad) {
        this.idEntregaDetalle = idEntregaDetalle;
        this.idEntregaSalida = idEntregaSalida;
        this.idPrenda = idPrenda;
        this.cantidad = cantidad;
    }

    public EntregaDetalle(int idEntregaSalida, int idPrenda, int cantidad) {
        this.idEntregaSalida = idEntregaSalida;
        this.idPrenda = idPrenda;
        this.cantidad = cantidad;
    }

    public int getIdEntregaDetalle() {
        return idEntregaDetalle;
    }

    public void setIdEntregaDetalle(int idEntregaDetalle) {
        this.idEntregaDetalle = idEntregaDetalle;
    }

    public int getIdEntregaSalida() {
        return idEntregaSalida;
    }

    public void setIdEntregaSalida(int idEntregaSalida) {
        this.idEntregaSalida = idEntregaSalida;
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
