package Modelo;

public class Asociacion {

    private int idAsociacion;
    private String nombre;
    private String ubicacion;
    private boolean verificacion;

    public Asociacion() {
    }

    //constructor
    public Asociacion(int idAsociacion, String nombre, String ubicacion, boolean verificacion) {
        this.idAsociacion = idAsociacion;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.verificacion = verificacion;
    }

    //getters y setters 
    public int getIdAsociacion() {
        return idAsociacion;
    }

    public void setIdAsociacion(int idAsociacion) {
        this.idAsociacion = idAsociacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public boolean isVerificacion() {
        return verificacion;
    }

    public void setVerificacion(boolean verificacion) {
        this.verificacion = verificacion;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
