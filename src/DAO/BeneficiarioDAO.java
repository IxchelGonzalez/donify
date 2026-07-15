//Contiene la comunicacion a la tabla (Beneficiarios) de nuestra base de datos 
package DAO;
//Importaciones de librerias necesarias para el funcionamiento del codigo 
import Conexion.ConexionBD;
import Modelo.Beneficiario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BeneficiarioDAO {
    
 //Inserta los beneficiarios creados en la base de datos 
    public boolean crearBeneficiario(Beneficiario beneficiario) {
        String sql = "INSERT INTO Beneficiarios(nombre, sexo, fecha_ultima_recepcion) VALUES (?, ?, ?)";
 
        //Realiza la conexion a la base de datos 
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            // coloca los valores en una consulta sql
            ps.setString(1, beneficiario.getNombre());
            ps.setString(2, beneficiario.getSexo());
            ps.setDate(3, beneficiario.getFechaUltimaRecepcion());
            
            //Regresa cuantas filas fueron modificadas en la base de datos 
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al crear beneficiario: " + e.getMessage());
            return false;
        }
    }
    // metodo para devolver la lista de beneficiarios 
    public List<Beneficiario> obtenerBeneficiarios() {
        List<Beneficiario> beneficiarios = new ArrayList<>();
        
        //codigo sql para mostrar los datos de los beneficiarios registrados en la tabla "Beneficiarios"
        String sql = "SELECT id_beneficiario, nombre, sexo, fecha_ultima_recepcion FROM Beneficiarios";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                beneficiarios.add(new Beneficiario(
                        rs.getInt("id_beneficiario"),
                        rs.getString("nombre"),
                        rs.getString("sexo"),
                        rs.getDate("fecha_ultima_recepcion")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener beneficiarios: " + e.getMessage());
        }

        return beneficiarios;
    }
// metodo que nos permite actualizar a los beneficiarios en la tabla 
    public boolean actualizarBeneficiario(Beneficiario beneficiario) {
        //instruccion sql para agregar los datos a la tabla 
        String sql = "UPDATE Beneficiarios SET nombre = ?, sexo = ?, fecha_ultima_recepcion = ? WHERE id_beneficiario = ?";

        //conexion a la base de datos 
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            //se agrega la informacion a la tabla 
            ps.setString(1, beneficiario.getNombre());
            ps.setString(2, beneficiario.getSexo());
            ps.setDate(3, beneficiario.getFechaUltimaRecepcion());
            ps.setInt(4, beneficiario.getIdBeneficiario());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar beneficiario: " + e.getMessage());
            return false;
        }
    }
// metodo para eliminar algun beneficiario 
    public boolean eliminarBeneficiario(int idBeneficiario) {
        
        //isntruccion sql para eliminar al beneficiario de la tabla 
        String sql = "DELETE FROM Beneficiarios WHERE id_beneficiario = ?";
 //coneccion con la base de datos 
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
//regresa un valor mayor a 1 si la tabla fue modificada con exito (indica las lineas modificadas)
            ps.setInt(1, idBeneficiario);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar beneficiario: " + e.getMessage());
            return false;
        }
    }
//nos ayuda a buscar un beneficiario usando el nombre 
    public boolean existeBeneficiario(String nombre) {
        //codigo sql para buscar al beneficiario acorde a su nombre en la tabla 
        String sql = "SELECT id_beneficiario FROM Beneficiarios WHERE nombre = ?";
 //conexion a la base de datos 
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Error al verificar beneficiario: " + e.getMessage());
            return false;
        }
    }
}
