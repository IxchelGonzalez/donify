package DAO;

import Conexion.ConexionBD;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class ReporteDonacionesDAO {

    /**
     * Reporte de donaciones recibidas. Si idAsociacion es null se incluyen las de
     * todas las instituciones; si se especifica, solo las de esa institucion.
     */
    public DefaultTableModel obtenerReporteDonaciones(Integer idAsociacion) {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{
                    "ID Donacion",
                    "Donador",
                    "Institucion",
                    "Prenda",
                    "Estado",
                    "Cantidad",
                    "Fecha donacion"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        StringBuilder sql = new StringBuilder(
                "SELECT di.id_donacion_ingreso, "
                + "d.usuario AS donador, "
                + "a.nombre AS asociacion, "
                + "p.tipo_prenda, "
                + "p.estado_prenda, "
                + "dd.cantidad, "
                + "di.fecha_donacion "
                + "FROM Donacion_Ingresos di "
                + "INNER JOIN Usuarios d ON di.id_donador = d.id_usuario "
                + "INNER JOIN Asociaciones a ON di.id_asociacion = a.id_asociacion "
                + "INNER JOIN Donacion_Detalle dd ON di.id_donacion_ingreso = dd.id_donacion_ingreso "
                + "INNER JOIN Prendas p ON dd.id_prenda = p.id_prenda ");

        if (idAsociacion != null) {
            sql.append("WHERE di.id_asociacion = ? ");
        }

        sql.append("ORDER BY di.fecha_donacion DESC");

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            if (idAsociacion != null) {
                ps.setInt(1, idAsociacion);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date fecha = rs.getDate("fecha_donacion");

                    modelo.addRow(new Object[]{
                        rs.getInt("id_donacion_ingreso"),
                        rs.getString("donador"),
                        rs.getString("asociacion"),
                        rs.getString("tipo_prenda"),
                        rs.getString("estado_prenda"),
                        rs.getInt("cantidad"),
                        fecha == null ? "" : fecha.toString()
                    });
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener reporte de donaciones: " + e.getMessage());
        }

        return modelo;
    }
}
