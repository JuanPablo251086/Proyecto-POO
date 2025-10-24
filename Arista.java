public class Arista
{
    private Interseccion from;
    private Interseccion to;
    private float weight;

    public Arista(Interseccion f, Interseccion t)
    {
        this.from = f;
        this.to = t;
        this.weight = 0;
    }
    @Override
    public String toString()
    {
        return "De" + this.from + " a " + this.to + " con peso " + this.weight;
    }
}