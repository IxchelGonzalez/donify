//revisado
package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import DAO.LoginDAO;
import javax.swing.JOptionPane;
import Modelo.Usuario;

public class Login extends JFrame {

    // Componentes
    private JLabel lblTitulo;
    private JLabel lblSubtitulo;
    private JLabel lblDescripcion;
    private JLabel lblUsuario;
    private JLabel lblContrasena;

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;

    private JButton btnIngresar;
    private JButton btnNuevoUsuario;
    private LoginDAO LoginDAO;

    // Constructor
 public Login() {
    LoginDAO = new LoginDAO();
    configurarVentana();
    crearComponentes();
    agregarComponentes();
    configurarEventos();
}

    // Configuración de ventana
    private void configurarVentana() {
        setTitle("Donify - Inicio de Sesión");
        setSize(500, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 247, 250));
    }

    // Crear componentes
    private void crearComponentes() {
        Font fuenteTitulo = new Font("SansSerif", Font.BOLD, 34);
        Font fuenteSubtitulo = new Font("SansSerif", Font.BOLD, 20);
        Font fuenteNormal = new Font("SansSerif", Font.PLAIN, 14);
        Font fuenteBoton = new Font("SansSerif", Font.BOLD, 14);

        lblTitulo = new JLabel("DONIFY", SwingConstants.CENTER);
        lblTitulo.setFont(fuenteTitulo);
        lblTitulo.setForeground(new Color(35, 75, 120));

        lblSubtitulo = new JLabel("Inicio de Sesión", SwingConstants.CENTER);
        lblSubtitulo.setFont(fuenteSubtitulo);
        lblSubtitulo.setForeground(new Color(50, 50, 50));
        
        lblDescripcion = new JLabel("Inicia sesión para ingresar al sistema.", SwingConstants.CENTER);
        lblDescripcion.setFont(fuenteNormal);
        lblDescripcion.setForeground(new Color(100, 100, 100));

        lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(fuenteNormal);
        lblUsuario.setForeground(new Color(60, 60, 60));

        txtUsuario = new JTextField();
        txtUsuario.setFont(fuenteNormal);
        txtUsuario.setPreferredSize(new Dimension(280, 36));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 220)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        lblContrasena = new JLabel("Contraseña");
        lblContrasena.setFont(fuenteNormal);
        lblContrasena.setForeground(new Color(60, 60, 60));

        txtContrasena = new JPasswordField();
        txtContrasena.setFont(fuenteNormal);
        txtContrasena.setPreferredSize(new Dimension(280, 36));
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 220)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        btnIngresar = new JButton("Ingresar");
        btnIngresar.setFont(fuenteBoton);
        btnIngresar.setPreferredSize(new Dimension(135, 38));
        btnIngresar.setBackground(new Color(40, 120, 210));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setBorderPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnNuevoUsuario = new JButton("Nuevo usuario");
        btnNuevoUsuario.setFont(fuenteBoton);
        btnNuevoUsuario.setPreferredSize(new Dimension(135, 38));
        btnNuevoUsuario.setBackground(new Color(225, 229, 235));
        btnNuevoUsuario.setForeground(new Color(60, 60, 60));
        btnNuevoUsuario.setFocusPainted(false);
        btnNuevoUsuario.setBorderPainted(false);
        btnNuevoUsuario.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Agregar componentes
    private void agregarComponentes() {
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(new Color(245, 247, 250));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 0);
        panelPrincipal.add(lblTitulo, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 6, 0);
        panelPrincipal.add(lblSubtitulo, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 22, 0);
        panelPrincipal.add(lblDescripcion, gbc);

        gbc.gridwidth = 2;
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 5, 0);
        panelPrincipal.add(lblUsuario, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 14, 0);
        panelPrincipal.add(txtUsuario, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 5, 0);
        panelPrincipal.add(lblContrasena, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 22, 0);
        panelPrincipal.add(txtContrasena, gbc);

        JPanel panelBotones = new JPanel(new GridBagLayout());
        panelBotones.setBackground(new Color(245, 247, 250));

        GridBagConstraints gbcBotones = new GridBagConstraints();
        gbcBotones.gridy = 0;
        gbcBotones.insets = new Insets(0, 5, 0, 5);

        gbcBotones.gridx = 0;
        panelBotones.add(btnIngresar, gbcBotones);

        gbcBotones.gridx = 1;
        panelBotones.add(btnNuevoUsuario, gbcBotones);

        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 0, 0);
        panelPrincipal.add(panelBotones, gbc);

        add(panelPrincipal, BorderLayout.CENTER);
    }

private void configurarEventos() {
    btnIngresar.addActionListener(e -> iniciarSesion());

    btnNuevoUsuario.addActionListener(e -> abrirRegistroUsuario());
}
 
 private void iniciarSesion() {
    String usuario = txtUsuario.getText().trim();
    String contrasena = new String(txtContrasena.getPassword());

    if (usuario.isEmpty() || contrasena.isEmpty()) {
        JOptionPane.showMessageDialog(
                this,
                "Debe ingresar usuario y contrasena.",
                "Campos vacios",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    Usuario usuarioSesion = LoginDAO.obtenerUsuarioPorCredenciales(usuario, contrasena);

    if (usuarioSesion != null) {
        JOptionPane.showMessageDialog(
                this,
                "Inicio de sesion correcto.",
                "Bienvenido",
                JOptionPane.INFORMATION_MESSAGE
        );

        new MenuPrincipal(usuarioSesion).setVisible(true);
        dispose();

    } else {
        JOptionPane.showMessageDialog(
                this,
                "Usuario o contrasena incorrectos.",
                "Acceso denegado",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
private void abrirRegistroUsuario() {
    JFrame ventanaRegistro = new JFrame("Donify - Nuevo usuario");
    ventanaRegistro.setSize(520, 430);
    ventanaRegistro.setResizable(false);
    ventanaRegistro.setLocationRelativeTo(this);
    ventanaRegistro.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    ventanaRegistro.setContentPane(new RegistroUsuario());
    ventanaRegistro.setVisible(true);
}
}

