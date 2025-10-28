public class Arista {
    // Configuración global ajustable
    private static int MAX_VEH_PER_MIN = 100;
    private static float MULT_CALLE = 1.20f;
    private static float MULT_AVENIDA = 0.80f;

    public static int getMaxVehPerMin() { return MAX_VEH_PER_MIN; }
    public static void setMaxVehPerMin(int v) { MAX_VEH_PER_MIN = Math.max(1, v); }

    public static float getMultCalle() { return MULT_CALLE; }
    public static void setMultCalle(float m) { MULT_CALLE = Math.max(0f, m); }

    public static float getMultAvenida() { return MULT_AVENIDA; }
    public static void setMultAvenida(float m) { MULT_AVENIDA = Math.max(0f, m); }

    // Estado de instancia
    protected Interseccion from;
    protected Interseccion to;
    protected float weight; // 0..1
    protected int vehiculosPorMin;
    protected float weightMultiplier = 1.0f;

    public Arista(Interseccion f, Interseccion t) {
        this.from = f;
        this.to = t;
        this.weight = 0f;
        this.vehiculosPorMin = 0;
    }

    // Compatibilidad: delega al nuevo cálculo
    public void calcularPeso() { recalcPeso(); }

    public void setVehiculosPorMin(int vpm) {
        if (vpm < 0) vpm = 0;
        this.vehiculosPorMin = vpm;
        recalcPeso();
    }

    public int getVehiculosPorMin() { return this.vehiculosPorMin; }
    public float getWeight() { return this.weight; }
    public Interseccion getFrom() { return this.from; }
    public Interseccion getTo() { return this.to; }
    public float getWeightMultiplier() { return this.weightMultiplier; }

    protected void setWeightMultiplier(float m) {
        this.weightMultiplier = Math.max(0f, m);
        recalcPeso();
    }

    public Interseccion getOrigen() { return this.from; }
    public Interseccion getDestino() { return this.to; }

    protected void recalcPeso() {
        float base = (float) this.vehiculosPorMin / (float) MAX_VEH_PER_MIN;
        float raw = base * this.weightMultiplier;
        if (raw < 0f) raw = 0f;
        if (raw > 1f) raw = 1f;
        this.weight = raw;
    }

    public void onConfigChanged() { recalcPeso(); }

    @Override
    public String toString() {
        return "Arista[" + from + " -> " + to + ", veh/min=" + vehiculosPorMin + ", peso=" + String.format("%.3f", weight) + "]";
    }
}
