/**
 * Clase Arista - Representa una conexión entre dos intersecciones
 * Equivalente a Arista.java
 */
export class Arista {
    static MAX_VEH_PER_MIN = 100;
    static TIPO_CALLE = 'calle';
    static TIPO_AVENIDA = 'avenida';

    constructor(from, to, tipo) {
        this.from = from; // Interseccion
        this.to = to;     // Interseccion
        this.tipo = tipo; // 'calle' o 'avenida'
        this.vehiculosPorMin = 0;
        this.weight = 0;
        this.weightMultiplier = 1.0;
    }

    /**
     * Calcula el peso de la arista basado en el tráfico
     * Replica calcularPeso() en Java
     */
    calcularPeso() {
        let rawWeight = this.vehiculosPorMin / Arista.MAX_VEH_PER_MIN;
        rawWeight *= this.weightMultiplier;
        
        // Clampear entre 0 y 1
        this.weight = Math.max(0, Math.min(1, rawWeight));
        
        return this.weight;
    }

    /**
     * Establece el tráfico en vehículos por minuto
     */
    setVehiculosPorMin(vehiculos) {
        this.vehiculosPorMin = Math.max(0, Math.min(Arista.MAX_VEH_PER_MIN, vehiculos));
        this.calcularPeso();
    }

    /**
     * Establece el multiplicador de peso
     */
    setWeightMultiplier(multiplier) {
        this.weightMultiplier = Math.max(0, multiplier);
        this.calcularPeso();
    }

    /**
     * Obtiene el identificador único de la arista
     */
    getId() {
        return `${this.from.getId()}->${this.to.getId()}`;
    }

    /**
     * Obtiene el nivel de congestión como porcentaje
     */
    getNivelCongestion() {
        return Math.round(this.weight * 100);
    }

    /**
     * Obtiene el color basado en el nivel de congestión
     */
    getColor() {
        if (this.weight < 0.3) return '#4ade80'; // Verde (bajo)
        if (this.weight < 0.6) return '#facc15'; // Amarillo (medio)
        if (this.weight < 0.8) return '#fb923c'; // Naranja (alto)
        return '#ef4444'; // Rojo (crítico)
    }

    /**
     * Representación en string de la arista
     */
    toString() {
        return `Arista[${this.tipo}](${this.from.toString()} -> ${this.to.toString()}, peso: ${this.weight.toFixed(3)})`;
    }

    /**
     * Clona la arista
     */
    clone() {
        const cloned = new Arista(this.from, this.to, this.tipo);
        cloned.vehiculosPorMin = this.vehiculosPorMin;
        cloned.weightMultiplier = this.weightMultiplier;
        cloned.calcularPeso();
        return cloned;
    }

    /**
     * Serializa la arista a JSON
     */
    toJSON() {
        return {
            from: this.from.getId(),
            to: this.to.getId(),
            tipo: this.tipo,
            vehiculosPorMin: this.vehiculosPorMin,
            weightMultiplier: this.weightMultiplier,
            weight: this.weight
        };
    }
}