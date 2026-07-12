package Vista;

import DAO.BeneficiarioDAO;
import Modelo.Beneficiario;
import java.awt.*;
import java.sql.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GestionBeneficiarios extends JPanel {

    private static final String VISTA_MENU = "menu";
    private static final String VISTA_CREAR = "crear";
    private static final String VISTA_CONSULTAR = "consultar";
    private static final String VISTA_ACTUALIZAR = "actualizar";
    private static final String VISTA_ELIMINAR = "eliminar";

    private final BeneficiarioDAO beneficiarioDAO;
    private CardLayout cardLayout;
    private JPanel panelContenido;

    private JButton btnMenuCrear, btnMenuConsultar, btnMenuActualizar, btnMenuEliminar;

    private JTextField txtCrearNombre, txtCrearFecha;
    private JComboBox<String> cmbCrearSexo;
    private JButton btnCrearBeneficiario, btnCerrarCrear;

    private JTable tablaConsultar, tablaActualizar, tablaEliminar;
    private DefaultTableModel modeloConsultar, modeloActualizar, modeloEliminar;

    private JButton btnRefrescarConsultar, btnCerrarConsultar;

    private JTextField txtActualizarId, txtActualizarNombre, txtActualizarFecha;
    private JComboBox<String> cmbActualizarSexo;
    private JButton btnActualizarBeneficiario, btnCerrarActualizar;

    private JButton btnEliminarBeneficiario, btnCerrarEliminar;

    public GestionBeneficiarios() {
        beneficiarioDAO = new BeneficiarioDAO();
        configurarPanel();
        crearComponentes();
        agregarComponentes();
        configurarEventos();
    }

    private void configurarPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
    }

    private void crearComponentes() {
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        panelContenido.setBackground(new Color(245, 247, 250));
    }

    private void agregarComponentes() {
        panelContenido.add(crearPanelMenu(), VISTA_MENU);
        panelContenido.add(crearPanelCrear(), VISTA_CREAR);
        panelContenido.add(crearPanelConsultar(), VISTA_CONSULTAR);
        panelContenido.add(crearPanelActualizar(), VISTA_ACTUALIZAR);
        panelContenido.add(crearPanelEliminar(), VISTA_ELIMINAR);
        add(panelContenido, BorderLayout.CENTER);
    }

    private void configurarEventos() {
        btnMenuCrear.addActionListener(e -> mostrarVista(VISTA_CREAR));
        btnMenuConsultar.addActionListener(e -> mostrarVista(VISTA_CONSULTAR));
        btnMenuActualizar.addActionListener(e -> mostrarVista(VISTA_ACTUALIZAR));
        btnMenuEliminar.addActionListener(e -> mostrarVista(VISTA_ELIMINAR));

        btnCrearBeneficiario.addActionListener(e -> crearBeneficiario());
        btnCerrarCrear.addActionListener(e -> {
            limpiarFormularioCrear();
            mostrarVista(VISTA_MENU);
        });

        btnRefrescarConsultar.addActionListener(e -> cargarTabla(modeloConsultar));
        btnCerrarConsultar.addActionListener(e -> mostrarVista(VISTA_MENU));

        tablaActualizar.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosBeneficiarioSeleccionado();
            }
        });

        btnActualizarBeneficiario.addActionListener(e -> actualizarBeneficiario());
        btnCerrarActualizar.addActionListener(e -> {
            limpiarFormularioActualizar();
            mostrarVista(VISTA_MENU);
        });

        btnEliminarBeneficiario.addActionListener(e -> eliminarBeneficiario());
        btnCerrarEliminar.addActionListener(e -> mostrarVista(VISTA_MENU));
    }

    private JPanel crearPanelMenu() {
        JPanel panel = crearPanelBase();

        panel.add(crearEncabezado(
                "Gestion de beneficiarios",
                "Selecciona la accion que deseas realizar."
        ), BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 18, 18));
        panelBotones.setBackground(new Color(245, 247, 250));

        btnMenuCrear = crearBotonMenu("Crear beneficiario", new Color(40, 120, 210), Color.WHITE);
        btnMenuConsultar = crearBotonMenu("Consultar beneficiarios", new Color(80, 145, 100), Color.WHITE);
        btnMenuActualizar = crearBotonMenu("Actualizar beneficiario", new Color(230, 150, 55), Color.WHITE);
        btnMenuEliminar = crearBotonMenu("Eliminar beneficiario", new Color(200, 70, 70), Color.WHITE);

        panelBotones.add(btnMenuCrear);
        panelBotones.add(btnMenuConsultar);
        panelBotones.add(btnMenuActualizar);
        panelBotones.add(btnMenuEliminar);

        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setBackground(new Color(245, 247, 250));
        contenedor.add(panelBotones);

        panel.add(contenedor, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelCrear() {
        JPanel panel = crearPanelBase();

        panel.add(crearEncabezado(
                "Crear beneficiario",
                "Registra la informacion del beneficiario."
        ), BorderLayout.NORTH);

        txtCrearNombre = crearCampoTexto();
        cmbCrearSexo = crearComboSexo();
        txtCrearFecha = crearCampoTexto();

        JPanel formulario = crearFormulario();
        agregarFilaFormulario(formulario, 0, "Nombre", txtCrearNombre);
        agregarFilaFormulario(formulario, 1, "Sexo", cmbCrearSexo);
        agregarFilaFormulario(formulario, 2, "Fecha ultima recepcion (AAAA-MM-DD)", txtCrearFecha);

        btnCrearBeneficiario = crearBoton("Crear beneficiario", new Color(40, 120, 210), Color.WHITE);
        btnCerrarCrear = crearBoton("Cerrar", new Color(220, 224, 230), new Color(50, 50, 50));

        panel.add(formulario, BorderLayout.CENTER);
        panel.add(crearPanelBotones(btnCrearBeneficiario, btnCerrarCrear), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelConsultar() {
        JPanel panel = crearPanelBase();

        panel.add(crearEncabezado(
                "Consultar beneficiarios",
                "Lista de beneficiarios registrados."
        ), BorderLayout.NORTH);

        modeloConsultar = crearModeloTabla();
        tablaConsultar = new JTable(modeloConsultar);
        configurarTabla(tablaConsultar);

        btnRefrescarConsultar = crearBoton("Actualizar lista", new Color(40, 120, 210), Color.WHITE);
        btnCerrarConsultar = crearBoton("Cerrar", new Color(220, 224, 230), new Color(50, 50, 50));

        panel.add(new JScrollPane(tablaConsultar), BorderLayout.CENTER);
        panel.add(crearPanelBotones(btnRefrescarConsultar, btnCerrarConsultar), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelActualizar() {
        JPanel panel = crearPanelBase();

        panel.add(crearEncabezado(
                "Actualizar beneficiario",
                "Selecciona un beneficiario y modifica sus datos."
        ), BorderLayout.NORTH);

        modeloActualizar = crearModeloTabla();
        tablaActualizar = new JTable(modeloActualizar);
        configurarTabla(tablaActualizar);

        txtActualizarId = crearCampoTexto();
        txtActualizarId.setEditable(false);
        txtActualizarNombre = crearCampoTexto();
        cmbActualizarSexo = crearComboSexo();
        txtActualizarFecha = crearCampoTexto();

        JPanel formulario = crearFormulario();
        agregarFilaFormulario(formulario, 0, "ID", txtActualizarId);
        agregarFilaFormulario(formulario, 1, "Nombre", txtActualizarNombre);
        agregarFilaFormulario(formulario, 2, "Sexo", cmbActualizarSexo);
        agregarFilaFormulario(formulario, 3, "Fecha ultima recepcion (AAAA-MM-DD)", txtActualizarFecha);

        JPanel centro = new JPanel(new BorderLayout(0, 15));
        centro.setBackground(new Color(245, 247, 250));
        centro.add(new JScrollPane(tablaActualizar), BorderLayout.CENTER);
        centro.add(formulario, BorderLayout.SOUTH);

        btnActualizarBeneficiario = crearBoton("Actualizar beneficiario", new Color(230, 150, 55), Color.WHITE);
        btnCerrarActualizar = crearBoton("Cerrar", new Color(220, 224, 230), new Color(50, 50, 50));

        panel.add(centro, BorderLayout.CENTER);
        panel.add(crearPanelBotones(btnActualizarBeneficiario, btnCerrarActualizar), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelEliminar() {
        JPanel panel = crearPanelBase();

        panel.add(crearEncabezado(
                "Eliminar beneficiario",
                "Selecciona un beneficiario y confirma la eliminacion."
        ), BorderLayout.NORTH);

        modeloEliminar = crearModeloTabla();
        tablaEliminar = new JTable(modeloEliminar);
        configurarTabla(tablaEliminar);

        btnEliminarBeneficiario = crearBoton("Eliminar beneficiario", new Color(200, 70, 70), Color.WHITE);
        btnCerrarEliminar = crearBoton("Cerrar", new Color(220, 224, 230), new Color(50, 50, 50));

        panel.add(new JScrollPane(tablaEliminar), BorderLayout.CENTER);
        panel.add(crearPanelBotones(btnEliminarBeneficiario, btnCerrarEliminar), BorderLayout.SOUTH);

        return panel;
    }

    private void crearBeneficiario() {
        String nombre = txtCrearNombre.getText().trim();
        String sexo = cmbCrearSexo.getSelectedItem().toString();
        String fechaTexto = txtCrearFecha.getText().trim();

        if (!validarCampos(nombre, fechaTexto)) {
            return;
        }

        Date fecha = fechaTexto.isEmpty() ? null : Date.valueOf(fechaTexto);
        Beneficiario beneficiario = new Beneficiario(nombre, sexo, fecha);

        if (beneficiarioDAO.crearBeneficiario(beneficiario)) {
            JOptionPane.showMessageDialog(this, "Beneficiario creado correctamente.");
            limpiarFormularioCrear();
        } else {
            mostrarError("No se pudo crear el beneficiario.");
        }
    }

    private void actualizarBeneficiario() {
        if (txtActualizarId.getText().trim().isEmpty()) {
            mostrarAdvertencia("Selecciona un beneficiario de la tabla.");
            return;
        }

        int idBeneficiario = Integer.parseInt(txtActualizarId.getText().trim());
        String nombre = txtActualizarNombre.getText().trim();
        String sexo = cmbActualizarSexo.getSelectedItem().toString();
        String fechaTexto = txtActualizarFecha.getText().trim();

        if (!validarCampos(nombre, fechaTexto)) {
            return;
        }

        Date fecha = fechaTexto.isEmpty() ? null : Date.valueOf(fechaTexto);
        Beneficiario beneficiario = new Beneficiario(idBeneficiario, nombre, sexo, fecha);

        if (beneficiarioDAO.actualizarBeneficiario(beneficiario)) {
            JOptionPane.showMessageDialog(this, "Beneficiario actualizado correctamente.");
            limpiarFormularioActualizar();
            cargarTabla(modeloActualizar);
        } else {
            mostrarError("No se pudo actualizar el beneficiario.");
        }
    }

    private void eliminarBeneficiario() {
        int fila = tablaEliminar.getSelectedRow();

        if (fila == -1) {
            mostrarAdvertencia("Selecciona un beneficiario de la tabla.");
            return;
        }

        int filaModelo = tablaEliminar.convertRowIndexToModel(fila);
        int idBeneficiario = (int) modeloEliminar.getValueAt(filaModelo, 0);
        String nombre = String.valueOf(modeloEliminar.getValueAt(filaModelo, 1));

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "Deseas eliminar al beneficiario \"" + nombre + "\"?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            if (beneficiarioDAO.eliminarBeneficiario(idBeneficiario)) {
                JOptionPane.showMessageDialog(this, "Beneficiario eliminado correctamente.");
                cargarTabla(modeloEliminar);
            } else {
                mostrarError("No se pudo eliminar el beneficiario.");
            }
        }
    }

    private boolean validarCampos(String nombre, String fechaTexto) {
        if (nombre.isEmpty()) {
            mostrarAdvertencia("El nombre es obligatorio.");
            return false;
        }

        if (!fechaTexto.isEmpty()) {
            try {
                Date.valueOf(fechaTexto);
            } catch (IllegalArgumentException e) {
                mostrarAdvertencia("La fecha debe tener el formato AAAA-MM-DD.");
                return false;
            }
        }

        return true;
    }

    private void cargarDatosBeneficiarioSeleccionado() {
        int fila = tablaActualizar.getSelectedRow();

        if (fila == -1) {
            return;
        }

        int filaModelo = tablaActualizar.convertRowIndexToModel(fila);

        txtActualizarId.setText(String.valueOf(modeloActualizar.getValueAt(filaModelo, 0)));
        txtActualizarNombre.setText(String.valueOf(modeloActualizar.getValueAt(filaModelo, 1)));
        cmbActualizarSexo.setSelectedItem(String.valueOf(modeloActualizar.getValueAt(filaModelo, 2)));
        txtActualizarFecha.setText(String.valueOf(modeloActualizar.getValueAt(filaModelo, 3)));
    }

    private void cargarTabla(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        List<Beneficiario> beneficiarios = beneficiarioDAO.obtenerBeneficiarios();

        for (Beneficiario beneficiario : beneficiarios) {
            modelo.addRow(new Object[]{
                beneficiario.getIdBeneficiario(),
                beneficiario.getNombre(),
                beneficiario.getSexo(),
                formatearFecha(beneficiario.getFechaUltimaRecepcion())
            });
        }
    }

    private String formatearFecha(Date fecha) {
        return fecha == null ? "" : fecha.toString();
    }

    private void mostrarVista(String vista) {
        if (VISTA_CONSULTAR.equals(vista)) {
            cargarTabla(modeloConsultar);
        }

        if (VISTA_ACTUALIZAR.equals(vista)) {
            limpiarFormularioActualizar();
            cargarTabla(modeloActualizar);
        }

        if (VISTA_ELIMINAR.equals(vista)) {
            cargarTabla(modeloEliminar);
        }

        cardLayout.show(panelContenido, vista);
    }

    private void limpiarFormularioCrear() {
        txtCrearNombre.setText("");
        cmbCrearSexo.setSelectedIndex(0);
        txtCrearFecha.setText("");
    }

    private void limpiarFormularioActualizar() {
        txtActualizarId.setText("");
        txtActualizarNombre.setText("");
        cmbActualizarSexo.setSelectedIndex(0);
        txtActualizarFecha.setText("");
    }

    private JPanel crearPanelBase() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(new Color(245, 247, 250));
        return panel;
    }

    private JPanel crearEncabezado(String titulo, String descripcion) {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 5));
        panel.setBackground(new Color(245, 247, 250));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(35, 75, 120));

        JLabel lblDescripcion = new JLabel(descripcion, SwingConstants.CENTER);
        lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblDescripcion.setForeground(new Color(90, 90, 90));

        panel.add(lblTitulo);
        panel.add(lblDescripcion);

        return panel;
    }

    private JPanel crearFormulario() {
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(new Color(245, 247, 250));
        return formulario;
    }

    private void agregarFilaFormulario(JPanel panel, int fila, String texto, JComponent campo) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("SansSerif", Font.PLAIN, 14));
        etiqueta.setForeground(new Color(60, 60, 60));

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(etiqueta, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        panel.add(campo, gbc);
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(280, 35));
        return campo;
    }

    private JComboBox<String> crearComboSexo() {
        JComboBox<String> combo = new JComboBox<>(new String[]{"Femenino", "Masculino", "Otro"});
        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(280, 35));
        return combo;
    }

    private JButton crearBoton(String texto, Color fondo, Color letra) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setPreferredSize(new Dimension(185, 38));
        boton.setBackground(fondo);
        boton.setForeground(letra);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private JButton crearBotonMenu(String texto, Color fondo, Color letra) {
        JButton boton = crearBoton(texto, fondo, letra);
        boton.setPreferredSize(new Dimension(230, 65));
        return boton;
    }

    private JPanel crearPanelBotones(JButton botonPrincipal, JButton botonSecundario) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        panel.setBackground(new Color(245, 247, 250));
        panel.add(botonPrincipal);
        panel.add(botonSecundario);
        return panel;
    }

    private DefaultTableModel crearModeloTabla() {
        return new DefaultTableModel(new String[]{"ID", "Nombre", "Sexo", "Fecha ultima recepcion"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columna) {
                return columna == 0 ? Integer.class : String.class;
            }
        };
    }

    private void configurarTabla(JTable tabla) {
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabla.setRowHeight(28);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tabla.setSelectionBackground(new Color(210, 225, 245));
        tabla.setSelectionForeground(new Color(30, 30, 30));
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
