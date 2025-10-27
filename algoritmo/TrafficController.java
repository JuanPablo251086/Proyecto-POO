

import java.util.*;

public class TrafficController {
    private Controller controller;

    public TrafficController(Controller ctrl) {
        this.controller = ctrl;
    }

    public List<Interseccion> calcularRutaMinima(Interseccion origen, Interseccion destino) throws GraphException {
        if (origen == null || destino == null) {
            throw new GraphException("Origen o destino nulo");
        }

        Map<Interseccion, Float> dist = new HashMap<>();
        Map<Interseccion, Interseccion> prev = new HashMap<>();
        Set<Interseccion> visitados = new HashSet<>();

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

            for (Arista ar : actual.getVecinos()) {
                Interseccion vecino = ar.getTo();
                if (visitados.contains(vecino)) continue;
                float pesoArista = ar.getWeight();
                float alt = dist.get(actual) + pesoArista; 

                if (alt < dist.get(vecino)) {
                    dist.put(vecino, alt);
                    prev.put(vecino, actual);
                    pq.remove(vecino); 
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
