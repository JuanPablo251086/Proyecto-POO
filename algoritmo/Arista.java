
public class Arista {
    protected Interseccion from;
    protected Interseccion to;
    protected float weight; 
    protected int vehiculosPorMin; 

    public static final int MAX_VEH_PER_MIN = 100;

    protected float weightMultiplier = 1.0f; 

    public Arista(Interseccion f, Interseccion t) {
        this.from = f;
        this.to = t;
        this.weight = 0f;
        this.vehiculosPorMin = 0;
    }

    public void calcularPeso() {
        float raw = (float) this.vehiculosPorMin / (float) MAX_VEH_PER_MIN;
        raw *= this.weightMultiplier;
        if (raw < 0f) raw = 0f;
        if (raw > 1f) raw = 1f;
        this.weight = raw;
    }

    public void setVehiculosPorMin(int vpm) {
        if (vpm < 0) vpm = 0;
        this.vehiculosPorMin = vpm;
        calcularPeso();
    }

    public int getVehiculosPorMin() {
        return this.vehiculosPorMin;
    }

    public float getWeight() {
        return this.weight;
    }

    public Interseccion getFrom() {
        return this.from;
    }

    public Interseccion getTo() {
        return this.to;
    }

    public float getWeightMultiplier() {
        return this.weightMultiplier;
    }

    public void setWeightMultiplier(float m) {
        this.weightMultiplier = m;
        calcularPeso();
    }

    @Override
    public String toString() {
        return "Arista[" + from + " -> " + to + ", veh/min=" + vehiculosPorMin + ", peso=" + String.format("%.3f", weight) + "]";
    }
}
