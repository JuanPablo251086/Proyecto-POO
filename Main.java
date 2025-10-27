// Main.java
// Arreglé duplicado de maxR/maxC y limpié main para crear controlador, inicializar, establecer pesos iniciales y correr una búsqueda de prueba.

public class Main {
    public static final int maxR = 5; // Configuración global del tamaño del mapa
    public static final int maxC = 5; // Recomiendo usar row/col pares (comentario original)

    public static void main(String[] args) {
        Controller controlador = new Controller(maxR, maxC);
        controlador.inicializarintersecciones();

        // Inicializa aristas y pesos aleatorios para empezar a probar
        controlador.inicializarAristasYPesosAleatorios();

        // Debug: imprime vecinos y aristas
        controlador.printallvecinosynodos();

        // Prueba rápida de ruta mínima (ejemplo)
        Interseccion origen = controlador.getInterseccion(1, 1);
        Interseccion destino = controlador.getInterseccion(5, 5);
        TrafficController traf = controlador.getTrafficController();
        try {
            java.util.List<Interseccion> ruta = traf.calcularRutaMinima(origen, destino);
            System.out.println("Ruta mínima encontrada:");
            System.out.println(ruta);
        } catch (GraphException ge) {
            System.out.println("No se pudo calcular ruta: " + ge.getMessage());
        }
    }
}
