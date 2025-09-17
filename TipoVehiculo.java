

public enum TipoVehiculo {
    // Tipos de vehículos: Largo del vehículo (m), ancho del vehículo (m), velocidad máxima (m/s), aceleración (m/s²)
    AUTOMOVIL(4.5, 1.8, 30.0, 3.0), 
    CAMION(12.0, 2.5, 25.0, 2.0), 
    BUS(12.0, 2.5, 20.0, 1.5); 

    private final double largo;
    private final double ancho;
    private final double velocidadMaxima;
    private final double aceleracion;

    TipoVehiculo(double largo, double ancho, double velocidadMaxima, double aceleracion) {
        this.largo = largo;
        this.ancho = ancho;
        this.velocidadMaxima = velocidadMaxima;
        this.aceleracion = aceleracion;
    }

    public double getLargo() {
        return largo;
    }

    public double getAncho() {
        return ancho;
    }

    public double getVelocidadMaxima() {
        return velocidadMaxima;
    }

    public double getAceleracion() {
        return aceleracion;
    }

    // Métodos de conversión entre km/h y m/s como helpers 
    public static double kmhToMps(double kmh) { return kmh / 3.6; }
    public static double mpsToKmh(double mps) { return mps * 3.6; }

}


