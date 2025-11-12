// Calle.js

// NOTA: Se asume que la clase 'Arista' e 'Interseccion' existen y están importadas/disponibles
// Por ejemplo: import Arista from './Arista';
// Por ejemplo: import Interseccion from './Interseccion';

class Calle extends Arista {
    /**
     * Constructor para una Calle.
     * @param {Interseccion} f Intersección de origen
     * @param {Interseccion} t Intersección de destino
     */
    constructor(f, t) {
        // Llama al constructor de la clase base Arista
        super(f, t);
        // Asume que Arista tiene un método estático o de instancia getMultCalle()
        this.setWeightMultiplier(Arista.getMultCalle());
    }

    /**
     * Constructor con vehículos por minuto.
     * @param {Interseccion} f Intersección de origen
     * @param {Interseccion} t Intersección de destino
     * @param {number} vpm Vehículos por minuto
     */
    static createWithVPM(f, t, vpm) {
        const instance = new Calle(f, t);
        instance.setVehiculosPorMin(vpm);
        return instance;
    }
}

// export default Calle; // Si estás usando módulos ES6