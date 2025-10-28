// Contiene el esqueleto/implementación del algoritmo de rutas (Dijkstra) sobre el grafo.
// Usa los pesos de las aristas (0..1); Dijkstra busca minimizar la suma de pesos.
// Nota: dado que los pesos están en 0..1, la suma tiene sentido. Si en el futuro quieres
// considerar "steps" (número de aristas) como tie-breaker, se puede ajustar fácilmente.

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

        // Dijkstra: distancia mínima (float), prev map
        Map<Interseccion, Float> dist = new HashMap<>();
        Map<Interseccion, Interseccion> prev = new HashMap<>();
        Set<Interseccion> visitados = new HashSet<>();

        // Inicializar distancias infinitas
        for (Interseccion n : controller.getInterseccionesStackeadas()) {
            dist.put(n, Float.POSITIVE_INFINITY);
            prev.put(n, null);
        }
        dist.put(origen, 0f);

        PriorityQueue<Interseccion> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(origen);

        while (!pq.isEmpty()) {
            Interseccion actual = pq.poll();
            if (visitados.contains(actual)) continue;
            visitados.add(actual);

            if (actual.equals(destino)) break;

            // Explorar vecinos (aristas salientes)
            for (Arista ar : actual.getVecinos()) {
                Interseccion vecino = ar.getTo();
                if (visitados.contains(vecino)) continue;
                float pesoArista = ar.getWeight();
                float alt = dist.get(actual) + pesoArista; // coste acumulado

                if (alt < dist.get(vecino)) {
                    dist.put(vecino, alt);
                    prev.put(vecino, actual);
                    pq.remove(vecino); // actualizar prioridad si ya estaba
                    pq.add(vecino);
                }
            }
        }

        // Reconstruir ruta
        if (dist.get(destino).isInfinite()) {
            throw new GraphException("No existe ruta entre " + origen + " y " + destino);
        }

        LinkedList<Interseccion> ruta = new LinkedList<>();
        Interseccion u = destino;
        while (u != null) {
            ruta.addFirst(u);
            u = prev.get(u);
        }
        return ruta;
    }
}
