import java.util.*;

public class Semaforo {
    // Atributos 
    private String colorActual;
    private int tiempoVerde;
    private int tiempoAmarillo;
    private int tiempoRojo;

    // Constructor
    public Semaforo(String colorActual, int tiempoVerde, int tiempoAmarillo, int tiempoRojo) {
        this.colorActual = colorActual;
        this.tiempoVerde = tiempoVerde;
        this.tiempoAmarillo = tiempoAmarillo;
        this.tiempoRojo = tiempoRojo;
    }

    // Métodos 
    public void cambiarColor() {
        // falta implementar cambio de color
    }

    public String getColor() {
        return colorActual;
    }

    public void setDuraciones(int tiempoVerde, int tiempoAmarillo, int tiempoRojo) {
        this.tiempoVerde = tiempoVerde;
        this.tiempoAmarillo = tiempoAmarillo;
        this.tiempoRojo = tiempoRojo;
    }

    public int getTiempoVerde() {
        return tiempoVerde;
    }

    public int getTiempoAmarillo() {
        return tiempoAmarillo;
    }

    public int getTiempoRojo() {
        return tiempoRojo;
    }

    public String toString() {
        return "Semaforo{" +
                "colorActual='" + colorActual + '\'' +
                ", tiempoVerde=" + tiempoVerde +
                ", tiempoAmarillo=" + tiempoAmarillo +
                ", tiempoRojo=" + tiempoRojo +
                '}';
    }
}
