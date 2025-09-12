/**
 * ControlUs: interfaz de usuario / controlador de eventos.
 *
 * - Detecta eventos provenientes de la UI (botones, inputs, alertas de sensores).
 * - Traduce eventos a órdenes para ControlSim (por ejemplo: iniciar, pausar,
 *   cambiar modo de control, aplicar una duración manual de semáforo, etc.)
 *
 * Suposiciones:
 * - ControlSim expone métodos como iniciarSimulacion(), pausarSimulacion(), stepSimulacion()
 *   y/o otras APIs para enviar órdenes.
 */
public class ControlUs {
    private ControlSim controlSim;

    public ControlUs(ControlSim controlSim) {
        this.controlSim = controlSim;
    }

    /**
     * Detecta un evento simple desde la capa de presentación.
     * El string 'evento' puede ser: "INICIAR", "PAUSAR", "STEP", "FORZAR_VERDE:<idInter>:<segundos>", etc.
     */
    public void detectarEvento(String evento) {
        if (evento == null || evento.trim().isEmpty()) {
            System.out.println("ControlUs: evento vacío.");
            return;
        }

        // Normalizamos y parseamos el evento
        String e = evento.trim().toUpperCase();
        try {
            if (e.equals("INICIAR")) {
                enviarOrden("INICIAR");
            } else if (e.equals("PAUSAR")) {
                enviarOrden("PAUSAR");
            } else if (e.equals("STEP")) {
                enviarOrden("STEP");
            } else if (e.startsWith("FORZAR_VERDE:")) {
                // ejemplo: FORZAR_VERDE:3:20  => intersección 3, 20 segundos
                enviarOrden(e);
            } else {
                // otros eventos: notificar a controlSim o logear
                System.out.println("ControlUs: evento no reconocido -> " + evento);
            }
        } catch (Exception ex) {
            System.err.println("ControlUs: error al procesar evento -> " + ex.getMessage());
        }
    }

    /**
     * Traduce una orden a llamadas concretas en ControlSim / Trafico.
     */
    public void enviarOrden(String orden) {
        if (controlSim == null) {
            System.err.println("ControlUs: controlSim no inicializado.");
            return;
        }
        String o = orden.trim().toUpperCase();

        try {
            if (o.equals("INICIAR")) {
                controlSim.iniciarSimulacion();
                System.out.println("ControlUs: orden INICIAR ejecutada.");
            } else if (o.equals("PAUSAR")) {
                controlSim.pausarSimulacion();
                System.out.println("ControlUs: orden PAUSAR ejecutada.");
            } else if (o.equals("STEP")) {
                controlSim.stepSimulacion();
                System.out.println("ControlUs: orden STEP ejecutada.");
            } else if (o.startsWith("FORZAR_VERDE:")) {
                String[] parts = o.split(":");
                if (parts.length >= 3) {
                    try {
                        int idInter = Integer.parseInt(parts[1]);
                        int segundos = Integer.parseInt(parts[2]);
                        // Para aplicar el cambio necesitamos acceso al objeto Trafico/Interseccion.
                        // Intento vía controlSim.getTrafico() -> buscar intersección -> setear semáforo.
                        Trafico t = controlSim.getTrafico();
                        if (t != null) {
                            Interseccion inter = t.obtenerInterseccionPorId(idInter); // supuesto método
                            if (inter != null) {
                                Semaforo s = inter.getSemaforoPrincipal(); // supuesto
                                if (s != null) {
                                    s.setDuraciones(segundos, s.getTiempoAmarillo(), s.getTiempoRojo());
                                    System.out.println("ControlUs: semáforo forzado en intersección " + idInter +
                                            " a verde por " + segundos + "s.");
                                    // después recalcular duraciones si aplica
                                    t.calcularDuraciones();
                                    controlSim.actualizarVista();
                                } else {
                                    System.err.println("ControlUs: semáforo principal no encontrado en intersección " + idInter);
                                }
                            } else {
                                System.err.println("ControlUs: intersección " + idInter + " no encontrada.");
                            }
                        } else {
                            System.err.println("ControlUs: trafico no disponible en ControlSim.");
                        }
                    } catch (NumberFormatException nfe) {
                        System.err.println("ControlUs: formato inválido en FORZAR_VERDE, use FORZAR_VERDE:id:segundos");
                    }
                } else {
                    System.err.println("ControlUs: parámetros insuficientes en orden FORZAR_VERDE.");
                }
            } else {
                System.out.println("ControlUs: orden no implementada -> " + orden);
            }
        } catch (Exception e) {
            System.err.println("ControlUs: error al ejecutar orden -> " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "ControlUs{controlSim=" + (controlSim != null ? controlSim.toString() : "null") + "}";
    }

    /* Setter / Getter */
    public ControlSim getControlSim() { return controlSim; }
    public void setControlSim(ControlSim controlSim) { this.controlSim = controlSim; }
}
