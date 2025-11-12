// VecinoException.js
class VecinoException extends Error {
    constructor(message) {
        super(message);
        this.name = 'VecinoException';
    }
}
// El comentario sobre si se usa o no se mantiene para referencia.