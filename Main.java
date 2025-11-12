public class Main {
    public static final int maxR = 6; // Configuración global del tamaño del mapa
    public static final int maxC = 6; // Recomiendo usar row/col pares 
    public static void main(String[] args) {
        Controller controlador = new Controller(maxR, maxC);
        controlador.inicializarintersecciones();

    // Ajustes de calibración global
    controlador.setMaxVehPerMin(120);
    controlador.setMultCalle(1.30f);
    controlador.setMultAvenida(0.75f);

        // Inicializa aristas y pesos aleatorios para empezar a probar
        controlador.inicializarAristasYPesosAleatorios();
        //esto lo vamos a modificar en la siguiente entrega para que sea mas accesible y facil de modificar.

    // Inicializar planes de semáforos según cargas actuales
    controlador.postConstruccionActualizarSemaforos();

    // Exportación de estado para consumo por UI o pruebas
    Vista vista = new Vista(controlador);
    vista.exportCSV("red_vial.csv");
    vista.exportJSON("red_vial.json");
    vista.exportConfigJSON("config.json");
    vista.exportJSONConRuta("red_vial_ruta.json", 1, 1, 5, 6);

        // Ejemplo interno: calcular ruta óptima sin imprimir resultado para evitar ruido en consola
        Interseccion origen = controlador.getInterseccion(1, 1);
        Interseccion destino = controlador.getInterseccion(5, 6);
        TrafficController traf = controlador.getTrafficController();
        try {
            traf.calcularRutaMinima(origen, destino);
        } catch (GraphException ignored) {
        }
    }
}
