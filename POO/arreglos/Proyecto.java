import java.util.ArrayList;

public class Proyecto {
    private String nombre;
    private ArrayList<Tarea> tareas;

    public Proyecto(String nombre) {
        this.nombre = nombre;
        this.tareas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Tarea> getTareas() {
        return tareas;
    }

    public void agregarTarea(Tarea t) {
        tareas.add(t);
    }

    @Override
    public String toString() {
        return nombre + " (Tareas: " + tareas.size() + ")";
    }
}
