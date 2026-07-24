package Vista;

import DAO.AsociacionDAO;
import DAO.ReporteDonacionesDAO;
import Modelo.Asociacion;
import Modelo.ResumenDonacion;
import Modelo.Usuario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JScrollPane;

public class ReporteDonacionesRecibidas extends JPanel {

    private final ReporteDonacionesDAO reporteDonacionesDAO;
    private final AsociacionDAO asociacionDAO;
    private final Usuario usuarioSesion;

    private JComboBox<Asociacion> cmbAsociacion;
    private JComboBox<String> cmbAnio;
    private JComboBox<String> cmbTrimestre;

    private JLabel lblAlcance;
    private JLabel lblTotalDonaciones;
    private JLabel lblPrendasRecibidas;
    private JLabel lblDonadores;
    private JLabel lblPromedio;
    private JLabel lblPrendaMasDonada;
    private JLabel lblEstadoPredominante;
    private JLabel lblDonadorPrincipal;
    private JLabel lblInstitucionPrincipal;

    private JButton btnGenerar;
    private JButton btnLimpiar;
    private JButton btnCerrar;

    private static final String[] TRIMESTRES = {
        "Todos",
        "Primer trimestre",
        "Segundo trimestre",
        "Tercer trimestre",
        "Cuarto trimestre"
    };

    public ReporteDonacionesRecibidas() {
        this(null);
    }

    public ReporteDonacionesRecibidas(
            Usuario usuarioSesion
    ) {
        this.usuarioSesion = usuarioSesion;
        reporteDonacionesDAO =
                new ReporteDonacionesDAO();
        asociacionDAO = new AsociacionDAO();

        configurarPanel();
        crearComponentes();
        agregarComponentes();
        configurarEventos();

        cargarAsociaciones();
        cargarAnios();
        cargarReporte();
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
        setLayout(new BorderLayout(0, 16));
        setBackground(new Color(240, 243, 248));
        setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        30,
                        20,
                        30
                )
        );
    }

    private void crearComponentes() {
        cmbAsociacion = new JComboBox<>();
        configurarCombo(cmbAsociacion, 220);

        cmbAnio = new JComboBox<>();
        configurarCombo(cmbAnio, 115);

        cmbTrimestre =
                new JComboBox<>(TRIMESTRES);
        configurarCombo(cmbTrimestre, 230);

        lblAlcance = new JLabel(
                "Reporte global de donaciones",
                SwingConstants.CENTER
        );

        lblAlcance.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        lblAlcance.setForeground(
                new Color(40, 120, 210)
        );

        lblAlcance.setOpaque(true);
        lblAlcance.setBackground(
                new Color(225, 236, 250)
        );

        lblAlcance.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        12,
                        8,
                        12
                )
        );

        lblTotalDonaciones = crearEtiquetaValor();
        lblPrendasRecibidas = crearEtiquetaValor();
        lblDonadores = crearEtiquetaValor();
        lblPromedio = crearEtiquetaValor();
        lblPrendaMasDonada = crearEtiquetaValor();
        lblEstadoPredominante = crearEtiquetaValor();
        lblDonadorPrincipal = crearEtiquetaValor();
        lblInstitucionPrincipal = crearEtiquetaValor();

        btnGenerar = crearBoton(
                "Generar reporte",
                new Color(40, 120, 210),
                Color.WHITE,
                170
        );

        btnLimpiar = crearBoton(
                "Limpiar filtros",
                new Color(90, 105, 120),
                Color.WHITE,
                170
        );

        btnCerrar = crearBoton(
                "Cerrar",
                new Color(220, 224, 230),
                new Color(50, 50, 50),
                150
        );
    }

    private void agregarComponentes() {
        JPanel panelSuperior =
                new JPanel(new BorderLayout(0, 10));

        panelSuperior.setBackground(
                new Color(240, 243, 248)
        );

        JLabel titulo = new JLabel(
                "Reporte de donaciones recibidas",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        29
                )
        );

        titulo.setForeground(
                new Color(30, 65, 110)
        );

        JLabel descripcion = new JLabel(
                "Genera indicadores por institución, "
                + "año y trimestre.",
                SwingConstants.CENTER
        );

        descripcion.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        descripcion.setForeground(
                new Color(100, 100, 100)
        );

        JPanel panelTitulos =
                new JPanel(new BorderLayout(0, 5));

        panelTitulos.setBackground(
                new Color(240, 243, 248)
        );

        panelTitulos.add(titulo, BorderLayout.NORTH);
        panelTitulos.add(
                descripcion,
                BorderLayout.SOUTH
        );

JPanel panelFiltros = new JPanel(
        new BorderLayout(0, 8)
);

panelFiltros.setBackground(
        new Color(240, 243, 248)
);

panelFiltros.setBorder(
        BorderFactory.createTitledBorder(
                "Filtros del reporte"
        )
);

// ---------------------------------------------------
// Primera fila: institución, año y temporada
// ---------------------------------------------------
JPanel filaCombos = new JPanel(
        new GridBagLayout()
);

filaCombos.setBackground(
        new Color(240, 243, 248)
);

