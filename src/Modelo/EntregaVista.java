package Modelo;
 
import java.sql.Date;
 
public class EntregaVista {
 
    private int idEntregaSalida;
    private int idEntregaDetalle;
    private int idBeneficiario;
    private String nombreBeneficiario;
    private int idAsociacion;
    private String nombreAsociacion;
    private int idPrenda;
    private String prenda;
    private int cantidad;
    private Date fechaEntrega;
    private String estado;
 
    public EntregaVista() {
    }
 
    public EntregaVista(int idEntregaSalida, int idEntregaDetalle, int idBeneficiario, String nombreBeneficiario,
            int idAsociacion, String nombreAsociacion, int idPrenda, String prenda, int cantidad,
            Date fechaEntrega, String estado) {
        this.idEntregaSalida = idEntregaSalida;
        this.idEntregaDetalle = idEntregaDetalle;
        this.idBeneficiario = idBeneficiario;
        this.nombreBeneficiario = nombreBeneficiario;
        this.idAsociacion = idAsociacion;
        this.nombreAsociacion = nombreAsociacion;
        this.idPrenda = idPrenda;
        this.prenda = prenda;
        this.cantidad = cantidad;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
    }
 
    public int getIdEntregaSalida() {
        return idEntregaSalida;
    }
 
    public void setIdEntregaSalida(int idEntregaSalida) {
        this.idEntregaSalida = idEntregaSalida;
    }
 
    public int getIdEntregaDetalle() {
        return idEntregaDetalle;
    }
 
    public void setIdEntregaDetalle(int idEntregaDetalle) {
        this.idEntregaDetalle = idEntregaDetalle;
    }
 
    public int getIdBeneficiario() {
        return idBeneficiario;
    }
 
    public void setIdBeneficiario(int idBeneficiario) {
        this.idBeneficiario = idBeneficiario;
    }
 
    public String getNombreBeneficiario() {
        return nombreBeneficiario;
    }
 
    public void setNombreBeneficiario(String nombreBeneficiario) {
        this.nombreBeneficiario = nombreBeneficiario;
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
 
    public boolean esPendiente() {
        return estado != null && EntregaSalida.ESTADO_PENDIENTE.equalsIgnoreCase(estado.trim());
    }
}
