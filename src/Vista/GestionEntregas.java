package Vista;

import DAO.EntregaDAO;
import Modelo.Asociacion;
import Modelo.Beneficiario;
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

public class GestionEntregas extends JPanel {

    private EntregaDAO entregaDAO;

    private JLabel lblTitulo;
    private JLabel lblDescripcion;

    private JComboBox<Beneficiario> cmbBeneficiarios;
    private JComboBox<Asociacion> cmbAsociaciones;
    private JComboBox<Prenda> cmbPrendas;
    private JTextField txtCantidad;
    private JTextField txtFecha;

    private JButton btnRegistrarEntrega;
    private JButton btnLimpiar;

    public GestionEntregas() {
        entregaDAO = new EntregaDAO();
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
        lblTitulo = new JLabel("Gestion de entregas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(35, 75, 120));

        lblDescripcion = new JLabel("Registra entregas de prendas a beneficiarios.", SwingConstants.CENTER);
        lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblDescripcion.setForeground(new Color(90, 90, 90));

        cmbBeneficiarios = new JComboBox<>();
        cmbAsociaciones = new JComboBox<>();
        cmbPrendas = new JComboBox<>();

        configurarCombo(cmbBeneficiarios);
        configurarCombo(cmbAsociaciones);
        configurarCombo(cmbPrendas);

        txtCantidad = crearCampoTexto();
        txtFecha = crearCampoTexto();

        btnRegistrarEntrega = crearBoton("Registrar entrega", new Color(40, 120, 210), Color.WHITE);
        btnLimpiar = crearBoton("Limpiar", new Color(220, 224, 230), new Color(50, 50, 50));
    }

    private void agregarComponentes() {
        JPanel panelEncabezado = new JPanel(new BorderLayout(0, 5));
        panelEncabezado.setBackground(new Color(245, 247, 250));
        panelEncabezado.add(lblTitulo, BorderLayout.CENTER);
        panelEncabezado.add(lblDescripcion, BorderLayout.SOUTH);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(new Color(245, 247, 250));

        agregarFila(panelFormulario, 0, "Beneficiario", cmbBeneficiarios);
        agregarFila(panelFormulario, 1, "Asociacion", cmbAsociaciones);
        agregarFila(panelFormulario, 2, "Prenda", cmbPrendas);
        agregarFila(panelFormulario, 3, "Cantidad", txtCantidad);
        agregarFila(panelFormulario, 4, "Fecha entrega (AAAA-MM-DD)", txtFecha);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        panelBotones.setBackground(new Color(245, 247, 250));
        panelBotones.add(btnRegistrarEntrega);
        panelBotones.add(btnLimpiar);

        add(panelEncabezado, BorderLayout.NORTH);
        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        btnRegistrarEntrega.addActionListener(e -> registrarEntrega());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
    }

    private void cargarCombos() {
        cmbBeneficiarios.removeAllItems();
        cmbAsociaciones.removeAllItems();
        cmbPrendas.removeAllItems();

        List<Beneficiario> beneficiarios = entregaDAO.obtenerBeneficiarios();
        List<Asociacion> asociaciones = entregaDAO.obtenerAsociaciones();
        List<Prenda> prendas = entregaDAO.obtenerPrendasConStock();

        for (Beneficiario beneficiario : beneficiarios) {
            cmbBeneficiarios.addItem(beneficiario);
        }

        for (Asociacion asociacion : asociaciones) {
            cmbAsociaciones.addItem(asociacion);
        }

        for (Prenda prenda : prendas) {
            cmbPrendas.addItem(prenda);
        }
    }

    private void registrarEntrega() {
        String cantidadTexto = txtCantidad.getText().trim();
        String fechaTexto = txtFecha.getText().trim();

        if (!validarCampos(cantidadTexto, fechaTexto)) {
            return;
        }

        Beneficiario beneficiario = (Beneficiario) cmbBeneficiarios.getSelectedItem();
        Asociacion asociacion = (Asociacion) cmbAsociaciones.getSelectedItem();
        Prenda prenda = (Prenda) cmbPrendas.getSelectedItem();

        if (beneficiario == null) {
            mostrarAdvertencia("Debe seleccionar un beneficiario.");
            return;
        }

        if (asociacion == null) {
            mostrarAdvertencia("Debe seleccionar una asociacion.");
            return;
        }

        if (prenda == null) {
            mostrarAdvertencia("Debe seleccionar una prenda con stock disponible.");
            return;
        }

        int cantidad = Integer.parseInt(cantidadTexto);

        if (cantidad > prenda.getStock()) {
            mostrarAdvertencia("No hay suficiente stock. Stock disponible: " + prenda.getStock());
            return;
        }

        Date fechaEntrega = Date.valueOf(fechaTexto);

        boolean entregaRegistrada = entregaDAO.registrarEntregaCompleta(
                beneficiario.getIdBeneficiario(),
                asociacion.getIdAsociacion(),
                fechaEntrega,
                prenda.getIdPrenda(),
                cantidad
        );

        if (entregaRegistrada) {
            JOptionPane.showMessageDialog(
                    this,
                    "Entrega registrada correctamente.",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );
            limpiarFormulario();
            cargarCombos();
        } else {
            mostrarError("No se pudo registrar la entrega. Verifica el stock disponible.");
        }
    }

    private boolean validarCampos(String cantidadTexto, String fechaTexto) {
        if (cantidadTexto.isEmpty() || fechaTexto.isEmpty()) {
            mostrarAdvertencia("La cantidad y la fecha son obligatorias.");
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
        txtCantidad.setText("");
        txtFecha.setText("");

        if (cmbBeneficiarios.getItemCount() > 0) {
            cmbBeneficiarios.setSelectedIndex(0);
        }

        if (cmbAsociaciones.getItemCount() > 0) {
            cmbAsociaciones.setSelectedIndex(0);
        }

        if (cmbPrendas.getItemCount() > 0) {
            cmbPrendas.setSelectedIndex(0);
        }
    }

    private void configurarCombo(JComboBox<?> combo) {
        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(320, 35));
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(320, 35));
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