GridBagConstraints gbc =
        new GridBagConstraints();

gbc.insets = new Insets(4, 8, 4, 8);
gbc.anchor = GridBagConstraints.WEST;
gbc.fill = GridBagConstraints.HORIZONTAL;
gbc.gridy = 0;

// Institución
gbc.gridx = 0;
gbc.weightx = 0;

filaCombos.add(
        crearEtiquetaFiltro("Institución:"),
        gbc
);

gbc.gridx = 1;
gbc.weightx = 1.0;

filaCombos.add(
        cmbAsociacion,
        gbc
);

// Año
gbc.gridx = 2;
gbc.weightx = 0;

filaCombos.add(
        crearEtiquetaFiltro("Año:"),
        gbc
);

gbc.gridx = 3;
gbc.weightx = 0.25;

filaCombos.add(
        cmbAnio,
        gbc
);

// Temporada
gbc.gridx = 4;
gbc.weightx = 0;

filaCombos.add(
        crearEtiquetaFiltro("Temporada:"),
        gbc
);

gbc.gridx = 5;
gbc.weightx = 0.55;

filaCombos.add(
        cmbTrimestre,
        gbc
);

// ---------------------------------------------------
// Segunda fila: botones
// ---------------------------------------------------
JPanel filaBotones = new JPanel(
        new FlowLayout(
                FlowLayout.CENTER,
                15,
                5
        )
);

filaBotones.setBackground(
        new Color(240, 243, 248)
);

filaBotones.add(btnGenerar);
filaBotones.add(btnLimpiar);

// ---------------------------------------------------
// Agregar ambas filas al panel de filtros
// ---------------------------------------------------
panelFiltros.add(
        filaCombos,
        BorderLayout.CENTER
);

panelFiltros.add(
        filaBotones,
        BorderLayout.SOUTH
);

        JPanel panelAlcance =
                new JPanel(new FlowLayout(
                        FlowLayout.CENTER,
                        0,
                        0
                ));

        panelAlcance.setBackground(
                new Color(240, 243, 248)
        );

        panelAlcance.add(lblAlcance);

        JPanel contenidoSuperior =
                new JPanel(new BorderLayout(0, 8));

        contenidoSuperior.setBackground(
                new Color(240, 243, 248)
        );

        contenidoSuperior.add(
                panelFiltros,
                BorderLayout.NORTH
        );

        contenidoSuperior.add(
                panelAlcance,
                BorderLayout.SOUTH
        );

        panelSuperior.add(
                panelTitulos,
                BorderLayout.NORTH
        );

        panelSuperior.add(
                contenidoSuperior,
                BorderLayout.SOUTH
        );

JPanel panelReportes = new JPanel(
        new GridLayout(
                2,
                4,
                14,
                14
        )
);

panelReportes.setPreferredSize(
        new Dimension(900, 300)
);

panelReportes.setMinimumSize(
        new Dimension(850, 280)
);

        panelReportes.setBackground(
                new Color(240, 243, 248)
        );

        panelReportes.add(
                crearTarjeta(
                        "Total de donaciones",
                        lblTotalDonaciones,
                        new Color(40, 120, 210)
                )
        );

        panelReportes.add(
                crearTarjeta(
                        "Prendas recibidas",
                        lblPrendasRecibidas,
                        new Color(80, 145, 100)
                )
        );

        panelReportes.add(
                crearTarjeta(
                        "Donadores participantes",
                        lblDonadores,
                        new Color(230, 150, 55)
                )
        );

        panelReportes.add(
                crearTarjeta(
                        "Promedio por donación",
                        lblPromedio,
                        new Color(200, 70, 70)
                )
        );

        panelReportes.add(
                crearTarjeta(
                        "Prenda más donada",
                        lblPrendaMasDonada,
                        new Color(120, 90, 190)
                )
        );

        panelReportes.add(
                crearTarjeta(
                        "Estado predominante",
                        lblEstadoPredominante,
                        new Color(35, 150, 160)
                )
        );

        panelReportes.add(
                crearTarjeta(
                        "Donador principal",
                        lblDonadorPrincipal,
                        new Color(180, 130, 40)
                )
        );

        panelReportes.add(
                crearTarjeta(
                        "Institución principal",
                        lblInstitucionPrincipal,
                        new Color(90, 100, 190)
                )
        );

        JPanel panelCerrar =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER
                        )
                );

        panelCerrar.setBackground(
                new Color(240, 243, 248)
        );

        panelCerrar.add(btnCerrar);

        add(panelSuperior, BorderLayout.NORTH);
        JScrollPane scrollReportes =
        new JScrollPane(panelReportes);

scrollReportes.setBorder(null);

scrollReportes.setHorizontalScrollBarPolicy(
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
);

scrollReportes.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
);

scrollReportes.getVerticalScrollBar()
        .setUnitIncrement(16);

scrollReportes.getViewport().setBackground(
        new Color(240, 243, 248)
);

