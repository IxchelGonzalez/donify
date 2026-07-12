package Vista;

import DAO.UsuarioDAO;
import Modelo.Usuario;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class GestionUsuarios extends JPanel {

    private static final String VISTA_MENU = "menu";
    private static final String VISTA_CREAR = "crear";
    private static final String VISTA_CONSULTAR = "consultar";
    private static final String VISTA_ACTUALIZAR = "actualizar";
    private static final String VISTA_ELIMINAR = "eliminar";

    private final UsuarioDAO usuarioDAO;

    private CardLayout cardLayout;
    private JPanel panelContenido;

    private JButton btnMenuCrear;
    private JButton btnMenuConsultar;
    private JButton btnMenuActualizar;
    private JButton btnMenuEliminar;

    private JTextField txtCrearUsuario;
    private JPasswordField txtCrearContrasena;
    private JPasswordField txtCrearConfirmarContrasena;
    private JTextField txtCrearCurp;
    private JComboBox<String> cmbCrearTipo;
    private JButton btnCrearUsuario;
    private JButton btnCerrarCrear;

    private JTable tablaConsultar;
    private JTable tablaActualizar;
    private JTable tablaEliminar;
    private DefaultTableModel modeloConsultar;
    private DefaultTableModel modeloActualizar;
    private DefaultTableModel modeloEliminar;

    private JButton btnRefrescarConsultar;
    private JButton btnCerrarConsultar;

    private JTextField txtActualizarId;
    private JTextField txtActualizarUsuario;
    private JPasswordField txtActualizarContrasena;
    private JPasswordField txtActualizarConfirmarContrasena;
    private JTextField txtActualizarCurp;
    private JComboBox<String> cmbActualizarTipo;
    private JButton btnActualizarUsuario;
    private JButton btnCerrarActualizar;

    private JButton btnEliminarUsuario;
    private JButton btnCerrarEliminar;

    private String usuarioOriginal;
    private String curpOriginal;

    public GestionUsuarios() {
        usuarioDAO = new UsuarioDAO();
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

        btnCrearUsuario.addActionListener(e -> crearUsuario());
        btnCerrarCrear.addActionListener(e -> {
            limpiarFormularioCrear();
            mostrarVista(VISTA_MENU);
        });

        btnRefrescarConsultar.addActionListener(e -> cargarTabla(modeloConsultar));
        btnCerrarConsultar.addActionListener(e -> mostrarVista(VISTA_MENU));

        tablaActualizar.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosUsuarioSeleccionado();
            }
        });

        btnActualizarUsuario.addActionListener(e -> actualizarUsuario());
        btnCerrarActualizar.addActionListener(e -> {
            limpiarFormularioActualizar();
            mostrarVista(VISTA_MENU);
        });

        btnEliminarUsuario.addActionListener(e -> eliminarUsuario());
        btnCerrarEliminar.addActionListener(e -> mostrarVista(VISTA_MENU));
    }

    private JPanel crearPanelMenu() {
        JPanel panel = crearPanelBase();

        panel.add(crearEncabezado(
                "Gestión de usuarios",
                "Selecciona la acción que deseas realizar."
        ), BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 18, 18));
        panelBotones.setBackground(new Color(245, 247, 250));

        btnMenuCrear = crearBotonMenu("Crear usuario", new Color(40, 120, 210), Color.WHITE);
        btnMenuConsultar = crearBotonMenu("Consultar usuarios", new Color(80, 145, 100), Color.WHITE);
        btnMenuActualizar = crearBotonMenu("Actualizar usuario", new Color(230, 150, 55), Color.WHITE);
        btnMenuEliminar = crearBotonMenu("Eliminar usuario", new Color(200, 70, 70), Color.WHITE);

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
                "Crear usuario",
                "Registra un nuevo donador o institución."
        ), BorderLayout.NORTH);

        txtCrearUsuario = crearCampoTexto();
        txtCrearContrasena = crearCampoPassword();
        txtCrearConfirmarContrasena = crearCampoPassword();
        txtCrearCurp = crearCampoTexto();
        cmbCrearTipo = crearComboTipoUsuario();

        JPanel formulario = crearFormulario();
        agregarFilaFormulario(formulario, 0, "Usuario", txtCrearUsuario);
        agregarFilaFormulario(formulario, 1, "Contraseña", txtCrearContrasena);
        agregarFilaFormulario(formulario, 2, "Confirmar contraseña", txtCrearConfirmarContrasena);
        agregarFilaFormulario(formulario, 3, "CURP", txtCrearCurp);
        agregarFilaFormulario(formulario, 4, "Tipo de usuario", cmbCrearTipo);

        btnCrearUsuario = crearBoton("Crear usuario", new Color(40, 120, 210), Color.WHITE);
        btnCerrarCrear = crearBoton("Cerrar", new Color(220, 224, 230), new Color(50, 50, 50));

        panel.add(formulario, BorderLayout.CENTER);
        panel.add(crearPanelBotones(btnCrearUsuario, btnCerrarCrear), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelConsultar() {
        JPanel panel = crearPanelBase();

        panel.add(crearEncabezado(
                "Consultar usuarios",
                "Lista de usuarios registrados en el sistema."
        ), BorderLayout.NORTH);

        modeloConsultar = crearModeloTabla(false);
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
                "Actualizar usuario",
                "Selecciona un usuario de la tabla y modifica sus datos."
        ), BorderLayout.NORTH);

        modeloActualizar = crearModeloTabla(true);
        tablaActualizar = new JTable(modeloActualizar);
        configurarTabla(tablaActualizar);
        ocultarColumnaContrasena(tablaActualizar);

        JScrollPane scroll = new JScrollPane(tablaActualizar);
        scroll.setPreferredSize(new Dimension(700, 150));

        txtActualizarId = crearCampoTexto();
        txtActualizarId.setEditable(false);

        txtActualizarUsuario = crearCampoTexto();
        txtActualizarContrasena = crearCampoPassword();
        txtActualizarConfirmarContrasena = crearCampoPassword();
        txtActualizarCurp = crearCampoTexto();
        cmbActualizarTipo = crearComboTipoUsuario();

        JPanel formulario = crearFormulario();
        agregarFilaFormulario(formulario, 0, "ID", txtActualizarId);
        agregarFilaFormulario(formulario, 1, "Usuario", txtActualizarUsuario);
        agregarFilaFormulario(formulario, 2, "Contraseña", txtActualizarContrasena);
        agregarFilaFormulario(formulario, 3, "Confirmar contraseña", txtActualizarConfirmarContrasena);
        agregarFilaFormulario(formulario, 4, "CURP", txtActualizarCurp);
        agregarFilaFormulario(formulario, 5, "Tipo de usuario", cmbActualizarTipo);

        JPanel centro = new JPanel(new BorderLayout(0, 15));
        centro.setBackground(new Color(245, 247, 250));
        centro.add(scroll, BorderLayout.NORTH);
        centro.add(formulario, BorderLayout.CENTER);

        btnActualizarUsuario = crearBoton("Actualizar usuario", new Color(230, 150, 55), Color.WHITE);
        btnCerrarActualizar = crearBoton("Cerrar", new Color(220, 224, 230), new Color(50, 50, 50));

        panel.add(centro, BorderLayout.CENTER);
        panel.add(crearPanelBotones(btnActualizarUsuario, btnCerrarActualizar), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelEliminar() {
        JPanel panel = crearPanelBase();

        panel.add(crearEncabezado(
                "Eliminar usuario",
                "Selecciona un usuario y confirma la eliminación."
        ), BorderLayout.NORTH);

        modeloEliminar = crearModeloTabla(false);
        tablaEliminar = new JTable(modeloEliminar);
        configurarTabla(tablaEliminar);

        btnEliminarUsuario = crearBoton("Eliminar usuario", new Color(200, 70, 70), Color.WHITE);
        btnCerrarEliminar = crearBoton("Cerrar", new Color(220, 224, 230), new Color(50, 50, 50));

        panel.add(new JScrollPane(tablaEliminar), BorderLayout.CENTER);
        panel.add(crearPanelBotones(btnEliminarUsuario, btnCerrarEliminar), BorderLayout.SOUTH);

        return panel;
    }

    private void crearUsuario() {
        String usuario = txtCrearUsuario.getText().trim();
        String contrasena = new String(txtCrearContrasena.getPassword());
        String confirmarContrasena = new String(txtCrearConfirmarContrasena.getPassword());
        String curp = txtCrearCurp.getText().trim().toUpperCase();
        String tipoUsuario = obtenerTipoUsuario(cmbCrearTipo);

        if (!validarCampos(usuario, contrasena, confirmarContrasena, curp)) {
            return;
        }

        if (usuarioDAO.existeUsuario(usuario)) {
            mostrarAdvertencia("El nombre de usuario ya existe.");
            return;
        }

        if (usuarioDAO.existeCurp(curp)) {
            mostrarAdvertencia("La CURP ya se encuentra registrada.");
            return;
        }

        Usuario nuevoUsuario = new Usuario(usuario, contrasena, curp, tipoUsuario);

        if (usuarioDAO.crearUsuario(nuevoUsuario)) {
            JOptionPane.showMessageDialog(this, "Usuario creado correctamente.");
            limpiarFormularioCrear();
        } else {
            mostrarError("No se pudo crear el usuario.");
        }
    }

    private void actualizarUsuario() {
        if (txtActualizarId.getText().trim().isEmpty()) {
            mostrarAdvertencia("Selecciona un usuario de la tabla.");
            return;
        }

        int idUsuario = Integer.parseInt(txtActualizarId.getText().trim());
        String usuario = txtActualizarUsuario.getText().trim();
        String contrasena = new String(txtActualizarContrasena.getPassword());
        String confirmarContrasena = new String(txtActualizarConfirmarContrasena.getPassword());
        String curp = txtActualizarCurp.getText().trim().toUpperCase();
        String tipoUsuario = obtenerTipoUsuario(cmbActualizarTipo);

        if (!validarCampos(usuario, contrasena, confirmarContrasena, curp)) {
            return;
        }

        if (!usuario.equalsIgnoreCase(usuarioOriginal) && usuarioDAO.existeUsuario(usuario)) {
            mostrarAdvertencia("El nombre de usuario ya existe.");
            return;
        }

        if (!curp.equalsIgnoreCase(curpOriginal) && usuarioDAO.existeCurp(curp)) {
            mostrarAdvertencia("La CURP ya se encuentra registrada.");
            return;
        }

        Usuario usuarioActualizado = new Usuario(idUsuario, usuario, contrasena, curp, tipoUsuario);

        if (usuarioDAO.actualizarUsuario(usuarioActualizado)) {
            JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente.");
            limpiarFormularioActualizar();
            cargarTabla(modeloActualizar);
        } else {
            mostrarError("No se pudo actualizar el usuario.");
        }
    }

    private void eliminarUsuario() {
        int fila = tablaEliminar.getSelectedRow();

        if (fila == -1) {
            mostrarAdvertencia("Selecciona un usuario de la tabla.");
            return;
        }

        int filaModelo = tablaEliminar.convertRowIndexToModel(fila);
        int idUsuario = (int) modeloEliminar.getValueAt(filaModelo, 0);
        String usuario = String.valueOf(modeloEliminar.getValueAt(filaModelo, 1));

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas eliminar al usuario \"" + usuario + "\"?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            if (usuarioDAO.eliminarUsuario(idUsuario)) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.");
                cargarTabla(modeloEliminar);
            } else {
                mostrarError("No se pudo eliminar el usuario.");
            }
        }
    }

    private boolean validarCampos(String usuario, String contrasena, String confirmarContrasena, String curp) {
        if (usuario.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty() || curp.isEmpty()) {
            mostrarAdvertencia("Todos los campos son obligatorios.");
            return false;
        }

        if (!contrasena.equals(confirmarContrasena)) {
            mostrarAdvertencia("Las contraseñas no coinciden.");
            return false;
        }

        if (curp.length() != 18) {
            mostrarAdvertencia("La CURP debe tener exactamente 18 caracteres.");
            return false;
        }

        return true;
    }

    private void cargarDatosUsuarioSeleccionado() {
        int fila = tablaActualizar.getSelectedRow();

        if (fila == -1) {
            return;
        }

        int filaModelo = tablaActualizar.convertRowIndexToModel(fila);

        txtActualizarId.setText(String.valueOf(modeloActualizar.getValueAt(filaModelo, 0)));
        txtActualizarUsuario.setText(String.valueOf(modeloActualizar.getValueAt(filaModelo, 1)));
        txtActualizarCurp.setText(String.valueOf(modeloActualizar.getValueAt(filaModelo, 2)));
        seleccionarTipoUsuario(cmbActualizarTipo, String.valueOf(modeloActualizar.getValueAt(filaModelo, 3)));

        String contrasena = String.valueOf(modeloActualizar.getValueAt(filaModelo, 4));
        txtActualizarContrasena.setText(contrasena);
        txtActualizarConfirmarContrasena.setText(contrasena);

        usuarioOriginal = txtActualizarUsuario.getText();
        curpOriginal = txtActualizarCurp.getText();
    }

    private void cargarTabla(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        List<Usuario> usuarios = usuarioDAO.obtenerUsuarios();

        for (Usuario usuario : usuarios) {
            if (modelo.getColumnCount() == 5) {
                modelo.addRow(new Object[]{
                    usuario.getIdUsuario(),
                    usuario.getUsuario(),
                    usuario.getCurp(),
                    usuario.getTipoUsuario(),
                    usuario.getContrasena()
                });
            } else {
                modelo.addRow(new Object[]{
                    usuario.getIdUsuario(),
                    usuario.getUsuario(),
                    usuario.getCurp(),
                    usuario.getTipoUsuario()
                });
            }
        }
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
        txtCrearUsuario.setText("");
        txtCrearContrasena.setText("");
        txtCrearConfirmarContrasena.setText("");
        txtCrearCurp.setText("");
        cmbCrearTipo.setSelectedIndex(0);
    }

    private void limpiarFormularioActualizar() {
        txtActualizarId.setText("");
        txtActualizarUsuario.setText("");
        txtActualizarContrasena.setText("");
        txtActualizarConfirmarContrasena.setText("");
        txtActualizarCurp.setText("");
        cmbActualizarTipo.setSelectedIndex(0);
        usuarioOriginal = "";
        curpOriginal = "";
    }

    private String obtenerTipoUsuario(JComboBox<String> combo) {
        return combo.getSelectedItem().toString().equals("Donador") ? "donador" : "institucion";
    }

    private void seleccionarTipoUsuario(JComboBox<String> combo, String tipoUsuario) {
        if ("institucion".equalsIgnoreCase(tipoUsuario)) {
            combo.setSelectedItem("Institucion");
        } else {
            combo.setSelectedItem("Donador");
        }
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

    private JPasswordField crearCampoPassword() {
        JPasswordField campo = new JPasswordField();
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(280, 35));
        return campo;
    }

    private JComboBox<String> crearComboTipoUsuario() {
        JComboBox<String> combo = new JComboBox<>(new String[]{"Donador", "Institucion"});
        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(280, 35));
        return combo;
    }

    private JButton crearBoton(String texto, Color fondo, Color letra) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setPreferredSize(new Dimension(165, 38));
        boton.setBackground(fondo);
        boton.setForeground(letra);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private JButton crearBotonMenu(String texto, Color fondo, Color letra) {
        JButton boton = crearBoton(texto, fondo, letra);
        boton.setPreferredSize(new Dimension(210, 65));
        return boton;
    }

    private JPanel crearPanelBotones(JButton botonPrincipal, JButton botonSecundario) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        panel.setBackground(new Color(245, 247, 250));
        panel.add(botonPrincipal);
        panel.add(botonSecundario);
        return panel;
    }

    private DefaultTableModel crearModeloTabla(boolean incluirContrasena) {
        String[] columnas = incluirContrasena
                ? new String[]{"ID", "Usuario", "CURP", "Tipo", "Contraseña"}
                : new String[]{"ID", "Usuario", "CURP", "Tipo"};

        return new DefaultTableModel(columnas, 0) {
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

    private void ocultarColumnaContrasena(JTable tabla) {
        tabla.getColumnModel().getColumn(4).setMinWidth(0);
        tabla.getColumnModel().getColumn(4).setMaxWidth(0);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(0);
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
       
