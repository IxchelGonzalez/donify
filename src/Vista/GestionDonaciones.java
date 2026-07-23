package Vista;

import DAO.DonacionDAO;
import Modelo.Asociacion;
import Modelo.Donador;
import Modelo.DonacionVista;
import Modelo.Prenda;
import java.awt.*;
import java.sql.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GestionDonaciones extends JPanel {

    private static final String VISTA_MENU = "menu";
    private static final String VISTA_CREAR = "crear";
    private static final String VISTA_CONSULTAR = "consultar";
    private static final String VISTA_ACTUALIZAR = "actualizar";
    private static final String VISTA_ELIMINAR = "eliminar";

    private final DonacionDAO donacionDAO;
    private CardLayout cardLayout;
    private JPanel panelContenido;

    private JButton btnMenuCrear, btnMenuConsultar, btnMenuActualizar, btnMenuEliminar;

    private JComboBox<Donador> cmbCrearDonador;
    private JComboBox<Asociacion> cmbCrearAsociacion;
    private JComboBox<String> cmbCrearTipoPrenda;
    private JComboBox<String> cmbCrearEstadoPrenda;
    private JTextField txtCrearCantidad, txtCrearFecha;
    private JButton btnCrearDonacion, btnCerrarCrear;

    private JTable tablaConsultar, tablaActualizar, tablaEliminar;
    private DefaultTableModel modeloConsultar, modeloActualizar, modeloEliminar;
    private List<DonacionVista> listaActualizar, listaEliminar;

    private JButton btnRefrescarConsultar, btnCerrarConsultar;

    private JTextField txtActualizarId;
    private JComboBox<Donador> cmbActualizarDonador;
    private JComboBox<Asociacion> cmbActualizarAsociacion;
    private JComboBox<String> cmbActualizarTipoPrenda;
    private JComboBox<String> cmbActualizarEstadoPrenda;
    private JTextField txtActualizarCantidad, txtActualizarFecha;
    private JButton btnActualizarDonacion, btnCerrarActualizar;

    private JButton btnEliminarDonacion, btnCerrarEliminar;

    public GestionDonaciones() {
        donacionDAO = new DonacionDAO();
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

        btnCrearDonacion.addActionListener(e -> crearDonacion());
        btnCerrarCrear.addActionListener(e -> {
            limpiarFormularioCrear();
            mostrarVista(VISTA_MENU);
        });

        btnRefrescarConsultar.addActionListener(e -> cargarTablaConsultar());
        btnCerrarConsultar.addActionListener(e -> mostrarVista(VISTA_MENU));

        tablaActualizar.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosDonacionSeleccionada();
            }
        });

        btnActualizarDonacion.addActionListener(e -> actualizarDonacion());
        btnCerrarActualizar.addActionListener(e -> {
            limpiarFormularioActualizar();
            mostrarVista(VISTA_MENU);
        });

        btnEliminarDonacion.addActionListener(e -> eliminarDonacion());
        btnCerrarEliminar.addActionListener(e -> mostrarVista(VISTA_MENU));
    }

    private JPanel crearPanelMenu() {
        JPanel panel = crearPanelBase();

        panel.add(crearEncabezado(
                "Gestion de donaciones",
                "Selecciona la accion que deseas realizar."
        ), BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 18, 18));
        panelBotones.setBackground(new Color(245, 247, 250));

        btnMenuCrear = crearBotonMenu("Registrar donacion", new Color(40, 120, 210), Color.WHITE);
        btnMenuConsultar = crearBotonMenu("Consultar donaciones", new Color(80, 145, 100), Color.WHITE);
        btnMenuActualizar = crearBotonMenu("Actualizar donacion", new Color(230, 150, 55), Color.WHITE);
        btnMenuEliminar = crearBotonMenu("Eliminar donacion", new Color(200, 70, 70), Color.WHITE);

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
                "Registrar donacion",
                "Selecciona al donador y registra la donacion de ropa."
        ), BorderLayout.NORTH);

        cmbCrearDonador = crearComboDonador();
        cmbCrearAsociacion = crearComboAsociacion();
        cmbCrearTipoPrenda = crearComboTipoPrenda();
        cmbCrearEstadoPrenda = crearComboEstadoPrenda();
        txtCrearCantidad = crearCampoTexto();
        txtCrearFecha = crearCampoTexto();

        JPanel formulario = crearFormulario();
        agregarFilaFormulario(formulario, 0, "Donador", cmbCrearDonador);
        agregarFilaFormulario(formulario, 1, "Asociacion", cmbCrearAsociacion);
        agregarFilaFormulario(formulario, 2, "Tipo de prenda", cmbCrearTipoPrenda);
        agregarFilaFormulario(formulario, 3, "Estado de la prenda", cmbCrearEstadoPrenda);
        agregarFilaFormulario(formulario, 4, "Cantidad", txtCrearCantidad);
        agregarFilaFormulario(formulario, 5, "Fecha donacion (AAAA-MM-DD)", txtCrearFecha);

        btnCrearDonacion = crearBoton("Registrar donacion", new Color(40, 120, 210), Color.WHITE);
        btnCerrarCrear = crearBoton("Cerrar", new Color(220, 224, 230), new Color(50, 50, 50));

        panel.add(formulario, BorderLayout.CENTER);
        panel.add(crearPanelBotones(btnCrearDonacion, btnCerrarCrear), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelConsultar() {
        JPanel panel = crearPanelBase();

        panel.add(crearEncabezado(
                "Consultar donaciones",
                "Lista de donaciones registradas."
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
                "Actualizar donacion",
                "Selecciona una donacion y modifica sus datos."
        ), BorderLayout.NORTH);

        modeloActualizar = crearModeloTabla();
        tablaActualizar = new JTable(modeloActualizar);
        configurarTabla(tablaActualizar);

        txtActualizarId = crearCampoTexto();
        txtActualizarId.setEditable(false);
        cmbActualizarDonador = crearComboDonador();
        cmbActualizarAsociacion = crearComboAsociacion();
        cmbActualizarTipoPrenda = crearComboTipoPrenda();
        cmbActualizarEstadoPrenda = crearComboEstadoPrenda();
        txtActualizarCantidad = crearCampoTexto();
        txtActualizarFecha = crearCampoTexto();

        JPanel formulario = crearFormulario();
        agregarFilaFormulario(formulario, 0, "ID", txtActualizarId);
        agregarFilaFormulario(formulario, 1, "Donador", cmbActualizarDonador);
        agregarFilaFormulario(formulario, 2, "Asociacion", cmbActualizarAsociacion);
        agregarFilaFormulario(formulario, 3, "Tipo de prenda", cmbActualizarTipoPrenda);
        agregarFilaFormulario(formulario, 4, "Estado de la prenda", cmbActualizarEstadoPrenda);
        agregarFilaFormulario(formulario, 5, "Cantidad", txtActualizarCantidad);
        agregarFilaFormulario(formulario, 6, "Fecha donacion (AAAA-MM-DD)", txtActualizarFecha);

        JScrollPane scrollTabla = new JScrollPane(tablaActualizar);
        scrollTabla.setPreferredSize(new Dimension(700, 150));

        JPanel centro = new JPanel(new BorderLayout(0, 15));
        centro.setBackground(new Color(245, 247, 250));
        centro.add(scrollTabla, BorderLayout.NORTH);
        centro.add(formulario, BorderLayout.CENTER);

        JScrollPane scrollCentro = new JScrollPane(centro);
        scrollCentro.setBorder(BorderFactory.createEmptyBorder());
        scrollCentro.getVerticalScrollBar().setUnitIncrement(16);
        scrollCentro.getViewport().setBackground(new Color(245, 247, 250));

        btnActualizarDonacion = crearBoton("Actualizar donacion", new Color(230, 150, 55), Color.WHITE);
        btnCerrarActualizar = crearBoton("Cerrar", new Color(220, 224, 230), new Color(50, 50, 50));

        panel.add(scrollCentro, BorderLayout.CENTER);
        panel.add(crearPanelBotones(btnActualizarDonacion, btnCerrarActualizar), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelEliminar() {
        JPanel panel = crearPanelBase();

        panel.add(crearEncabezado(
                "Eliminar donacion",
                "Selecciona una donacion y confirma la eliminacion."
        ), BorderLayout.NORTH);

        modeloEliminar = crearModeloTabla();
        tablaEliminar = new JTable(modeloEliminar);
        configurarTabla(tablaEliminar);

        btnEliminarDonacion = crearBoton("Eliminar donacion", new Color(200, 70, 70), Color.WHITE);
        btnCerrarEliminar = crearBoton("Cerrar", new Color(220, 224, 230), new Color(50, 50, 50));

        panel.add(new JScrollPane(tablaEliminar), BorderLayout.CENTER);
        panel.add(crearPanelBotones(btnEliminarDonacion, btnCerrarEliminar), BorderLayout.SOUTH);

        return panel;
    }

    private void crearDonacion() {
        Donador donador = (Donador) cmbCrearDonador.getSelectedItem();
        Asociacion asociacion = (Asociacion) cmbCrearAsociacion.getSelectedItem();
        String tipoPrenda = obtenerTextoCombo(cmbCrearTipoPrenda);
        String estadoPrenda = (String) cmbCrearEstadoPrenda.getSelectedItem();
        String cantidadTexto = txtCrearCantidad.getText().trim();
        String fechaTexto = txtCrearFecha.getText().trim();

        if (!validarSelecciones(donador, asociacion, tipoPrenda)) {
            return;
        }

        if (!validarCampos(cantidadTexto, fechaTexto)) {
            return;
        }

        int cantidad = Integer.parseInt(cantidadTexto);
        Date fechaDonacion = Date.valueOf(fechaTexto);

        int idPrenda = donacionDAO.obtenerOCrearIdPrenda(tipoPrenda, estadoPrenda);

        if (idPrenda == 0) {
            mostrarError("No se pudo registrar el tipo de prenda.");
            return;
        }

        boolean donacionRegistrada = donacionDAO.registrarDonacionCompleta(
                donador.getIdDonador(),
                asociacion.getIdAsociacion(),
                fechaDonacion,
                idPrenda,
                cantidad
        );

        if (donacionRegistrada) {
            JOptionPane.showMessageDialog(this, "Donacion registrada correctamente.");
            limpiarFormularioCrear();
        } else {
            mostrarError("No se pudo registrar la donacion.");
        }
    }

    private void actualizarDonacion() {
        if (txtActualizarId.getText().trim().isEmpty()) {
            mostrarAdvertencia("Selecciona una donacion de la tabla.");
            return;
        }

        int fila = tablaActualizar.getSelectedRow();

        if (fila == -1) {
            mostrarAdvertencia("Selecciona una donacion de la tabla.");
            return;
        }

        int filaModelo = tablaActualizar.convertRowIndexToModel(fila);
        DonacionVista donacionOriginal = listaActualizar.get(filaModelo);

        Donador donador = (Donador) cmbActualizarDonador.getSelectedItem();
        Asociacion asociacion = (Asociacion) cmbActualizarAsociacion.getSelectedItem();
        String tipoPrenda = obtenerTextoCombo(cmbActualizarTipoPrenda);
        String estadoPrenda = (String) cmbActualizarEstadoPrenda.getSelectedItem();
        String cantidadTexto = txtActualizarCantidad.getText().trim();
        String fechaTexto = txtActualizarFecha.getText().trim();

        if (!validarSelecciones(donador, asociacion, tipoPrenda)) {
            return;
        }

        if (!validarCampos(cantidadTexto, fechaTexto)) {
            return;
        }

        int cantidad = Integer.parseInt(cantidadTexto);
        Date fechaDonacion = Date.valueOf(fechaTexto);

        int idPrendaNueva = donacionDAO.obtenerOCrearIdPrenda(tipoPrenda, estadoPrenda);

        if (idPrendaNueva == 0) {
            mostrarError("No se pudo registrar el tipo de prenda.");
            return;
        }

        boolean actualizado = donacionDAO.actualizarDonacionCompleta(
                donacionOriginal.getIdDonacionIngreso(),
                donacionOriginal.getIdDonacionDetalle(),
                donador.getIdDonador(),
                asociacion.getIdAsociacion(),
                fechaDonacion,
                donacionOriginal.getIdPrenda(),
                donacionOriginal.getCantidad(),
                idPrendaNueva,
                cantidad
        );

        if (actualizado) {
            JOptionPane.showMessageDialog(this, "Donacion actualizada correctamente.");
            limpiarFormularioActualizar();
            cargarTablaActualizar();
        } else {
            mostrarError("No se pudo actualizar la donacion.");
        }
    }

    private void eliminarDonacion() {
        int fila = tablaEliminar.getSelectedRow();

        if (fila == -1) {
            mostrarAdvertencia("Selecciona una donacion de la tabla.");
            return;
        }

        int filaModelo = tablaEliminar.convertRowIndexToModel(fila);
        DonacionVista donacion = listaEliminar.get(filaModelo);

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "Deseas eliminar la donacion de \"" + donacion.getNombreDonador() + "\"?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            boolean eliminado = donacionDAO.eliminarDonacionCompleta(
                    donacion.getIdDonacionIngreso(),
                    donacion.getIdDonacionDetalle(),
                    donacion.getIdPrenda(),
                    donacion.getCantidad()
            );

            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Donacion eliminada correctamente.");
                cargarTablaEliminar();
            } else {
                mostrarError("No se pudo eliminar la donacion.");
            }
        }
    }

    private boolean validarSelecciones(Donador donador, Asociacion asociacion, String tipoPrenda) {
        if (donador == null) {
            mostrarAdvertencia("Debe seleccionar un donador. Si no hay donadores registrados, "
                    + "solicita a un donador que se registre primero.");
            return false;
        }

        if (asociacion == null) {
            mostrarAdvertencia("Debe seleccionar una asociacion.");
            return false;
        }

        if (tipoPrenda == null || tipoPrenda.isEmpty()) {
            mostrarAdvertencia("Debe indicar el tipo de prenda.");
            return false;
        }

        return true;
    }

    private boolean validarCampos(String cantidadTexto, String fechaTexto) {
        if (cantidadTexto.isEmpty() || fechaTexto.isEmpty()) {
            mostrarAdvertencia("Todos los campos son obligatorios.");
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

    private void cargarDatosDonacionSeleccionada() {
        int fila = tablaActualizar.getSelectedRow();

        if (fila == -1) {
            return;
        }

        int filaModelo = tablaActualizar.convertRowIndexToModel(fila);
        DonacionVista donacion = listaActualizar.get(filaModelo);

        txtActualizarId.setText(String.valueOf(donacion.getIdDonacionIngreso()));
        seleccionarDonadorPorId(cmbActualizarDonador, donacion.getIdDonador());
        seleccionarAsociacionPorId(cmbActualizarAsociacion, donacion.getIdAsociacion());

        Prenda prenda = donacionDAO.obtenerPrendaPorId(donacion.getIdPrenda());

        if (prenda != null) {
            cmbActualizarTipoPrenda.setSelectedItem(prenda.getTipoPrenda());
            cmbActualizarEstadoPrenda.setSelectedItem(prenda.getEstadoPrenda());
        }

        txtActualizarCantidad.setText(String.valueOf(donacion.getCantidad()));
        txtActualizarFecha.setText(formatearFecha(donacion.getFechaDonacion()));
    }

    private void seleccionarDonadorPorId(JComboBox<Donador> combo, int idDonador) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getIdDonador() == idDonador) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarAsociacionPorId(JComboBox<Asociacion> combo, int idAsociacion) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getIdAsociacion() == idAsociacion) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void cargarTablaConsultar() {
        modeloConsultar.setRowCount(0);
        List<DonacionVista> donaciones = donacionDAO.obtenerDonacionesCompletas();

        for (DonacionVista donacion : donaciones) {
            modeloConsultar.addRow(new Object[]{
                donacion.getIdDonacionIngreso(),
                donacion.getNombreDonador(),
                donacion.getNombreAsociacion(),
                donacion.getPrenda(),
                donacion.getCantidad(),
                formatearFecha(donacion.getFechaDonacion())
            });
        }
    }

    private void cargarTablaActualizar() {
        modeloActualizar.setRowCount(0);
        listaActualizar = donacionDAO.obtenerDonacionesCompletas();

        for (DonacionVista donacion : listaActualizar) {
            modeloActualizar.addRow(new Object[]{
                donacion.getIdDonacionIngreso(),
                donacion.getNombreDonador(),
                donacion.getNombreAsociacion(),
                donacion.getPrenda(),
                donacion.getCantidad(),
                formatearFecha(donacion.getFechaDonacion())
            });
        }
    }

    private void cargarTablaEliminar() {
        modeloEliminar.setRowCount(0);
        listaEliminar = donacionDAO.obtenerDonacionesCompletas();

        for (DonacionVista donacion : listaEliminar) {
            modeloEliminar.addRow(new Object[]{
                donacion.getIdDonacionIngreso(),
                donacion.getNombreDonador(),
                donacion.getNombreAsociacion(),
                donacion.getPrenda(),
                donacion.getCantidad(),
                formatearFecha(donacion.getFechaDonacion())
            });
        }
    }

    private String formatearFecha(Date fecha) {
        return fecha == null ? "" : fecha.toString();
    }

    private void mostrarVista(String vista) {
        if (VISTA_CREAR.equals(vista)) {
            cargarComboDonador(cmbCrearDonador);
            cargarComboAsociacion(cmbCrearAsociacion);
            cargarComboTipoPrenda(cmbCrearTipoPrenda);
        }

        if (VISTA_CONSULTAR.equals(vista)) {
            cargarTablaConsultar();
        }

        if (VISTA_ACTUALIZAR.equals(vista)) {
            limpiarFormularioActualizar();
            cargarComboDonador(cmbActualizarDonador);
            cargarComboAsociacion(cmbActualizarAsociacion);
            cargarComboTipoPrenda(cmbActualizarTipoPrenda);
            cargarTablaActualizar();
        }

        if (VISTA_ELIMINAR.equals(vista)) {
            cargarTablaEliminar();
        }

        cardLayout.show(panelContenido, vista);
    }

    private void cargarComboDonador(JComboBox<Donador> combo) {
        Object seleccionado = combo.getSelectedItem();
        combo.removeAllItems();

        List<Donador> donadores = donacionDAO.obtenerDonadores();
        for (Donador donador : donadores) {
            combo.addItem(donador);
        }

        if (seleccionado != null) {
            combo.setSelectedItem(seleccionado);
        }
    }

    private void cargarComboAsociacion(JComboBox<Asociacion> combo) {
        Object seleccionado = combo.getSelectedItem();
        combo.removeAllItems();

        List<Asociacion> asociaciones = donacionDAO.obtenerAsociaciones();
        for (Asociacion asociacion : asociaciones) {
            combo.addItem(asociacion);
        }

        if (seleccionado != null) {
            combo.setSelectedItem(seleccionado);
        }
    }

    /**
     * Carga los tipos de prenda ya existentes como sugerencias; el combo es
     * editable para permitir capturar un tipo de prenda nuevo.
     */
    private void cargarComboTipoPrenda(JComboBox<String> combo) {
        Object seleccionado = combo.getSelectedItem();
        combo.removeAllItems();

        List<String> tipos = donacionDAO.obtenerTiposPrenda();
        for (String tipo : tipos) {
            combo.addItem(tipo);
        }

        if (seleccionado != null) {
            combo.setSelectedItem(seleccionado);
        } else {
            combo.setSelectedItem("");
        }
    }

    private String obtenerTextoCombo(JComboBox<String> combo) {
        Object seleccionado = combo.getEditor().getItem();
        return seleccionado == null ? "" : seleccionado.toString().trim();
    }

    private void limpiarFormularioCrear() {
        if (cmbCrearDonador.getItemCount() > 0) {
            cmbCrearDonador.setSelectedIndex(0);
        }

        if (cmbCrearAsociacion.getItemCount() > 0) {
            cmbCrearAsociacion.setSelectedIndex(0);
        }

        cmbCrearTipoPrenda.setSelectedItem("");
        cmbCrearEstadoPrenda.setSelectedIndex(0);

        txtCrearCantidad.setText("");
        txtCrearFecha.setText("");
    }

    private void limpiarFormularioActualizar() {
        txtActualizarId.setText("");
        txtActualizarCantidad.setText("");
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

    private JComboBox<Donador> crearComboDonador() {
        JComboBox<Donador> combo = new JComboBox<>();
        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(280, 35));
        return combo;
    }

    private JComboBox<Asociacion> crearComboAsociacion() {
        JComboBox<Asociacion> combo = new JComboBox<>();
        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(280, 35));
        return combo;
    }

    private JComboBox<String> crearComboTipoPrenda() {
        JComboBox<String> combo = new JComboBox<>();
        combo.setEditable(true);
        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(280, 35));
        return combo;
    }

    private JComboBox<String> crearComboEstadoPrenda() {
        JComboBox<String> combo = new JComboBox<>(new String[]{"Mala", "Buena", "Excelente"});
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
        return new DefaultTableModel(new String[]{"ID", "Donador", "Asociacion", "Prenda", "Cantidad", "Fecha"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columna) {
                if (columna == 0 || columna == 4) {
                    return Integer.class;
                }
                return String.class;
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
