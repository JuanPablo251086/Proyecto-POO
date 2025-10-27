import java.util.ArrayList;
public class Interseccion
{
    private int row, col;
    private ArrayList<Arista> vecinos;
    public Interseccion(int r, int c)
    {
        this.row = r;
        this.col = c;
        this.vecinos = new ArrayList<Arista>();
    }

    @Override
    public String toString() {
        return "(" + this.row + "," + this.col+")";
    }
    public int[] CalcularVecinos() {

    // Vecino vertical
    int vrow = (this.col % 2 == 1) ? this.row - 1 : this.row + 1;
    int vcol = this.col;
    if (vrow < 1 || vrow > Main.maxR || vcol < 1 || vcol > Main.maxC) {
        vrow = 0;
        vcol = 0;
    }

    // Vecino horizontal
    int hrow = this.row;
    int hcol = (this.row % 2 == 1) ? this.col + 1 : this.col - 1;
    if (hrow < 1 || hrow > Main.maxR || hcol < 1 || hcol > Main.maxC) {
        hrow = 0;
        hcol = 0;
    }

    return new int[]{vrow, vcol, hrow, hcol};
}

    
    public void anadirvecinos(Interseccion to)
    {
        vecinos.add(new Arista(this,to));
    }
    public ArrayList<Arista> getVecinos()
    {
        return this.vecinos;
    }
}
