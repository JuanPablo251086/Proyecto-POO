import { Interseccion } from './Interseccion.js';
import { Arista } from './Arista.js';
import { TrafficController } from './TrafficController.js';

/**
 * Clase Controller - Gestiona el sistema completo de tráfico
 * Equivalente a Controller.java
 */
export class Controller {
    constructor(gridSize = 6) {
        this.gridSize = gridSize;
        this.intersecciones = new Map(); // Map<string, Interseccion>
        this.aristas = []; // Array de todas las aristas
        this.trafficController = null;
        this.historial = []; // Para undo/redo
        this.historialIndex = -1;
    }

    /**
     * Inicializa el sistema completo
     */
    inicializar() {
        this.intersecciones.clear();
        this.aristas = [];

        // Crear todas las intersecciones
        for (let row = 1; row <= this.gridSize; row++) {
            for (let col = 1; col <= this.gridSize; col++) {
                const interseccion = new Interseccion(row, col, this.gridSize);
                this.intersecciones.set(interseccion.getId(), interseccion);
            }
        }

        // Crear aristas entre intersecciones
        for (const [id, interseccion] of this.intersecciones) {
            const vecinos = interseccion.calcularVecinos();

            // Vecino vertical (calle)
            if (vecinos.vertical.valid) {
                const to = this.getInterseccion(vecinos.vertical.row, vecinos.vertical.col);
                if (to) {
                    const arista = new Arista(interseccion, to, Arista.TIPO_CALLE);
                    interseccion.agregarVecino(arista);
                    this.aristas.push(arista);
                }
            }

            // Vecino horizontal (avenida)
            if (vecinos.horizontal.valid) {
                const to = this.getInterseccion(vecinos.horizontal.row, vecinos.horizontal.col);
                if (to) {
                    const arista = new Arista(interseccion, to, Arista.TIPO_AVENIDA);
                    interseccion.agregarVecino(arista);
                    this.aristas.push(arista);
                }
            }
        }

        // Inicializar el controlador de tráfico
        this.trafficController = new TrafficController(this.intersecciones);

        // Guardar estado inicial
        this.guardarEstado();

        return {
            totalIntersecciones: this.intersecciones.size,
            totalAristas: this.aristas.length,
            mensaje: 'Sistema inicializado correctamente'
        };
    }

    /**
     * Obtiene una intersección por coordenadas
     */
    getInterseccion(row, col) {
        return this.intersecciones.get(`${row},${col}`);
    }

    /**
     * Establece tráfico aleatorio en todas las aristas
     */
    generarTraficoAleatorio() {
        for (const arista of this.aristas) {
            const trafico = Math.floor(Math.random() * Arista.MAX_VEH_PER_MIN);
            arista.setVehiculosPorMin(trafico);
        }
        this.guardarEstado();
    }

    /**
     * Establece un nivel de tráfico específico en todas las aristas
     */
    establecerTraficoGlobal(nivel) {
        const vehiculos = Math.floor((nivel / 100) * Arista.MAX_VEH_PER_MIN);
        for (const arista of this.aristas) {
            arista.setVehiculosPorMin(vehiculos);
        }
        this.guardarEstado();
    }

    /**
     * Limpia todo el tráfico
     */
    limpiarTrafico() {
        for (const arista of this.aristas) {
            arista.setVehiculosPorMin(0);
        }
        this.guardarEstado();
    }

    /**
     * Carga datos de tráfico desde un objeto JSON
     */
    cargarTraficoDesdeJSON(jsonData) {
        try {
            // Validar estructura del JSON
            if (!jsonData.trafico || !Array.isArray(jsonData.trafico)) {
                throw new Error('El JSON debe contener un array "trafico"');
            }

            // Limpiar tráfico actual
            this.limpiarTrafico();

            // Aplicar datos del JSON
            for (const dato of jsonData.trafico) {
                const { from, to, vehiculosPorMin, weightMultiplier } = dato;
                
                if (!from || !to) {
                    console.warn('Entrada inválida en JSON:', dato);
                    continue;
                }

                // Buscar la arista correspondiente
                const arista = this.aristas.find(a => 
                    a.from.getId() === from && a.to.getId() === to
                );

                if (arista) {
                    if (vehiculosPorMin !== undefined) {
                        arista.setVehiculosPorMin(vehiculosPorMin);
                    }
                    if (weightMultiplier !== undefined) {
                        arista.setWeightMultiplier(weightMultiplier);
                    }
                } else {
                    console.warn(`No se encontró arista: ${from} -> ${to}`);
                }
            }

            this.guardarEstado();

            return {
                exito: true,
                mensaje: `Tráfico cargado: ${jsonData.trafico.length} aristas actualizadas`,
                aristasActualizadas: jsonData.trafico.length
            };

        } catch (error) {
            return {
                exito: false,
                mensaje: `Error al cargar JSON: ${error.message}`,
                aristasActualizadas: 0
            };
        }
    }

