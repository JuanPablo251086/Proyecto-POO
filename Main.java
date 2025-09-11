public class Main {
    public static void main(String[] args) {
        Trafico trafico = new Trafico(null); // sin algoritmo personalizado
        Interseccion i1 = new Interseccion(1, "Central");
        i1.addSensor(new Sensor(1));
        i1.addSensor(new Sensor(2));
        i1.addSemaforo(new Semaforo("ROJO", 10, 3, 13));
        trafico.addInterseccion(i1);

        Simulacion sim = new Simulacion();
        ControlSim cs = new ControlSim(trafico, sim);
        ControlUs cu = new ControlUs(cs);

        cu.detectarEvento("INICIAR");
        cu.detectarEvento("STEP");
        Metrica m = trafico.obtenerMetricas();
        m.mostrarResultados();
    }
}
