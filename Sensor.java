
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


    public int getIdSensor(){
        return idSensor; 
    }
    public int getConteoVehiculosDetectados(){
        return conteoVehiculosDetectados; 
    }


    public void setCantidad(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        this.conteoVehiculosDetectados = cantidad;
    }

    public void actualizarConteo(int delta) {
        long nuevo = (long) conteoVehiculosDetectados + delta;
        if (nuevo < 0) {
           
            throw new IllegalArgumentException("El conteo no puede ser negativo");
        }
        if (nuevo > Integer.MAX_VALUE) {

            throw new IllegalArgumentException("El conteo excede el límite soportado");
        }
        this.conteoVehiculosDetectados = (int) nuevo; 
    }


    @Override
    public String toString() {
        return "Sensor{idSensor=" + idSensor +
               ", conteoVehiculosDetectados=" + conteoVehiculosDetectados + '}';
    }

}