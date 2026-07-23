package Vista;

import DAO.AsociacionDAO;
import DAO.InventarioDAO;
import Modelo.Asociacion;
import Modelo.Prenda;
import Modelo.Usuario;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ConsultaInventario extends JPanel {

    private final InventarioDAO inventarioDAO;
    private final AsociacionDAO asociacionDAO;
    private final Usuario usuarioSesion;
    private static final Asociacion TODAS_LAS_ASOCIACIONES = new Asociacion(0, "Todas las asociaciones", "", false);

    private JLabel lblTitulo;
    private JLabel lblDescripcion;

    private JLabel lblAsociacion;
    private JComboBox<Asociacion> cmbAsociacion;
    private JComboBox<String> cmbTipoPrenda;
    private JTextField txtCantidadMinima;
    private JButton btnFiltrar;
    private JButton btnLimpiarFiltros;

    private JTable tablaInventario;
    private DefaultTableModel modeloTabla;
    private JButton btnActualizar;
    private JButton btnCerrar;

    public ConsultaInventario() {
        this(null);
    }

    public ConsultaInventario(Usuario usuarioSesion) {
        this.usuarioSesion = usuarioSesion;
        inventarioDAO = new InventarioDAO();
        asociacionDAO = new AsociacionDAO();
        configurarPanel();
        crearComponentes();
        agregarComponentes();
        configurarEventos();
        cargarComboTipos();
        if (!esInstitucion()) {
            cargarComboAsociaciones();
        }
        cargarInventario();
    }

    private boolean esInstitucion() {
        return usuarioSesion != null
                && "institucion".equalsIgnoreCase(usuarioSesion.getTipoUsuario())
                && usuarioSesion.tieneAsociacion();
    }

    private void configurarPanel() {
        setLayout(new BorderLayout(0, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
    }

    private void crearComponentes() {
        lblTitulo = new JLabel("Consulta de inventario", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(35, 75, 120));

        String descripcion = esInstitucion()
                ? "Stock disponible de prendas donadas a tu institucion."
                : "Consulta el stock disponible de prendas registradas.";
        lblDescripcion = new JLabel(descripcion, SwingConstants.CENTER);
        lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblDescripcion.setForeground(new Color(90, 90, 90));

        lblAsociacion = new JLabel("Institucion:");
        lblAsociacion.setFont(new Font("SansSerif", Font.PLAIN, 14));

        cmbAsociacion = new JComboBox<>();
        cmbAsociacion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cmbAsociacion.setPreferredSize(new Dimension(220, 32));

        cmbTipoPrenda = new JComboBox<>();
        cmbTipoPrenda.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cmbTipoPrenda.setPreferredSize(new Dimension(220, 32));

        txtCantidadMinima = new JTextField();
        txtCantidadMinima.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtCantidadMinima.setPreferredSize(new Dimension(120, 32));

        btnFiltrar = crearBoton("Filtrar", new Color(40, 120, 210), Color.WHITE);
        btnLimpiarFiltros = crearBoton("Limpiar filtros", new Color(220, 224, 230), new Color(50, 50, 50));

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

        btnActualizar = crearBoton("Actualizar inventario", new Color(80, 145, 100), Color.WHITE);
        btnCerrar = crearBoton("Cerrar", new Color(220, 224, 230), new Color(50, 50, 50));
    }

    private void agregarComponentes() {
        JPanel panelEncabezado = new JPanel(new BorderLayout(0, 5));
        panelEncabezado.setBackground(new Color(245, 247, 250));
        panelEncabezado.add(lblTitulo, BorderLayout.CENTER);
        panelEncabezado.add(lblDescripcion, BorderLayout.SOUTH);

        JPanel panelFiltros = new JPanel();
        panelFiltros.setLayout(new BoxLayout(panelFiltros, BoxLayout.Y_AXIS));
        panelFiltros.setBackground(new Color(245, 247, 250));
        panelFiltros.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel lblTipo = new JLabel("Tipo de ropa:");
        lblTipo.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JLabel lblCantidad = new JLabel("Cantidad minima:");
        lblCantidad.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Fila 1: campos de filtro (tipo de ropa y cantidad)
        JPanel filaCampos = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        filaCampos.setBackground(new Color(245, 247, 250));

        if (!esInstitucion()) {
            filaCampos.add(lblAsociacion);
            filaCampos.add(cmbAsociacion);
        }

        filaCampos.add(lblTipo);
        filaCampos.add(cmbTipoPrenda);
        filaCampos.add(lblCantidad);
        filaCampos.add(txtCantidadMinima);

        // Fila 2: botones de accion, separada de la fila de campos para que
        // el alto del panel se calcule correctamente y no se encimen con la tabla.
        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        filaBotones.setBackground(new Color(245, 247, 250));
        filaBotones.add(btnFiltrar);
        filaBotones.add(btnLimpiarFiltros);

        filaCampos.setAlignmentX(Component.CENTER_ALIGNMENT);
        filaBotones.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelFiltros.add(filaCampos);
        panelFiltros.add(filaBotones);

        JPanel panelNorte = new JPanel(new BorderLayout(0, 12));
        panelNorte.setBackground(new Color(245, 247, 250));
        panelNorte.add(panelEncabezado, BorderLayout.NORTH);
        panelNorte.add(panelFiltros, BorderLayout.SOUTH);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        panelBotones.setBackground(new Color(245, 247, 250));
        panelBotones.add(btnActualizar);
        panelBotones.add(btnCerrar);

        add(panelNorte, BorderLayout.NORTH);
        add(new JScrollPane(tablaInventario), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        cmbAsociacion.addActionListener(e -> cargarInventario());
        btnFiltrar.addActionListener(e -> cargarInventario());
        btnLimpiarFiltros.addActionListener(e -> {
            if (cmbAsociacion.getItemCount() > 0) {
                cmbAsociacion.setSelectedIndex(0);
            }
            cmbTipoPrenda.setSelectedIndex(0);
            txtCantidadMinima.setText("");
            cargarInventario();
        });
        btnActualizar.addActionListener(e -> {
            if (!esInstitucion()) {
                cargarComboAsociaciones();
            }
            cargarComboTipos();
            cargarInventario();
        });
        btnCerrar.addActionListener(e -> setVisible(false));
    }

    private void cargarComboAsociaciones() {
        Asociacion seleccionada = (Asociacion) cmbAsociacion.getSelectedItem();
        int idSeleccionado = seleccionada != null ? seleccionada.getIdAsociacion() : 0;

        cmbAsociacion.removeAllItems();
        cmbAsociacion.addItem(TODAS_LAS_ASOCIACIONES);

        for (Asociacion asociacion : asociacionDAO.obtenerAsociaciones()) {
            cmbAsociacion.addItem(asociacion);
        }

        // Se vuelve a seleccionar por ID (no por referencia de objeto) para evitar que
        // el combo quede apuntando a una instancia obsoleta tras recargar la lista.
        for (int i = 0; i < cmbAsociacion.getItemCount(); i++) {
            if (cmbAsociacion.getItemAt(i).getIdAsociacion() == idSeleccionado) {
                cmbAsociacion.setSelectedIndex(i);
                break;
            }
        }
    }

    private void cargarComboTipos() {
        Object seleccionado = cmbTipoPrenda.getSelectedItem();
        cmbTipoPrenda.removeAllItems();
        cmbTipoPrenda.addItem("Todos");

        List<String> tipos = inventarioDAO.obtenerTiposPrenda();
        for (String tipo : tipos) {
            cmbTipoPrenda.addItem(tipo);
        }

        if (seleccionado != null) {
            cmbTipoPrenda.setSelectedItem(seleccionado);
        }
    }

    private void cargarInventario() {
        modeloTabla.setRowCount(0);

        String tipoSeleccionado = (String) cmbTipoPrenda.getSelectedItem();
        String tipoFiltro = (tipoSeleccionado == null || tipoSeleccionado.equals("Todos")) ? null : tipoSeleccionado;

        Integer cantidadMinima = null;
        String cantidadTexto = txtCantidadMinima.getText().trim();

        if (!cantidadTexto.isEmpty()) {
            try {
                cantidadMinima = Integer.parseInt(cantidadTexto);
            } catch (NumberFormatException e) {
                mostrarAdvertencia("La cantidad minima debe ser un numero.");
                return;
            }
        }

        List<Prenda> prendas;

        if (esInstitucion()) {
            prendas = inventarioDAO.obtenerInventarioPorAsociacion(usuarioSesion.getIdAsociacion(), tipoFiltro, cantidadMinima);
        } else {
            Asociacion asociacionSeleccionada = (Asociacion) cmbAsociacion.getSelectedItem();

            if (asociacionSeleccionada != null && asociacionSeleccionada.getIdAsociacion() > 0) {
                prendas = inventarioDAO.obtenerInventarioPorAsociacion(
                        asociacionSeleccionada.getIdAsociacion(), tipoFiltro, cantidadMinima);
            } else {
                prendas = inventarioDAO.obtenerInventario(tipoFiltro, cantidadMinima);
            }
        }

        for (Prenda prenda : prendas) {
            modeloTabla.addRow(new Object[]{
                prenda.getIdPrenda(),
                prenda.getTipoPrenda(),
                prenda.getEstadoPrenda(),
                prenda.getStock()
            });
        }
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private JButton crearBoton(String texto, Color fondo, Color letra) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setPreferredSize(new Dimension(160, 34));
        boton.setBackground(fondo);
        boton.setForeground(letra);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
}
