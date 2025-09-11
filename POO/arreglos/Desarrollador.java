import java.util.ArrayList;

public class Desarrollador {
    private String nombre;
    private ArrayList<Tarea> tareas;

    public Desarrollador(String nombre) {
        this.nombre = nombre;
        this.tareas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Tarea> getTareas() {
        return tareas;
    }

    public boolean asignarTarea(Tarea t) {
        if (t.getAsignado() == null) {
            t.setAsignado(this);
            tareas.add(t);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return nombre;
    }
}

