package Vista;

import DAO.UsuarioDAO;
import Modelo.Usuario;
import java.awt.*;
import javax.swing.*;

public class RegistroUsuario extends JPanel {

    private UsuarioDAO usuarioDAO;

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JPasswordField txtConfirmarContrasena;
    private JTextField txtCurp;
    private JComboBox<String> cmbTipoUsuario;

    private JButton btnCrearUsuario;
    private JButton btnCerrar;

    public RegistroUsuario() {
        usuarioDAO = new UsuarioDAO();
        configurarPanel();
        crearComponentes();
        agregarComponentes();
        configurarEventos();
    }

    private void configurarPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
    }

    private void crearComponentes() {
        txtUsuario = crearCampoTexto();
        txtContrasena = crearCampoPassword();
        txtConfirmarContrasena = crearCampoPassword();
        txtCurp = crearCampoTexto();

        cmbTipoUsuario = new JComboBox<>(new String[]{"Donador", "Institucion"});
        cmbTipoUsuario.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cmbTipoUsuario.setPreferredSize(new Dimension(280, 35));

        btnCrearUsuario = crearBoton("Crear usuario", new Color(40, 120, 210), Color.WHITE);
        btnCerrar = crearBoton("Cerrar", new Color(220, 224, 230), new Color(50, 50, 50));
    }

    private void agregarComponentes() {
        JPanel encabezado = new JPanel(new GridLayout(2, 1, 0, 5));
        encabezado.setBackground(new Color(245, 247, 250));

        JLabel lblTitulo = new JLabel("Crear usuario", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(35, 75, 120));

        JLabel lblDescripcion = new JLabel("Registra una cuenta como donador o institucion.", SwingConstants.CENTER);
        lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblDescripcion.setForeground(new Color(90, 90, 90));

        encabezado.add(lblTitulo);
        encabezado.add(lblDescripcion);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(new Color(245, 247, 250));

        agregarFila(formulario, 0, "Usuario", txtUsuario);
        agregarFila(formulario, 1, "Contrasena", txtContrasena);
        agregarFila(formulario, 2, "Confirmar contrasena", txtConfirmarContrasena);
        agregarFila(formulario, 3, "CURP", txtCurp);
        agregarFila(formulario, 4, "Tipo de usuario", cmbTipoUsuario);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        botones.setBackground(new Color(245, 247, 250));
        botones.add(btnCrearUsuario);
        botones.add(btnCerrar);

        add(encabezado, BorderLayout.NORTH);
        add(formulario, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        btnCrearUsuario.addActionListener(e -> crearUsuario());
        btnCerrar.addActionListener(e -> cerrarVentana());
    }

    private void crearUsuario() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());
        String confirmarContrasena = new String(txtConfirmarContrasena.getPassword());
        String curp = txtCurp.getText().trim().toUpperCase();
        String tipoUsuario = obtenerTipoUsuario();

        if (!validarCampos(usuario, contrasena, confirmarContrasena, curp)) {
            return;
        }

        if (usuarioDAO.existeUsuario(usuario)) {
            mostrarAdvertencia("El nombre de usuario ya existe.");
            return;
        }

        if (usuarioDAO.existeCurp(curp)) {
            mostrarAdvertencia("La CURP ya esta registrada.");
            return;
        }

        Usuario nuevoUsuario = new Usuario(usuario, contrasena, curp, tipoUsuario);

        if (usuarioDAO.crearUsuario(nuevoUsuario)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Usuario creado correctamente.",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );
            cerrarVentana();
        } else {
            mostrarError("No se pudo crear el usuario.");
        }
    }

    private boolean validarCampos(String usuario, String contrasena, String confirmarContrasena, String curp) {
        if (usuario.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty() || curp.isEmpty()) {
            mostrarAdvertencia("Todos los campos son obligatorios.");
            return false;
        }

        if (!contrasena.equals(confirmarContrasena)) {
            mostrarAdvertencia("Las contrasenas no coinciden.");
            return false;
        }

        if (curp.length() != 18) {
            mostrarAdvertencia("La CURP debe tener exactamente 18 caracteres.");
            return false;
        }

        return true;
    }

    private String obtenerTipoUsuario() {
        return cmbTipoUsuario.getSelectedItem().toString().equals("Donador")
                ? "donador"
                : "institucion";
    }

    private void cerrarVentana() {
        Window ventana = SwingUtilities.getWindowAncestor(this);

        if (ventana != null) {
            ventana.dispose();
        }
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

    private JButton crearBoton(String texto, Color fondo, Color letra) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setPreferredSize(new Dimension(150, 38));
        boton.setBackground(fondo);
        boton.setForeground(letra);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private void agregarFila(JPanel panel, int fila, String texto, JComponent campo) {
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
        gbc.gridy = fila;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(campo, gbc);
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}