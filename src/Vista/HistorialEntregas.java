package Vista;

import DAO.HistorialEntregasDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class HistorialEntregas extends JPanel {

    private HistorialEntregasDAO historialEntregasDAO;

    private JLabel lblTitulo;
    private JLabel lblDescripcion;
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;
    private JButton btnActualizar;
    private JButton btnCerrar;

    public HistorialEntregas() {
        historialEntregasDAO = new HistorialEntregasDAO();
        configurarPanel();
        crearComponentes();
        agregarComponentes();
        configurarEventos();
        cargarHistorial();
    }

    private void configurarPanel() {
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
    }

    private void crearComponentes() {
        lblTitulo = new JLabel("Historial de entregas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(35, 75, 120));

        lblDescripcion = new JLabel("Consulta las entregas realizadas a beneficiarios.", SwingConstants.CENTER);
        lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblDescripcion.setForeground(new Color(90, 90, 90));

        modeloTabla = new DefaultTableModel();
        tablaHistorial = new JTable(modeloTabla);
        tablaHistorial.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaHistorial.setRowHeight(28);
        tablaHistorial.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tablaHistorial.setSelectionBackground(new Color(210, 225, 245));

        btnActualizar = crearBoton("Actualizar historial", new Color(40, 120, 210), Color.WHITE);
        btnCerrar = crearBoton("Cerrar", new Color(220, 224, 230), new Color(50, 50, 50));
    }

    private void agregarComponentes() {
        JPanel panelEncabezado = new JPanel(new BorderLayout(0, 5));
        panelEncabezado.setBackground(new Color(245, 247, 250));
        panelEncabezado.add(lblTitulo, BorderLayout.CENTER);
        panelEncabezado.add(lblDescripcion, BorderLayout.SOUTH);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        panelBotones.setBackground(new Color(245, 247, 250));
        panelBotones.add(btnActualizar);
        panelBotones.add(btnCerrar);

        add(panelEncabezado, BorderLayout.NORTH);
        add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        btnActualizar.addActionListener(e -> cargarHistorial());
        btnCerrar.addActionListener(e -> setVisible(false));
    }

    private void cargarHistorial() {
        modeloTabla = historialEntregasDAO.obtenerHistorialEntregas();
        tablaHistorial.setModel(modeloTabla);
    }

    private JButton crearBoton(String texto, Color fondo, Color letra) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setPreferredSize(new Dimension(190, 38));
        boton.setBackground(fondo);
        boton.setForeground(letra);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
}
