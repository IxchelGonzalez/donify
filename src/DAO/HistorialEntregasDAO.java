package DAO;

import Conexion.ConexionBD;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class HistorialEntregasDAO {

    /**
     * Obtiene el historial sin filtros de fecha.
     */
    public DefaultTableModel obtenerHistorialEntregas(Integer idAsociacion) {
        return obtenerHistorialEntregas(
                idAsociacion,
                null,
                null
        );
    }

    /**
     * Obtiene el historial aplicando filtros opcionales.
     *
     * @param idAsociacion asociación seleccionada; null significa todas.
     * @param anio año seleccionado; null significa todos.
     * @param mes mes seleccionado del 1 al 12; null significa todos.
     * @return modelo con el historial de entregas.
     */
    public DefaultTableModel obtenerHistorialEntregas(
            Integer idAsociacion,
            Integer anio,
            Integer mes
    ) {
        DefaultTableModel modelo = crearModeloTabla();

        StringBuilder sql = new StringBuilder(
                "SELECT es.id_entrega_salida, "
                + "b.nombre AS beneficiario, "
                + "a.nombre AS asociacion, "
                + "p.tipo_prenda, "
                + "p.estado_prenda, "
                + "ed.cantidad, "
                + "es.fecha_entrega, "
                + "es.estado "
                + "FROM Entrega_Salida es "
                + "INNER JOIN Beneficiarios b "
                + "ON es.id_beneficiario = b.id_beneficiario "
                + "INNER JOIN Asociaciones a "
                + "ON es.id_asociacion = a.id_asociacion "
                + "INNER JOIN Entrega_Detalle ed "
                + "ON es.id_entrega_salida = ed.id_entrega_salida "
                + "INNER JOIN Prendas p "
                + "ON ed.id_prenda = p.id_prenda "
                + "WHERE 1 = 1 "
        );

        if (idAsociacion != null) {
            sql.append("AND es.id_asociacion = ? ");
        }

        if (anio != null) {
            sql.append("AND YEAR(es.fecha_entrega) = ? ");
        }

        if (mes != null) {
            sql.append("AND MONTH(es.fecha_entrega) = ? ");
        }

        sql.append(
                "ORDER BY es.fecha_entrega DESC, "
                + "es.id_entrega_salida DESC"
        );

        try (
            Connection con = ConexionBD.conectar();
            PreparedStatement ps =
                    con.prepareStatement(sql.toString())
        ) {
            int parametro = 1;

            if (idAsociacion != null) {
                ps.setInt(parametro++, idAsociacion);
            }

            if (anio != null) {
                ps.setInt(parametro++, anio);
            }

            if (mes != null) {
                ps.setInt(parametro, mes);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date fecha = rs.getDate("fecha_entrega");

                    modelo.addRow(new Object[]{
                        rs.getInt("id_entrega_salida"),
                        rs.getString("beneficiario"),
                        rs.getString("asociacion"),
                        rs.getString("tipo_prenda"),
                        rs.getString("estado_prenda"),
                        rs.getInt("cantidad"),
                        fecha == null ? "" : fecha.toString(),
                        rs.getString("estado")
                    });
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al obtener historial de entregas: "
                    + e.getMessage()
            );
        }

        return modelo;
    }

    /**
     * Obtiene los años que tienen entregas registradas.
     */
    public List<Integer> obtenerAniosDisponibles(
            Integer idAsociacion
    ) {
        List<Integer> anios = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT YEAR(fecha_entrega) AS anio "
                + "FROM Entrega_Salida "
                + "WHERE fecha_entrega IS NOT NULL "
        );

        if (idAsociacion != null) {
            sql.append("AND id_asociacion = ? ");
        }

        sql.append("ORDER BY anio DESC");

        try (
            Connection con = ConexionBD.conectar();
            PreparedStatement ps =
                    con.prepareStatement(sql.toString())
        ) {
            if (idAsociacion != null) {
                ps.setInt(1, idAsociacion);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    anios.add(rs.getInt("anio"));
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al obtener años disponibles: "
                    + e.getMessage()
            );
        }

        return anios;
    }

    private DefaultTableModel crearModeloTabla() {
        return new DefaultTableModel(
                new String[]{
                    "ID Entrega",
                    "Beneficiario",
                    "Institución",
                    "Prenda",
                    "Estado prenda",
                    "Cantidad",
                    "Fecha entrega",
                    "Estado entrega"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna
            ) {
                return false;
            }
        };
    }
}