add(scrollReportes, BorderLayout.CENTER);
        add(panelCerrar, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        btnGenerar.addActionListener(
                e -> cargarReporte()
        );

        btnLimpiar.addActionListener(
                e -> limpiarFiltros()
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
        cmbAsociacion.removeAllItems();

        List<Asociacion> asociaciones =
                asociacionDAO.obtenerAsociaciones();

        if (esInstitucion()) {
            for (Asociacion asociacion
                    : asociaciones) {

                if (asociacion.getIdAsociacion()
                        == idAsociacionSesion()) {

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
    }

    private void cargarAnios() {
        String seleccionAnterior =
                (String) cmbAnio.getSelectedItem();

        cmbAnio.removeAllItems();
        cmbAnio.addItem("Todos");

        List<Integer> anios =
                reporteDonacionesDAO
                        .obtenerAniosDisponibles(
                                obtenerIdAsociacionFiltro()
                        );

        for (Integer anio : anios) {
            cmbAnio.addItem(
                    String.valueOf(anio)
            );
        }

        if (seleccionAnterior != null) {
            cmbAnio.setSelectedItem(
                    seleccionAnterior
            );
        }

        if (cmbAnio.getSelectedIndex() == -1) {
            cmbAnio.setSelectedIndex(0);
        }
    }

    private void cargarReporte() {
        Integer idAsociacion =
                obtenerIdAsociacionFiltro();

        Integer anio = obtenerAnioFiltro();

        Integer trimestre =
                obtenerTrimestreFiltro();

        ResumenDonacion resumen =
                reporteDonacionesDAO
                        .obtenerResumenDonaciones(
                                idAsociacion,
                                anio,
                                trimestre
                        );

        establecerValor(
                lblTotalDonaciones,
                String.valueOf(
                        resumen.getTotalDonaciones()
                )
        );

        establecerValor(
                lblPrendasRecibidas,
                String.valueOf(
                        resumen.getPrendasRecibidas()
                )
        );

        establecerValor(
                lblDonadores,
                String.valueOf(
                        resumen.getDonadoresParticipantes()
                )
        );

        establecerValor(
                lblPromedio,
                String.format(
                        "%.2f",
                        resumen
                                .getPromedioPrendasPorDonacion()
                )
        );

        establecerValor(
                lblPrendaMasDonada,
                resumen.getPrendaMasDonada()
        );

        establecerValor(
                lblEstadoPredominante,
                resumen.getEstadoMasFrecuente()
        );

        establecerValor(
                lblDonadorPrincipal,
                resumen.getDonadorPrincipal()
        );

        establecerValor(
                lblInstitucionPrincipal,
                resumen.getInstitucionPrincipal()
        );

        actualizarAlcance(
                idAsociacion,
                anio,
                trimestre
        );
    }

    private void actualizarAlcance(
            Integer idAsociacion,
            Integer anio,
            Integer trimestre
    ) {
        String institucion =
                idAsociacion == null
                ? "todas las instituciones"
                : String.valueOf(
                        cmbAsociacion.getSelectedItem()
                );

        String periodo =
                anio == null
                ? "todos los años"
                : String.valueOf(anio);

        String temporada =
                trimestre == null
                ? "todos los trimestres"
                : TRIMESTRES[trimestre];

        lblAlcance.setText(
                "Institución: "
                + institucion
                + " | Año: "
                + periodo
                + " | Temporada: "
                + temporada
        );
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

    private Integer obtenerTrimestreFiltro() {
        int indice =
                cmbTrimestre.getSelectedIndex();

        return indice <= 0 ? null : indice;
    }

    private void limpiarFiltros() {
        if (!esInstitucion()) {
            cmbAsociacion.setSelectedIndex(0);
        }

        cargarAnios();
        cmbAnio.setSelectedIndex(0);
        cmbTrimestre.setSelectedIndex(0);

        cargarReporte();
    }

    private JPanel crearTarjeta(
            String titulo,
            JLabel valor,
            Color colorAcento
    ) {
        JPanel tarjeta =
                new JPanel(new BorderLayout(0, 6));

        tarjeta.setBackground(Color.WHITE);

        tarjeta.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                6,
                                0,
                                0,
                                colorAcento
                        ),
                        BorderFactory.createEmptyBorder(
                                10,
                                10,
                                10,
                                10
                        )
                )
        );

        JLabel lblTituloTarjeta = new JLabel(
                "<html><div style='text-align:center;'>"
                + titulo
                + "</div></html>",
                SwingConstants.CENTER
        );

        lblTituloTarjeta.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        lblTituloTarjeta.setForeground(
                new Color(70, 70, 70)
        );

        valor.setForeground(colorAcento);

        tarjeta.add(
                lblTituloTarjeta,
                BorderLayout.NORTH
        );

        tarjeta.add(valor, BorderLayout.CENTER);

        return tarjeta;
    }

    private JLabel crearEtiquetaValor() {
        JLabel etiqueta = new JLabel(
                "0",
                SwingConstants.CENTER
        );

        etiqueta.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        17
                )
        );

        return etiqueta;
    }

    private void establecerValor(
            JLabel etiqueta,
            String valor
    ) {
        etiqueta.setText(
                "<html><div style='text-align:center;'>"
                + valor
                + "</div></html>"
        );
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