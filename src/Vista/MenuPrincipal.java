package Vista;

import Modelo.Usuario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

public class MenuPrincipal extends JFrame {

    private static final String MODULO_DONACIONES = "Gestion de donaciones";
    private static final String MODULO_INVENTARIO = "Consulta de inventario";
    private static final String MODULO_ENTREGAS = "Gestion de entregas";
    private static final String MODULO_REPORTES = "Generacion de reportes";
    private static final String MODULO_BENEFICIARIOS = "Gestion de beneficiarios";
    private static final String MODULO_USUARIOS = "Gestion de usuarios";

    private Usuario usuarioSesion;

    private JPanel panelMenu;
    private JTabbedPane pestañas;

    public MenuPrincipal(Usuario usuarioSesion) {
        this.usuarioSesion = usuarioSesion;
        configurarVentana();
        crearComponentes();
        agregarComponentes();
    }

    private void configurarVentana() {
        setTitle("Donify - Menu Principal");
        setSize(1050, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
    }

    private void crearComponentes() {
        panelMenu = new JPanel(new BorderLayout());
        panelMenu.setPreferredSize(new Dimension(260, 650));
        panelMenu.setBackground(new Color(35, 75, 120));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        pestañas = new JTabbedPane();
        pestañas.setFont(new Font("SansSerif", Font.PLAIN, 14));
    }

    private void agregarComponentes() {
        panelMenu.add(crearPanelUsuario(), BorderLayout.NORTH);
        panelMenu.add(crearPanelBotones(), BorderLayout.CENTER);
        panelMenu.add(crearPanelCerrarSesion(), BorderLayout.SOUTH);

        add(panelMenu, BorderLayout.WEST);
        add(pestañas, BorderLayout.CENTER);
    }

    private JPanel crearPanelUsuario() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 0, 5));
        panel.setBackground(new Color(35, 75, 120));

        JLabel lblTitulo = new JLabel("DONIFY", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblUsuario = new JLabel(usuarioSesion.getUsuario(), SwingConstants.CENTER);
        lblUsuario.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblUsuario.setForeground(Color.WHITE);

        JLabel lblTipo = new JLabel(usuarioSesion.getTipoUsuario(), SwingConstants.CENTER);
        lblTipo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblTipo.setForeground(new Color(220, 230, 245));

        panel.add(lblTitulo);
        panel.add(lblUsuario);
        panel.add(lblTipo);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 10));
        panel.setBackground(new Color(35, 75, 120));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        agregarBotonSiTienePermiso(panel, MODULO_DONACIONES);
        agregarBotonSiTienePermiso(panel, MODULO_INVENTARIO);
        agregarBotonSiTienePermiso(panel, MODULO_ENTREGAS);
        agregarBotonSiTienePermiso(panel, MODULO_REPORTES);
        agregarBotonSiTienePermiso(panel, MODULO_BENEFICIARIOS);
        agregarBotonSiTienePermiso(panel, MODULO_USUARIOS);

        return panel;
    }

    private void agregarBotonSiTienePermiso(JPanel panel, String modulo) {
        if (tienePermiso(modulo)) {
            panel.add(crearBotonModulo(modulo));
        }
    }

    private JPanel crearPanelCerrarSesion() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(35, 75, 120));

        JButton btnCerrarSesion = crearBoton("Cerrar sesion");
        btnCerrarSesion.setBackground(new Color(220, 224, 230));
        btnCerrarSesion.setForeground(new Color(50, 50, 50));

        btnCerrarSesion.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });

        panel.add(btnCerrarSesion, BorderLayout.CENTER);
        return panel;
    }

    private JButton crearBotonModulo(String modulo) {
        JButton boton = crearBoton(modulo);
        boton.addActionListener(e -> abrirModulo(modulo));
        return boton;
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setBackground(new Color(245, 247, 250));
        boton.setForeground(new Color(35, 75, 120));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private boolean tienePermiso(String modulo) {
        String tipo = usuarioSesion.getTipoUsuario().toLowerCase();

        if (tipo.equals("administrador")) {
            return true;
        }

        if (tipo.equals("institucion")) {
            return !modulo.equals(MODULO_USUARIOS);
        }

        if (tipo.equals("donador")) {
        }

        return false;
    }

    private void abrirModulo(String modulo) {

        if (existePestaña(modulo)) {
            seleccionarPestaña(modulo);
            return;
        }

        JPanel panelModulo = new JPanel();

        if (modulo.equals(MODULO_USUARIOS)) {
            panelModulo = new GestionUsuarios();
        } else if (modulo.equals(MODULO_BENEFICIARIOS)) {
            panelModulo = new GestionBeneficiarios();
        } else if (modulo.equals(MODULO_DONACIONES)) {
            panelModulo = new GestionDonaciones();
        } else if (modulo.equals(MODULO_INVENTARIO)) {
            panelModulo = new ConsultaInventario();
        } else if (modulo.equals(MODULO_ENTREGAS)) {
            panelModulo = new GestionEntregas();
        } else if (modulo.equals(MODULO_REPORTES)) {
            panelModulo = new GeneracionReportes();
        } 

        pestañas.addTab(modulo, panelModulo);
        pestañas.setSelectedComponent(panelModulo);
    }

    private boolean existePestaña(String titulo) {
        for (int i = 0; i < pestañas.getTabCount(); i++) {
            if (pestañas.getTitleAt(i).equals(titulo)) {
                return true;
            }
        }
        return false;
    }

    private void seleccionarPestaña(String titulo) {
        for (int i = 0; i < pestañas.getTabCount(); i++) {
            if (pestañas.getTitleAt(i).equals(titulo)) {
                pestañas.setSelectedIndex(i);
                return;
            }
        }
    }
}


