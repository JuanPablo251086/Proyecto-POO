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
    /**
     * Establece un nuevo tamaño para la grilla
     * @param {number} size Nuevo tamaño de la grilla (ej: 8)
     */
    setGridSize(size) {
        this.gridSize = size;
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
        actualizarSemaforo(to);
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

    // Getters de configuración global
    public int getMaxVehPerMin() { return Arista.getMaxVehPerMin(); }
    public float getMultCalle() { return Arista.getMultCalle(); }
    public float getMultAvenida() { return Arista.getMultAvenida(); }

    // Alias para UI
    public List<Interseccion> getIntersecciones() { return this.interseccionesstackeadas; }

    // Ajustes de configuración global y recálculo de pesos
    public void setMaxVehPerMin(int v) {
        Arista.setMaxVehPerMin(v);
        recalcularPesos();
    }

    public void setMultCalle(float m) {
        Arista.setMultCalle(m);
        for (Arista a : allAristas) {
            if (a instanceof Calle) {
                a.setWeightMultiplier(Arista.getMultCalle());
                a.onConfigChanged();
            }
        }
        actualizarSemaforos();
    }

    public void setMultAvenida(float m) {
        Arista.setMultAvenida(m);
        for (Arista a : allAristas) {
            if (a instanceof Avenida) {
                a.setWeightMultiplier(Arista.getMultAvenida());
                a.onConfigChanged();
            }
        }
        actualizarSemaforos();
    }

    public void recalcularPesos() {
        for (Arista a : allAristas) {
            a.onConfigChanged();
        }
        actualizarSemaforos();
    }

    // Seteo de tráfico por IDs "r,c"
    public void setVehiculos(String fromId, String toId, int vpm) throws GraphException {
        Interseccion from = buscarPorId(fromId);
        Interseccion to = buscarPorId(toId);
        setVehiculosEnArista(from, to, vpm);
    }

    private Interseccion buscarPorId(String id) throws GraphException {
        String[] parts = id.split(",");
        if (parts.length != 2) throw new GraphException("Formato de id inválido: " + id);
        int r;
        int c;
        try {
            r = Integer.parseInt(parts[0].trim());
            c = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException ex) {
            throw new GraphException("Formato de id inválido: " + id);
        }
        Interseccion inter = getInterseccion(r, c);
        if (inter == null) throw new GraphException("Interseccion no encontrada: " + id);
        return inter;
    }

    public java.util.List<Interseccion> calcularRuta(int filaOrigen, int colOrigen,
                                                     int filaDestino, int colDestino) throws GraphException {
        Interseccion origen = getInterseccion(filaOrigen, colOrigen);
        Interseccion destino = getInterseccion(filaDestino, colDestino);
        if (origen == null || destino == null) {
            throw new GraphException("Origen o destino inválido");
        }
        return trafico.calcularRutaMinima(origen, destino);
    }

    public void actualizarSemaforos() {
        for (Interseccion i : interseccionesstackeadas) {
            actualizarSemaforo(i);
        }
    }

    public void actualizarSemaforo(Interseccion destino) {
        float sumV = 0f, sumH = 0f;
        int cntV = 0, cntH = 0;

        for (Arista a : allAristas) {
            if (a.getTo().equals(destino)) {
                Interseccion from = a.getFrom();
                if (from.getCol() == destino.getCol() && from.getRow() != destino.getRow()) {
                    sumV += a.getWeight(); cntV++;
                } else if (from.getRow() == destino.getRow() && from.getCol() != destino.getCol()) {
                    sumH += a.getWeight(); cntH++;
                }
            }
        }
        float cargaNS = (cntV == 0) ? 0f : (sumV / cntV);
        float cargaEW = (cntH == 0) ? 0f : (sumH / cntH);

        destino.getSemaforo().actualizarConCargas(cargaNS, cargaEW);
    }

    public void postConstruccionActualizarSemaforos() {
        actualizarSemaforos();
    }

    public void setSemaforosPolicy(int cycleSeconds, int yellow, int allRed, int minGreen, int maxGreen) {
        for (Interseccion i : interseccionesstackeadas) {
            Semaforo s = i.getSemaforo();
            s.setCycleSeconds(cycleSeconds);
            s.setYellow(yellow);
            s.setAllRed(allRed);
            s.setMinGreen(minGreen);
            s.setMaxGreen(maxGreen);
        }
        actualizarSemaforos();
    }
}
