import java.util.ArrayList;

public class Mascota {
    private String nombre;
    private String especie;
    private String raza;
    private int edad;
    private Dueño dueño;
    private ArrayList<Consulta> consultas;

    
    public Mascota(String nombre, String especie, String raza, int edad, Dueño dueño) {
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.edad = edad;
        this.dueño = dueño;
        this.consultas = new ArrayList<>();
    }

    // Getter
    public String getNombre() {
        return nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public String getRaza() {
        return raza;
    }

    public int getEdad() {
        return edad;
    }

    public Dueño getDueño() {
        return dueño;
    }

    public ArrayList<Consulta> getConsultas() {
        return consultas;
    }

    // método
    public void agregarConsulta(Consulta c) {
        consultas.add(c);
    }
}
