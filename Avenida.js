// Avenida.js

// NOTA: Se asume que la clase 'Arista' e 'Interseccion' existen y están importadas/disponibles
// Por ejemplo: import Arista from './Arista';
// Por ejemplo: import Interseccion from './Interseccion';

class Avenida extends Arista {
    /**
     * Constructor para una Avenida.
     * @param {Interseccion} f Intersección de origen
     * @param {Interseccion} t Intersección de destino
     */
    constructor(f, t) {
        // Llama al constructor de la clase base Arista
        super(f, t);
        // Asume que Arista tiene un método estático o de instancia getMultAvenida()
        this.setWeightMultiplier(Arista.getMultAvenida());
    }

    /**
     * Constructor con vehículos por minuto.
     * @param {Interseccion} f Intersección de origen
     * @param {Interseccion} t Intersección de destino
     * @param {number} vpm Vehículos por minuto
     */
    static createWithVPM(f, t, vpm) {
        const instance = new Avenida(f, t);
        instance.setVehiculosPorMin(vpm);
        return instance;
    }
}

// Nota: En JS es común usar el segundo constructor como un método estático de fábrica si hay ambigüedad
// o directamente sobrecargar (lo cual es menos común que en Java), o simplificarlo
// usando valores por defecto, pero mantengo la lógica de los constructores originales.

// export default Avenida; // Si estás usando módulos ES6