package Vista;

import DAO.AsociacionDAO;
import DAO.ReporteGeneralDAO;
import Modelo.Asociacion;
import Modelo.Usuario;
import java.awt.*;
import javax.swing.*;

public class GeneracionReportes extends JPanel {

    private final ReporteGeneralDAO reporteGeneralDAO;
    private final AsociacionDAO asociacionDAO;
    private final Usuario usuarioSesion;

    private JLabel lblTitulo;
    private JLabel lblDescripcion;
    private JLabel lblAlcance;

    private JLabel lblTotalDonaciones;
    private JLabel lblTotalPrendasRecibidas;
    private JLabel lblTotalEntregas;
    private JLabel lblTotalPrendasEntregadas;
    private JLabel lblTotalBeneficiarios;
    private JLabel lblStockDisponible;
    private JLabel lblPrendaMayorStock;
    private JLabel lblBeneficiarioMasEntregas;

    private JButton btnActualizar;

    public GeneracionReportes() {
        this(null);
    }

    public GeneracionReportes(Usuario usuarioSesion) {
        this.usuarioSesion = usuarioSesion;
        reporteGeneralDAO = new ReporteGeneralDAO();
        asociacionDAO = new AsociacionDAO();
        configurarPanel();
        crearComponentes();
        agregarComponentes();
        configurarEventos();
        cargarReportes();
    }

    private boolean esInstitucion() {
        return usuarioSesion != null
                && "institucion".equalsIgnoreCase(usuarioSesion.getTipoUsuario())
                && usuarioSesion.tieneAsociacion();
    }

    private Integer idAsociacionSesion() {
        return esInstitucion() ? usuarioSesion.getIdAsociacion() : null;
    }

    private void configurarPanel() {
        setLayout(new BorderLayout(0, 18));
        setBackground(new Color(240, 243, 248));
        setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
    }

    private void crearComponentes() {
        lblTitulo = new JLabel("Generacion de reportes", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 30));
        lblTitulo.setForeground(new Color(30, 65, 110));

        lblDescripcion = new JLabel("Resumen general de donaciones, entregas e inventario.", SwingConstants.CENTER);
        lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblDescripcion.setForeground(new Color(100, 100, 100));

        String textoAlcance = esInstitucion()
                ? "Mostrando el reporte de tu institucion: " + obtenerNombreInstitucion()
                : "Mostrando el reporte global de todo el programa";

        lblAlcance = new JLabel(textoAlcance, SwingConstants.CENTER);
        lblAlcance.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblAlcance.setForeground(new Color(40, 120, 210));
        lblAlcance.setOpaque(true);
        lblAlcance.setBackground(new Color(225, 236, 250));
        lblAlcance.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        lblTotalDonaciones = crearEtiquetaValor();
        lblTotalPrendasRecibidas = crearEtiquetaValor();
        lblTotalEntregas = crearEtiquetaValor();
        lblTotalPrendasEntregadas = crearEtiquetaValor();
        lblTotalBeneficiarios = crearEtiquetaValor();
        lblStockDisponible = crearEtiquetaValor();
        lblPrendaMayorStock = crearEtiquetaValor();
        lblBeneficiarioMasEntregas = crearEtiquetaValor();

        btnActualizar = new JButton("Actualizar reportes");
        btnActualizar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnActualizar.setPreferredSize(new Dimension(200, 40));
        btnActualizar.setBackground(new Color(40, 120, 210));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setBorderPainted(false);
        btnActualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private String obtenerNombreInstitucion() {
        for (Asociacion asociacion : asociacionDAO.obtenerAsociaciones()) {
            if (asociacion.getIdAsociacion() == usuarioSesion.getIdAsociacion()) {
                return asociacion.getNombre();
            }
        }
        return "Institucion";
    }

