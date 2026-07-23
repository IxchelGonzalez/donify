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

    /**
     * Inventario global (stock general de la tabla Prendas), con filtros opcionales
     * de tipo de prenda y cantidad minima en stock.
     */
    public List<Prenda> obtenerInventario(String tipoPrenda, Integer cantidadMinima) {
        List<Prenda> inventario = new ArrayList<>();

        // Se calcula el stock combinado de TODAS las asociaciones a partir del
        // historial real de donaciones y entregas (igual que obtenerInventarioPorAsociacion,
        // pero sin filtrar por id_asociacion), en lugar de confiar en la columna
        // "stock" de Prendas, que es un contador que puede desincronizarse.
        StringBuilder sql = new StringBuilder(
                "SELECT p.id_prenda, p.tipo_prenda, p.estado_prenda, "
                + "(COALESCE(don.total_donado, 0) - COALESCE(ent.total_entregado, 0)) AS stock "
                + "FROM Prendas p "
                + "LEFT JOIN ("
                + "  SELECT dd.id_prenda, SUM(dd.cantidad) AS total_donado "
                + "  FROM Donacion_Detalle dd "
                + "  GROUP BY dd.id_prenda"
                + ") don ON don.id_prenda = p.id_prenda "
                + "LEFT JOIN ("
                + "  SELECT ed.id_prenda, SUM(ed.cantidad) AS total_entregado "
                + "  FROM Entrega_Detalle ed "
                + "  GROUP BY ed.id_prenda"
                + ") ent ON ent.id_prenda = p.id_prenda "
                + "WHERE 1 = 1");

        if (tipoPrenda != null && !tipoPrenda.isEmpty()) {
            sql.append(" AND p.tipo_prenda = ?");
        }

        sql.append(" HAVING stock > 0");

        if (cantidadMinima != null) {
            sql.append(" AND stock >= ?");
        }

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int indice = 1;

            if (tipoPrenda != null && !tipoPrenda.isEmpty()) {
                ps.setString(indice++, tipoPrenda);
            }

            if (cantidadMinima != null) {
                ps.setInt(indice++, cantidadMinima);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    inventario.add(new Prenda(
                            rs.getInt("id_prenda"),
                            rs.getString("tipo_prenda"),
                            rs.getString("estado_prenda"),
                            rs.getInt("stock")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar inventario: " + e.getMessage());
        }

        return inventario;
    }

    /**
     * Inventario de una institucion especifica: total donado a esa institucion menos
     * el total ya entregado por esa institucion, con filtros opcionales de tipo de
     * prenda y cantidad minima en stock.
     */
    public List<Prenda> obtenerInventarioPorAsociacion(int idAsociacion, String tipoPrenda, Integer cantidadMinima) {
        List<Prenda> inventario = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT p.id_prenda, p.tipo_prenda, p.estado_prenda, "
                + "(COALESCE(don.total_donado, 0) - COALESCE(ent.total_entregado, 0)) AS stock "
                + "FROM Prendas p "
                + "INNER JOIN ("
                + "  SELECT dd.id_prenda, SUM(dd.cantidad) AS total_donado "
                + "  FROM Donacion_Detalle dd "
                + "  INNER JOIN Donacion_Ingresos di ON dd.id_donacion_ingreso = di.id_donacion_ingreso "
                + "  WHERE di.id_asociacion = ? "
                + "  GROUP BY dd.id_prenda"
                + ") don ON don.id_prenda = p.id_prenda "
                + "LEFT JOIN ("
                + "  SELECT ed.id_prenda, SUM(ed.cantidad) AS total_entregado "
                + "  FROM Entrega_Detalle ed "
                + "  INNER JOIN Entrega_Salida es ON ed.id_entrega_salida = es.id_entrega_salida "
                + "  WHERE es.id_asociacion = ? "
                + "  GROUP BY ed.id_prenda"
                + ") ent ON ent.id_prenda = p.id_prenda "
                + "WHERE 1 = 1");

        if (tipoPrenda != null && !tipoPrenda.isEmpty()) {
            sql.append(" AND p.tipo_prenda = ?");
        }

        sql.append(" HAVING stock > 0");

        if (cantidadMinima != null) {
            sql.append(" AND stock >= ?");
        }

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int indice = 1;
            ps.setInt(indice++, idAsociacion);
            ps.setInt(indice++, idAsociacion);

            if (tipoPrenda != null && !tipoPrenda.isEmpty()) {
                ps.setString(indice++, tipoPrenda);
            }

            if (cantidadMinima != null) {
                ps.setInt(indice++, cantidadMinima);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    inventario.add(new Prenda(
                            rs.getInt("id_prenda"),
                            rs.getString("tipo_prenda"),
                            rs.getString("estado_prenda"),
                            rs.getInt("stock")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar inventario por institucion: " + e.getMessage());
        }

        return inventario;
    }

    public List<String> obtenerTiposPrenda() {
        List<String> tipos = new ArrayList<>();
        String sql = "SELECT DISTINCT tipo_prenda FROM Prendas ORDER BY tipo_prenda";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tipos.add(rs.getString("tipo_prenda"));
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar tipos de prenda: " + e.getMessage());
        }

        return tipos;
    }
}
