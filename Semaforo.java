import java.time.Instant;

public class Semaforo {
    private int cycleSeconds = 60;
    private int yellow = 3;
    private int allRed = 1;
    private int minGreen = 10;
    private int maxGreen = 40;

    private int greenNS;
    private int greenEW;
    private float loadNS;
    private float loadEW;
    private Instant lastUpdated;

    public int getCycleSeconds() { return cycleSeconds; }
    public void setCycleSeconds(int v) { cycleSeconds = Math.max(20, v); }
    public int getYellow() { return yellow; }
    public void setYellow(int v) { yellow = Math.max(2, v); }
    public int getAllRed() { return allRed; }
    public void setAllRed(int v) { allRed = Math.max(0, v); }
    public int getMinGreen() { return minGreen; }
    public void setMinGreen(int v) { minGreen = Math.max(5, v); }
    public int getMaxGreen() { return maxGreen; }
    public void setMaxGreen(int v) { maxGreen = Math.max(minGreen, v); }

    public int getGreenNS() { return greenNS; }
    public int getGreenEW() { return greenEW; }
    public float getLoadNS() { return loadNS; }
    public float getLoadEW() { return loadEW; }
    public Instant getLastUpdated() { return lastUpdated; }

    public void actualizarConCargas(float cargaNS, float cargaEW) {
        this.loadNS = clamp01(cargaNS);
        this.loadEW = clamp01(cargaEW);

        int budget = cycleSeconds - (2 * yellow) - allRed;
        if (budget < 2 * minGreen) {
            int needed = 2 * minGreen + allRed + 2 * yellow;
            cycleSeconds = Math.max(cycleSeconds, needed);
            budget = cycleSeconds - (2 * yellow) - allRed;
        }

        float dNS = 0.001f + loadNS;
        float dEW = 0.001f + loadEW;
        float sum = dNS + dEW;

        int gNS = Math.round(budget * (dNS / sum));
        int gEW = budget - gNS;

        gNS = clamp(gNS, minGreen, maxGreen);
        gEW = clamp(gEW, minGreen, maxGreen);

        int used = gNS + gEW;
        int delta = budget - used;
        if (delta != 0) {
            if (delta > 0) {
                if (loadNS >= loadEW && gNS < maxGreen) gNS += Math.min(delta, maxGreen - gNS);
                else if (gEW < maxGreen) gEW += Math.min(delta, maxGreen - gEW);
            } else {
                int take = -delta;
                if (loadNS <= loadEW && gNS > minGreen) gNS -= Math.min(take, gNS - minGreen);
                else if (gEW > minGreen) gEW -= Math.min(take, gEW - minGreen);
            }
        }

        this.greenNS = gNS;
        this.greenEW = gEW;
        this.lastUpdated = Instant.now();
    }

    private static float clamp01(float v) { return Math.max(0f, Math.min(1f, v)); }
    private static int clamp(int v, int lo, int hi) { return Math.max(lo, Math.min(hi, v)); }
}
