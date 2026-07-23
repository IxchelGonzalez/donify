package Vista;

import DAO.EntregaDAO;
import DAO.InventarioDAO;
import Modelo.Asociacion;
import Modelo.Beneficiario;
import Modelo.EntregaSalida;
import Modelo.EntregaVista;
import Modelo.Prenda;
import Modelo.Usuario;
import java.awt.*;
import java.sql.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;

public class GestionEntregas extends JPanel {

    private static final String VISTA_MENU = "menu";
    private static final String VISTA_CREAR = "crear";
    private static final String VISTA_CONSULTAR = "consultar";
    private static final String VISTA_ACTUALIZAR = "actualizar";
    private static final String VISTA_ELIMINAR = "eliminar";

    private final EntregaDAO entregaDAO;
    private final InventarioDAO inventarioDAO;
    private final Usuario usuarioSesion;

    private CardLayout cardLayout;
    private JPanel panelContenido;

    private JButton btnMenuCrear, btnMenuConsultar, btnMenuActualizar, btnMenuEliminar;

    private JComboBox<Beneficiario> cmbCrearBeneficiario;
    private JComboBox<Asociacion> cmbCrearAsociacion;
    private JLabel lblCrearInstitucionFija;
    private JComboBox<Prenda> cmbCrearPrenda;
    private JTextField txtCrearCantidad, txtCrearFecha;
    private JButton btnCrearEntrega, btnCerrarCrear;

    private JTable tablaConsultar, tablaActualizar, tablaEliminar;
    private DefaultTableModel modeloConsultar, modeloActualizar, modeloEliminar;
    private List<EntregaVista> listaActualizar, listaEliminar;

    private JButton btnRefrescarConsultar, btnCerrarConsultar;

    private JTextField txtActualizarId;
    private JComboBox<Beneficiario> cmbActualizarBeneficiario;
    private JComboBox<Asociacion> cmbActualizarAsociacion;
    private JLabel lblActualizarInstitucionFija;
    private JComboBox<Prenda> cmbActualizarPrenda;
    private JTextField txtActualizarCantidad, txtActualizarFecha;
    private JComboBox<String> cmbActualizarEstado;
    private JButton btnActualizarEntrega, btnCerrarActualizar;

    private JButton btnEliminarEntrega, btnCerrarEliminar;

    public GestionEntregas() {
        this(null);
    }

    public GestionEntregas(Usuario usuarioSesion) {
        this.usuarioSesion = usuarioSesion;
        entregaDAO = new EntregaDAO();
        inventarioDAO = new InventarioDAO();
        configurarPanel();
        crearComponentes();
        agregarComponentes();
        configurarEventos();
    }

    private boolean esInstitucion() {
        return usuarioSesion != null
                && "institucion".equalsIgnoreCase(usuarioSesion.getTipoUsuario())
                && usuarioSesion.tieneAsociacion();
    }

