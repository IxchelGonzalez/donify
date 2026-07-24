package Modelo;

/**
 * Contiene los indicadores del dashboard de donaciones recibidas.
 */
public class ResumenDonacion {

    private int totalDonaciones;
    private int prendasRecibidas;
    private int donadoresParticipantes;
    private double promedioPrendasPorDonacion;

    private String prendaMasDonada;
    private String estadoMasFrecuente;
    private String donadorPrincipal;
    private String institucionPrincipal;

    public ResumenDonacion() {
        prendaMasDonada = "Sin datos";
        estadoMasFrecuente = "Sin datos";
        donadorPrincipal = "Sin datos";
        institucionPrincipal = "Sin datos";
    }

    public int getTotalDonaciones() {
        return totalDonaciones;
    }

    public void setTotalDonaciones(int totalDonaciones) {
        this.totalDonaciones = totalDonaciones;
    }

    public int getPrendasRecibidas() {
        return prendasRecibidas;
    }

    public void setPrendasRecibidas(int prendasRecibidas) {
        this.prendasRecibidas = prendasRecibidas;
    }

    public int getDonadoresParticipantes() {
        return donadoresParticipantes;
    }

    public void setDonadoresParticipantes(
            int donadoresParticipantes
    ) {
        this.donadoresParticipantes =
                donadoresParticipantes;
    }

    public double getPromedioPrendasPorDonacion() {
        return promedioPrendasPorDonacion;
    }

    public void setPromedioPrendasPorDonacion(
            double promedioPrendasPorDonacion
    ) {
        this.promedioPrendasPorDonacion =
                promedioPrendasPorDonacion;
    }

    public String getPrendaMasDonada() {
        return prendaMasDonada;
    }

    public void setPrendaMasDonada(String prendaMasDonada) {
        this.prendaMasDonada = prendaMasDonada;
    }

    public String getEstadoMasFrecuente() {
        return estadoMasFrecuente;
    }

    public void setEstadoMasFrecuente(
            String estadoMasFrecuente
    ) {
        this.estadoMasFrecuente = estadoMasFrecuente;
    }

    public String getDonadorPrincipal() {
        return donadorPrincipal;
    }

    public void setDonadorPrincipal(
            String donadorPrincipal
    ) {
        this.donadorPrincipal = donadorPrincipal;
    }

    public String getInstitucionPrincipal() {
        return institucionPrincipal;
    }

    public void setInstitucionPrincipal(
            String institucionPrincipal
    ) {
        this.institucionPrincipal = institucionPrincipal;
    }
}