package logica;

import estructura.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    private static String rutaArchivo = "";
    private static boolean archivoCargado = false;
    
    private static final String[] SECCIONES_FIJAS = {"A", "B", "C"};

    public static void main(String[] args) {
        Scanner sn = new Scanner(System.in);
        int opcion;

        ListaEnlazada listaPorNombre = new ListaEnlazada();
        ListaEnlazada listaPorCarnet = new ListaEnlazada();
        ListaEnlazada listaPorNotaDesc = new ListaEnlazada();

        do {
            System.out.println("\n=========================================");
            System.out.println("     MENU PRINCIPAL - PROYECTO FINAL     ");
            System.out.println("=========================================");
            System.out.println("1. Establecer y visualizar datos de archivo");
            System.out.println("2. Cargar datos a las listas (Simulacion)");
            System.out.println("3. Visualizar nombre ordenado por seccion (Ascendentemente)");
            System.out.println("4. Visualizar carnet ordenado por seccion (Ascendentemente)");
            System.out.println("5. Visualizar nota ordenada (Descendentemente)");
            System.out.println("6. Visualizar top 5 notas mas altas y mas bajas por seccion");
            System.out.println("7. Visualizar promedio por seccion");
            System.out.println("8. Salir");
            System.out.print("Elija una opcion: ");
            
            try {
                opcion = sn.nextInt();
                sn.nextLine(); 
            } catch (Exception e) {
                System.out.println("Por favor, ingrese un numero valido.");
                sn.nextLine();
                opcion = 0;
                continue;
            }

            switch (opcion) {
                case 1:
                    if (rutaArchivo.isEmpty()) {
                        System.out.print("Ingrese el nombre del archivo (.txt): ");
                        rutaArchivo = sn.nextLine();
                    }
                    
                    File archivo = new File(rutaArchivo);
                    if (archivo.exists() && !archivo.isDirectory()) {
                        System.out.println("\n--- CONTENIDO ORIGINAL DEL ARCHIVO ---");
                        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
                            String linea;
                            while ((linea = br.readLine()) != null) {
                                System.out.println(linea);
                            }
                        } catch (Exception e) {
                            System.out.println("Error al leer: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Error! El archivo '" + rutaArchivo + "' no existe.");
                        rutaArchivo = "";
                    }
                    break;

                case 2:
                    if (rutaArchivo.isEmpty()) {
                        System.out.println("Primero debe establecer el archivo en la Opcion 1.");
                        break;
                    }
                    
                    try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
                        String linea;
                        System.out.println("\n--- INICIANDO SIMULACION DE CARGA ORDENADA ---");
                        while ((linea = br.readLine()) != null) {
                            if (linea.trim().isEmpty()) continue;
                            String[] datos = linea.split(",");
                            
                            long carnet = Long.parseLong(datos[0].trim());
                            String nombre = datos[1].trim();
                            String curso = datos[2].trim();
                            String seccion = datos[3].trim();
                            double nota = Double.parseDouble(datos[4].trim());

                            Alumno al = new Alumno(carnet, nombre, curso, seccion, nota);

                            listaPorNombre.insertarOrdenadoPorNombre(al);
                            listaPorCarnet.insertarOrdenadoPorCarnet(al);
                            listaPorNotaDesc.insertarOrdenadoPorNotaDesc(al);

                            System.out.println("\n-> Leyendo e insertando a: " + nombre);
                            System.out.print("Estado de la Lista (Ordenada por Nombre): ");
                            listaPorNombre.mostrarSimulacionTexto();
                            Thread.sleep(400); 
                        }
                        archivoCargado = true;
                        System.out.println("\nTodos los datos han sido cargados!");
                    } catch (Exception e) {
                        System.out.println("Error procesando el archivo: " + e.getMessage());
                    }
                    break;

                case 3:
                    if (!archivoCargado) { System.out.println("Cargue los datos primero (Opcion 2)"); break; }
                    System.out.println("\n--- REPORTE ALUMNOS POR NOMBRE (FILTRADO POR SECCIONES) ---");
                    imprimirAgrupadoPorSeccion(listaPorNombre, 3);
                    break;

                case 4:
                    if (!archivoCargado) { System.out.println("Cargue los datos primero (Opcion 2)"); break; }
                    System.out.println("\n--- REPORTE ALUMNOS POR CARNET (FILTRADO POR SECCIONES) ---");
                    imprimirAgrupadoPorSeccion(listaPorCarnet, 4);
                    break;

                case 5:
                    if (!archivoCargado) { System.out.println("Cargue los datos primero (Opcion 2)"); break; }
                    System.out.println("\n--- REPORTE ALUMNOS POR NOTA (DESCENDENTE) ---");
                    listaPorNotaDesc.mostrarSimulacionTexto();
                    break;

                case 6:
                    if (!archivoCargado) { System.out.println("Cargue los datos primero (Opcion 2)"); break; }
                    System.out.println("\n--- NOTAS MAS ALTAS Y MAS BAJAS POR SECCION ---");
                    imprimirTopNotasPorSeccion(listaPorNotaDesc);
                    break;
                    
                case 7:
                    if (!archivoCargado) { System.out.println("Cargue los datos primero (Opcion 2)"); break; }
                    System.out.println("\n--- PROMEDIO DE NOTAS POR SECCION ---");
                    imprimirPromediosPorSeccion(listaPorNotaDesc);
                    break;

                case 8:
                    System.out.println("Saliendo del sistema...");
                    break;
                
                default:
                    System.out.println("Opcion no valida.");
            }
        } while (opcion != 8);
    }

    private static void imprimirAgrupadoPorSeccion(ListaEnlazada lista, int tipoReporte) {
        for (String seccion : SECCIONES_FIJAS) {
            boolean tieneDatos = false;
            Nodo aux = lista.inicio;
            
            while (aux != null) {
                if (aux.alumno.getSeccion().equalsIgnoreCase(seccion)) {
                    if (!tieneDatos) {
                        System.out.println("\n-----------------------------------------");
                        System.out.println(" SECCION: " + seccion.toUpperCase());
                        System.out.println("-----------------------------------------");
                        tieneDatos = true;
                    }
                    if (tipoReporte == 3) {
                        System.out.println("Nombre: " + aux.alumno.getNombre() + " | Carnet: " + aux.alumno.getCarnet() + " | Curso: " + aux.alumno.getCodigoCurso() + " | Nota: " + aux.alumno.getNota());
                    } else if (tipoReporte == 4) {
                        System.out.println("Carnet: " + aux.alumno.getCarnet() + " | Nombre: " + aux.alumno.getNombre() + " | Curso: " + aux.alumno.getCodigoCurso() + " | Nota: " + aux.alumno.getNota());
                    }
                }
                aux = aux.siguiente;
            }
            if (!tieneDatos) {
                System.out.println("\nSECCION " + seccion + ": [Sin alumnos registrados]");
            }
        }
    }

    private static void imprimirTopNotasPorSeccion(ListaEnlazada listaDesc) {
        for (String seccion : SECCIONES_FIJAS) {
            System.out.println("\n=========================================");
            System.out.println(" SECCION: " + seccion.toUpperCase());
            System.out.println("=========================================");
            
            System.out.println("> Notas Mas Altas:");
            Nodo aux = listaDesc.inicio;
            int cAltas = 0;
            while (aux != null && cAltas < 5) {
                if (aux.alumno.getSeccion().equalsIgnoreCase(seccion)) {
                    System.out.println("  " + (cAltas+1) + ". Nota: " + aux.alumno.getNota() + " | Alumno: " + aux.alumno.getNombre() + " | Carnet: " + aux.alumno.getCarnet());
                    cAltas++;
                }
                aux = aux.siguiente;
            }
            if (cAltas == 0) System.out.println("  [No hay alumnos en esta seccion]");
            
            System.out.println("\n> Notas Mas Bajas:");
            aux = listaDesc.inicio;
            int totalElementosSeccion = 0;
            while (aux != null) {
                if (aux.alumno.getSeccion().equalsIgnoreCase(seccion)) {
                    totalElementosSeccion++;
                }
                aux = aux.siguiente;
            }
            
            aux = listaDesc.inicio;
            int saltos = totalElementosSeccion - 5;
            if (saltos < 0) saltos = 0;
            
            int escaneados = 0;
            int cBajas = 0;
            while (aux != null && cBajas < 5) {
                if (aux.alumno.getSeccion().equalsIgnoreCase(seccion)) {
                    if (escaneados >= saltos) {
                        System.out.println("  " + (cBajas+1) + ". Nota: " + aux.alumno.getNota() + " | Alumno: " + aux.alumno.getNombre() + " | Carnet: " + aux.alumno.getCarnet());
                        cBajas++;
                    }
                    escaneados++;
                }
                aux = aux.siguiente;
            }
            if (cBajas == 0) System.out.println("  [No hay alumnos en esta seccion]");
        }
    }

    private static void imprimirPromediosPorSeccion(ListaEnlazada lista) {
        for (String seccion : SECCIONES_FIJAS) {
            Nodo aux = lista.inicio;
            double suma = 0;
            int cuenta = 0;
            while (aux != null) {
                if (aux.alumno.getSeccion().equalsIgnoreCase(seccion)) {
                    suma += aux.alumno.getNota();
                    cuenta++;
                }
                aux = aux.siguiente;
            }
            if (cuenta > 0) {
                System.out.printf("Seccion: %s | Promedio de Notas: %.2f\n", seccion.toUpperCase(), (suma / cuenta));
            } else {
                System.out.println("Seccion: " + seccion.toUpperCase() + " | Promedio de Notas: 0.00 (Sin alumnos)");
            }
        }
    }
}