    /**
     * Exporta el estado actual del tráfico a JSON
     */
    exportarTraficoJSON() {
        const trafico = this.aristas
            .filter(arista => arista.vehiculosPorMin > 0 || arista.weightMultiplier !== 1.0)
            .map(arista => arista.toJSON());

        return {
            version: '1.0',
            gridSize: this.gridSize,
            timestamp: new Date().toISOString(),
            trafico
        };
    }

    /**
     * Calcula la ruta óptima entre dos puntos
     */
    calcularRuta(origenRow, origenCol, destinoRow, destinoCol, algoritmo = 'dijkstra') {
        const origen = this.getInterseccion(origenRow, origenCol);
        const destino = this.getInterseccion(destinoRow, destinoCol);

        if (!origen || !destino) {
            return {
                encontrada: false,
                mensaje: 'Intersecciones inválidas'
            };
        }

        if (algoritmo === 'astar') {
            return this.trafficController.aStar(origen, destino);
        } else if (algoritmo === 'comparar') {
            return this.trafficController.compararAlgoritmos(origen, destino);
        } else {
            return this.trafficController.dijkstra(origen, destino);
        }
    }

    /**
     * Obtiene estadísticas del sistema
     */
    obtenerEstadisticas() {
        let totalTrafico = 0;
        let aristasCongestionadas = 0;
        let pesoTotal = 0;

        for (const arista of this.aristas) {
            totalTrafico += arista.vehiculosPorMin;
            pesoTotal += arista.weight;
            if (arista.weight > 0.7) {
                aristasCongestionadas++;
            }
        }

        const pesoPromedio = this.aristas.length > 0 ? pesoTotal / this.aristas.length : 0;

        return {
            totalIntersecciones: this.intersecciones.size,
            totalAristas: this.aristas.length,
            traficoTotal: totalTrafico,
            traficoPromedio: Math.round(totalTrafico / this.aristas.length),
            aristasCongestionadas,
            pesoPromedio: pesoPromedio.toFixed(3),
            congestionGeneral: Math.round(pesoPromedio * 100) + '%'
        };
    }

    /**
     * Guarda el estado actual para undo/redo
     */
    guardarEstado() {
        const estado = {
            aristas: this.aristas.map(a => ({
                id: a.getId(),
                vehiculosPorMin: a.vehiculosPorMin,
                weightMultiplier: a.weightMultiplier
            }))
        };

        // Eliminar estados futuros si estamos en medio del historial
        if (this.historialIndex < this.historial.length - 1) {
            this.historial = this.historial.slice(0, this.historialIndex + 1);
        }

        this.historial.push(estado);
        this.historialIndex++;

        // Limitar tamaño del historial
        if (this.historial.length > 50) {
            this.historial.shift();
            this.historialIndex--;
        }
    }

    /**
     * Deshace la última acción
     */
    undo() {
        if (this.historialIndex > 0) {
            this.historialIndex--;
            this.restaurarEstado(this.historial[this.historialIndex]);
            return { exito: true, mensaje: 'Acción deshecha' };
        }
        return { exito: false, mensaje: 'No hay más acciones para deshacer' };
    }

    /**
     * Rehace la última acción deshecha
     */
    redo() {
        if (this.historialIndex < this.historial.length - 1) {
            this.historialIndex++;
            this.restaurarEstado(this.historial[this.historialIndex]);
            return { exito: true, mensaje: 'Acción rehecha' };
        }
        return { exito: false, mensaje: 'No hay más acciones para rehacer' };
    }

    /**
     * Restaura un estado del historial
     */
    restaurarEstado(estado) {
        for (const aristaData of estado.aristas) {
            const arista = this.aristas.find(a => a.getId() === aristaData.id);
            if (arista) {
                arista.vehiculosPorMin = aristaData.vehiculosPorMin;
                arista.weightMultiplier = aristaData.weightMultiplier;
                arista.calcularPeso();
            }
        }
    }
}