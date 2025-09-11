

/*
Programación Orientada a Objetos
Proyecto: Sistema de Semáforos Inteligentes
Clase: Vehículo 
Descripción: Representa el comportamiento de un vehículo que pasa por una intersección.
*/ 

public class Vehiculo{
    //Atributos
    private final String idVehiculo;
    private TipoVehiculo tipo;              // Podemos agregar ENUM del tipo de vehículos
    private int velocidad;             
    private int tiempoAparicion;            // Momento en que aparece en la simulación
    private String idInterseccionDestino;   // Identificador de la intersección de destino
    private double posicion;                // distancia recorrida en metros
    private double tiempoEspera;            // acumulado en segundos



    
    // Constructor de la clase
        public Vehiculo(String idVehiculo, String tipo, double velocidad, 
        int tiempoAparicion, String idInterseccionDestino, double posicion) {
            // Validaciones de los parámetros de entrada
            if (idVehiculo == null || idVehiculo.isBlank()) throw new IllegalArgumentException("El idVehiculo no puede estar vacío");
            if (tipo == null || tipo.isBlank()) throw new IllegalArgumentException("El tipo no puede estar vacío");
            if (velocidad < 0) throw new IllegalArgumentException("La velocidad no puede ser negativa");
            if (tiempoAparicion < 0) throw new IllegalArgumentException("El tiempo de aparición no puede ser negativo");
            if (idInterseccionDestino == null || idInterseccionDestino.isBlank())
            throw new IllegalArgumentException("El destino no puede estar vacío");
            if (posicion < 0) throw new IllegalArgumentException("La posición no puede ser negativa");

        this.idVehiculo = idVehiculo;
        this.tipo = TipoVehiculo.valueOf(tipo.toUpperCase());
        this.velocidad = (int) velocidad;
        this.tiempoAparicion = tiempoAparicion;
        this.idInterseccionDestino = idInterseccionDestino;
        this.posicion = posicion;
    }

    // Métodos de la clase
    // Getters y Setters
    public String getId() {
        return idVehiculo;
    }

    public String getTipo() {
        return tipo.name();
    }

    public double getVelocidad() {
        return velocidad;
    }

    public int getTiempoAparicion() {
        return tiempoAparicion;
    }

    public String getIdInterseccionDestino() {
        return idInterseccionDestino;
    }

    public void setTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo no puede estar vacío");
        }
        this.tipo = TipoVehiculo.valueOf(tipo.toUpperCase());
    }

    public void setVelocidad(double velocidad) {
        if (velocidad < 0) {
            throw new IllegalArgumentException("La velocidad no puede ser negativa");
        }
        this.velocidad = (int) velocidad;
    }

    public void setTiempoAparicion(int tiempoAparicion) {
        if (tiempoAparicion < 0) {
            throw new IllegalArgumentException("El tiempo de aparición no puede ser negativo");
        }
        this.tiempoAparicion = tiempoAparicion;
    }

    public void setIdInterseccionDestino(String idInterseccionDestino) {
        if (idInterseccionDestino == null || idInterseccionDestino.isBlank()) {
            throw new IllegalArgumentException("El destino no puede estar vacío");
        }
        this.idInterseccionDestino = idInterseccionDestino;
    }



    // Metodos restantes 
    public void acelerar(int incremento) { // Simula la aceleración del vehículo (con incremento de velocidad)
        if (incremento > 0) {
            this.velocidad += incremento;
        }
    }

    // Simula la desaceleración del vehículo (con decremento de velocidad)
    public void frenar(int decremento) {
        if (decremento > 0) {
            this.velocidad = Math.max(0, this.velocidad - decremento);
        }
    }

    //Metodos para el movimiento y manejo de la posición del vehículo 
    public void mover(double tiempoSegundos) {
        if (tiempoSegundos < 0) {
            throw new IllegalArgumentException("El tiempo no puede ser negativo");
        }
        // Convertir velocidad de km/h a m/s y calcular la nueva posición
        double velocidadMS = (this.velocidad * 1000) / 3600.0;
        this.posicion += velocidadMS * tiempoSegundos;
    }

    public double getPosicion() {
        return posicion;
    }
    public void setPosicion(double posicion) {
        if (posicion < 0) {
            throw new IllegalArgumentException("La posición no puede ser negativa");
        }
        this.posicion = posicion;
    }
    public double getTiempoEspera() {
        return tiempoEspera;
    }
    public void setTiempoEspera(double tiempoEspera) {
        if (tiempoEspera < 0) {
            throw new IllegalArgumentException("El tiempo de espera no puede ser negativo");
        }
        this.tiempoEspera = tiempoEspera;
    }


    // Método toString para representar el objeto como una cadena
    @Override
    public String toString() {
        return "Vehiculo{id='" + idVehiculo + "', tipo='" + tipo + 
               "', velocidad=" + velocidad + " km/h, destino='" + idInterseccionDestino + "'}";
    }



}


