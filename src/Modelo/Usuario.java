package Modelo;

public class Usuario {

    private int idUsuario;
    private String usuario;
    private String contrasena;
    private String curp;
    private String tipoUsuario;

    public Usuario() {
    }

    public Usuario(int idUsuario, String usuario, String contrasena, String curp, String tipoUsuario) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.curp = curp;
        this.tipoUsuario = tipoUsuario;
    }

    public Usuario(String usuario, String contrasena, String curp, String tipoUsuario) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.curp = curp;
        this.tipoUsuario = tipoUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}