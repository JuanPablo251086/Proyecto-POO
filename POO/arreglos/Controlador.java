import java.util.ArrayList;

public class Controlador {
    private ArrayList<Proyecto> proyectos;
    private ArrayList<Desarrollador> desarrolladores;

    public Controlador() {
        proyectos = new ArrayList<>();
        desarrolladores = new ArrayList<>();
    }

    public void agregarProyecto(String nombre) {
        proyectos.add(new Proyecto(nombre));
    }

    public void agregarDesarrollador(String nombre) {
        desarrolladores.add(new Desarrollador(nombre));
    }

    public ArrayList<Proyecto> getProyectos() {
        return proyectos;
    }

    public ArrayList<Desarrollador> getDesarrolladores() {
        return desarrolladores;
    }

    public Proyecto getProyecto(int index) {
        if (index >= 0 && index < proyectos.size()) {
            return proyectos.get(index);
        }
        return null;
    }

    public Desarrollador getDesarrollador(int index) {
        if (index >= 0 && index < desarrolladores.size()) {
            return desarrolladores.get(index);
        }
        return null;
    }
}
