import java.util.*;

public class Semaforo {

    private String colorActual;
    private int tiempoVerde;
    private int tiempoAmarillo;
    private int tiempoRojo;


    public Semaforo(String colorActual, int tiempoVerde, int tiempoAmarillo, int tiempoRojo) {
        if (colorActual == null || colorActual.isBlank()) colorActual = "ROJO";
        this.colorActual = colorActual.toUpperCase();
        this.tiempoVerde = tiempoVerde;
        this.tiempoAmarillo = tiempoAmarillo;
        this.tiempoRojo = tiempoRojo;
    }


    public void cambiarColor() {
        String c = (colorActual == null) ? "ROJO" : colorActual.toUpperCase();
        switch (c) {
            case "VERDE":
                colorActual = "AMARILLO";
                break;
            case "AMARILLO":
                colorActual = "ROJO";
                break;
            case "ROJO":
            default:
                colorActual = "VERDE";
                break;
        }
    }

    public String getColor() {
        return colorActual;
    }

    public void setDuraciones(int tiempoVerde, int tiempoAmarillo, int tiempoRojo) {
        if (tiempoVerde < 0 || tiempoAmarillo < 0 || tiempoRojo < 0)
            throw new IllegalArgumentException("Los tiempos no pueden ser negativos");
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
