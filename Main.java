package logica;

import estructura.*;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static String rutaArchivo = "";
    private static boolean archivoCargado = false;

    private static List<String> cursosDetectados = new ArrayList<>();
    private static List<String> seccionesDetectadas = new ArrayList<>();

    private static ListaEnlazada listaPorNombre = new ListaEnlazada();
    private static ListaEnlazada listaPorCarnet = new ListaEnlazada();
    private static ListaEnlazada listaPorNotaDesc = new ListaEnlazada();

    private static JTextArea txtReportes;
    private static JPanel panelSimulacion;
    private static JComboBox<String> comboCurso;     
    private static JComboBox<String> comboSeccion;   
    private static JPanel panelMenu; 
    private static int ultimaOpcionSeleccionada = 0;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        JFrame ventana = new JFrame("Proyecto Final Programacion");
        ventana.setSize(1200, 680);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(new BorderLayout());

        JPanel panelHerramientas = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        panelHerramientas.setBorder(BorderFactory.createEtchedBorder());
        
        comboCurso = new JComboBox<>(new String[]{"Todos"});
        comboSeccion = new JComboBox<>(new String[]{"Todas"});
        
        panelHerramientas.add(new JLabel("Filtrar por Curso:"));
        panelHerramientas.add(comboCurso);
        panelHerramientas.add(new JLabel("Filtrar por Seccion:"));
        panelHerramientas.add(comboSeccion);
        panelHerramientas.add(new JLabel("<html><font color='green'>TXT activo</font></html>"));
        
        ventana.add(panelHerramientas, BorderLayout.NORTH);

        panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(8, 1, 6, 6));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelMenu.setBackground(new Color(236, 240, 241));
        panelMenu.setPreferredSize(new Dimension(340, 0));

        JButton btnOp1 = new JButton("1. Establecer y visualizar datos de archivo");
        JButton btnOp2 = new JButton("2. Cargar datos a las listas");
        JButton btnOp3 = new JButton("3. Visualizar nombre ordenado por seccion (Asc)");
        JButton btnOp4 = new JButton("4. Visualizar carnet ordenado por seccion (Asc)");
        JButton BBtnOp5 = new JButton("5. Visualizar nota ordenada (Descendentemente)");
        JButton btnOp6 = new JButton("6. Visualizar top 5 notas mas altas y mas bajas");
        JButton btnOp7 = new JButton("7. Visualizar promedio por seccion");
        JButton btnOp8 = new JButton("8. Salir");

        panelMenu.add(btnOp1); panelMenu.add(btnOp2); panelMenu.add(btnOp3); panelMenu.add(btnOp4);
        panelMenu.add(BBtnOp5); panelMenu.add(btnOp6); panelMenu.add(btnOp7); panelMenu.add(btnOp8);
        ventana.add(panelMenu, BorderLayout.WEST);

        txtReportes = new JTextArea();
        txtReportes.setEditable(false);
        txtReportes.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtReportes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollReportes = new JScrollPane(txtReportes);
        scrollReportes.setBorder(BorderFactory.createTitledBorder("Reportes e Impresion de Datos"));

        panelSimulacion = new JPanel();
        panelSimulacion.setLayout(new BoxLayout(panelSimulacion, BoxLayout.Y_AXIS));
        panelSimulacion.setBackground(new Color(240, 244, 248));
        JScrollPane scrollSimulacion = new JScrollPane(panelSimulacion);
        scrollSimulacion.setPreferredSize(new Dimension(340, 0));
        scrollSimulacion.setBorder(BorderFactory.createTitledBorder("Estructura de Nodos en RAM"));

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.add(scrollReportes, BorderLayout.CENTER);
        panelCentral.add(scrollSimulacion, BorderLayout.EAST);
        ventana.add(panelCentral, BorderLayout.CENTER);

        btnOp1.addActionListener(e -> { ultimaOpcionSeleccionada = 1; ejecutarOpcion1(); });
        btnOp2.addActionListener(e -> { ultimaOpcionSeleccionada = 2; ejecutarOpcion2Lenta(); });
        btnOp3.addActionListener(e -> { ultimaOpcionSeleccionada = 3; ejecutarOpcion3(); });
        btnOp4.addActionListener(e -> { ultimaOpcionSeleccionada = 4; ejecutarOpcion4(); });
        BBtnOp5.addActionListener(e -> { ultimaOpcionSeleccionada = 5; ejecutarOpcion5(); });
        btnOp6.addActionListener(e -> { ultimaOpcionSeleccionada = 6; ejecutarOpcion6(); });
        btnOp7.addActionListener(e -> { ultimaOpcionSeleccionada = 7; ejecutarOpcion7(); });
        btnOp8.addActionListener(e -> System.exit(0));

        java.awt.event.ActionListener filtroAccion = e -> {
            if (!archivoCargado || comboCurso.getItemCount() <= 1) return;
            
            if (ultimaOpcionSeleccionada >= 3 && ultimaOpcionSeleccionada <= 7) {
                switch (ultimaOpcionSeleccionada) {
                    case 3 -> ejecutarOpcion3();
                    case 4 -> ejecutarOpcion4();
                    case 5 -> ejecutarOpcion5();
                    case 6 -> ejecutarOpcion6();
                    case 7 -> ejecutarOpcion7();
                }
            }
        };

        comboCurso.addActionListener(filtroAccion);
        comboSeccion.addActionListener(filtroAccion);

        ventana.setVisible(true);
    }

    private static void ejecutarOpcion1() {
        String archivoIngresado = JOptionPane.showInputDialog(null, 
                "Ingrese el nombre o ruta del archivo (.txt):", "Opcion 1", JOptionPane.QUESTION_MESSAGE);
        
        if (archivoIngresado == null || archivoIngresado.trim().isEmpty()) return;

        File file = new File(archivoIngresado);
        if (file.exists() && !file.isDirectory()) {
            rutaArchivo = archivoIngresado;
            archivoCargado = true;
            txtReportes.setText("Archivo asignado correctamente. Contenido original:\n\n");
            
            panelSimulacion.removeAll();
            panelSimulacion.revalidate();
            panelSimulacion.repaint();

            detectarFiltrosDinamicamente();

            try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    txtReportes.append(linea + "\n");
                }
            } catch (Exception e) {
                txtReportes.setText("Error al leer el archivo: " + e.getMessage());
            }
        } else {
            txtReportes.setText("El archivo ingresado no existe.\n");
        }
    }

    private static void ejecutarOpcion2Lenta() {
        if (!archivoCargado) {
            txtReportes.setText("Debe establecer la ruta del archivo en la opcion 1 primero.\n");
            return;
        }

        listaPorNombre.inicio = null;
        listaPorCarnet.inicio = null;
        listaPorNotaDesc.inicio = null;
        txtReportes.setText("Iniciando simulacion en RAM...\n\n");
        
        setComponentesMenuHabilitados(false);
        detectarFiltrosDinamicamente(); 

        Thread hiloSimulacion = new Thread(() -> {
            try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    if (linea.trim().isEmpty()) continue;
                    String[] datos = linea.split(",");
                    if (datos.length == 5) {
                        long carnet = Long.parseLong(datos[0].trim());
                        String nombre = datos[1].trim();
                        String codigoCurso = datos[2].trim();
                        String seccion = datos[3].trim();
                        double nota = Double.parseDouble(datos[4].trim());

                        Alumno nuevoAlumno = new Alumno(carnet, nombre, codigoCurso, seccion, nota);
                        
                        listaPorNombre.insertarOrdenadoPorNombre(nuevoAlumno);
                        listaPorCarnet.insertarOrdenadoPorCarnet(nuevoAlumno);
                        listaPorNotaDesc.insertarOrdenadoPorNotaDesc(nuevoAlumno);

                        SwingUtilities.invokeLater(() -> {
                            txtReportes.append("Insertando Nodo Ordenado: " + nombre + " [Nota: " + nota + "]\n");
                            listaPorNotaDesc.renderizarEnPanel(panelSimulacion, "SIMULANDO EN RAM...", "Todas");
                        });

                        Thread.sleep(1000);
                    }
                }
                
                SwingUtilities.invokeLater(() -> {
                    txtReportes.append("\nSimulacion Completada. Todos los nodos enlazados en RAM.\n");
                    listaPorNotaDesc.renderizarEnPanel(panelSimulacion, "SIMULACION COMPLETADA", "Todas");
                    setComponentesMenuHabilitados(true); 
                });

            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    txtReportes.append("Error en simulacion: " + e.getMessage() + "\n");
                    setComponentesMenuHabilitados(true);
                });
            }
        });
        
        hiloSimulacion.start();
    }

    private static void detectarFiltrosDinamicamente() {
        cursosDetectados.clear();
        seccionesDetectadas.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] datos = linea.split(",");
                if (datos.length == 5) {
                    String curso = datos[2].trim().toUpperCase();
                    String seccion = datos[3].trim().toUpperCase();

                    if (!cursosDetectados.contains(curso)) cursosDetectados.add(curso);
                    if (!seccionesDetectadas.contains(seccion)) seccionesDetectadas.add(seccion);
                }
            }
        } catch (Exception ignored) {}

        comboCurso.removeAllItems();
        comboCurso.addItem("Todos");
        for (String c : cursosDetectados) comboCurso.addItem(c);

        comboSeccion.removeAllItems();
        comboSeccion.addItem("Todas");
        for (String s : seccionesDetectadas) comboSeccion.addItem(s);
    }

    private static void setComponentesMenuHabilitados(boolean activo) {
        for (Component c : panelMenu.getComponents()) {
            c.setEnabled(activo);
        }
        comboSeccion.setEnabled(activo);
        comboCurso.setEnabled(activo);
    }

    private static void ejecutarOpcion3() {
        if (!archivoCargado || listaPorNombre.inicio == null) {
            txtReportes.setText("Debe cargar los datos en la opcion 2 antes de emitir reportes.\n");
            return;
        }
        String fCurso = comboCurso.getSelectedItem().toString();
        String fSeccion = comboSeccion.getSelectedItem().toString();
        
        txtReportes.setText("\nREPORTE: ORDENADO ASCENDENTEMENTE POR NOMBRE\n");
        imprimirPorSeccionFiltradoHibrido(listaPorNombre, fCurso, fSeccion, true);
        listaPorNombre.renderizarEnPanel(panelSimulacion, "LISTA POR NOMBRE", fSeccion);
    }

    private static void ejecutarOpcion4() {
        if (!archivoCargado || listaPorCarnet.inicio == null) {
            txtReportes.setText("Debe cargar los datos en la opcion 2 antes de emitir reportes.\n");
            return;
        }
        String fCurso = comboCurso.getSelectedItem().toString();
        String fSeccion = comboSeccion.getSelectedItem().toString();

        txtReportes.setText("\nREPORTE: ORDENADO ASCENDENTEMENTE POR NUMERO DE CARNET\n");
        imprimirPorSeccionFiltradoHibrido(listaPorCarnet, fCurso, fSeccion, false);
        listaPorCarnet.renderizarEnPanel(panelSimulacion, "LISTA POR CARNET", fSeccion);
    }

    private static void ejecutarOpcion5() {
        if (!archivoCargado || listaPorNotaDesc.inicio == null) {
            txtReportes.setText("Debe cargar los datos en la opcion 2 antes de emitir reportes.\n");
            return;
        }
        String fCurso = comboCurso.getSelectedItem().toString();
        String fSeccion = comboSeccion.getSelectedItem().toString();

        txtReportes.setText("\nREPORTE: NOTAS ORDENADAS DESCENDENTEMENTE\n");
        txtReportes.append("Nota, Carnet y Nombre\n");
        txtReportes.append("----------------------------------------------------------------------\n");
        
        setComponentesMenuHabilitados(false);
        
        panelSimulacion.removeAll();
        panelSimulacion.revalidate();
        panelSimulacion.repaint();

        Thread hiloSimulacionOpcion5 = new Thread(() -> {
            Nodo actual = listaPorNotaDesc.inicio;
            ListaEnlazada listaSimulacionPasoAPaso = new ListaEnlazada();

            while (actual != null) {
                boolean cumpleCurso = fCurso.equals("Todos") || actual.alumno.getCodigoCurso().equalsIgnoreCase(fCurso);
                boolean cumpleSeccion = fSeccion.equals("Todas") || actual.alumno.getSeccion().equalsIgnoreCase(fSeccion);

                if (cumpleCurso && cumpleSeccion) {
                    final Nodo nodoActualParaImprimir = actual;
                    
                    listaSimulacionPasoAPaso.insertarOrdenadoPorNotaDesc(actual.alumno);

                    SwingUtilities.invokeLater(() -> {
                        txtReportes.append(String.format("  [Nota: %.1f] -> Carnet: %d | Nombre: %s (Curso: %s, Sec: %s)\n", 
                                nodoActualParaImprimir.alumno.getNota(), nodoActualParaImprimir.alumno.getCarnet(), nodoActualParaImprimir.alumno.getNombre(),
                                nodoActualParaImprimir.alumno.getCodigoCurso(), nodoActualParaImprimir.alumno.getSeccion()));
                        
                        listaSimulacionPasoAPaso.renderizarEnPanel(panelSimulacion, "SIMULANDO NOTAS", fSeccion);
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                actual = actual.siguiente;
            }
            
            SwingUtilities.invokeLater(() -> {
                txtReportes.append("  [Fin Estructura] -> null\n");
                listaPorNotaDesc.renderizarEnPanel(panelSimulacion, "SIMULACION COMPLETADA", fSeccion);
                setComponentesMenuHabilitados(true);
            });
        });

        hiloSimulacionOpcion5.start();
    }

    private static void ejecutarOpcion6() {
        if (!archivoCargado || listaPorNotaDesc.inicio == null) {
            txtReportes.setText("Debe cargar los datos en la opcion 2 antes de emitir reportes.\n");
            return;
        }
        String fCurso = comboCurso.getSelectedItem().toString();
        String fSeccion = comboSeccion.getSelectedItem().toString();

        txtReportes.setText("\nREPORTE: TOP 5 NOTAS MAS ALTAS Y MAS BAJAS\n");
        imprimirTopNotasHibrido(listaPorNotaDesc, fCurso, fSeccion);
        listaPorNotaDesc.renderizarEnPanel(panelSimulacion, "TOP 5 EN RAM", fSeccion);
    }

    private static void ejecutarOpcion7() {
        if (!archivoCargado || listaPorNotaDesc.inicio == null) {
            txtReportes.setText("Debe cargar los datos en la opcion 2 antes de emitir reportes.\n");
            return;
        }
        String fCurso = comboCurso.getSelectedItem().toString();
        String fSeccion = comboSeccion.getSelectedItem().toString();

        txtReportes.setText("\nREPORTE: PROMEDIO DE NOTAS POR CURSO Y SECCION\n");
        imprimirPromediosHibrido(listaPorNotaDesc, fCurso, fSeccion);
        listaPorNotaDesc.renderizarEnPanel(panelSimulacion, "PROMEDIOS EN RAM", fSeccion);
    }

    private static void imprimirPorSeccionFiltradoHibrido(ListaEnlazada lista, String fCurso, String fSeccion, boolean esPorNombre) {
        if (fCurso.equals("Todos") && fSeccion.equals("Todas")) {
            for (String curso : cursosDetectados) {
                txtReportes.append("\n--------------------------------------------------");
                txtReportes.append("\n  CODIGO DE CURSO: " + curso);
                txtReportes.append("\n--------------------------------------------------\n");

                for (String seccion : seccionesDetectadas) {
                    txtReportes.append("  [Seccion " + seccion + "]\n");
                    Nodo aux = lista.inicio;
                    int contador = 0;
                    while (aux != null) {
                        if (aux.alumno.getCodigoCurso().equalsIgnoreCase(curso) && aux.alumno.getSeccion().equalsIgnoreCase(seccion)) {
                            if (esPorNombre) {
                                txtReportes.append(String.format("    - Nombre: %-22s | Carnet: %-10d | Nota: %.1f\n", 
                                        aux.alumno.getNombre(), aux.alumno.getCarnet(), aux.alumno.getNota()));
                            } else {
                                txtReportes.append(String.format("    - Carnet: %-10d | Nombre: %-22s | Nota: %.1f\n", 
                                        aux.alumno.getCarnet(), aux.alumno.getNombre(), aux.alumno.getNota()));
                            }
                            contador++;
                        }
                        aux = aux.siguiente;
                    }
                    if (contador == 0) txtReportes.append("    - [No hay alumnos registrados]\n");
                }
            }
        } else {
            for (String curso : cursosDetectados) {
                if (!fCurso.equals("Todos") && !curso.equalsIgnoreCase(fCurso)) continue;

                txtReportes.append("\n--------------------------------------------------");
                txtReportes.append("\n  FILTRADO - CURSO: " + curso);
                txtReportes.append("\n--------------------------------------------------\n");

                for (String seccion : seccionesDetectadas) {
                    if (!fSeccion.equals("Todas") && !seccion.equalsIgnoreCase(fSeccion)) continue;
                    
                    txtReportes.append("  [Seccion " + seccion + "]\n");
                    Nodo aux = lista.inicio;
                    int contador = 0;
                    while (aux != null) {
                        if (aux.alumno.getCodigoCurso().equalsIgnoreCase(curso) && aux.alumno.getSeccion().equalsIgnoreCase(seccion)) {
                            if (esPorNombre) {
                                txtReportes.append(String.format("    - %s, %d, %.1f\n", aux.alumno.getNombre(), aux.alumno.getCarnet(), aux.alumno.getNota()));
                            } else {
                                txtReportes.append(String.format("    - %d, %s, %.1f\n", aux.alumno.getCarnet(), aux.alumno.getNombre(), aux.alumno.getNota()));
                            }
                            contador++;
                        }
                        aux = aux.siguiente;
                    }
                    if (contador == 0) txtReportes.append("    - [Sin registros para el filtro]\n");
                }
            }
        }
    }

    private static void imprimirTopNotasHibrido(ListaEnlazada lista, String fCurso, String fSeccion) {
        for (String curso : cursosDetectados) {
            if (!fCurso.equals("Todos") && !curso.equalsIgnoreCase(fCurso)) continue;

            txtReportes.append("\n--------------------------------------------------");
            txtReportes.append("\n  CODIGO DE CURSO: " + curso);
            txtReportes.append("\n--------------------------------------------------\n");

            for (String seccion : seccionesDetectadas) {
                if (!fSeccion.equals("Todas") && !seccion.equalsIgnoreCase(fSeccion)) continue;

                txtReportes.append("  [Seccion " + seccion + "]\n");
                
                txtReportes.append("    > 5 NOTAS MAS ALTAS:\n");
                Nodo aux = lista.inicio;
                int cAltas = 0;
                while (aux != null && cAltas < 5) {
                    if (aux.alumno.getCodigoCurso().equalsIgnoreCase(curso) && aux.alumno.getSeccion().equalsIgnoreCase(seccion)) {
                        txtReportes.append(String.format("      %d. Nota: %.1f | %s | %d | %s | %s\n", 
                                (cAltas+1), aux.alumno.getNota(), aux.alumno.getNombre(), aux.alumno.getCarnet(), aux.alumno.getCodigoCurso(), aux.alumno.getSeccion()));
                        cAltas++;
                    }
                    aux = aux.siguiente;
                }
                if (cAltas == 0) txtReportes.append("      [Sin registros]\n");

                txtReportes.append("\n    > 5 NOTAS MAS BAJAS:\n");
                aux = lista.inicio;
                int totalGrupo = 0;
                while (aux != null) {
                    if (aux.alumno.getCodigoCurso().equalsIgnoreCase(curso) && aux.alumno.getSeccion().equalsIgnoreCase(seccion)) totalGrupo++;
                    aux = aux.siguiente;
                }
                int saltos = totalGrupo - 5;
                if (saltos < 0) saltos = 0;

                aux = lista.inicio;
                int escaneados = 0, cBajas = 0;
                while (aux != null && cBajas < 5) {
                    if (aux.alumno.getCodigoCurso().equalsIgnoreCase(curso) && aux.alumno.getSeccion().equalsIgnoreCase(seccion)) {
                        if (escaneados >= saltos) {
                            txtReportes.append(String.format("      %d. Nota: %.1f | %s | %d | %s | %s\n", 
                                    (cBajas+1), aux.alumno.getNota(), aux.alumno.getNombre(), aux.alumno.getCarnet(), aux.alumno.getCodigoCurso(), aux.alumno.getSeccion()));
                            cBajas++;
                        }
                        escaneados++;
                    }
                    aux = aux.siguiente;
                }
                if (cBajas == 0) txtReportes.append("      [Sin registros]\n");
                txtReportes.append("\n");
            }
        }
    }

    private static void imprimirPromediosHibrido(ListaEnlazada lista, String fCurso, String fSeccion) {
        for (String curso : cursosDetectados) {
            if (!fCurso.equals("Todos") && !curso.equalsIgnoreCase(fCurso)) continue;

            txtReportes.append("--------------------------------------------------\n");
            txtReportes.append("  CURSO: " + curso + "\n");
            txtReportes.append("--------------------------------------------------\n");

            for (String seccion : seccionesDetectadas) {
                if (!fSeccion.equals("Todas") && !seccion.equalsIgnoreCase(fSeccion)) continue;

                Nodo aux = lista.inicio;
                double suma = 0;
                int cuenta = 0;
                while (aux != null) {
                    if (aux.alumno.getCodigoCurso().equalsIgnoreCase(curso) && aux.alumno.getSeccion().equalsIgnoreCase(seccion)) {
                        suma += aux.alumno.getNota();
                        cuenta++;
                    }
                    aux = aux.siguiente;
                }
                if (cuenta > 0) {
                    txtReportes.append(String.format("  Seccion %s -> Promedio: %.2f (Alumnos: %d)\n", seccion, (suma / cuenta), cuenta));
                } else {
                    txtReportes.append("  Seccion " + seccion + " -> [Sin alumnos para promediar]\n");
                }
            }
            txtReportes.append("\n");
        }
    }
}