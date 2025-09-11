public class NotasPlus {
    private Estudiante[] estudiantes;
    private Curso[] cursos;
    private int[][] calificaciones;

    public NotasPlus() {
        estudiantes = new Estudiante[5];
        cursos = new Curso[3];
        calificaciones = new int[5][3];

        estudiantes[0] = new Estudiante("Roberto");
        estudiantes[1] = new Estudiante("Rodrigo");
        estudiantes[2] = new Estudiante("Daniela");
        estudiantes[3] = new Estudiante("Daniel");
        estudiantes[4] = new Estudiante("Jose");

        cursos[0] = new Curso("Matemáticas");
        cursos[1] = new Curso("Programación");
        cursos[2] = new Curso("Física");

        calificaciones[0][0] = 85;
        calificaciones[0][1] = 90;
        calificaciones[0][2] = 95;
        calificaciones[1][0] = 80;
        calificaciones[1][1] = 78;
        calificaciones[1][2] = 88;
        calificaciones[2][0] = 90;
        calificaciones[2][1] = 85;
        calificaciones[2][2] = 80;
        calificaciones[3][0] = 70;
        calificaciones[3][1] = 75;
        calificaciones[3][2] = 80;
        calificaciones[4][0] = 88;
        calificaciones[4][1] = 92;
        calificaciones[4][2] = 85;
    }

    public void iniciar()
    {
    }

    public int calcularPromedioGeneral() 
    {
        return (calcularPromedioEstudiante(0) + calcularPromedioEstudiante(1) + calcularPromedioEstudiante(2) + calcularPromedioEstudiante(3) + calcularPromedioEstudiante(4))
               / estudiantes.length;
    }

    public int calcularPromedioEstudiante(int indiceEstudiante) 
    {
        return (calificaciones[indiceEstudiante][0] + calificaciones[indiceEstudiante][1] + calificaciones[indiceEstudiante][2])
               / cursos.length;
    }

    public int calcularPromedioCurso(int indiceCurso) 
    {
        return (calificaciones[0][indiceCurso] + calificaciones[1][indiceCurso] + calificaciones[2][indiceCurso] + calificaciones[3][indiceCurso] + calificaciones[4][indiceCurso])
               / estudiantes.length;
    }
}
