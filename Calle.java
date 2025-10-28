public class Calle extends Arista {

    public Calle(Interseccion f, Interseccion t) {
        super(f, t);
        setWeightMultiplier(Arista.getMultCalle());
    }

    public Calle(Interseccion f, Interseccion t, int vpm) {
        this(f, t);
        setVehiculosPorMin(vpm);
    }
}
