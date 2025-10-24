public class Arista
{
    protected Interseccion from;
    protected Interseccion to;
    protected float weight;

    //TODO
    protected int vehiculospormin; //le estabamos dando demasiado enfoque a diferentes tipos de vehiculos, pero eso sobrecomplica el diseño. Mejor dejemoslo como solo un numero correspondiente a cada calle, representado un promedio digamos que de carros por minuto que circulan.
    //en si la idea es que sirva para caluclar el peso, que es lo que nos importa para esta clase relamente. 


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

    public void calcularpeso()
    {
        //podemos hacer algo sencillo por el momento, como carros * weightmultiplier dependiendo si es calle o avenida. Luego lo podemos complicar más
    }
}