//realiza la conexion a la base de datos para el inventario 
package DAO;

import Conexion.ConexionBD;
import Modelo.Prenda;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventarioDAO {

    //metodo de conexion a la base para obtener el inventario 
    public List<Prenda> obtenerInventario() {
        List<Prenda> inventario = new ArrayList<>();
        String sql = "SELECT id_prenda, tipo_prenda, estado_prenda, stock FROM Prendas";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                inventario.add(new Prenda(
                        rs.getInt("id_prenda"),
                        rs.getString("tipo_prenda"),
                        rs.getString("estado_prenda"),
                        rs.getInt("stock")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar inventario: " + e.getMessage());
        }

        return inventario;
    }
}