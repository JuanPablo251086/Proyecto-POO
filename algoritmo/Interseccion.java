

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Interseccion {
    private int row, col;
    private float weight; 
    private ArrayList<Arista> vecinos;

    public Interseccion(int r, int c) {
        this.row = r;
        this.col = c;
        this.weight = 0f;
        this.vecinos = new ArrayList<Arista>();
    }

    @Override
    public String toString() {
        return "(" + this.row + "," + this.col + ")";
    }

    public int[] CalcularVecinos() {
        int vrow = (this.col % 2 == 1) ? this.row - 1 : this.row + 1;
        int vcol = this.col;
        if (vrow < 1 || vrow > Main.maxR || vcol < 1 || vcol > Main.maxC) {
            vrow = 0;
            vcol = 0;
        }

        int hrow = this.row;
        int hcol = (this.row % 2 == 1) ? this.col + 1 : this.col - 1;
        if (hrow < 1 || hrow > Main.maxR || hcol < 1 || hcol > Main.maxC) {
            hrow = 0;
            hcol = 0;
        }

        return new int[]{vrow, vcol, hrow, hcol};
    }

    
    public void anadirArista(Arista ar) {
        if (ar != null) {
            vecinos.add(ar);
        }
    }

    public List<Arista> getVecinos() {
        return this.vecinos;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setWeight(float w) {
        this.weight = w;
    }

    public float getWeight() {
        return this.weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Interseccion)) return false;
        Interseccion that = (Interseccion) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
