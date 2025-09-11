import java.util.List;

/**
 * Interfaz simple para algoritmos de control que puedan aplicarse
 * a la lista de intersecciones del tráfico.
 */
public interface Algoritmo {
    /**
     * Aplica el algoritmo sobre la lista de intersecciones.
     * Implementación mínima: puede ajustar duraciones de semáforos.
     */
    void aplicar(List<Interseccion> listaIntersecciones);
}
