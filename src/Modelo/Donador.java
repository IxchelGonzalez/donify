package Modelo;

public class Donador {

    private int idDonador;
    private String nombre;
    private String correo;
    private String telefono;

    //constructor
    public Donador() {
    }

    public Donador(int idDonador, String nombre, String correo, String telefono) {
        this.idDonador = idDonador;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
    }

    public Donador(String nombre, String correo, String telefono) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
    }

    //getters y setters 
    public int getIdDonador() {
        return idDonador;
    }

    public void setIdDonador(int idDonador) {
        this.idDonador = idDonador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return nombre;
    }
}