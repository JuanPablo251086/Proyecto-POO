public class Consulta {
    private String fecha;
    private String motivo;
    private String tratamiento;
    private Mascota mascota;

    
    public Consulta(String fecha, String motivo, String tratamiento, Mascota mascota) {
        this.fecha = fecha;
        this.motivo = motivo;
        this.tratamiento = tratamiento;
        this.mascota = mascota;
    }

    // Getter
    public String getFecha() {
        return fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public Mascota getMascota() {
        return mascota;
    }
}
