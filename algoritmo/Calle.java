
public class Calle extends Arista {

    public Calle(Interseccion f, Interseccion t) {
        super(f, t);
        this.weightMultiplier = 1.0f;
        calcularPeso();
    }

}
