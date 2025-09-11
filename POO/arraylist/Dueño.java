import java.util.ArrayList;

public class Dueño {
    private String nombre;
    private String telefono;
    private String email;
    private ArrayList<Mascota> mascotas;

    
    public Dueño(String nombre, String telefono, String email) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.mascotas = new ArrayList<>();
    }

    // Getter
    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<Mascota> getMascotas() {
        return mascotas;
    }

    // Método
    public void agregarMascota(Mascota m) {
        mascotas.add(m);
    }
}
