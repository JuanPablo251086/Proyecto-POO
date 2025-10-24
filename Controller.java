
import java.util.ArrayList;
public class Controller
{
    private int locmaxR;
    private int locmaxC;
    private ArrayList<ArrayList<Interseccion>> intersecciones;
    private ArrayList<Interseccion> interseccionesstackeadas;
    public Controller(int maxR, int maxC)
    {
        this.locmaxR = maxR;
        this.locmaxC = maxC;
        this.intersecciones = new ArrayList<ArrayList<Interseccion>>();
        this.interseccionesstackeadas = new ArrayList<Interseccion>();
    }

    public void inicializarintersecciones()
{
    intersecciones.clear();


    for (int r = 1; r <= locmaxR; r++) {
        ArrayList<Interseccion> fila = new ArrayList<Interseccion>();
        intersecciones.add(fila);
        for (int c = 1; c <= locmaxC; c++) {
            Interseccion inter = new Interseccion(r, c);
            fila.add(inter);
            interseccionesstackeadas.add(inter);
        }
    }


    for (int r = 1; r <= locmaxR; r++) {
        for (int c = 1; c <= locmaxC; c++) {
            Interseccion inter = getInterseccion(r, c);
            int[] vecinos = inter.CalcularVecinos();
            

            if (vecinos[0] != 0 && vecinos[1] != 0) {
                Interseccion vecinoV = getInterseccion(vecinos[0], vecinos[1]);
                if (vecinoV != null) {
                    inter.anadirvecinos(vecinoV);
                }
            }
            

            if (vecinos[2] != 0 && vecinos[3] != 0) {
                Interseccion vecinoH = getInterseccion(vecinos[2], vecinos[3]);
                if (vecinoH != null) {
                    inter.anadirvecinos(vecinoH);
                }
            }
        }
    }
}

    public Interseccion getInterseccion(int r, int c)
    {
        if (r < 1 || r > locmaxR || c < 1 || c > locmaxC) {
            return null;
        }
        int rowIndex = r - 1;
        int colIndex = c - 1;

        if (rowIndex < 0 || rowIndex >= intersecciones.size()) {
            return null;
        }
        ArrayList<Interseccion> fila = intersecciones.get(rowIndex);
        if (colIndex < 0 || colIndex >= fila.size()) {
            return null;
        }
        return fila.get(colIndex);
    }
     public void printallvecinosynodos()
    {
        int size = this.interseccionesstackeadas.size();
        for (int i = 0; i < size; i++) {
            ArrayList<Arista> item = this.interseccionesstackeadas.get(i).getVecinos();

            System.out.println(item);

    }
    }
}
   
