

/*
Programación Orientada a Objetos
Proyecto: Sistema de Semáforos Inteligentes
Clase: Sensor
Descripción: Representa un sensor que cuenta vehículos en una intersección.
*/ 

public class Sensor{
    //Atributos
    private final int idSensor; 
    private int conteoVehiculosDetectados; 
    
    //Constructor de la clase 
    public Sensor(int idSensor){
        if (idSensor <= 0) {
            throw new IllegalArgumentException("El ID del sensor debe ser un número positivo.");
        }
        this.idSensor = idSensor;
        this.conteoVehiculosDetectados = 0; 
    }

    //Métodos de la clase 
    //Getters 
    public int getIdSensor(){
        return idSensor; 
    }
    public int getConteoVehiculosDetectados(){
        return conteoVehiculosDetectados; 
    }

    //Setters 
    public void setCantidad(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        this.conteoVehiculosDetectados = cantidad;
    }

    // Restantes métodos de la clase
    public void actualizarConteo(int delta) {
        long nuevo = (long) conteoVehiculosDetectados + delta;
        if (nuevo < 0) {
            // Asegurarse de que el conteo no sea negativo (no puede haber negativos vehículos)
            throw new IllegalArgumentException("El conteo no puede ser negativo");
        }
        if (nuevo > Integer.MAX_VALUE) {
            // Asegurarse de que el conteo no exceda el límite de un entero en memoria de la computadora
            throw new IllegalArgumentException("El conteo excede el límite soportado");
        }
        this.conteoVehiculosDetectados = (int) nuevo; //retorna el número actualizado de vehículos detectados
    }

    // Método toString para poder representar el objeto como una cadena
    @Override
    public String toString() {
        return "Sensor{idSensor=" + idSensor +
               ", conteoVehiculosDetectados=" + conteoVehiculosDetectados + '}';
    }

}