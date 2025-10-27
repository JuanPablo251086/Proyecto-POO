public class Main {
    public static final int maxR = 6; // Configuración global del tamaño del mapa
    public static final int maxC = 6; // Recomiendo usar row/col pares 
    public static void main(String[] args) {
        Controller controlador = new Controller(maxR, maxC);
        controlador.inicializarintersecciones();

        // Inicializa aristas y pesos aleatorios para empezar a probar
        controlador.inicializarAristasYPesosAleatorios();
        //esto lo vamos a modificar en la siguiente entrega para que sea mas accesible y facil de modificar.

        // Debug: imprime vecinos y aristas
        controlador.printallvecinosynodos();

        // Prueba rápida de ruta mínima (ejemplo)
        Interseccion origen = controlador.getInterseccion(1, 1);
        Interseccion destino = controlador.getInterseccion(5, 6);
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
