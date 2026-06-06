package estructura;

public class Nodo {
    public Alumno alumno;
    public Nodo siguiente;

    public Nodo(Alumno alumno) {
        this.alumno = alumno;
        this.siguiente = null;
    }
}