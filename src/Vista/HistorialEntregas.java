package Vista;

import DAO.AsociacionDAO;
import DAO.HistorialEntregasDAO;
import Modelo.Asociacion;
import Modelo.Usuario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class HistorialEntregas extends JPanel {

    private HistorialEntregasDAO historialEntregasDAO;
    private AsociacionDAO asociacionDAO;

    private final Usuario usuarioSesion;

    private JLabel lblTitulo;
    private JLabel lblDescripcion;

    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;

    private JComboBox<Asociacion> cmbAsociacion;
    private JComboBox<String> cmbAnio;
    private JComboBox<String> cmbMes;

    private JButton btnBuscar;
    private JButton btnLimpiar;
    private JButton btnActualizar;
    private JButton btnCerrar;

    private static final String[] MESES = {
        "Todos",
        "Enero",
        "Febrero",
        "Marzo",
        "Abril",
        "Mayo",
        "Junio",
        "Julio",
        "Agosto",
        "Septiembre",
        "Octubre",
        "Noviembre",
        "Diciembre"
    };

    public HistorialEntregas() {
        this(null);
    }

    public HistorialEntregas(Usuario usuarioSesion) {
        this.usuarioSesion = usuarioSesion;

        historialEntregasDAO =
                new HistorialEntregasDAO();

        asociacionDAO =
                new AsociacionDAO();

        configurarPanel();
        crearComponentes();
        agregarComponentes();
        configurarEventos();

        cargarAsociaciones();
        cargarAnios();
        cargarHistorial();
    }

    private boolean esInstitucion() {
        return usuarioSesion != null
                && "institucion".equalsIgnoreCase(
                        usuarioSesion.getTipoUsuario()
                )
                && usuarioSesion.tieneAsociacion();
    }

    private Integer idAsociacionSesion() {
        return esInstitucion()
                ? usuarioSesion.getIdAsociacion()
                : null;
    }

    private void configurarPanel() {
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 247, 250));

        setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        35,
                        25,
                        35
                )
        );
    }

    private void crearComponentes() {
        lblTitulo = new JLabel(
                "Historial de entregas",
                SwingConstants.CENTER
        );

        lblTitulo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        28
                )
        );

        lblTitulo.setForeground(
                new Color(35, 75, 120)
        );

        lblDescripcion = new JLabel(
                "Consulta las entregas realizadas a beneficiarios.",
                SwingConstants.CENTER
        );

        lblDescripcion.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        lblDescripcion.setForeground(
                new Color(90, 90, 90)
        );

        modeloTabla = crearModeloVacio();

        tablaHistorial =
                new JTable(modeloTabla);

        tablaHistorial.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        tablaHistorial.setRowHeight(28);

        tablaHistorial.getTableHeader().setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        tablaHistorial.setSelectionBackground(
                new Color(210, 225, 245)
        );

        cmbAsociacion = new JComboBox<>();
        configurarCombo(cmbAsociacion, 250);

        cmbAnio = new JComboBox<>();
        configurarCombo(cmbAnio, 110);

        cmbMes = new JComboBox<>(MESES);
        configurarCombo(cmbMes, 135);

        btnBuscar = crearBoton(
                "Buscar",
                new Color(40, 120, 210),
                Color.WHITE,
                120
        );

        btnLimpiar = crearBoton(
                "Limpiar filtros",
                new Color(90, 105, 120),
                Color.WHITE,
                150
        );

        btnActualizar = crearBoton(
                "Actualizar historial",
                new Color(40, 120, 210),
                Color.WHITE,
                190
        );

        btnCerrar = crearBoton(
                "Cerrar",
                new Color(220, 224, 230),
                new Color(50, 50, 50),
                190
        );
    }

    private void agregarComponentes() {
        JPanel panelEncabezado =
                new JPanel(
                        new BorderLayout(0, 15)
                );

        panelEncabezado.setBackground(
                new Color(245, 247, 250)
        );

        JPanel panelTitulos =
                new JPanel(
                        new BorderLayout(0, 5)
                );

        panelTitulos.setBackground(
                new Color(245, 247, 250)
        );

        panelTitulos.add(
                lblTitulo,
                BorderLayout.CENTER
        );

        panelTitulos.add(
                lblDescripcion,
                BorderLayout.SOUTH
        );

JPanel panelFiltros = new JPanel(new BorderLayout(0, 10));

panelFiltros.setBackground(new Color(245, 247, 250));

panelFiltros.setBorder(
        BorderFactory.createTitledBorder(
                "Filtros de búsqueda"
        )
);
JPanel filaFiltros = new JPanel(
        new FlowLayout(
                FlowLayout.CENTER,
                10,
                5
        )
);

filaFiltros.setBackground(
        new Color(245, 247, 250)
);

filaFiltros.add(crearEtiquetaFiltro("Asociación:"));
filaFiltros.add(cmbAsociacion);

filaFiltros.add(crearEtiquetaFiltro("Año:"));
filaFiltros.add(cmbAnio);

filaFiltros.add(crearEtiquetaFiltro("Mes:"));
filaFiltros.add(cmbMes);

JPanel filaBotones = new JPanel(
        new FlowLayout(
                FlowLayout.CENTER,
                15,
                5
        )
);

filaBotones.setBackground(
        new Color(245, 247, 250)
);

filaBotones.add(btnBuscar);
filaBotones.add(btnLimpiar);

panelFiltros.add(
        filaFiltros,
        BorderLayout.NORTH
);
panelFiltros.add(
        filaFiltros,
        BorderLayout.NORTH
);

panelFiltros.add(
        filaBotones,
        BorderLayout.SOUTH
);

panelEncabezado.add(
        panelTitulos,
        BorderLayout.NORTH
);

panelEncabezado.add(
        panelFiltros,
        BorderLayout.SOUTH
);

        JPanel panelBotones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                12,
                                5
                        )
                );

        panelBotones.setBackground(
                new Color(245, 247, 250)
        );

        panelBotones.add(btnActualizar);
        panelBotones.add(btnCerrar);

        JScrollPane scrollTabla =
                new JScrollPane(tablaHistorial);

        add(
                panelEncabezado,
                BorderLayout.NORTH
        );

        add(
                scrollTabla,
                BorderLayout.CENTER
        );

        add(
                panelBotones,
                BorderLayout.SOUTH
        );
    }

    private void configurarEventos() {
        btnBuscar.addActionListener(
                e -> cargarHistorial()
        );

        btnLimpiar.addActionListener(
                e -> limpiarFiltros()
        );

        btnActualizar.addActionListener(
                e -> {
                    cargarAsociaciones();
                    cargarAnios();
                    cargarHistorial();
                }
        );

        btnCerrar.addActionListener(
                e -> setVisible(false)
        );

        cmbAsociacion.addActionListener(
                e -> {
                    if (!esInstitucion()) {
                        cargarAnios();
                    }
                }
        );
    }

    private void cargarAsociaciones() {
        Asociacion seleccionAnterior =
                (Asociacion)
                cmbAsociacion.getSelectedItem();

        Integer idSeleccionAnterior =
                seleccionAnterior == null
                ? null
                : seleccionAnterior.getIdAsociacion();

        cmbAsociacion.removeAllItems();

        List<Asociacion> asociaciones =
                asociacionDAO.obtenerAsociaciones();

        if (esInstitucion()) {
            for (Asociacion asociacion
                    : asociaciones) {

                if (asociacion.getIdAsociacion()
                        == usuarioSesion
                                .getIdAsociacion()) {

                    cmbAsociacion.addItem(
                            asociacion
                    );

                    break;
                }
            }

            cmbAsociacion.setEnabled(false);
            return;
        }

        cmbAsociacion.addItem(
                new Asociacion(
                        0,
                        "Todas",
                        "",
                        false
                )
        );

        for (Asociacion asociacion
                : asociaciones) {

            cmbAsociacion.addItem(asociacion);
        }

        cmbAsociacion.setEnabled(true);

        if (idSeleccionAnterior != null) {
            seleccionarAsociacionPorId(
                    idSeleccionAnterior
            );
        }
    }

    private void cargarAnios() {
        String anioSeleccionado =
                (String) cmbAnio.getSelectedItem();

        cmbAnio.removeAllItems();
        cmbAnio.addItem("Todos");

        Integer idAsociacion =
                obtenerIdAsociacionFiltro();

        List<Integer> anios =
                historialEntregasDAO
                        .obtenerAniosDisponibles(
                                idAsociacion
                        );

        for (Integer anio : anios) {
            cmbAnio.addItem(
                    String.valueOf(anio)
            );
        }

        if (anioSeleccionado != null) {
            cmbAnio.setSelectedItem(
                    anioSeleccionado
            );
        }

        if (cmbAnio.getSelectedIndex() == -1) {
            cmbAnio.setSelectedIndex(0);
        }
    }

    private void cargarHistorial() {
        Integer idAsociacion =
                obtenerIdAsociacionFiltro();

        Integer anio =
                obtenerAnioFiltro();

        Integer mes =
                obtenerMesFiltro();

        modeloTabla =
                historialEntregasDAO
                        .obtenerHistorialEntregas(
                                idAsociacion,
                                anio,
                                mes
                        );

        tablaHistorial.setModel(modeloTabla);
    }

    private Integer obtenerIdAsociacionFiltro() {
        if (esInstitucion()) {
            return idAsociacionSesion();
        }

        Asociacion asociacion =
                (Asociacion)
                cmbAsociacion.getSelectedItem();

        if (asociacion == null
                || asociacion.getIdAsociacion() == 0) {
            return null;
        }

        return asociacion.getIdAsociacion();
    }

    private Integer obtenerAnioFiltro() {
        String seleccion =
                (String) cmbAnio.getSelectedItem();

        if (seleccion == null
                || "Todos".equals(seleccion)) {
            return null;
        }

        return Integer.valueOf(seleccion);
    }

    private Integer obtenerMesFiltro() {
        int indice =
                cmbMes.getSelectedIndex();

        if (indice <= 0) {
            return null;
        }

        return indice;
    }

    private void limpiarFiltros() {
        if (!esInstitucion()) {
            cmbAsociacion.setSelectedIndex(0);
        }

        cargarAnios();
        cmbAnio.setSelectedIndex(0);
        cmbMes.setSelectedIndex(0);

        cargarHistorial();
    }

    private void seleccionarAsociacionPorId(
            int idAsociacion
    ) {
        for (
            int i = 0;
            i < cmbAsociacion.getItemCount();
            i++
        ) {
            Asociacion asociacion =
                    cmbAsociacion.getItemAt(i);

            if (asociacion.getIdAsociacion()
                    == idAsociacion) {

                cmbAsociacion.setSelectedIndex(i);
                return;
            }
        }
    }

    private JLabel crearEtiquetaFiltro(
            String texto
    ) {
        JLabel etiqueta = new JLabel(texto);

        etiqueta.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        etiqueta.setForeground(
                new Color(55, 55, 55)
        );

        return etiqueta;
    }

    private void configurarCombo(
            JComboBox<?> combo,
            int ancho
    ) {
        combo.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        combo.setPreferredSize(
                new Dimension(ancho, 34)
        );
    }

    private DefaultTableModel crearModeloVacio() {
        return new DefaultTableModel(
                new String[]{
                    "ID Entrega",
                    "Beneficiario",
                    "Institución",
                    "Prenda",
                    "Estado prenda",
                    "Cantidad",
                    "Fecha entrega",
                    "Estado entrega"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna
            ) {
                return false;
            }
        };
    }

    private JButton crearBoton(
            String texto,
            Color fondo,
            Color letra,
            int ancho
    ) {
        JButton boton = new JButton(texto);

        boton.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        boton.setPreferredSize(
                new Dimension(ancho, 38)
        );

        boton.setBackground(fondo);
        boton.setForeground(letra);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);

        boton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return boton;
    }
}