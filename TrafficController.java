// Controlador de rutas: resuelve caminos óptimos sobre la red vial usando A*.
// El costo combina el peso de la arista (0..1), la saturación del destino y la
// heurística euclidiana entre nodos para priorizar rutas prometedoras.

import java.util.*;

public class TrafficController {
    private Controller controller;

    public TrafficController(Controller ctrl) {
        this.controller = ctrl;
    }

    // Retorna lista de intersecciones (origen ... destino) que minimizan la suma de pesos.
    public List<Interseccion> calcularRutaMinima(Interseccion origen, Interseccion destino) throws GraphException {
        if (origen == null || destino == null) {
            throw new GraphException("Origen o destino nulo");
        }

        controller.actualizarSemaforos();

        Map<Interseccion, Float> gScore = new HashMap<>();
        Map<Interseccion, Float> fScore = new HashMap<>();
        Map<Interseccion, Interseccion> cameFrom = new HashMap<>();
        Set<Interseccion> cerrados = new HashSet<>();

        for (Interseccion nodo : controller.getInterseccionesStackeadas()) {
            gScore.put(nodo, Float.POSITIVE_INFINITY);
            fScore.put(nodo, Float.POSITIVE_INFINITY);
            cameFrom.put(nodo, null);
        }
        gScore.put(origen, 0f);
        fScore.put(origen, heuristica(origen, destino));

        PriorityQueue<Interseccion> abiertos = new PriorityQueue<>(Comparator.comparingDouble(fScore::get));
        abiertos.add(origen);

        while (!abiertos.isEmpty()) {
            Interseccion actual = abiertos.poll();
            if (actual.equals(destino)) {
                return reconstruirRuta(cameFrom, destino);
            }
            if (!cerrados.add(actual)) {
                continue;
            }

            for (Arista ar : actual.getVecinos()) {
                Interseccion vecino = ar.getTo();
                if (cerrados.contains(vecino)) continue;

                float costoDinamico = calcularCostoDinamico(ar);
                float tentativo = gScore.get(actual) + costoDinamico;

                if (tentativo < gScore.get(vecino)) {
                    cameFrom.put(vecino, actual);
                    gScore.put(vecino, tentativo);
                    float estimado = tentativo + heuristica(vecino, destino);
                    fScore.put(vecino, estimado);
                    abiertos.remove(vecino);
                    abiertos.add(vecino);
                }
            }
        }

        throw new GraphException("No existe ruta entre " + origen + " y " + destino);
    }

    private List<Interseccion> reconstruirRuta(Map<Interseccion, Interseccion> prev, Interseccion destino) {
        LinkedList<Interseccion> ruta = new LinkedList<>();
        Interseccion actual = destino;
        while (actual != null) {
            ruta.addFirst(actual);
            actual = prev.get(actual);
        }
        return ruta;
    }

    private float heuristica(Interseccion a, Interseccion b) {
        int dx = a.getRow() - b.getRow();
        int dy = a.getCol() - b.getCol();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    private float calcularCostoDinamico(Arista arista) {
        float base = arista.getWeight();
        float semaforoLoad = obtenerCargaSemaforo(arista);
        float penalizacionSemaforo = 0.25f * semaforoLoad;
        float costo = base + penalizacionSemaforo;
        return Math.min(1f, Math.max(0f, costo));
    }

    private float obtenerCargaSemaforo(Arista arista) {
        Semaforo semaforoDestino = arista.getTo().getSemaforo();
        if (semaforoDestino == null) return 0f;
        boolean esVertical = arista instanceof Avenida ||
                arista.getFrom().getCol() == arista.getTo().getCol();
        return esVertical ? semaforoDestino.getLoadNS() : semaforoDestino.getLoadEW();
    }
}
