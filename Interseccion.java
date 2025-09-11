import java.util.*;

public class Interseccion {
    // Atributos 
    private int idInter;
    private String nombreInterseccion;
    private List<Semaforo> listaSemaforos;
    private List<Sensor> listaSensores;

    // Constructor
    public Interseccion(int idInter, String nombreInterseccion) {
        this.idInter = idInter;
        this.nombreInterseccion = nombreInterseccion;
        this.listaSemaforos = new ArrayList<>();
        this.listaSensores = new ArrayList<>();
    }

    // Métodos 
    public void actualizarSensores() {
        // falta implementar actualización de sensores
    }

    public int obtenerFlujo() {
        // falta calcular flujo real con sensores
        return 0;
    }

    public Semaforo getSemaforoPrincipal() {
        // falta retornar semáforo principal
        return listaSemaforos.isEmpty() ? null : listaSemaforos.get(0);
    }

    public int getId() {
        return idInter;
    }

    public String getNombre() {
        return nombreInterseccion;
    }

    public String toString() {
        return "Interseccion{" +
                "idInter=" + idInter +
                ", nombreInterseccion='" + nombreInterseccion + '\'' +
                ", listaSemaforos=" + listaSemaforos +
                ", listaSensores=" + listaSensores +
                '}';
    }
}
