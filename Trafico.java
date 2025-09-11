import java.util.ArrayList;
import java.util.List;

public class Trafico {
    private Algoritmo algoritmoControl;
    private ArrayList<Interseccion> listaDeIntersecciones;

    public Trafico(Algoritmo algoritmoControl) {
        this.algoritmoControl = algoritmoControl;
        this.listaDeIntersecciones = new ArrayList<>();
    }


    public void actualizarEstado() {
        for (Interseccion inter : listaDeIntersecciones) {
            inter.actualizarSensores();
        }
    }


    public void calcularDuraciones() {
        if (algoritmoControl != null) {
            algoritmoControl.aplicar(listaDeIntersecciones);
            return;
        }

 
        for (Interseccion inter : listaDeIntersecciones) {
            int flujo = inter.obtenerFlujo();
            int tiempoVerde = Math.min(60, 10 + flujo * 2);
            int tiempoAmarillo = 3;
            int tiempoRojo = tiempoVerde + tiempoAmarillo;
            Semaforo s = inter.getSemaforoPrincipal();
            if (s != null) {
                s.setDuraciones(tiempoVerde, tiempoAmarillo, tiempoRojo);
            }
        }
    }


    public Metrica obtenerMetricas() {
        Metrica m = new Metrica();
        int totalVehiculos = 0;
        int interCount = listaDeIntersecciones.size();
        double sumaVerde = 0;

        for (Interseccion inter : listaDeIntersecciones) {
            totalVehiculos += inter.obtenerFlujo();
            Semaforo s = inter.getSemaforoPrincipal();
            if (s != null) sumaVerde += s.getTiempoVerde();
        }

        double promEspera = interCount == 0 ? 0.0 : (totalVehiculos / (double) Math.max(1, interCount));
        double consumo = sumaVerde * 0.1; 

        m.setVehiculosProcesados(totalVehiculos);
        m.setTiempoPromedioEspera(promEspera);
        m.setConsumoEnergia(consumo);
        m.setEmbotellamientosReducidos(0); 
        return m;
    }

    public void addInterseccion(Interseccion interseccion) {
        if (interseccion != null) listaDeIntersecciones.add(interseccion);
    }

    public String toString() {
        return "Trafico{intersecciones=" + listaDeIntersecciones + "}";
    }


    public Interseccion obtenerInterseccionPorId(int id) {
        for (Interseccion inter : listaDeIntersecciones) {
            if (inter.getId() == id) return inter;
        }
        return null;
    }


    public Algoritmo getAlgoritmoControl() { return algoritmoControl; }
    public void setAlgoritmoControl(Algoritmo algoritmoControl) { this.algoritmoControl = algoritmoControl; }
    public List<Interseccion> getListaDeIntersecciones() { return listaDeIntersecciones; }
}
