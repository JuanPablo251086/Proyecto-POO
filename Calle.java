// Calle.java
// Subclase de Arista para calles (por ejemplo, vías de menor capacidad).
// Asumí que las calles tienen un multiplicador mayor (más penalización por vehículo).

public class Calle extends Arista {

    public Calle(Interseccion f, Interseccion t) {
        super(f, t);
        // Asunción: las calles tienen mayor peso por vehículo (menos capacidad)
        this.weightMultiplier = 1.0f;
        calcularPeso();
    }

    // Si se desea lógica específica puede sobrescribirse calcularPeso aquí
}
