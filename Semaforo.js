// Semaforo.js
class Semaforo {
    constructor() {
        this.cycleSeconds = 60;
        this.yellow = 3;
        this.allRed = 1;
        this.minGreen = 10;
        this.maxGreen = 40;

        this._greenNS = 0; // Uso de _ para indicar que es 'privada'
        this._greenEW = 0;
        this._loadNS = 0.0;
        this._loadEW = 0.0;
        this._lastUpdated = null;
    }

    // Métodos Getters
    getCycleSeconds() { return this.cycleSeconds; }
    getYellow() { return this.yellow; }
    getAllRed() { return this.allRed; }
    getMinGreen() { return this.minGreen; }
    getMaxGreen() { return this.maxGreen; }
    getGreenNS() { return this._greenNS; }
    getGreenEW() { return this._greenEW; }
    getLoadNS() { return this._loadNS; }
    getLoadEW() { return this._loadEW; }
    getLastUpdated() { return this._lastUpdated; }

    // Métodos Setters
    setCycleSeconds(v) { this.cycleSeconds = Math.max(20, v); }
    setYellow(v) { this.yellow = Math.max(2, v); }
    setAllRed(v) { this.allRed = Math.max(0, v); }
    setMinGreen(v) { this.minGreen = Math.max(5, v); }
    setMaxGreen(v) { this.maxGreen = Math.max(this.minGreen, v); }

    /**
     * Limita un valor al rango [0, 1].
     * @param {number} v
     * @returns {number}
     */
    static _clamp01(v) { return Math.max(0.0, Math.min(1.0, v)); }

    /**
     * Limita un valor al rango [lo, hi].
     * @param {number} v
     * @param {number} lo
     * @param {number} hi
     * @returns {number}
     */
    static _clamp(v, lo, hi) { return Math.max(lo, Math.min(hi, v)); }

    /**
     * Actualiza los tiempos de luz verde basándose en la carga de tráfico.
     * @param {number} cargaNS Carga de tráfico Norte-Sur (0 a 1)
     * @param {number} cargaEW Carga de tráfico Este-Oeste (0 a 1)
     */
    actualizarConCargas(cargaNS, cargaEW) {
        this._loadNS = Semaforo._clamp01(cargaNS);
        this._loadEW = Semaforo._clamp01(cargaEW);

        let budget = this.cycleSeconds - (2 * this.yellow) - this.allRed;

        // Ajuste del ciclo si el presupuesto es insuficiente para el mínimo verde
        if (budget < 2 * this.minGreen) {
            const needed = 2 * this.minGreen + this.allRed + 2 * this.yellow;
            this.cycleSeconds = Math.max(this.cycleSeconds, needed);
            budget = this.cycleSeconds - (2 * this.yellow) - this.allRed;
        }

        // Cálculo de tiempos basado en la proporción de carga
        const dNS = 0.001 + this._loadNS;
        const dEW = 0.001 + this._loadEW;
        const sum = dNS + dEW;

        let gNS = Math.round(budget * (dNS / sum));
        let gEW = budget - gNS;

        // Aplicar límites MinGreen y MaxGreen
        gNS = Semaforo._clamp(gNS, this.minGreen, this.maxGreen);
        gEW = Semaforo._clamp(gEW, this.minGreen, this.maxGreen);

        // Redistribución del tiempo restante (delta)
        const used = gNS + gEW;
        let delta = budget - used;

        if (delta !== 0) {
            if (delta > 0) {
                // Hay tiempo extra: distribuirlo al carril con más carga si no ha alcanzado el máximo
                if (this._loadNS >= this._loadEW && gNS < this.maxGreen) {
                    gNS += Math.min(delta, this.maxGreen - gNS);
                } else if (gEW < this.maxGreen) {
                    gEW += Math.min(delta, this.maxGreen - gEW);
                }
            } else {
                // Hay exceso de tiempo asignado: restarlo al carril con menos carga si está por encima del mínimo
                const take = -delta;
                if (this._loadNS <= this._loadEW && gNS > this.minGreen) {
                    gNS -= Math.min(take, gNS - this.minGreen);
                } else if (gEW > this.minGreen) {
                    gEW -= Math.min(take, gEW - this.minGreen);
                }
            }
        }

        this._greenNS = gNS;
        this._greenEW = gEW;
        this._lastUpdated = Date.now(); // En JS, se usa Date.now() o new Date()
    }
}