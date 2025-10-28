public class Avenida extends Arista {
    public Avenida(Interseccion f, Interseccion t) {
        super(f, t);
        setWeightMultiplier(Arista.getMultAvenida());
    }

    public Avenida(Interseccion f, Interseccion t, int vpm) {
        this(f, t);
        setVehiculosPorMin(vpm);
    }
}