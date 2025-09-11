import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Veterinaria vet = new Veterinaria();

        int opcion;
        do {
            System.out.println("\n=== SISTEMA VETERINARIO ===");
            System.out.println("1. Registrar Dueño");
            System.out.println("2. Registrar Mascota");
            System.out.println("3. Registrar Consulta");
            System.out.println("4. Reporte: Especie más atendida");
            System.out.println("5. Reporte: Mascota más consultada");
            System.out.println("6. Reporte: Promedio consultas durante el mes");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Nombre del dueño: ");
                    String nombre = sc.nextLine();
                    System.out.print("número de teléfono: ");
                    String tel = sc.nextLine();
                    System.out.print("Correo: ");
                    String email = sc.nextLine();
                    vet.registrarDueño(new Dueño(nombre, tel, email));
                    System.out.println("Dueño registrado con éxito.");
                    break;

                case 2:
                    if (vet.getDueños().isEmpty()) {
                        System.out.println("Primero registre un dueño.");
                        break;
                    }
                    System.out.print("Nombre de la mascota: ");
                    String nMascota = sc.nextLine();
                    System.out.print("Especie: ");
                    String especie = sc.nextLine();
                    System.out.print("Raza: ");
                    String raza = sc.nextLine();
                    System.out.print("Edad: ");
                    int edad = sc.nextInt(); sc.nextLine();

                    System.out.println("Seleccione dueño:");
                    for (int i = 0; i < vet.getDueños().size(); i++) {
                        System.out.println((i + 1) + ". " + vet.getDueños().get(i).getNombre());
                    }
                    int idxDueño = sc.nextInt(); sc.nextLine();
                    Dueño d = vet.getDueños().get(idxDueño - 1);

                    Mascota m = new Mascota(nMascota, especie, raza, edad, d);
                    vet.registrarMascota(m);
                    System.out.println("Mascota registrada con éxito.");
                    break;

                case 3:
                    if (vet.getMascotas().isEmpty()) {
                        System.out.println("Primero registre una mascota.");
                        break;
                    }
                    System.out.println("Seleccione mascota:");
                    for (int i = 0; i < vet.getMascotas().size(); i++) {
                        System.out.println((i + 1) + ". " + vet.getMascotas().get(i).getNombre());
                    }
                    int idxMascota = sc.nextInt(); sc.nextLine();
                    Mascota mascotaSel = vet.getMascotas().get(idxMascota - 1);

                    System.out.print("Fecha: ");
                    String fecha = sc.nextLine();
                    System.out.print("Motivo: ");
                    String motivo = sc.nextLine();
                    System.out.print("Tratamiento: ");
                    String trat = sc.nextLine();

                    Consulta c = new Consulta(fecha, motivo, trat, mascotaSel);
                    vet.registrarConsulta(c);
                    System.out.println("Consulta registrada con éxito.");
                    break;

                case 4:
                    System.out.println("Especie más atendida: " + vet.getEspecieMasAtendida());
                    break;

                case 5:
                    Mascota mas = vet.getMascotaMasConsultada();
                    if (mas != null) {
                        System.out.println("Mascota más consultada: " + mas.getNombre());
                    } else {
                        System.out.println("No hay consultas registradas.");
                    }
                    break;

                case 6:
                    System.out.println("Promedio consulta: " + vet.getPromedioConsultasPorMes());
                    break;

                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);

        sc.close();
    }
}
