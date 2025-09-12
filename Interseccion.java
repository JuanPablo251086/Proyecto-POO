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
    /**
     * Actualiza los sensores de la intersección.
     * Implementación simple: simula incremento aleatorio en cada sensor (0-5 vehículos).
     */
    public void actualizarSensores() {
        Random rnd = new Random();
        for (Sensor s : listaSensores) {
            int incremento = rnd.nextInt(6); // 0..5
            s.actualizarConteo(incremento);
        }
    }

    /**
     * Obtiene el flujo total detectado por los sensores (suma de conteos).
     */
    public int obtenerFlujo() {
        int total = 0;
        for (Sensor s : listaSensores) {
            total += s.getConteoVehiculosDetectados();
        }
        return total;
    }

    public Semaforo getSemaforoPrincipal() {
        // retorna el primer semáforo si existe
        return listaSemaforos.isEmpty() ? null : listaSemaforos.get(0);
    }

    public int getId() {
        return idInter;
    }

    public String getNombre() {
        return nombreInterseccion;
    }

    // métodos utilitarios para añadir semáforos y sensores
    public void addSemaforo(Semaforo s) {
        if (s != null) listaSemaforos.add(s);
    }

    public void addSensor(Sensor s) {
        if (s != null) listaSensores.add(s);
    }

    public List<Semaforo> getListaSemaforos() {
        return Collections.unmodifiableList(listaSemaforos);
    }

    public List<Sensor> getListaSensores() {
        return Collections.unmodifiableList(listaSensores);
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
