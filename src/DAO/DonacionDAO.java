//metodo que realiza la comunicacion a l atabla de donaciones 
package DAO;

import Conexion.ConexionBD;
import Modelo.Asociacion;
import Modelo.Donador;
import Modelo.Prenda;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DonacionDAO {
 //metodo que realiza el registro de donadores 
    public int registrarDonador(Donador donador) {
        //codigo sql que inserta la informacion del donador en la tabla 
        String sql = "INSERT INTO Donadores(nombre, correo, telefono) VALUES (?, ?, ?)";

        //conexion a la base de datos 
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            //inserta los datos en sus respectivos lugares 
            ps.setString(1, donador.getNombre());
            ps.setString(2, donador.getCorreo());
            ps.setString(3, donador.getTelefono());

            int filasAfectadas = ps.executeUpdate();
            //muestra el numero de filas afectadas, si es mayor a 1 se agrego correctamente el usuario 
            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al registrar donador: " + e.getMessage());
        }

        return 0;
    }

    //metodo para obtener los donadores 
    public List<Donador> obtenerDonadores() {
        //crea la lista para mostrar todos los donadores 
        List<Donador> donadores = new ArrayList<>();
        //codigo sql para consultar todos los donadores registrados en la tabla donadores 
        String sql = "SELECT id_donador, nombre, correo, telefono FROM Donadores";
        //conexion a la base de datos 
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            //linea que indica que mientras exista una siguiente linea siga imprimiendo lso datos de los donadores 
            while (rs.next()) {
                donadores.add(new Donador(
                        rs.getInt("id_donador"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("telefono")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener donadores: " + e.getMessage());
        }

        return donadores;
    }
    //metodo que nos permite obtener las asociaciones 
    public List<Asociacion> obtenerAsociaciones() {
        //array(lista) para obtener las asociaciones 
        List<Asociacion> asociaciones = new ArrayList<>();
        //codigo sql para obtenr todos los datos de las asociaciones 
        String sql = "SELECT id_asociacion, nombre, ubicacion, verificacion FROM Asociaciones";

        //conexion a la base de datos 
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            //comando que nos indica que mientras exista una segunda linea siga imprimiendo los datos ingresados en la base de datos 
            while (rs.next()) {
                asociaciones.add(new Asociacion(
                        rs.getInt("id_asociacion"),
                        rs.getString("nombre"),
                        rs.getString("ubicacion"),
                        rs.getBoolean("verificacion")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener asociaciones: " + e.getMessage());
        }

        return asociaciones;
    }
    //metodo para obtener las prendas registradas en la base de datos 
    public List<Prenda> obtenerPrendas() {
        //array que nos permite crear una lista de prendas 
        List<Prenda> prendas = new ArrayList<>();
        //codigo sql que extrae la informacion almacenada en la base de datos 
        String sql = "SELECT id_prenda, tipo_prenda, estado_prenda, stock FROM Prendas";

        //conexion a la base de datos 
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            //linea que nos indica que mientras exista otra linea siga imprimiendo los datos registrados en nuestra tabla 
            while (rs.next()) {
                prendas.add(new Prenda(
                        rs.getInt("id_prenda"),
                        rs.getString("tipo_prenda"),
                        rs.getString("estado_prenda"),
                        rs.getInt("stock")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener prendas: " + e.getMessage());
        }

        return prendas;
    }

    //metodo bookeano para regitrar donaciones completas 
    public boolean registrarDonacionCompleta(int idDonador, int idAsociacion, Date fechaDonacion,
                                             int idPrenda, int cantidad) {
        //variable que alamcenara la instancia de la conexion mas adelante
        Connection con = null;

        //codigo sql para insertar valores en la base de datos 
        String sqlIngreso = "INSERT INTO Donacion_Ingresos(id_donador, id_asociacion, fecha_donacion) "
                          + "VALUES (?, ?, ?)";

        String sqlDetalle = "INSERT INTO Donacion_Detalle(id_donacion_ingreso, id_prenda, cantidad) "
                          + "VALUES (?, ?, ?)";

        //actualiza la cantidad de prendas que existen de cada tipo dependiendo del stock registrado al momento del registro de la prenda
        String sqlActualizarStock = "UPDATE Prendas SET stock = stock + ? WHERE id_prenda = ?";

        try {
            con = ConexionBD.conectar();

            if (con == null) {
                return false;
            }
            //no guarda la informacion de inmediato en caso de que alguna de las 3 acciones fallen 
            con.setAutoCommit(false);

            //guarda el id de la tabla 
            int idDonacionIngreso = 0;
 
            //se agregan los valores a las tablas 
            try (PreparedStatement psIngreso = con.prepareStatement(sqlIngreso, Statement.RETURN_GENERATED_KEYS)) {
                psIngreso.setInt(1, idDonador);
                psIngreso.setInt(2, idAsociacion);
                psIngreso.setDate(3, fechaDonacion);
                psIngreso.executeUpdate();
                //trae el id generado o guardado en int idDonacion
                try (ResultSet rs = psIngreso.getGeneratedKeys()) {
                    //si existe registro...
                    if (rs.next()) {
                        //entonces el iddonacion es igual a rs.getInt(1)
                        idDonacionIngreso = rs.getInt(1);
                    }
                }
            }
            //if por si nunca se obtuvo un id 
            if (idDonacionIngreso == 0) {
                //elimina toda "actualizacion" de las lineas anteriores en caso de que algo no salga correcto
                con.rollback();
                return false;
            }
            // se realiza otro insert 
            try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle)) {
                psDetalle.setInt(1, idDonacionIngreso);
                psDetalle.setInt(2, idPrenda);
                psDetalle.setInt(3, cantidad);
                psDetalle.executeUpdate();
            }
            //actualiza el stock
            try (PreparedStatement psStock = con.prepareStatement(sqlActualizarStock)) {
                psStock.setInt(1, cantidad);
                psStock.setInt(2, idPrenda);
                psStock.executeUpdate();
            }
            //ya que las 3 acciones estan correctas se guardan en la base 
            con.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al registrar donacion: " + e.getMessage());

            try {
                //intenta revertir los cambios en caso de que algo salga mal 
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("Error al revertir la donacion: " + ex.getMessage());
            }

            return false;
            //reactiva el comportamiento normal de la conexion fuera exitoso el registro o no 
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    //elimina los registros 
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
    }
}