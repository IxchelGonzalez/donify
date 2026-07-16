package Modelo;

public class Prenda {

    private int idPrenda;
    private String tipoPrenda;
    private String estadoPrenda;
    private int stock;

    //constructor
    public Prenda() {
    }

    public Prenda(int idPrenda, String tipoPrenda, String estadoPrenda, int stock) {
        this.idPrenda = idPrenda;
        this.tipoPrenda = tipoPrenda;
        this.estadoPrenda = estadoPrenda;
        this.stock = stock;
    }

    //getters y setters 
    public int getIdPrenda() {
        return idPrenda;
    }

    public void setIdPrenda(int idPrenda) {
        this.idPrenda = idPrenda;
    }

    public String getTipoPrenda() {
        return tipoPrenda;
    }

    public void setTipoPrenda(String tipoPrenda) {
        this.tipoPrenda = tipoPrenda;
    }

    public String getEstadoPrenda() {
        return estadoPrenda;
    }

    public void setEstadoPrenda(String estadoPrenda) {
        this.estadoPrenda = estadoPrenda;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return tipoPrenda + " - " + estadoPrenda;
    }
}