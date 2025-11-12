/**
 * Clase Interseccion - Representa una intersección en el sistema de tráfico
 * Equivalente a Interseccion.java
 */
export class Interseccion {
    constructor(row, col, gridSize = 6) {
        this.row = row;
        this.col = col;
        this.gridSize = gridSize;
        this.weight = 0;
        this.vecinos = []; // Array de Arista
    }

    /**
     * Calcula los vecinos de esta intersección según el patrón hexagonal
     * Replica la lógica de CalcularVecinos() en Java
     */
    calcularVecinos() {
        this.vecinos = [];

        // Vecino vertical (calle)
        const vrow = (this.col % 2 === 1) ? this.row - 1 : this.row + 1;
        const vcol = this.col;

        // Vecino horizontal (avenida)
        const hrow = this.row;
        const hcol = (this.row % 2 === 1) ? this.col + 1 : this.col - 1;

        return {
            vertical: { row: vrow, col: vcol, valid: this.isValidPosition(vrow, vcol) },
            horizontal: { row: hrow, col: hcol, valid: this.isValidPosition(hrow, hcol) }
        };
    }

    /**
     * Verifica si una posición es válida en la grilla
     */
    isValidPosition(row, col) {
        return row >= 1 && row <= this.gridSize && col >= 1 && col <= this.gridSize;
    }

    /**
     * Agrega un vecino (arista) a esta intersección
     */
    agregarVecino(arista) {
        this.vecinos.push(arista);
    }

    /**
     * Obtiene el identificador único de la intersección
     */
    getId() {
        return `${this.row},${this.col}`;
    }

    /**
     * Representación en string de la intersección
     */
    toString() {
        return `Interseccion(${this.row}, ${this.col})`;
    }

    /**
     * Compara si dos intersecciones son iguales
     */
    equals(other) {
        if (!other) return false;
        return this.row === other.row && this.col === other.col;
    }

    /**
     * Obtiene el peso total considerando todas las aristas vecinas
     */
    calcularPesoTotal() {
        if (this.vecinos.length === 0) return 0;
        
        const sumaWeights = this.vecinos.reduce((sum, arista) => sum + arista.weight, 0);
        return sumaWeights / this.vecinos.length;
    }
}