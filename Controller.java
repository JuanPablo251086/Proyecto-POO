import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Controller {
    private ArrayList<Calle> calles;
    private ArrayList<Avenida> avenidas;
    private ArrayList<Arista> allAristas;

    private int locmaxR;
    private int locmaxC;
    private ArrayList<ArrayList<Interseccion>> intersecciones;
    private ArrayList<Interseccion> interseccionesstackeadas;
    private Vista vista;
    private TrafficController trafico;

    public Controller(int maxR, int maxC) {
        this.locmaxR = maxR;
        this.locmaxC = maxC;
        this.intersecciones = new ArrayList<ArrayList<Interseccion>>();
        this.interseccionesstackeadas = new ArrayList<Interseccion>();
        this.calles = new ArrayList<>();
        this.avenidas = new ArrayList<>();
        this.allAristas = new ArrayList<>();
        this.trafico = new TrafficController(this); // inyecta controlador para usar grafo
    }

    public void inicializarintersecciones() {
        intersecciones.clear();
        interseccionesstackeadas.clear();
        calles.clear();
        avenidas.clear();
        allAristas.clear();

        for (int r = 1; r <= locmaxR; r++) {
            ArrayList<Interseccion> fila = new ArrayList<Interseccion>();
            intersecciones.add(fila);
            for (int c = 1; c <= locmaxC; c++) {
                Interseccion inter = new Interseccion(r, c);
                fila.add(inter);
                interseccionesstackeadas.add(inter);
            }
        }

        //QUE es esto????
        for (int r = 1; r <= locmaxR; r++) {
            for (int c = 1; c <= locmaxC; c++) {
                Interseccion inter = getInterseccion(r, c);
                int[] vecinos = inter.CalcularVecinos();

                // Vecino vertical (vrow,vcol)
                if (vecinos[0] != 0 && vecinos[1] != 0) {
                    Interseccion vecinoV = getInterseccion(vecinos[0], vecinos[1]);
                    if (vecinoV != null) {
                        // Determinar tipo: si misma fila -> horizontal (Calle), si misma columna -> Avenida
                        // Aquí: vertical => columna igual => Avenida
                        if (!existeArista(inter, vecinoV)) {
                            Avenida a1 = new Avenida(inter, vecinoV);
                            //Avenida a2 = new Avenida(vecinoV, inter); elimine este comportamiento que estaba causando el BUG
                            inter.anadirArista(a1);
                            avenidas.add(a1);

                            allAristas.add(a1);

                        }
                    }
                }

                // Vecino horizontal (hrow,hcol)
                if (vecinos[2] != 0 && vecinos[3] != 0) {
                    Interseccion vecinoH = getInterseccion(vecinos[2], vecinos[3]);
                    if (vecinoH != null) {
                        if (!existeArista(inter, vecinoH)) {
                            Calle c1 = new Calle(inter, vecinoH);
                            inter.anadirArista(c1);
                            calles.add(c1);
                            allAristas.add(c1);
                        
                        }
                    }
                }
            }
        }
    }

    private boolean existeArista(Interseccion a, Interseccion b) {
        for (Arista ar : a.getVecinos()) {
            if (ar.getTo().equals(b)) return true;
        }
        return false;
    }

    public Interseccion getInterseccion(int r, int c) {
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

    // Imprime para debugging: por cada intersección, lista sus aristas salientes
    public void printallvecinosynodos() {
        int size = this.interseccionesstackeadas.size();
        for (int i = 0; i < size; i++) {
            Interseccion node = this.interseccionesstackeadas.get(i);
            List<Arista> item = node.getVecinos();
            System.out.println("Interseccion " + node + " -> ");
            for (Arista a : item) {
                System.out.println("   " + a);
            }
        }
    }

    // Inicializa vehiculosPorMin aleatorios en todas las aristas y recalcula pesos.
    // Esto es útil para pruebas iniciales.
    public void inicializarAristasYPesosAleatorios() {
        Random rnd = new Random();
        for (Arista a : allAristas) {
            // generar entre 0 y 80 vehículos por minuto (asunción)
            int vpm = rnd.nextInt(81);
            a.setVehiculosPorMin(vpm);
        }
    }

    // Seteo manual posible: asignar vehiculosPorMin a una arista específica (útil para UI/test)
    public void setVehiculosEnArista(Interseccion from, Interseccion to, int vpm) throws GraphException {
        boolean found = false;
        for (Arista a : allAristas) {
            if (a.getFrom().equals(from) && a.getTo().equals(to)) {
                a.setVehiculosPorMin(vpm);
                found = true;
                break;
            }
        }
        if (!found) throw new GraphException("Arista no encontrada entre " + from + " y " + to);
    }

    public TrafficController getTrafficController() {
        return this.trafico;
    }

    // Accesores útiles
    public List<Arista> getAllAristas() {
        return this.allAristas;
    }

    public List<Interseccion> getInterseccionesStackeadas() {
        return this.interseccionesstackeadas;
    }
}