    private Integer idAsociacionSesion() {
        return esInstitucion() ? usuarioSesion.getIdAsociacion() : null;
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

        cmbCrearAsociacion.addActionListener(e -> cargarComboPrenda(cmbCrearPrenda, cmbCrearAsociacion));
        btnCrearEntrega.addActionListener(e -> crearEntrega());
        btnCerrarCrear.addActionListener(e -> {
            limpiarFormularioCrear();
            mostrarVista(VISTA_MENU);
        });

        btnRefrescarConsultar.addActionListener(e -> cargarTablaConsultar());
        btnCerrarConsultar.addActionListener(e -> mostrarVista(VISTA_MENU));

        tablaActualizar.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosEntregaSeleccionada();
            }
        });

        cmbActualizarAsociacion.addActionListener(e -> cargarComboPrenda(cmbActualizarPrenda, cmbActualizarAsociacion));
        btnActualizarEntrega.addActionListener(e -> actualizarEntrega());
        btnCerrarActualizar.addActionListener(e -> {
            limpiarFormularioActualizar();
            mostrarVista(VISTA_MENU);
        });

        btnEliminarEntrega.addActionListener(e -> eliminarEntrega());
        btnCerrarEliminar.addActionListener(e -> mostrarVista(VISTA_MENU));
    }

    private JPanel crearPanelMenu() {
        JPanel panel = crearPanelBase();

        panel.add(crearEncabezado(
                "Gestion de entregas",
                "Selecciona la accion que deseas realizar."
        ), BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 18, 18));
        panelBotones.setBackground(new Color(245, 247, 250));

        btnMenuCrear = crearBotonMenu("Registrar entrega", new Color(40, 120, 210), Color.WHITE);
        btnMenuConsultar = crearBotonMenu("Consultar entregas", new Color(80, 145, 100), Color.WHITE);
        btnMenuActualizar = crearBotonMenu("Actualizar entrega", new Color(230, 150, 55), Color.WHITE);
        btnMenuEliminar = crearBotonMenu("Eliminar entrega", new Color(200, 70, 70), Color.WHITE);

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
                "Registrar entrega",
                "Registra la entrega de prendas a un beneficiario."
        ), BorderLayout.NORTH);

        cmbCrearBeneficiario = crearComboBeneficiario();
        cmbCrearAsociacion = crearComboAsociacion();
        cmbCrearPrenda = crearComboPrenda();
        txtCrearCantidad = crearCampoTexto();
        txtCrearFecha = crearCampoTexto();

        lblCrearInstitucionFija = crearCampoInstitucionFija();

        JPanel formulario = crearFormulario();
        agregarFilaFormulario(formulario, 0, "Beneficiario", cmbCrearBeneficiario);

        if (esInstitucion()) {
            agregarFilaFormulario(formulario, 1, "Institucion", lblCrearInstitucionFija);
        } else {
            agregarFilaFormulario(formulario, 1, "Institucion", cmbCrearAsociacion);
        }

        agregarFilaFormulario(formulario, 2, "Prenda", cmbCrearPrenda);
        agregarFilaFormulario(formulario, 3, "Cantidad", txtCrearCantidad);
        agregarFilaFormulario(formulario, 4, "Fecha entrega (AAAA-MM-DD)", txtCrearFecha);

        btnCrearEntrega = crearBoton("Registrar entrega", new Color(40, 120, 210), Color.WHITE);
        btnCerrarCrear = crearBoton("Cerrar", new Color(220, 224, 230), new Color(50, 50, 50));

        panel.add(formulario, BorderLayout.CENTER);
        panel.add(crearPanelBotones(btnCrearEntrega, btnCerrarCrear), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelConsultar() {
        JPanel panel = crearPanelBase();

        panel.add(crearEncabezado(
                "Consultar entregas",
                esInstitucion() ? "Entregas realizadas por tu institucion." : "Lista de entregas registradas."
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
                "Actualizar entrega",
                "Solo se muestran entregas que aun estan pendientes."
        ), BorderLayout.NORTH);

        modeloActualizar = crearModeloTabla();
        tablaActualizar = new JTable(modeloActualizar);
        configurarTabla(tablaActualizar);

        txtActualizarId = crearCampoTexto();
        txtActualizarId.setEditable(false);
        cmbActualizarBeneficiario = crearComboBeneficiario();
        cmbActualizarAsociacion = crearComboAsociacion();
        lblActualizarInstitucionFija = crearCampoInstitucionFija();
        cmbActualizarPrenda = crearComboPrenda();
        txtActualizarCantidad = crearCampoTexto();
        txtActualizarFecha = crearCampoTexto();
        cmbActualizarEstado = new JComboBox<>(new String[]{
            EntregaSalida.ESTADO_PENDIENTE, EntregaSalida.ESTADO_ENTREGADA
        });
        cmbActualizarEstado.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cmbActualizarEstado.setPreferredSize(new Dimension(280, 35));

        JPanel formulario = crearFormulario();
        agregarFilaFormulario(formulario, 0, "ID", txtActualizarId);
        agregarFilaFormulario(formulario, 1, "Beneficiario", cmbActualizarBeneficiario);

        if (esInstitucion()) {
            agregarFilaFormulario(formulario, 2, "Institucion", lblActualizarInstitucionFija);
        } else {
            agregarFilaFormulario(formulario, 2, "Institucion", cmbActualizarAsociacion);
        }

        agregarFilaFormulario(formulario, 3, "Prenda", cmbActualizarPrenda);
        agregarFilaFormulario(formulario, 4, "Cantidad", txtActualizarCantidad);
        agregarFilaFormulario(formulario, 5, "Fecha entrega (AAAA-MM-DD)", txtActualizarFecha);
        agregarFilaFormulario(formulario, 6, "Estado", cmbActualizarEstado);

        JScrollPane scrollTablaActualizar
                = new JScrollPane(tablaActualizar);

        tablaActualizar.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaActualizar.setFillsViewportHeight(true);

        JSplitPane centro = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                scrollTablaActualizar,
                formulario
        );

        centro.setResizeWeight(0.35);
        centro.setDividerLocation(170);
        centro.setDividerSize(6);
        centro.setContinuousLayout(true);
        centro.setBorder(null);
        centro.setBackground(new Color(245, 247, 250));

        btnActualizarEntrega = crearBoton("Actualizar entrega", new Color(230, 150, 55), Color.WHITE);
        btnCerrarActualizar = crearBoton("Cerrar", new Color(220, 224, 230), new Color(50, 50, 50));

        panel.add(centro, BorderLayout.CENTER);
        panel.add(crearPanelBotones(btnActualizarEntrega, btnCerrarActualizar), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelEliminar() {
        JPanel panel = crearPanelBase();

        panel.add(crearEncabezado(
                "Eliminar entrega",
                "Solo se pueden eliminar entregas que aun estan pendientes."
        ), BorderLayout.NORTH);

        modeloEliminar = crearModeloTabla();
        tablaEliminar = new JTable(modeloEliminar);
        configurarTabla(tablaEliminar);

        btnEliminarEntrega = crearBoton("Eliminar entrega", new Color(200, 70, 70), Color.WHITE);
        btnCerrarEliminar = crearBoton("Cerrar", new Color(220, 224, 230), new Color(50, 50, 50));

        panel.add(new JScrollPane(tablaEliminar), BorderLayout.CENTER);
        panel.add(crearPanelBotones(btnEliminarEntrega, btnCerrarEliminar), BorderLayout.SOUTH);

        return panel;
    }

    private void crearEntrega() {
        Beneficiario beneficiario = (Beneficiario) cmbCrearBeneficiario.getSelectedItem();
        Prenda prenda = (Prenda) cmbCrearPrenda.getSelectedItem();
        String cantidadTexto = txtCrearCantidad.getText().trim();
        String fechaTexto = txtCrearFecha.getText().trim();

        int idAsociacion = esInstitucion()
                ? usuarioSesion.getIdAsociacion()
                : obtenerIdAsociacionSeleccionada(cmbCrearAsociacion);

        if (beneficiario == null) {
            mostrarAdvertencia("Debe seleccionar un beneficiario.");
            return;
        }

        if (idAsociacion == 0) {
            mostrarAdvertencia("Debe seleccionar una institucion.");
            return;
        }

        if (prenda == null) {
            mostrarAdvertencia("Debe seleccionar una prenda con stock disponible.");
            return;
        }

        if (!validarCampos(cantidadTexto, fechaTexto)) {
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
                idAsociacion,
                fechaEntrega,
                prenda.getIdPrenda(),
                cantidad
        );

        if (entregaRegistrada) {
            JOptionPane.showMessageDialog(this, "Entrega registrada correctamente.");
            limpiarFormularioCrear();
        } else {
            mostrarError("No se pudo registrar la entrega. Verifica el stock disponible.");
        }
    }

    private void actualizarEntrega() {
        if (txtActualizarId.getText().trim().isEmpty()) {
            mostrarAdvertencia("Selecciona una entrega de la tabla.");
            return;
        }

        int fila = tablaActualizar.getSelectedRow();

        if (fila == -1) {
            mostrarAdvertencia("Selecciona una entrega de la tabla.");
            return;
        }

        int filaModelo = tablaActualizar.convertRowIndexToModel(fila);
        EntregaVista entregaOriginal = listaActualizar.get(filaModelo);

        Beneficiario beneficiario = (Beneficiario) cmbActualizarBeneficiario.getSelectedItem();
        Prenda prenda = (Prenda) cmbActualizarPrenda.getSelectedItem();
        String cantidadTexto = txtActualizarCantidad.getText().trim();
        String fechaTexto = txtActualizarFecha.getText().trim();
        String nuevoEstado = (String) cmbActualizarEstado.getSelectedItem();

        int idAsociacion = esInstitucion()
                ? usuarioSesion.getIdAsociacion()
                : obtenerIdAsociacionSeleccionada(cmbActualizarAsociacion);

        if (beneficiario == null) {
            mostrarAdvertencia("Debe seleccionar un beneficiario.");
            return;
        }

        if (idAsociacion == 0) {
            mostrarAdvertencia("Debe seleccionar una institucion.");
            return;
        }

        if (prenda == null) {
            mostrarAdvertencia("Debe seleccionar una prenda.");
            return;
        }

        if (!validarCampos(cantidadTexto, fechaTexto)) {
            return;
        }

        int cantidad = Integer.parseInt(cantidadTexto);
        Date fechaEntrega = Date.valueOf(fechaTexto);

        boolean actualizado = entregaDAO.actualizarEntregaCompleta(
                entregaOriginal.getIdEntregaSalida(),
                entregaOriginal.getIdEntregaDetalle(),
                beneficiario.getIdBeneficiario(),
                idAsociacion,
                fechaEntrega,
                nuevoEstado,
                entregaOriginal.getIdPrenda(),
                entregaOriginal.getCantidad(),
                prenda.getIdPrenda(),
                cantidad
        );

        if (actualizado) {
            JOptionPane.showMessageDialog(this, "Entrega actualizada correctamente.");
            limpiarFormularioActualizar();
            cargarTablaActualizar();
        } else {
            mostrarError("No se pudo actualizar la entrega. Verifica que siga pendiente y que haya stock suficiente.");
        }
    }

    private void eliminarEntrega() {
        int fila = tablaEliminar.getSelectedRow();

        if (fila == -1) {
            mostrarAdvertencia("Selecciona una entrega de la tabla.");
            return;
        }

        int filaModelo = tablaEliminar.convertRowIndexToModel(fila);
        EntregaVista entrega = listaEliminar.get(filaModelo);

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "Deseas eliminar la entrega a \"" + entrega.getNombreBeneficiario() + "\"?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            boolean eliminado = entregaDAO.eliminarEntregaCompleta(
                    entrega.getIdEntregaSalida(),
                    entrega.getIdEntregaDetalle(),
                    entrega.getIdPrenda(),
                    entrega.getCantidad()
            );

            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Entrega eliminada correctamente. El stock fue devuelto.");
                cargarTablaEliminar();
            } else {
                mostrarError("No se pudo eliminar la entrega. Verifica que siga pendiente.");
            }
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

    private void cargarDatosEntregaSeleccionada() {
        int fila = tablaActualizar.getSelectedRow();

        if (fila == -1) {
            return;
        }

        int filaModelo = tablaActualizar.convertRowIndexToModel(fila);
        EntregaVista entrega = listaActualizar.get(filaModelo);

        txtActualizarId.setText(String.valueOf(entrega.getIdEntregaSalida()));
        seleccionarBeneficiarioPorId(cmbActualizarBeneficiario, entrega.getIdBeneficiario());

        if (!esInstitucion()) {
            seleccionarAsociacionPorId(cmbActualizarAsociacion, entrega.getIdAsociacion());
        }

        asegurarPrendaEnCombo(cmbActualizarPrenda, entrega.getIdPrenda());
        seleccionarPrendaPorId(cmbActualizarPrenda, entrega.getIdPrenda());
        txtActualizarCantidad.setText(String.valueOf(entrega.getCantidad()));
        txtActualizarFecha.setText(formatearFecha(entrega.getFechaEntrega()));
        cmbActualizarEstado.setSelectedItem(entrega.getEstado());
    }

    private void asegurarPrendaEnCombo(JComboBox<Prenda> combo, int idPrenda) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getIdPrenda() == idPrenda) {
                return;
            }
        }

        Prenda prenda = entregaDAO.obtenerPrendaPorId(idPrenda);

        if (prenda != null) {
            combo.addItem(prenda);
        }
    }

    private void seleccionarBeneficiarioPorId(JComboBox<Beneficiario> combo, int idBeneficiario) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getIdBeneficiario() == idBeneficiario) {
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

    private void seleccionarPrendaPorId(JComboBox<Prenda> combo, int idPrenda) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getIdPrenda() == idPrenda) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private int obtenerIdAsociacionSeleccionada(JComboBox<Asociacion> combo) {
        Asociacion asociacion = (Asociacion) combo.getSelectedItem();
        return asociacion == null ? 0 : asociacion.getIdAsociacion();
    }

    private void cargarTablaConsultar() {
        modeloConsultar.setRowCount(0);
        List<EntregaVista> entregas = entregaDAO.obtenerEntregasCompletas(idAsociacionSesion());

        for (EntregaVista entrega : entregas) {
            modeloConsultar.addRow(filaDeEntrega(entrega));
        }
    }

    private void cargarTablaActualizar() {
        modeloActualizar.setRowCount(0);
        listaActualizar = filtrarPendientes(entregaDAO.obtenerEntregasCompletas(idAsociacionSesion()));

        for (EntregaVista entrega : listaActualizar) {
            modeloActualizar.addRow(filaDeEntrega(entrega));
        }
    }

    private void cargarTablaEliminar() {
        modeloEliminar.setRowCount(0);
        listaEliminar = filtrarPendientes(entregaDAO.obtenerEntregasCompletas(idAsociacionSesion()));

        for (EntregaVista entrega : listaEliminar) {
            modeloEliminar.addRow(filaDeEntrega(entrega));
        }
    }

    private List<EntregaVista> filtrarPendientes(List<EntregaVista> entregas) {
        List<EntregaVista> pendientes = new java.util.ArrayList<>();

        for (EntregaVista entrega : entregas) {
            if (entrega.esPendiente()) {
                pendientes.add(entrega);
            }
        }

        return pendientes;
    }

    private Object[] filaDeEntrega(EntregaVista entrega) {
        return new Object[]{
            entrega.getIdEntregaSalida(),
            entrega.getNombreBeneficiario(),
            entrega.getNombreAsociacion(),
            entrega.getPrenda(),
            entrega.getCantidad(),
            formatearFecha(entrega.getFechaEntrega()),
            entrega.getEstado()
        };
    }

    private String formatearFecha(Date fecha) {
        return fecha == null ? "" : fecha.toString();
    }

    private void mostrarVista(String vista) {
        if (VISTA_CREAR.equals(vista)) {
            cargarComboBeneficiario(cmbCrearBeneficiario);
            cargarComboAsociacion(cmbCrearAsociacion);
            cargarComboPrenda(cmbCrearPrenda, cmbCrearAsociacion);
        }

        if (VISTA_CONSULTAR.equals(vista)) {
            cargarTablaConsultar();
        }

        if (VISTA_ACTUALIZAR.equals(vista)) {
            cargarComboBeneficiario(cmbActualizarBeneficiario);
            cargarComboAsociacion(cmbActualizarAsociacion);
            cargarComboPrenda(cmbActualizarPrenda, cmbActualizarAsociacion);
            cargarTablaActualizar();
            limpiarFormularioActualizar();

        }

        if (VISTA_ELIMINAR.equals(vista)) {
            cargarTablaEliminar();
        }

        cardLayout.show(panelContenido, vista);
    }

    private void cargarComboBeneficiario(JComboBox<Beneficiario> combo) {
        Object seleccionado = combo.getSelectedItem();
        combo.removeAllItems();

        for (Beneficiario beneficiario : entregaDAO.obtenerBeneficiarios()) {
            combo.addItem(beneficiario);
        }

        if (seleccionado != null) {
            combo.setSelectedItem(seleccionado);
        }
    }

    private void cargarComboAsociacion(JComboBox<Asociacion> combo) {
        Object seleccionado = combo.getSelectedItem();
        combo.removeAllItems();

        for (Asociacion asociacion : entregaDAO.obtenerAsociaciones()) {
            combo.addItem(asociacion);
        }

        if (seleccionado != null) {
            combo.setSelectedItem(seleccionado);
        }
    }

    /**
     * Carga en el combo solo las prendas con stock disponible de la institucion
     * correspondiente: la de la sesion si el usuario es de tipo institucion, o
     * la seleccionada en comboAsociacion en caso contrario. Si no hay
     * institucion seleccionada, el combo queda vacio.
     */
    private void cargarComboPrenda(JComboBox<Prenda> combo, JComboBox<Asociacion> comboAsociacion) {
        Object seleccionado = combo.getSelectedItem();
        combo.removeAllItems();

        Integer idAsociacion;

        if (esInstitucion()) {
            idAsociacion = usuarioSesion.getIdAsociacion();
        } else {
            idAsociacion = obtenerIdAsociacionSeleccionadaOpcional(comboAsociacion);
        }

        if (idAsociacion != null) {
            List<Prenda> prendas = inventarioDAO.obtenerInventarioPorAsociacion(idAsociacion, null, 1);

            for (Prenda prenda : prendas) {
                combo.addItem(prenda);
            }
        }

        if (seleccionado != null) {
            combo.setSelectedItem(seleccionado);
        }
    }

    private Integer obtenerIdAsociacionSeleccionadaOpcional(JComboBox<Asociacion> combo) {
        Asociacion asociacion = (Asociacion) combo.getSelectedItem();
        return asociacion == null ? null : asociacion.getIdAsociacion();
    }

    private void limpiarFormularioCrear() {
        if (cmbCrearBeneficiario.getItemCount() > 0) {
            cmbCrearBeneficiario.setSelectedIndex(0);
        }

        if (cmbCrearAsociacion.getItemCount() > 0) {
            cmbCrearAsociacion.setSelectedIndex(0);
        }

        cargarComboPrenda(cmbCrearPrenda, cmbCrearAsociacion);

        if (cmbCrearPrenda.getItemCount() > 0) {
            cmbCrearPrenda.setSelectedIndex(0);
        }

        txtCrearCantidad.setText("");
        txtCrearFecha.setText("");
    }

    private void limpiarFormularioActualizar() {
        txtActualizarId.setText("");
        txtActualizarCantidad.setText("");
        txtActualizarFecha.setText("");

        tablaActualizar.clearSelection();

        if (cmbActualizarBeneficiario.getItemCount() > 0) {
            cmbActualizarBeneficiario.setSelectedIndex(-1);
        }

        if (cmbActualizarAsociacion.getItemCount() > 0) {
            cmbActualizarAsociacion.setSelectedIndex(-1);
        }

        cmbActualizarPrenda.removeAllItems();
        cmbActualizarPrenda.setSelectedIndex(-1);

        cmbActualizarEstado.setSelectedIndex(-1);
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

    private JLabel crearCampoInstitucionFija() {
        String nombre = "Tu institucion";

        if (esInstitucion()) {
            for (Asociacion asociacion : entregaDAO.obtenerAsociaciones()) {
                if (asociacion.getIdAsociacion() == usuarioSesion.getIdAsociacion()) {
                    nombre = asociacion.getNombre();
                    break;
                }
            }
        }

        JLabel etiqueta = new JLabel(nombre);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 14));
        etiqueta.setForeground(new Color(35, 75, 120));
        etiqueta.setPreferredSize(new Dimension(280, 35));
        return etiqueta;
    }

    private JComboBox<Beneficiario> crearComboBeneficiario() {
        JComboBox<Beneficiario> combo = new JComboBox<>();
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

    private JComboBox<Prenda> crearComboPrenda() {
        JComboBox<Prenda> combo = new JComboBox<>();
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
        return new DefaultTableModel(
                new String[]{"ID", "Beneficiario", "Institucion", "Prenda", "Cantidad", "Fecha", "Estado"}, 0) {
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
