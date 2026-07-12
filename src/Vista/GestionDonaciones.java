package Vista;

import DAO.DonacionDAO;
import Modelo.Asociacion;
import Modelo.Donador;
import Modelo.Prenda;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GestionDonaciones extends JPanel {

    private DonacionDAO donacionDAO;

    private JLabel lblTitulo;
    private JLabel lblDescripcion;

    private JTextField txtNombreDonador;
    private JTextField txtCorreoDonador;
    private JTextField txtTelefonoDonador;
    private JComboBox<Asociacion> cmbAsociaciones;
    private JComboBox<Prenda> cmbPrendas;
    private JTextField txtCantidad;
    private JTextField txtFecha;

    private JButton btnRegistrarDonacion;
    private JButton btnLimpiar;

    public GestionDonaciones() {
        donacionDAO = new DonacionDAO();
        configurarPanel();
        crearComponentes();
        agregarComponentes();
        configurarEventos();
        cargarCombos();
    }

    private void configurarPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
    }

    private void crearComponentes() {
        lblTitulo = new JLabel("Gestion de donaciones", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(35, 75, 120));

        lblDescripcion = new JLabel("Registra donaciones de ropa y actualiza el inventario.", SwingConstants.CENTER);
        lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblDescripcion.setForeground(new Color(90, 90, 90));

        txtNombreDonador = crearCampoTexto();
        txtCorreoDonador = crearCampoTexto();
        txtTelefonoDonador = crearCampoTexto();
        cmbAsociaciones = new JComboBox<>();
        cmbPrendas = new JComboBox<>();
        txtCantidad = crearCampoTexto();
        txtFecha = crearCampoTexto();

        cmbAsociaciones.setPreferredSize(new Dimension(300, 35));
        cmbPrendas.setPreferredSize(new Dimension(300, 35));
        cmbAsociaciones.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cmbPrendas.setFont(new Font("SansSerif", Font.PLAIN, 14));

        btnRegistrarDonacion = crearBoton("Registrar donacion", new Color(40, 120, 210), Color.WHITE);
        btnLimpiar = crearBoton("Limpiar", new Color(220, 224, 230), new Color(50, 50, 50));
    }

    private void agregarComponentes() {
        JPanel panelEncabezado = new JPanel(new BorderLayout(0, 5));
        panelEncabezado.setBackground(new Color(245, 247, 250));
        panelEncabezado.add(lblTitulo, BorderLayout.CENTER);
        panelEncabezado.add(lblDescripcion, BorderLayout.SOUTH);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(new Color(245, 247, 250));

        agregarFila(panelFormulario, 0, "Nombre del donador", txtNombreDonador);
        agregarFila(panelFormulario, 1, "Correo del donador", txtCorreoDonador);
        agregarFila(panelFormulario, 2, "Telefono del donador", txtTelefonoDonador);
        agregarFila(panelFormulario, 3, "Asociacion", cmbAsociaciones);
        agregarFila(panelFormulario, 4, "Prenda", cmbPrendas);
        agregarFila(panelFormulario, 5, "Cantidad", txtCantidad);
        agregarFila(panelFormulario, 6, "Fecha donacion (AAAA-MM-DD)", txtFecha);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        panelBotones.setBackground(new Color(245, 247, 250));
        panelBotones.add(btnRegistrarDonacion);
        panelBotones.add(btnLimpiar);

        add(panelEncabezado, BorderLayout.NORTH);
        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        btnRegistrarDonacion.addActionListener(e -> registrarDonacion());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
    }

    private void cargarCombos() {
        cmbAsociaciones.removeAllItems();
        cmbPrendas.removeAllItems();

        List<Asociacion> asociaciones = donacionDAO.obtenerAsociaciones();
        List<Prenda> prendas = donacionDAO.obtenerPrendas();

        for (Asociacion asociacion : asociaciones) {
            cmbAsociaciones.addItem(asociacion);
        }

        for (Prenda prenda : prendas) {
            cmbPrendas.addItem(prenda);
        }
    }

    private void registrarDonacion() {
        String nombreDonador = txtNombreDonador.getText().trim();
        String correoDonador = txtCorreoDonador.getText().trim();
        String telefonoDonador = txtTelefonoDonador.getText().trim();
        String cantidadTexto = txtCantidad.getText().trim();
        String fechaTexto = txtFecha.getText().trim();

        if (!validarCampos(nombreDonador, correoDonador, telefonoDonador, cantidadTexto, fechaTexto)) {
            return;
        }

        Asociacion asociacion = (Asociacion) cmbAsociaciones.getSelectedItem();
        Prenda prenda = (Prenda) cmbPrendas.getSelectedItem();

        if (asociacion == null) {
            mostrarAdvertencia("Debe seleccionar una asociacion.");
            return;
        }

        if (prenda == null) {
            mostrarAdvertencia("Debe seleccionar una prenda.");
            return;
        }

        int cantidad = Integer.parseInt(cantidadTexto);
        Date fechaDonacion = Date.valueOf(fechaTexto);

        Donador donador = new Donador(nombreDonador, correoDonador, telefonoDonador);
        int idDonador = donacionDAO.registrarDonador(donador);

        if (idDonador == 0) {
            mostrarError("No se pudo registrar el donador.");
            return;
        }

        boolean donacionRegistrada = donacionDAO.registrarDonacionCompleta(
                idDonador,
                asociacion.getIdAsociacion(),
                fechaDonacion,
                prenda.getIdPrenda(),
                cantidad
        );

        if (donacionRegistrada) {
            JOptionPane.showMessageDialog(
                    this,
                    "Donacion registrada correctamente.",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );
            limpiarFormulario();
            cargarCombos();
        } else {
            mostrarError("No se pudo registrar la donacion.");
        }
    }

    private boolean validarCampos(String nombre, String correo, String telefono, String cantidadTexto, String fechaTexto) {
        if (nombre.isEmpty() || correo.isEmpty() || telefono.isEmpty()
                || cantidadTexto.isEmpty() || fechaTexto.isEmpty()) {
            mostrarAdvertencia("Todos los campos son obligatorios.");
            return false;
        }

        if (telefono.length() != 10) {
            mostrarAdvertencia("El telefono debe tener 10 digitos.");
            return false;
        }

        try {
            int cantidad = Integer.parseInt(cantidadTexto);

            if (cantidad <= 0) {
                mostrarAdvertencia("La cantidad debe ser mayor a 0.");
                return false;
            }

        } catch (NumberFormatException e) {
            mostrarAdvertencia("La cantidad debe ser numerica.");
            return false;
        }

        try {
            Date.valueOf(fechaTexto);
        } catch (IllegalArgumentException e) {
            mostrarAdvertencia("La fecha debe tener el formato AAAA-MM-DD.");
            return false;
        }

        return true;
    }

    private void limpiarFormulario() {
        txtNombreDonador.setText("");
        txtCorreoDonador.setText("");
        txtTelefonoDonador.setText("");
        txtCantidad.setText("");
        txtFecha.setText("");

        if (cmbAsociaciones.getItemCount() > 0) {
            cmbAsociaciones.setSelectedIndex(0);
        }

        if (cmbPrendas.getItemCount() > 0) {
            cmbPrendas.setSelectedIndex(0);
        }
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(300, 35));
        return campo;
    }

    private JButton crearBoton(String texto, Color fondo, Color letra) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setPreferredSize(new Dimension(180, 38));
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