public class Main
{
    public static int maxR = 5;
    public static int maxC = 5;

    public static void main(String[] args)
{
    Controller controlador = new Controller(maxR, maxC);
    controlador.inicializarintersecciones();

    Interseccion inter = controlador.getInterseccion(3,3);
    int[] vecinos = inter.CalcularVecinos();
    controlador.printallvecinosynodos();

    //si pueden alguien eche un chataso que les de condiciones inciiales aleatorias para empezar a probar el programa despues
}
}