/**
 * ControlSim: controlador central de la simulación de tráfico.
 *
 * Suposiciones:
 * - Existe una clase "Trafico" con métodos:
 *     - void actualizarEstado()
 *     - void calcularDuraciones()
 *     - String toString()  (opcional)
 * - Existe una clase "Simulacion" con métodos de vista:
 *     - void mostrarMapa()
 *     - void mostrarVehiculos()
 *     - void mostrarSemaforos()
 *     - void actualizarDatos(Metrica)  (opcional)
 */
public class ControlSim {
    private Trafico trafico;
    private Simulacion vista;
    private boolean simulacionActiva;

    public ControlSim(Trafico trafico, Simulacion vista) {
        this.trafico = trafico;
        this.vista = vista;
        this.simulacionActiva = false;
    }

    /**
     * Inicia la simulación. Este método realiza una iteración de inicio
     * del motor de simulación y actualiza la vista inicial.
     */
    public void iniciarSimulacion() {
        if (trafico == null || vista == null) {
            System.err.println("ControlSim: trafico o vista no inicializados.");
            return;
        }
        simulacionActiva = true;
        System.out.println("ControlSim: iniciando simulación...");

        // Primera actualización del estado del tráfico
        try {
            trafico.actualizarEstado();    // actualiza posiciones, contadores, etc.
            trafico.calcularDuraciones();  // calcula duraciones de semáforos si aplica
        } catch (Exception e) {
            System.err.println("ControlSim: error al inicializar trafico -> " + e.getMessage());
        }

        actualizarVista();
    }

    /**
     * Pausa la simulación (marca el estado y puede dejar la vista en su estado actual).
     */
    public void pausarSimulacion() {
        if (!simulacionActiva) {
            System.out.println("ControlSim: la simulación ya está pausada.");
            return;
        }
        simulacionActiva = false;
        System.out.println("ControlSim: simulación pausada.");
    }

    /**
     * Ejecuta una actualización de la vista a partir del estado actual del tráfico.
     * Llama a funciones de la vista que ya deberían existir en Simulacion.
     */
    public void actualizarVista() {
        if (vista == null) {
            System.err.println("ControlSim: vista no inicializada, no se puede actualizar.");
            return;
        }
        try {
            // Mostrar mapa base
            vista.mostrarMapa();

            vista.mostrarVehiculos();
            vista.mostrarSemaforos();

            // Si Trafico proporciona métricas: actualizarlas en la vista
            // Ejemplo (descomentar si Trafico tiene obtenerMetricas()):
            // Metrica m = trafico.obtenerMetricas();
            // if (m != null) vista.actualizarDatos(m);

        } catch (Exception e) {
            System.err.println("ControlSim: error al actualizar vista -> " + e.getMessage());
        }
    }

    /**
     * Iteración de un "tick" de simulación: actualizar estado y vista.
     * Llamar desde donde controlen el bucle principal de la simulación.
     */
    public void stepSimulacion() {
        if (!simulacionActiva) {
            System.out.println("ControlSim: simulación no activa, use iniciarSimulacion().");
            return;
        }
        try {
            trafico.actualizarEstado();
            trafico.calcularDuraciones();
            actualizarVista();
        } catch (Exception e) {
            System.err.println("ControlSim: error en stepSimulacion -> " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "ControlSim{simulacionActiva=" + simulacionActiva +
                ", trafico=" + (trafico != null ? trafico.toString() : "null") +
                ", vista=" + (vista != null ? vista.toString() : "null") + "}";
    }

    /* Getters / Setters (opcional) */
    public Trafico getTrafico() { return trafico; }
    public void setTrafico(Trafico trafico) { this.trafico = trafico; }
    public Simulacion getVista() { return vista; }
    public void setVista(Simulacion vista) { this.vista = vista; }
    public boolean isSimulacionActiva() { return simulacionActiva; }
}
