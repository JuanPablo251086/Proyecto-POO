public class Tarea {
    private String nombre;
    private int horas;
    private int prioridad;
    private Desarrollador asignado;

    public Tarea(String nombre, int horas, int prioridad) {
        this.nombre = nombre;
        this.horas = horas;
        this.prioridad = prioridad;
        this.asignado = null;
    }

    public String getNombre() {
        return nombre;
    }

    public int getHoras() {
        return horas;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public Desarrollador getAsignado() {
        return asignado;
    }

    public void setAsignado(Desarrollador asignado) {
        this.asignado = asignado;
    }

    @Override
    public String toString() {
        return nombre + " (Horas: " + horas + ", Prioridad: " + prioridad + 
               ", Asignado: " + (asignado != null ? asignado.getNombre() : "Nadie") + ")";
    }
}