    private void agregarComponentes() {
        JPanel panelEncabezado = new JPanel(new BorderLayout(0, 6));
        panelEncabezado.setBackground(new Color(240, 243, 248));
        panelEncabezado.add(lblTitulo, BorderLayout.NORTH);
        panelEncabezado.add(lblDescripcion, BorderLayout.CENTER);

        JPanel panelAlcance = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelAlcance.setBackground(new Color(240, 243, 248));
        panelAlcance.add(lblAlcance);
        panelEncabezado.add(panelAlcance, BorderLayout.SOUTH);

        JPanel panelReportes = new JPanel(new GridLayout(2, 4, 14, 14));
        panelReportes.setBackground(new Color(240, 243, 248));

        panelReportes.add(crearTarjeta("Total de donaciones", lblTotalDonaciones, new Color(40, 120, 210)));
        panelReportes.add(crearTarjeta("Prendas recibidas", lblTotalPrendasRecibidas, new Color(80, 145, 100)));
        panelReportes.add(crearTarjeta("Total de entregas", lblTotalEntregas, new Color(230, 150, 55)));
        panelReportes.add(crearTarjeta("Prendas entregadas", lblTotalPrendasEntregadas, new Color(200, 70, 70)));
        panelReportes.add(crearTarjeta("Beneficiarios registrados", lblTotalBeneficiarios, new Color(120, 90, 190)));
        panelReportes.add(crearTarjeta("Stock disponible", lblStockDisponible, new Color(35, 150, 160)));
        panelReportes.add(crearTarjeta("Prenda con mayor stock", lblPrendaMayorStock, new Color(180, 130, 40)));
        panelReportes.add(crearTarjeta("Beneficiario con mas entregas", lblBeneficiarioMasEntregas, new Color(90, 100, 190)));

        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(240, 243, 248));
        panelBoton.add(btnActualizar);

        add(panelEncabezado, BorderLayout.NORTH);
        add(panelReportes, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        btnActualizar.addActionListener(e -> cargarReportes());
    }

    private void cargarReportes() {
        Integer idAsociacion = idAsociacionSesion();

        establecerValor(lblTotalDonaciones, String.valueOf(reporteGeneralDAO.obtenerTotalDonaciones(idAsociacion)));
        establecerValor(lblTotalPrendasRecibidas, String.valueOf(reporteGeneralDAO.obtenerTotalPrendasRecibidas(idAsociacion)));
        establecerValor(lblTotalEntregas, String.valueOf(reporteGeneralDAO.obtenerTotalEntregas(idAsociacion)));
        establecerValor(lblTotalPrendasEntregadas, String.valueOf(reporteGeneralDAO.obtenerTotalPrendasEntregadas(idAsociacion)));
        establecerValor(lblTotalBeneficiarios, String.valueOf(reporteGeneralDAO.obtenerTotalBeneficiarios()));
        establecerValor(lblStockDisponible, String.valueOf(reporteGeneralDAO.obtenerStockTotalDisponible(idAsociacion)));
        establecerValor(lblPrendaMayorStock, reporteGeneralDAO.obtenerPrendaMayorStock(idAsociacion));
        establecerValor(lblBeneficiarioMasEntregas, reporteGeneralDAO.obtenerBeneficiarioMasEntregas(idAsociacion));
    }

    private JPanel crearTarjeta(String titulo, JLabel valor, Color colorAcento) {
        JPanel tarjeta = new JPanel(new BorderLayout(0, 6));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 6, 0, 0, colorAcento),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));

        JLabel lblTituloTarjeta = new JLabel(
                "<html><div style='text-align:center;'>" + titulo + "</div></html>", SwingConstants.CENTER);
        lblTituloTarjeta.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTituloTarjeta.setForeground(new Color(70, 70, 70));

        valor.setForeground(colorAcento);

        tarjeta.add(lblTituloTarjeta, BorderLayout.NORTH);
        tarjeta.add(valor, BorderLayout.CENTER);

        return tarjeta;
    }

    private JLabel crearEtiquetaValor() {
        JLabel etiqueta = new JLabel("0", SwingConstants.CENTER);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 20));
        etiqueta.setForeground(new Color(35, 75, 120));
        return etiqueta;
    }

    private void establecerValor(JLabel etiqueta, String valor) {
        etiqueta.setText("<html><div style='text-align:center;'>" + valor + "</div></html>");
    }
}
