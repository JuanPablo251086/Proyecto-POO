// GraphException.js
// Excepción personalizada para problemas del grafo / rutas
class GraphException extends Error {
    constructor(message) {
        super(message);
        this.name = 'GraphException';
    }
}