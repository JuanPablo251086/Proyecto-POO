public class Calle extends Arista {

    public Calle(Interseccion f, Interseccion t) {
        super(f, t);
        // Asunción: las calles tienen mayor peso por vehículo (menos capacidad)
        //el polimorfismo nos deja editar esto mas facilmente despues y ver como cambia el comportamiento de dijakstra
        this.weightMultiplier = 1.0f;
        calcularPeso();
    }

    // Si se desea lógica específica puede sobrescribirse calcularPeso aquí
}
