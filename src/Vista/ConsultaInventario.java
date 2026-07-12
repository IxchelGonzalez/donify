package Vista;

import DAO.InventarioDAO;
import Modelo.Prenda;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class ConsultaInventario extends JPanel {

    private InventarioDAO inventarioDAO;

    private JLabel lblTitulo;
    private JLabel lblDescripcion;
    private JTable tablaInventario;
    private DefaultTableModel modeloTabla;
    private JButton btnActualizar;
    private JButton btnCerrar;

    public ConsultaInventario() {
        inventarioDAO = new InventarioDAO();
        configurarPanel();
        crearComponentes();
        agregarComponentes();
        configurarEventos();
        cargarInventario();
    }

    private void configurarPanel() {
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
    }

    private void crearComponentes() {
        lblTitulo = new JLabel("Consulta de inventario", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(35, 75, 120));

        lblDescripcion = new JLabel("Consulta el stock disponible de prendas registradas.", SwingConstants.CENTER);
        lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblDescripcion.setForeground(new Color(90, 90, 90));

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Tipo de prenda", "Estado", "Stock"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columna) {
                return columna == 0 || columna == 3 ? Integer.class : String.class;
            }
        };

        tablaInventario = new JTable(modeloTabla);
        tablaInventario.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaInventario.setRowHeight(28);
        tablaInventario.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tablaInventario.setSelectionBackground(new Color(210, 225, 245));

        btnActualizar = crearBoton("Actualizar inventario", new Color(40, 120, 210), Color.WHITE);
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
        add(new JScrollPane(tablaInventario), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        btnActualizar.addActionListener(e -> cargarInventario());
        btnCerrar.addActionListener(e -> setVisible(false));
    }

    private void cargarInventario() {
        modeloTabla.setRowCount(0);

        List<Prenda> prendas = inventarioDAO.obtenerInventario();

        for (Prenda prenda : prendas) {
            modeloTabla.addRow(new Object[]{
                prenda.getIdPrenda(),
                prenda.getTipoPrenda(),
                prenda.getEstadoPrenda(),
                prenda.getStock()
            });
        }
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