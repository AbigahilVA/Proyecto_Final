package estructura;

public class Alumno {
    private long carnet;
    private String nombre;
    private String codigoCurso;
    private String seccion;
    private double nota;

    public Alumno(long carnet, String nombre, String codigoCurso, String seccion, double nota) {
        this.carnet = carnet;
        this.nombre = nombre;
        this.codigoCurso = codigoCurso;
        this.seccion = seccion;
        this.nota = nota;
    }

    public long getCarnet() { return carnet; }
    public String getNombre() { return nombre; }
    public String getCodigoCurso() { return codigoCurso; }
    public String getSeccion() { return seccion; }
    public double getNota() { return nota; }

    @Override
    public String toString() {
        return "Carnet: " + carnet + " | " + nombre + " | Curso: " + codigoCurso + " | Sec: " + seccion + " | Nota: " + nota;
    }
}