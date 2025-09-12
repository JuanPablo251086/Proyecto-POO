
public class Simulacion {
    public void mostrarMapa() {
        System.out.println("[Simulacion] Mostrar mapa ()");
    }

    public void mostrarVehiculos() {
        System.out.println("[Simulacion] Mostrar vehiculos ()");
    }

    public void mostrarSemaforos() {
        System.out.println("[Simulacion] Mostrar semaforos ()");
    }

    public void actualizarDatos(Metrica m) {
        if (m != null) {
            System.out.println("[Simulacion] Actualizando datos con métricas: " + m.toString());
        }
    }

    public String toString() {
        return "Simulacion{placeholder=true}";
    }
}
