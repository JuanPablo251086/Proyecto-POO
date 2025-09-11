public class Metrica {
    private double tiempoPromedioEspera;
    private int vehiculosProcesados;
    private int embotellamientosReducidos;
    private double consumoEnergia;

    public Metrica() {
        this.tiempoPromedioEspera = 0.0;
        this.vehiculosProcesados = 0;
        this.embotellamientosReducidos = 0;
        this.consumoEnergia = 0.0;
    }

    /**
     * Método pensado para recalcular métricas finales si es necesario.
     * Implementation mínima: no hace cálculos adicionales.
     */
    public void actualizarMetricas() {
        // placeholder: si fuera necesario realizar cálculos finales, irían aquí.
    }

    public void mostrarResultados() {
        System.out.println("====== METRICAS ======");
        System.out.println("Tiempo promedio de espera: " + tiempoPromedioEspera + " s");
        System.out.println("Vehículos procesados: " + vehiculosProcesados);
        System.out.println("Embotellamientos reducidos: " + embotellamientosReducidos);
        System.out.println("Consumo de energía estimado: " + consumoEnergia);
        System.out.println("======================");
    }

    // Setters para que Trafico pueda poblar la métrica
    public void setTiempoPromedioEspera(double tiempoPromedioEspera) {
        this.tiempoPromedioEspera = tiempoPromedioEspera;
    }

    public void setVehiculosProcesados(int vehiculosProcesados) {
        this.vehiculosProcesados = vehiculosProcesados;
    }

    public void setEmbotellamientosReducidos(int embotellamientosReducidos) {
        this.embotellamientosReducidos = embotellamientosReducidos;
    }

    public void setConsumoEnergia(double consumoEnergia) {
        this.consumoEnergia = consumoEnergia;
    }

    public double getPromEspera() {
        return tiempoPromedioEspera;
    }

    public int getVehiculosProcesados() {
        return vehiculosProcesados;
    }

    public double getConsumoEnergia() {
        return consumoEnergia;
    }

    public int getEmbotellamientosReducidos() {
        return embotellamientosReducidos;
    }

    public String toString() {
        return "Metrica{" +
                "tiempoPromedioEspera=" + tiempoPromedioEspera +
                ", vehiculosProcesados=" + vehiculosProcesados +
                ", embotellamientosReducidos=" + embotellamientosReducidos +
                ", consumoEnergia=" + consumoEnergia +
                '}';
    }
}
