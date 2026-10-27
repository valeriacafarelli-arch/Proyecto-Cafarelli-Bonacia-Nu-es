/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectocafarellibonacianues;

/**
 *
 * @author valeriacafarelli-arch
 */
import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GestorArchivo {

    private Grafo grafo;
    private File archivoEstaciones;
    private File archivoConexiones;

    public GestorArchivo(Grafo grafo) {
        this.grafo = grafo;
    }

    public File seleccionarArchivo(Component padre) {
        JFileChooser selector = new JFileChooser(".");
        selector.setFileFilter(new FileNameExtensionFilter("Archivos CSV (*.csv)", "csv"));
        if (selector.showOpenDialog(padre) == JFileChooser.APPROVE_OPTION) {
            return selector.getSelectedFile();
        }
        return null;
    }

    public void cargarEstaciones(File archivo) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea = br.readLine();
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    String[] p = linea.split(",");
                    if (p.length >= 5) {
                        String id = p[0].trim();
                        String nombre = p[1].trim();
                        String lineaNom = p[2].trim();
                        String zona = p[3].trim();
                        int capacidad = Integer.parseInt(p[4].trim());

                        if (grafo.buscarEstacion(id) == null) {
                            grafo.agregarEstacion(new Estacion(id, nombre, lineaNom, zona, capacidad));
                        }
                    }
                }
            }
            br.close();
            archivoEstaciones = archivo;
        } catch (Exception ex) {
            System.out.println("Error al cargar estaciones: " + ex.getMessage());
        }
    }

    public void cargarConexiones(File archivo) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea = br.readLine();
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    String[] p = linea.split(",");
                    if (p.length >= 3) {
                        String origen = p[0].trim();
                        String destino = p[1].trim();
                        int tiempo = Integer.parseInt(p[2].trim());

                        if (grafo.buscarEstacion(origen) == null) {
                            grafo.agregarEstacion(new Estacion(origen));
                        }
                        if (grafo.buscarEstacion(destino) == null) {
                            grafo.agregarEstacion(new Estacion(destino));
                        }
                        try {
                            grafo.agregarConexion(origen, destino, tiempo);
                        } catch (Exception e) {}
                    }
                }
            }
            br.close();
            archivoConexiones = archivo;
        } catch (Exception ex) {
            System.out.println("Error al cargar conexiones: " + ex.getMessage());
        }
    }

    public void cargar(File archivo) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String primeraLinea = br.readLine();
            br.close();

            if (primeraLinea != null) {
                primeraLinea = primeraLinea.toLowerCase();
                if (primeraLinea.contains("id_estacion")) {
                    grafo.limpiar();
                    archivoConexiones = null;
                    cargarEstaciones(archivo);
                } else if (primeraLinea.contains("estacion_origen")) {
                    if (archivoEstaciones == null) {
                        grafo.limpiar();
                    } else {
                        grafo.eliminarTodasLasConexiones();
                    }
                    cargarConexiones(archivo);
                }
            }
            grafo.setModificado(false);
        } catch (Exception ex) {
            System.out.println("Error al leer el archivo: " + ex.getMessage());
        }
    }

    public void guardarEstaciones(File archivo) {
        try {
            PrintWriter pw = new PrintWriter(archivo);
            pw.println("ID_Estacion,Nombre,Linea,Zona,Capacidad_Pasajeros_Hora");
            Estacion[] todas = grafo.getEstaciones();
            for (int i = 0; i < todas.length; i++) {
                Estacion e = todas[i];
                pw.println(e.getId() + "," + e.getNombre() + "," + e.getLinea() + "," + e.getZona() + "," + e.getCapacidad());
            }
            pw.close();
        } catch (Exception ex) {
            System.out.println("Error al guardar estaciones: " + ex.getMessage());
        }
    }

    public void guardarConexiones(File archivo) {
        try {
            PrintWriter pw = new PrintWriter(archivo);
            pw.println("Estacion_Origen,Estacion_Destino,Tiempo_Traslado_Min");
            Estacion[] todas = grafo.getEstaciones();
            for (int i = 0; i < todas.length; i++) {
                Estacion e = todas[i];
                for (Conexion c = e.getPrimeraConexion(); c != null; c = c.getSiguiente()) {
                    if (e.getId().compareTo(c.getDestino().getId()) < 0) {
                        pw.println(e.getId() + "," + c.getDestino().getId() + "," + c.getTiempo());
                    }
                }
            }
            pw.close();
        } catch (Exception ex) {
            System.out.println("Error al guardar conexiones: " + ex.getMessage());
        }
    }

    public void actualizarRepositorio() {
        if (archivoEstaciones != null) {
            guardarEstaciones(archivoEstaciones);
        }
        if (archivoConexiones != null) {
            guardarConexiones(archivoConexiones);
        }
        grafo.setModificado(false);
    }

    public boolean hayArchivoCargado() {
        return archivoEstaciones != null || archivoConexiones != null;
    }

    public String getNombreArchivos() {
        String a = (archivoEstaciones == null) ? "" : archivoEstaciones.getName();
        String b = (archivoConexiones == null) ? "" : archivoConexiones.getName();
        if (a.isEmpty()) {
            return b.isEmpty() ? "Ninguno" : b;
        }
        return b.isEmpty() ? a : a + " + " + b;
    }
}