import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Controlador controlador = new Controlador();

        int opcion = -1; // variable de control
        while (opcion != 0) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Crear proyecto");
            System.out.println("2. Crear desarrollador");
            System.out.println("3. Crear tarea y ponersela a un proyecto");
            System.out.println("4. Asignar tarea a un desarrollador");
            System.out.println("5. Ver proyectos y tareas");
            System.out.println("6. Ver desarrolladores");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");
            opcion = sc.nextInt();
            sc.nextLine(); 

            if (opcion == 1) {
                System.out.print("Nombre del proyecto: ");
                String nombre = sc.nextLine();
                controlador.agregarProyecto(nombre);
            } else if (opcion == 2) {
                System.out.print("Nombre del desarrollador: ");
                String nombre = sc.nextLine();
                controlador.agregarDesarrollador(nombre);
            } else if (opcion == 3) {
                if (controlador.getProyectos().isEmpty()) {
                    System.out.println("tenes que crear un proyecto.");
                } else {
                    System.out.print("Nombre de la tarea: ");
                    String tnombre = sc.nextLine();
                    System.out.print("Horas estimadas: ");
                    int horas = sc.nextInt();
                    System.out.print("Prioridad (1=alta, 2=media, 3=baja): ");
                    int prioridad = sc.nextInt();
                    sc.nextLine();

                    System.out.println("escoge proyecto:");
                    for (int i = 0; i < controlador.getProyectos().size(); i++) {
                        System.out.println(i + ". " + controlador.getProyectos().get(i).getNombre());
                    }
                    int pIndex = sc.nextInt();
                    sc.nextLine();

                    Proyecto p = controlador.getProyecto(pIndex);
                    if (p != null) {
                        p.agregarTarea(new Tarea(tnombre, horas, prioridad));
                    }
                }
            } else if (opcion == 4) {
                if (controlador.getProyectos().isEmpty() || controlador.getDesarrolladores().isEmpty()) {
                    System.out.println("tiene que haber al menos un proyecto y un desarrollador.");
                } else {
                    System.out.println("escoge proyecto:");
                    for (int i = 0; i < controlador.getProyectos().size(); i++) {
                        System.out.println(i + ". " + controlador.getProyectos().get(i).getNombre());
                    }
                    int pIndex = sc.nextInt();

                    Proyecto p = controlador.getProyecto(pIndex);
                    if (p != null && !p.getTareas().isEmpty()) {
                        System.out.println("Seleccione tarea:");
                        for (int i = 0; i < p.getTareas().size(); i++) {
                            System.out.println(i + ". " + p.getTareas().get(i));
                        }
                        int tIndex = sc.nextInt();

                        System.out.println("Seleccione desarrollador:");
                        for (int i = 0; i < controlador.getDesarrolladores().size(); i++) {
                            System.out.println(i + ". " + controlador.getDesarrolladores().get(i));
                        }
                        int dIndex = sc.nextInt();
                        sc.nextLine();

                        Tarea t = p.getTareas().get(tIndex);
                        Desarrollador d = controlador.getDesarrollador(dIndex);

                        if (d.asignarTarea(t)) {
                            System.out.println("Tarea asignada correctamente.");
                        } else {
                            System.out.println("El desarrollador no puede pasarse de las 40 horas.");
                        }
                    }
                }
            } else if (opcion == 5) {
                for (Proyecto p : controlador.getProyectos()) {
                    System.out.println(p);
                }
            } else if (opcion == 6) {
                for (Desarrollador d : controlador.getDesarrolladores()) {
                    System.out.println(d);
                    for (Tarea t : d.getTareas()) {
                        System.out.println("  - " + t);
                    }
                }
            }
        }
        System.out.println("Saliendo del sistema...");
        sc.close();
    }
}
