package DAO;

import Conexion.ConexionBD;
import Modelo.ResumenDonacion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReporteDonacionesDAO {

    /**
     * Obtiene todos los indicadores del dashboard.
     *
     * @param idAsociacion null para todas las asociaciones.
     * @param anio null para todos los años.
     * @param trimestre null para todos los trimestres.
     */
    public ResumenDonacion obtenerResumenDonaciones(
            Integer idAsociacion,
            Integer anio,
            Integer trimestre
    ) {
        ResumenDonacion resumen =
                new ResumenDonacion();

        cargarTotales(
                resumen,
                idAsociacion,
                anio,
                trimestre
        );

        resumen.setPrendaMasDonada(
                obtenerPrendaMasDonada(
                        idAsociacion,
                        anio,
                        trimestre
                )
        );

        resumen.setEstadoMasFrecuente(
                obtenerEstadoMasFrecuente(
                        idAsociacion,
                        anio,
                        trimestre
                )
        );

        resumen.setDonadorPrincipal(
                obtenerDonadorPrincipal(
                        idAsociacion,
                        anio,
                        trimestre
                )
        );

        resumen.setInstitucionPrincipal(
                obtenerInstitucionPrincipal(
                        idAsociacion,
                        anio,
                        trimestre
                )
        );

        return resumen;
    }

    private void cargarTotales(
            ResumenDonacion resumen,
            Integer idAsociacion,
            Integer anio,
            Integer trimestre
    ) {
        StringBuilder sql = new StringBuilder(
                "SELECT "
                + "COUNT(DISTINCT di.id_donacion_ingreso) "
                + "AS total_donaciones, "
                + "COALESCE(SUM(dd.cantidad), 0) "
                + "AS prendas_recibidas, "
                + "COUNT(DISTINCT di.id_donador) "
                + "AS donadores, "
                + "COALESCE("
                + "SUM(dd.cantidad) / "
                + "NULLIF(COUNT(DISTINCT "
                + "di.id_donacion_ingreso), 0), "
                + "0) AS promedio "
                + "FROM Donacion_Ingresos di "
                + "INNER JOIN Donacion_Detalle dd "
                + "ON di.id_donacion_ingreso = "
                + "dd.id_donacion_ingreso "
                + "WHERE 1 = 1 "
        );

        agregarFiltros(
                sql,
                idAsociacion,
                anio,
                trimestre
        );

        try (
            Connection con = ConexionBD.conectar();
            PreparedStatement ps =
                    con.prepareStatement(sql.toString())
        ) {
            colocarParametros(
                    ps,
                    idAsociacion,
                    anio,
                    trimestre
            );

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    resumen.setTotalDonaciones(
                            rs.getInt("total_donaciones")
                    );

                    resumen.setPrendasRecibidas(
                            rs.getInt("prendas_recibidas")
                    );

                    resumen.setDonadoresParticipantes(
                            rs.getInt("donadores")
                    );

                    resumen.setPromedioPrendasPorDonacion(
                            rs.getDouble("promedio")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al obtener totales de donaciones: "
                    + e.getMessage()
            );
        }
    }

    private String obtenerPrendaMasDonada(
            Integer idAsociacion,
            Integer anio,
            Integer trimestre
    ) {
        StringBuilder sql = new StringBuilder(
                "SELECT p.tipo_prenda, "
                + "p.estado_prenda, "
                + "SUM(dd.cantidad) AS total "
                + "FROM Donacion_Ingresos di "
                + "INNER JOIN Donacion_Detalle dd "
                + "ON di.id_donacion_ingreso = "
                + "dd.id_donacion_ingreso "
                + "INNER JOIN Prendas p "
                + "ON dd.id_prenda = p.id_prenda "
                + "WHERE 1 = 1 "
        );

        agregarFiltros(
                sql,
                idAsociacion,
                anio,
                trimestre
        );

        sql.append(
                "GROUP BY p.id_prenda, "
                + "p.tipo_prenda, p.estado_prenda "
                + "ORDER BY total DESC "
                + "LIMIT 1"
        );

        try (
            Connection con = ConexionBD.conectar();
            PreparedStatement ps =
                    con.prepareStatement(sql.toString())
        ) {
            colocarParametros(
                    ps,
                    idAsociacion,
                    anio,
                    trimestre
            );

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("tipo_prenda")
                            + " - "
                            + rs.getString("estado_prenda")
                            + " ("
                            + rs.getInt("total")
                            + ")";
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al obtener prenda más donada: "
                    + e.getMessage()
            );
        }

        return "Sin datos";
    }

    private String obtenerEstadoMasFrecuente(
            Integer idAsociacion,
            Integer anio,
            Integer trimestre
    ) {
        StringBuilder sql = new StringBuilder(
                "SELECT p.estado_prenda, "
                + "SUM(dd.cantidad) AS total "
                + "FROM Donacion_Ingresos di "
                + "INNER JOIN Donacion_Detalle dd "
                + "ON di.id_donacion_ingreso = "
                + "dd.id_donacion_ingreso "
                + "INNER JOIN Prendas p "
                + "ON dd.id_prenda = p.id_prenda "
                + "WHERE 1 = 1 "
        );

        agregarFiltros(
                sql,
                idAsociacion,
                anio,
                trimestre
        );

        sql.append(
                "GROUP BY p.estado_prenda "
                + "ORDER BY total DESC "
                + "LIMIT 1"
        );

        try (
            Connection con = ConexionBD.conectar();
            PreparedStatement ps =
                    con.prepareStatement(sql.toString())
        ) {
            colocarParametros(
                    ps,
                    idAsociacion,
                    anio,
                    trimestre
            );

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("estado_prenda")
                            + " ("
                            + rs.getInt("total")
                            + ")";
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al obtener estado predominante: "
                    + e.getMessage()
            );
        }

        return "Sin datos";
    }

    private String obtenerDonadorPrincipal(
            Integer idAsociacion,
            Integer anio,
            Integer trimestre
    ) {
        StringBuilder sql = new StringBuilder(
                "SELECT u.usuario, "
                + "SUM(dd.cantidad) AS total "
                + "FROM Donacion_Ingresos di "
                + "INNER JOIN Usuarios u "
                + "ON di.id_donador = u.id_usuario "
                + "INNER JOIN Donacion_Detalle dd "
                + "ON di.id_donacion_ingreso = "
                + "dd.id_donacion_ingreso "
                + "WHERE 1 = 1 "
        );

        agregarFiltros(
                sql,
                idAsociacion,
                anio,
                trimestre
        );

        sql.append(
                "GROUP BY u.id_usuario, u.usuario "
                + "ORDER BY total DESC "
                + "LIMIT 1"
        );

        try (
            Connection con = ConexionBD.conectar();
            PreparedStatement ps =
                    con.prepareStatement(sql.toString())
        ) {
            colocarParametros(
                    ps,
                    idAsociacion,
                    anio,
                    trimestre
            );

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("usuario")
                            + " ("
                            + rs.getInt("total")
                            + ")";
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al obtener donador principal: "
                    + e.getMessage()
            );
        }

        return "Sin datos";
    }

    private String obtenerInstitucionPrincipal(
            Integer idAsociacion,
            Integer anio,
            Integer trimestre
    ) {
        StringBuilder sql = new StringBuilder(
                "SELECT a.nombre, "
                + "SUM(dd.cantidad) AS total "
                + "FROM Donacion_Ingresos di "
                + "INNER JOIN Asociaciones a "
                + "ON di.id_asociacion = a.id_asociacion "
                + "INNER JOIN Donacion_Detalle dd "
                + "ON di.id_donacion_ingreso = "
                + "dd.id_donacion_ingreso "
                + "WHERE 1 = 1 "
        );

        agregarFiltros(
                sql,
                idAsociacion,
                anio,
                trimestre
        );

        sql.append(
                "GROUP BY a.id_asociacion, a.nombre "
                + "ORDER BY total DESC "
                + "LIMIT 1"
        );

        try (
            Connection con = ConexionBD.conectar();
            PreparedStatement ps =
                    con.prepareStatement(sql.toString())
        ) {
            colocarParametros(
                    ps,
                    idAsociacion,
                    anio,
                    trimestre
            );

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nombre")
                            + " ("
                            + rs.getInt("total")
                            + ")";
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al obtener institución principal: "
                    + e.getMessage()
            );
        }

        return "Sin datos";
    }

    /**
     * Obtiene los años en los que existen donaciones.
     */
    public List<Integer> obtenerAniosDisponibles(
            Integer idAsociacion
    ) {
        List<Integer> anios = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT YEAR(fecha_donacion) AS anio "
                + "FROM Donacion_Ingresos "
                + "WHERE fecha_donacion IS NOT NULL "
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
                    "Error al obtener años de donaciones: "
                    + e.getMessage()
            );
        }

        return anios;
    }

    private void agregarFiltros(
            StringBuilder sql,
            Integer idAsociacion,
            Integer anio,
            Integer trimestre
    ) {
        if (idAsociacion != null) {
            sql.append("AND di.id_asociacion = ? ");
        }

        if (anio != null) {
            sql.append("AND YEAR(di.fecha_donacion) = ? ");
        }

        if (trimestre != null) {
            sql.append("AND QUARTER(di.fecha_donacion) = ? ");
        }
    }

    private void colocarParametros(
            PreparedStatement ps,
            Integer idAsociacion,
            Integer anio,
            Integer trimestre
    ) throws SQLException {
        int parametro = 1;

        if (idAsociacion != null) {
            ps.setInt(parametro++, idAsociacion);
        }

        if (anio != null) {
            ps.setInt(parametro++, anio);
        }

        if (trimestre != null) {
            ps.setInt(parametro, trimestre);
        }
    }
}