import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Vista {
    private final Controller controller;

    public Vista(Controller controller) {
        this.controller = controller;
    }

    public void exportCSV(String filePath) {
        List<Arista> aristas = controller.getAllAristas();
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Tipo,Origen,Destino,VehiculosPorMin,Weight,WeightMultiplier\n");
            for (Arista a : aristas) {
                String line = String.format(Locale.US,
                        "%s,\"%s\",\"%s\",%d,%.3f,%.2f\n",
                        a.getClass().getSimpleName(),
                        a.getOrigen().getId(),
                        a.getDestino().getId(),
                        a.getVehiculosPorMin(),
                        a.getWeight(),
                        a.getWeightMultiplier());
                writer.write(line);
            }
        } catch (IOException ignored) {
        }
    }

    public void exportJSON(String filePath) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        sb.append("\"config\":{");
        sb.append("\"maxVehPerMin\":").append(controller.getMaxVehPerMin()).append(',');
        sb.append("\"multCalle\":").append(String.format(Locale.US, "%.2f", controller.getMultCalle())).append(',');
        sb.append("\"multAvenida\":").append(String.format(Locale.US, "%.2f", controller.getMultAvenida()));
        sb.append("},");

        sb.append("\"aristas\":[");
        List<Arista> aristas = controller.getAllAristas();
        for (int i = 0; i < aristas.size(); i++) {
            Arista a = aristas.get(i);
            sb.append("{");
            sb.append("\"tipo\":\"").append(jsonString(a.getClass().getSimpleName())).append("\",");
            sb.append("\"origen\":\"").append(jsonString(a.getOrigen().getId())).append("\",");
            sb.append("\"destino\":\"").append(jsonString(a.getDestino().getId())).append("\",");
            sb.append("\"vehiculosPorMin\":").append(a.getVehiculosPorMin()).append(',');
            sb.append("\"weight\":").append(String.format(Locale.US, "%.3f", a.getWeight())).append(',');
            sb.append("\"weightMultiplier\":").append(String.format(Locale.US, "%.2f", a.getWeightMultiplier()));
            sb.append("}");
            if (i < aristas.size() - 1) sb.append(',');
        }
        sb.append("]");

        sb.append(",\"semaforos\":[");
        List<Interseccion> nodos = controller.getIntersecciones();
        for (int i = 0; i < nodos.size(); i++) {
            Interseccion n = nodos.get(i);
            Semaforo s = n.getSemaforo();
            sb.append("{");
            sb.append("\"id\":\"").append(jsonString(n.getId())).append("\",");
            sb.append("\"fila\":").append(n.getRow()).append(',');
            sb.append("\"columna\":").append(n.getCol()).append(',');
            sb.append("\"cycleSeconds\":").append(s.getCycleSeconds()).append(',');
            sb.append("\"yellow\":").append(s.getYellow()).append(',');
            sb.append("\"allRed\":").append(s.getAllRed()).append(',');
            sb.append("\"minGreen\":").append(s.getMinGreen()).append(',');
            sb.append("\"maxGreen\":").append(s.getMaxGreen()).append(',');
            sb.append("\"greenNS\":").append(s.getGreenNS()).append(',');
            sb.append("\"greenEW\":").append(s.getGreenEW()).append(',');
            sb.append("\"loadNS\":").append(String.format(Locale.US, "%.3f", s.getLoadNS())).append(',');
            sb.append("\"loadEW\":").append(String.format(Locale.US, "%.3f", s.getLoadEW()));
            sb.append("}");
            if (i < nodos.size() - 1) sb.append(',');
        }
        sb.append("]");

        sb.append("}");

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(sb.toString());
        } catch (IOException ignored) {
        }
    }

    public void exportJSONConRuta(String filePath, int filaOrigen, int colOrigen,
                                  int filaDestino, int colDestino) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        sb.append("\"config\":{");
        sb.append("\"maxVehPerMin\":").append(controller.getMaxVehPerMin()).append(',');
        sb.append("\"multCalle\":").append(String.format(Locale.US, "%.2f", controller.getMultCalle())).append(',');
        sb.append("\"multAvenida\":").append(String.format(Locale.US, "%.2f", controller.getMultAvenida()));
        sb.append("},");

        sb.append("\"aristas\":[");
        List<Arista> aristas = controller.getAllAristas();
        for (int i = 0; i < aristas.size(); i++) {
            Arista a = aristas.get(i);
            sb.append("{");
            sb.append("\"tipo\":\"").append(jsonString(a.getClass().getSimpleName())).append("\",");
            sb.append("\"origen\":\"").append(jsonString(a.getOrigen().getId())).append("\",");
            sb.append("\"destino\":\"").append(jsonString(a.getDestino().getId())).append("\",");
            sb.append("\"vehiculosPorMin\":").append(a.getVehiculosPorMin()).append(',');
            sb.append("\"weight\":").append(String.format(Locale.US, "%.3f", a.getWeight())).append(',');
            sb.append("\"weightMultiplier\":").append(String.format(Locale.US, "%.2f", a.getWeightMultiplier()));
            sb.append("}");
            if (i < aristas.size() - 1) sb.append(',');
        }
    sb.append("]");

        sb.append(",\"rutaOptima\":[");
        List<Interseccion> ruta = null;
        try {
            ruta = controller.calcularRuta(filaOrigen, colOrigen, filaDestino, colDestino);
        } catch (GraphException ignored) {
        }
        if (ruta != null) {
            for (int i = 0; i < ruta.size(); i++) {
                Interseccion node = ruta.get(i);
                sb.append("{");
                sb.append("\"id\":\"").append(jsonString(node.getId())).append("\",");
                sb.append("\"fila\":").append(node.getRow()).append(',');
                sb.append("\"columna\":").append(node.getCol());
                sb.append("}");
                if (i < ruta.size() - 1) sb.append(',');
            }
        }
        sb.append("]");

        sb.append(",\"semaforos\":[");
        List<Interseccion> nodos = controller.getIntersecciones();
        for (int i = 0; i < nodos.size(); i++) {
            Interseccion n = nodos.get(i);
            Semaforo s = n.getSemaforo();
            sb.append("{");
            sb.append("\"id\":\"").append(jsonString(n.getId())).append("\",");
            sb.append("\"fila\":").append(n.getRow()).append(',');
            sb.append("\"columna\":").append(n.getCol()).append(',');
            sb.append("\"cycleSeconds\":").append(s.getCycleSeconds()).append(',');
            sb.append("\"yellow\":").append(s.getYellow()).append(',');
            sb.append("\"allRed\":").append(s.getAllRed()).append(',');
            sb.append("\"minGreen\":").append(s.getMinGreen()).append(',');
            sb.append("\"maxGreen\":").append(s.getMaxGreen()).append(',');
            sb.append("\"greenNS\":").append(s.getGreenNS()).append(',');
            sb.append("\"greenEW\":").append(s.getGreenEW()).append(',');
            sb.append("\"loadNS\":").append(String.format(Locale.US, "%.3f", s.getLoadNS())).append(',');
            sb.append("\"loadEW\":").append(String.format(Locale.US, "%.3f", s.getLoadEW()));
            sb.append("}");
            if (i < nodos.size() - 1) sb.append(',');
        }
        sb.append("]");

    sb.append("]");

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(sb.toString());
        } catch (IOException ignored) {
        }
    }

    public void exportConfigJSON(String filePath) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"maxVehPerMin\":").append(controller.getMaxVehPerMin()).append(',');
        sb.append("\"multCalle\":").append(String.format(Locale.US, "%.2f", controller.getMultCalle())).append(',');
        sb.append("\"multAvenida\":").append(String.format(Locale.US, "%.2f", controller.getMultAvenida()));
        sb.append("}");

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(sb.toString());
        } catch (IOException ignored) {
        }
    }

    private static String jsonString(String s) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '"' || ch == '\\') {
                out.append('\\').append(ch);
            } else {
                out.append(ch);
            }
        }
        return out.toString();
    }
}
