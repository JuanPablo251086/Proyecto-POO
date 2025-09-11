import java.util.ArrayList;
import java.util.HashMap;

public class Veterinaria {
    private ArrayList<Dueño> dueños;
    private ArrayList<Mascota> mascotas;
    private ArrayList<Consulta> consultas;

    
    public Veterinaria() {
        this.dueños = new ArrayList<>();
        this.mascotas = new ArrayList<>();
        this.consultas = new ArrayList<>();
    }

    // Métodos de registro
    public void registrarDueño(Dueño d) {
        dueños.add(d);
    }

    public void registrarMascota(Mascota m) {
        mascotas.add(m);
        m.getDueño().agregarMascota(m);
    }

    public void registrarConsulta(Consulta c) {
        consultas.add(c);
        c.getMascota().agregarConsulta(c);
    }

    
    public String getEspecieMasAtendida() {
        HashMap<String, Integer> conteo = new HashMap<>();
        for (Consulta c : consultas) {
            String especie = c.getMascota().getEspecie();
            conteo.put(especie, conteo.getOrDefault(especie, 0) + 1);
        }
        String especieMas = null;
        int max = 0;
        for (String especie : conteo.keySet()) {
            if (conteo.get(especie) > max) {
                max = conteo.get(especie);
                especieMas = especie;
            }
        }
        return especieMas;
    }

    public Mascota getMascotaMasConsultada() {
        Mascota masConsultada = null;
        int max = 0;
        for (Mascota m : mascotas) {
            if (m.getConsultas().size() > max) {
                max = m.getConsultas().size();
                masConsultada = m;
            }
        }
        return masConsultada;
    }

    public double getPromedioConsultasPorMes() {
        if (consultas.isEmpty()) return 0;
        // las fechas deberian de ser año-mes
        HashMap<String, Integer> conteoPorMes = new HashMap<>();
        for (Consulta c : consultas) {
            String mes = c.getFecha().substring(0, 7); // Año-Mes
            conteoPorMes.put(mes, conteoPorMes.getOrDefault(mes, 0) + 1);
        }
        int totalConsultas = consultas.size();
        int totalMeses = conteoPorMes.size();
        return (double) totalConsultas / totalMeses;
    }

    // Getter
    public ArrayList<Dueño> getDueños() {
        return dueños;
    }

    public ArrayList<Mascota> getMascotas() {
        return mascotas;
    }

    public ArrayList<Consulta> getConsultas() {
        return consultas;
    }
}
