package estructura;

import javax.swing.*;
import java.awt.*;

public class ListaEnlazada {
    public Nodo inicio;

    public ListaEnlazada() {
        this.inicio = null;
    }

    public void insertarOrdenadoPorNombre(Alumno nuevoAlumno) {
        Nodo nuevo = new Nodo(nuevoAlumno);
        if (inicio == null || inicio.alumno.getNombre().compareToIgnoreCase(nuevoAlumno.getNombre()) > 0) {
            nuevo.siguiente = inicio;
            inicio = nuevo;
        } else {
            Nodo actual = inicio;
            while (actual.siguiente != null && actual.siguiente.alumno.getNombre().compareToIgnoreCase(nuevoAlumno.getNombre()) < 0) {
                actual = actual.siguiente;
            }
            nuevo.siguiente = actual.siguiente;
            actual.siguiente = nuevo;
        }
    }

    public void insertarOrdenadoPorCarnet(Alumno nuevoAlumno) {
        Nodo nuevo = new Nodo(nuevoAlumno);
        if (inicio == null || inicio.alumno.getCarnet() > nuevoAlumno.getCarnet()) {
            nuevo.siguiente = inicio;
            inicio = nuevo;
        } else {
            Nodo actual = inicio;
            while (actual.siguiente != null && actual.siguiente.alumno.getCarnet() < nuevoAlumno.getCarnet()) {
                actual = actual.siguiente;
            }
            nuevo.siguiente = actual.siguiente;
            actual.siguiente = nuevo;
        }
    }

    public void insertarOrdenadoPorNotaDesc(Alumno nuevoAlumno) {
        Nodo nuevo = new Nodo(nuevoAlumno);
        if (inicio == null || inicio.alumno.getNota() < nuevoAlumno.getNota()) {
            nuevo.siguiente = inicio;
            inicio = nuevo;
        } else {
            Nodo actual = inicio;
            while (actual.siguiente != null && actual.siguiente.alumno.getNota() > nuevoAlumno.getNota()) {
                actual = actual.siguiente;
            }
            nuevo.siguiente = actual.siguiente;
            actual.siguiente = nuevo;
        }
    }

    public void renderizarEnPanel(JPanel panelContenedor, String titulo, String seccionFiltro) {
        if (panelContenedor == null) return;

        panelContenedor.removeAll();
        panelContenedor.setLayout(new BoxLayout(panelContenedor, BoxLayout.Y_AXIS));
        panelContenedor.setBackground(new Color(240, 244, 248));

        JLabel lblTitulo = new JLabel("<html><body><center><b>" + titulo + "</b><br><font color='gray'>Seccion: " + seccionFiltro + "</font></center></body></html>", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 12));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        panelContenedor.add(lblTitulo);

        JPanel panelInicio = new JPanel();
        panelInicio.setBackground(new Color(52, 152, 219));
        panelInicio.setMaximumSize(new Dimension(100, 25));
        JLabel lblInicio = new JLabel("INICIO", SwingConstants.CENTER);
        lblInicio.setForeground(Color.WHITE);
        lblInicio.setFont(new Font("Arial", Font.BOLD, 11));
        panelInicio.add(lblInicio);
        panelInicio.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelContenedor.add(panelInicio);

        Nodo actual = inicio;
        while (actual != null) {
            if (!seccionFiltro.equals("Todas") && !actual.alumno.getSeccion().equalsIgnoreCase(seccionFiltro)) {
                actual = actual.siguiente;
                continue;
            }

            panelContenedor.add(crearFlechaSeparadora());

            JPanel panelNodo = new JPanel(new GridLayout(3, 1));
            panelNodo.setBackground(Color.WHITE);
            panelNodo.setBorder(BorderFactory.createLineBorder(new Color(44, 62, 80), 2));
            panelNodo.setMaximumSize(new Dimension(290, 70));
            panelNodo.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblNombre = new JLabel("  Estudiante: " + actual.alumno.getNombre(), SwingConstants.LEFT);
            lblNombre.setFont(new Font("Arial", Font.BOLD, 11));
            
            JLabel lblDatos1 = new JLabel("  Carnet: " + actual.alumno.getCarnet() + "  |  Nota: " + actual.alumno.getNota(), SwingConstants.LEFT);
            lblDatos1.setFont(new Font("Arial", Font.PLAIN, 10));
            lblDatos1.setForeground(Color.DARK_GRAY);

            JLabel lblDatos2 = new JLabel("  Curso: " + actual.alumno.getCodigoCurso() + "  |  Seccion: " + actual.alumno.getSeccion(), SwingConstants.LEFT);
            lblDatos2.setFont(new Font("Arial", Font.PLAIN, 10));
            lblDatos2.setForeground(Color.BLUE);

            panelNodo.add(lblNombre);
            panelNodo.add(lblDatos1);
            panelNodo.add(lblDatos2);
            panelContenedor.add(panelNodo);

            actual = actual.siguiente;
        }

        panelContenedor.add(crearFlechaSeparadora());

        JPanel panelNull = new JPanel();
        panelNull.setBackground(new Color(231, 76, 60));
        panelNull.setMaximumSize(new Dimension(100, 25));
        JLabel lblNull = new JLabel("NULL", SwingConstants.CENTER);
        lblNull.setForeground(Color.WHITE);
        lblNull.setFont(new Font("Arial", Font.BOLD, 11));
        panelNull.add(lblNull);
        panelNull.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelContenedor.add(panelNull);

        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

    private JPanel crearFlechaSeparadora() {
        JPanel p = new JPanel();
        p.setBackground(new Color(240, 244, 248));
        p.setMaximumSize(new Dimension(20, 15));
        JLabel l = new JLabel("|");
        l.setFont(new Font("Arial", Font.BOLD, 11));
        l.setForeground(new Color(44, 62, 80));
        p.add(l);
        p.setAlignmentX(Component.CENTER_ALIGNMENT);
        return p;
    }
}