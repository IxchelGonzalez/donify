package Vista;

import DAO.ReporteGeneralDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GeneracionReportes extends JPanel {

    private ReporteGeneralDAO reporteGeneralDAO;

    private JLabel lblTitulo;
    private JLabel lblDescripcion;

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
        reporteGeneralDAO = new ReporteGeneralDAO();
        configurarPanel();
        crearComponentes();
        agregarComponentes();
        configurarEventos();
        cargarReportes();
    }

    private void configurarPanel() {
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
    }

    private void crearComponentes() {
        lblTitulo = new JLabel("Generacion de reportes", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(35, 75, 120));

        lblDescripcion = new JLabel("Resumen general de donaciones, entregas e inventario.", SwingConstants.CENTER);
        lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblDescripcion.setForeground(new Color(90, 90, 90));

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
        btnActualizar.setPreferredSize(new Dimension(190, 38));
        btnActualizar.setBackground(new Color(40, 120, 210));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setBorderPainted(false);
        btnActualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void agregarComponentes() {
        JPanel panelEncabezado = new JPanel(new BorderLayout(0, 5));
        panelEncabezado.setBackground(new Color(245, 247, 250));
        panelEncabezado.add(lblTitulo, BorderLayout.CENTER);
        panelEncabezado.add(lblDescripcion, BorderLayout.SOUTH);

        JPanel panelReportes = new JPanel(new GridLayout(4, 2, 16, 16));
        panelReportes.setBackground(new Color(245, 247, 250));

        panelReportes.add(crearTarjeta("Total de donaciones", lblTotalDonaciones));
        panelReportes.add(crearTarjeta("Prendas recibidas", lblTotalPrendasRecibidas));
        panelReportes.add(crearTarjeta("Total de entregas", lblTotalEntregas));
        panelReportes.add(crearTarjeta("Prendas entregadas", lblTotalPrendasEntregadas));
        panelReportes.add(crearTarjeta("Beneficiarios registrados", lblTotalBeneficiarios));
        panelReportes.add(crearTarjeta("Stock disponible", lblStockDisponible));
        panelReportes.add(crearTarjeta("Prenda con mayor stock", lblPrendaMayorStock));
        panelReportes.add(crearTarjeta("Beneficiario con mas entregas", lblBeneficiarioMasEntregas));

        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(245, 247, 250));
        panelBoton.add(btnActualizar);

        add(panelEncabezado, BorderLayout.NORTH);
        add(panelReportes, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        btnActualizar.addActionListener(e -> cargarReportes());
    }

    private void cargarReportes() {
        lblTotalDonaciones.setText(String.valueOf(reporteGeneralDAO.obtenerTotalDonaciones()));
        lblTotalPrendasRecibidas.setText(String.valueOf(reporteGeneralDAO.obtenerTotalPrendasRecibidas()));
        lblTotalEntregas.setText(String.valueOf(reporteGeneralDAO.obtenerTotalEntregas()));
        lblTotalPrendasEntregadas.setText(String.valueOf(reporteGeneralDAO.obtenerTotalPrendasEntregadas()));
        lblTotalBeneficiarios.setText(String.valueOf(reporteGeneralDAO.obtenerTotalBeneficiarios()));
        lblStockDisponible.setText(String.valueOf(reporteGeneralDAO.obtenerStockTotalDisponible()));
        lblPrendaMayorStock.setText(reporteGeneralDAO.obtenerPrendaMayorStock());
        lblBeneficiarioMasEntregas.setText(reporteGeneralDAO.obtenerBeneficiarioMasEntregas());
    }

    private JPanel crearTarjeta(String titulo, JLabel valor) {
        JPanel tarjeta = new JPanel(new BorderLayout(0, 10));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        JLabel lblTituloTarjeta = new JLabel(titulo, SwingConstants.CENTER);
        lblTituloTarjeta.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTituloTarjeta.setForeground(new Color(60, 60, 60));

        tarjeta.add(lblTituloTarjeta, BorderLayout.NORTH);
        tarjeta.add(valor, BorderLayout.CENTER);

        return tarjeta;
    }

    private JLabel crearEtiquetaValor() {
        JLabel etiqueta = new JLabel("0", SwingConstants.CENTER);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 22));
        etiqueta.setForeground(new Color(35, 75, 120));
        return etiqueta;
    }
}
