public class Avenida extends Arista
{
    float weightmultiplier;
    //TODO
    public Avenida(Interseccion f, Interseccion t)
    {
        super(f, t);
    }
}
//puede servir mas que nada para la configuración global con el mutliplier, que se puede modificar como veamos que funcione mejor. 
//recuerden que los pesos establecimos que van a estar de 0 a 1. 