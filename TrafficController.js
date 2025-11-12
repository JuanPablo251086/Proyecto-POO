/**
 * Clase TrafficController - Maneja los algoritmos de pathfinding
 * Equivalente a TrafficController.java
 */
export class TrafficController {
    constructor(intersecciones) {
        this.intersecciones = intersecciones; // Map<string, Interseccion>
    }

    /**
     * Implementación del algoritmo de Dijkstra
     * Encuentra la ruta más corta entre origen y destino
     */
    dijkstra(origen, destino) {
        const dist = new Map();
        const prev = new Map();
        const visitados = new Set();
        const noVisitados = new Set();

        // Inicialización
        for (const [id, interseccion] of this.intersecciones) {
            dist.set(id, Infinity);
            prev.set(id, null);
            noVisitados.add(id);
        }

        const origenId = origen.getId();
        const destinoId = destino.getId();
        dist.set(origenId, 0);

        while (noVisitados.size > 0) {
            // Encontrar el nodo no visitado con menor distancia
            let nodoActualId = null;
            let menorDist = Infinity;

            for (const id of noVisitados) {
                if (dist.get(id) < menorDist) {
                    menorDist = dist.get(id);
                    nodoActualId = id;
                }
            }

            if (nodoActualId === null || menorDist === Infinity) {
                break; // No hay más nodos alcanzables
            }

            // Si llegamos al destino, podemos terminar
            if (nodoActualId === destinoId) {
                break;
            }

            noVisitados.delete(nodoActualId);
            visitados.add(nodoActualId);

            const nodoActual = this.intersecciones.get(nodoActualId);

            // Revisar vecinos
            for (const arista of nodoActual.vecinos) {
                const vecinoId = arista.to.getId();

                if (visitados.has(vecinoId)) {
                    continue;
                }

                const distAlternativa = dist.get(nodoActualId) + arista.weight;

                if (distAlternativa < dist.get(vecinoId)) {
                    dist.set(vecinoId, distAlternativa);
                    prev.set(vecinoId, nodoActualId);
                }
            }
        }

        // Reconstruir el camino
        const ruta = [];
        const aristas = [];
        let actualId = destinoId;
        let pesoTotal = dist.get(destinoId);

        if (pesoTotal === Infinity) {
            return {
                encontrada: false,
                ruta: [],
                aristas: [],
                pesoTotal: Infinity,
                mensaje: 'No existe ruta entre origen y destino'
            };
        }

        while (actualId !== null) {
            ruta.unshift(this.intersecciones.get(actualId));
            const prevId = prev.get(actualId);
            
            if (prevId !== null) {
                const prevInterseccion = this.intersecciones.get(prevId);
                // Encontrar la arista correspondiente
                const arista = prevInterseccion.vecinos.find(a => a.to.getId() === actualId);
                if (arista) {
                    aristas.unshift(arista);
                }
            }
            
            actualId = prevId;
        }

        return {
            encontrada: true,
            ruta,
            aristas,
            pesoTotal,
            mensaje: `Ruta encontrada con peso total: ${pesoTotal.toFixed(3)}`
        };
    }

    /**
     * Heurística para A* (distancia Manhattan en grilla hexagonal)
     */
    heuristica(interseccion, destino) {
        const dr = Math.abs(interseccion.row - destino.row);
        const dc = Math.abs(interseccion.col - destino.col);
        return (dr + dc) / 10; // Normalizado
    }

    /**
     * Implementación del algoritmo A*
     * Similar a Dijkstra pero con heurística para mejor rendimiento
     */
    aStar(origen, destino) {
        const gScore = new Map(); // Costo desde origen
        const fScore = new Map(); // gScore + heurística
        const prev = new Map();
        const openSet = new Set();
        const closedSet = new Set();

        // Inicialización
        for (const [id] of this.intersecciones) {
            gScore.set(id, Infinity);
            fScore.set(id, Infinity);
            prev.set(id, null);
        }

        const origenId = origen.getId();
        const destinoId = destino.getId();
        
        gScore.set(origenId, 0);
        fScore.set(origenId, this.heuristica(origen, destino));
        openSet.add(origenId);

        while (openSet.size > 0) {
            // Encontrar nodo en openSet con menor fScore
            let actualId = null;
            let menorF = Infinity;

            for (const id of openSet) {
                if (fScore.get(id) < menorF) {
                    menorF = fScore.get(id);
                    actualId = id;
                }
            }

            if (actualId === null) {
                break;
            }

            // Si llegamos al destino
            if (actualId === destinoId) {
                // Reconstruir ruta
                const ruta = [];
                const aristas = [];
                let current = destinoId;
                let pesoTotal = gScore.get(destinoId);

                while (current !== null) {
                    ruta.unshift(this.intersecciones.get(current));
                    const prevId = prev.get(current);
                    
                    if (prevId !== null) {
                        const prevInterseccion = this.intersecciones.get(prevId);
                        const arista = prevInterseccion.vecinos.find(a => a.to.getId() === current);
                        if (arista) {
                            aristas.unshift(arista);
                        }
                    }
                    
                    current = prevId;
                }

                return {
                    encontrada: true,
                    ruta,
                    aristas,
                    pesoTotal,
                    mensaje: `Ruta encontrada (A*) con peso total: ${pesoTotal.toFixed(3)}`
                };
            }

            openSet.delete(actualId);
            closedSet.add(actualId);

            const nodoActual = this.intersecciones.get(actualId);

            // Revisar vecinos
            for (const arista of nodoActual.vecinos) {
                const vecinoId = arista.to.getId();

                if (closedSet.has(vecinoId)) {
                    continue;
                }

                const gScoreTentativo = gScore.get(actualId) + arista.weight;

                if (!openSet.has(vecinoId)) {
                    openSet.add(vecinoId);
                } else if (gScoreTentativo >= gScore.get(vecinoId)) {
                    continue;
                }

                prev.set(vecinoId, actualId);
                gScore.set(vecinoId, gScoreTentativo);
                fScore.set(vecinoId, gScoreTentativo + this.heuristica(arista.to, destino));
            }
        }

        return {
            encontrada: false,
            ruta: [],
            aristas: [],
            pesoTotal: Infinity,
            mensaje: 'No existe ruta entre origen y destino'
        };
    }

    /**
     * Compara ambos algoritmos y retorna estadísticas
     */
    compararAlgoritmos(origen, destino) {
        const startDijkstra = performance.now();
        const resultadoDijkstra = this.dijkstra(origen, destino);
        const tiempoDijkstra = performance.now() - startDijkstra;

        const startAStar = performance.now();
        const resultadoAStar = this.aStar(origen, destino);
        const tiempoAStar = performance.now() - startAStar;

        return {
            dijkstra: {
                ...resultadoDijkstra,
                tiempo: tiempoDijkstra
            },
            aStar: {
                ...resultadoAStar,
                tiempo: tiempoAStar
            }
        };
    }
}