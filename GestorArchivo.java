package urbanroute;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Se encarga de todo lo que tiene que ver con archivos: dejar que el
 * usuario elija un CSV, leerlo para llenar el grafo y la tabla hash, y
 * guardar los cambios de vuelta en el archivo.
 *
 * Trabajamos con dos tipos de CSV y sabemos cuál es mirando su primera línea:
 * - Estaciones: ID_Estacion,Nombre,Linea,Zona,Capacidad_Pasajeros_Hora
 * - Conexiones: Estacion_Origen,Estacion_Destino,Tiempo_Traslado_Min
 */
public class GestorArchivo {

    /** Los dos tipos de archivo que sabemos leer. */
    public enum TipoArchivo { ESTACIONES, CONEXIONES }

    /** Dónde está el archivo maestro de estaciones (se carga al abrir el programa). */
    public static final String RUTA_ESTACIONES_DEFECTO = "datos/estaciones.csv";
    /** Dónde está el archivo maestro de conexiones (se carga al abrir el programa). */
    public static final String RUTA_CONEXIONES_DEFECTO = "datos/conexiones.csv";

    private static final String ENCABEZADO_ESTACIONES = "ID_Estacion,Nombre,Linea,Zona,Capacidad_Pasajeros_Hora";
    private static final String ENCABEZADO_CONEXIONES = "Estacion_Origen,Estacion_Destino,Tiempo_Traslado_Min";
    private static final int MAX_DETALLES = 8; // máximo de líneas con problema que mostramos en el mensaje

    private final Grafo grafo;
    private File archivoEstaciones; // el CSV de estaciones que está cargado (null si no hay)
    private File archivoConexiones; // el CSV de conexiones que está cargado (null si no hay)

    /** Lleva la cuenta de cuántas líneas se leyeron bien y cuáles se saltaron. */
    private static class Conteo {
        int aceptados;
        int omitidos;
        final StringBuilder detalle = new StringBuilder();

        void omitir(int linea, String motivo) {
            omitidos++;
            if (omitidos <= MAX_DETALLES) {
                detalle.append("\n- Línea ").append(linea).append(": ").append(motivo);
            }
        }
    }

    /** @param grafo el grafo que este gestor va a llenar y a guardar */
    public GestorArchivo(Grafo grafo) { this.grafo = grafo; }

    /**
     * Abre la ventanita para escoger un archivo (solo muestra .csv).
     *
     * @param padre la ventana sobre la que aparece el selector
     * @return el archivo elegido, o null si el usuario canceló
     */
    public File seleccionarArchivo(Component padre) {
        JFileChooser selector = new JFileChooser(new File("."));
        selector.setDialogTitle("Seleccionar archivo CSV");
        selector.setFileFilter(new FileNameExtensionFilter("Archivos CSV (*.csv)", "csv"));
        if (selector.showOpenDialog(padre) == JFileChooser.APPROVE_OPTION) {
            return selector.getSelectedFile();
        }
        return null;
    }

    /**
     * Carga los dos archivos maestros. Se llama al iniciar el programa.
     *
     * @return un resumen de lo que se cargó
     * @throws ArchivoInvalidoException si no se pueden leer
     */
    public String cargarPorDefecto() throws ArchivoInvalidoException {
        String r1 = cargar(new File(RUTA_ESTACIONES_DEFECTO));
        String r2 = cargar(new File(RUTA_CONEXIONES_DEFECTO));
        return r1 + "\n" + r2;
    }

    /**
     * Carga un CSV (sabe solo si es de estaciones o de conexiones).
     * - Si es de estaciones: borra todo lo que había en memoria y carga las nuevas.
     * - Si es de conexiones: cambia las conexiones. Si ya había estaciones
     *   cargadas las deja; si no, crea las estaciones que aparezcan con
     *   datos en blanco.
     * Ojo: la interfaz tiene que avisar "¿quieres guardar antes?" ANTES de
     * llamar a este método, porque aquí se pierde lo que no se guardó.
     *
     * @param archivo el CSV a cargar
     * @return un resumen: cuántas líneas se cargaron y cuáles se saltaron
     * @throws ArchivoInvalidoException si no se puede leer o el formato no es el esperado
     */
    public String cargar(File archivo) throws ArchivoInvalidoException {
        TipoArchivo tipo = detectarTipo(archivo); // si algo anda mal, aquí se lanza el error
        try {
            Conteo c;
            if (tipo == TipoArchivo.ESTACIONES) {
                grafo.limpiar();
                archivoConexiones = null;
                c = leerEstaciones(archivo);
                archivoEstaciones = archivo;
            } else {
                if (archivoEstaciones == null) grafo.limpiar();
                else grafo.eliminarTodasLasConexiones();
                c = leerConexiones(archivo);
                archivoConexiones = archivo;
            }
            grafo.setModificado(false); // recién cargado = no hay nada pendiente de guardar
            String texto = archivo.getName() + ": " + c.aceptados + " "
                    + (tipo == TipoArchivo.ESTACIONES ? "estaciones" : "conexiones") + " cargadas.";
            if (c.omitidos > 0) {
                texto += " Líneas omitidas: " + c.omitidos + "." + c.detalle;
            }
            return texto;
        } catch (IOException ex) {
            throw new ArchivoInvalidoException("No se pudo leer el archivo " + archivo.getName() + ".", ex);
        }
    }

    /**
     * Mira la primera línea del archivo para saber si es de estaciones o
     * de conexiones.
     *
     * @param archivo el CSV a revisar
     * @return el tipo de archivo
     * @throws ArchivoInvalidoException si no existe, está vacío o no empieza como esperamos
     */
    public TipoArchivo detectarTipo(File archivo) throws ArchivoInvalidoException {
        if (archivo == null || !archivo.isFile()) {
            throw new ArchivoInvalidoException("El archivo no existe.");
        }
        try (BufferedReader br = abrir(archivo)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = sinBOM(linea).trim().toLowerCase();
                if (linea.isEmpty()) continue; // saltamos líneas en blanco
                if (linea.startsWith("id_estacion")) return TipoArchivo.ESTACIONES;
                if (linea.startsWith("estacion_origen")) return TipoArchivo.CONEXIONES;
                throw new ArchivoInvalidoException("Encabezado no reconocido en " + archivo.getName()
                        + ". Debe iniciar con ID_Estacion o Estacion_Origen.");
            }
        } catch (IOException ex) {
            throw new ArchivoInvalidoException("No se pudo leer el archivo " + archivo.getName() + ".", ex);
        }
        throw new ArchivoInvalidoException("El archivo " + archivo.getName() + " está vacío.");
    }

    /**
     * Guarda en los CSV lo que hay ahora en memoria (las estaciones o
     * conexiones que se agregaron o eliminaron). Primero escribe en un
     * archivo temporal y después lo cambia por el original, así si algo
     * falla a mitad no se daña el archivo bueno.
     *
     * @throws ArchivoInvalidoException si no hay archivo asociado o falla al guardar
     */
    public void actualizarRepositorio() throws ArchivoInvalidoException {
        if (archivoConexiones == null) {
            throw new ArchivoInvalidoException("No hay un archivo de conexiones asociado. Cargue uno primero.");
        }
        try {
            if (archivoEstaciones != null) guardarEstaciones(archivoEstaciones);
            guardarConexiones(archivoConexiones);
            grafo.setModificado(false); // ya está todo guardado
        } catch (IOException ex) {
            throw new ArchivoInvalidoException("No se pudo guardar el repositorio.", ex);
        }
    }

    /** @return true si ya se cargó algún archivo */
    public boolean hayArchivoCargado() { return archivoEstaciones != null || archivoConexiones != null; }

    /** @return los nombres de los archivos cargados (para mostrarlos en la barra de estado) */
    public String getNombreArchivos() {
        String a = archivoEstaciones == null ? "" : archivoEstaciones.getName();
        String b = archivoConexiones == null ? "" : archivoConexiones.getName();
        if (a.isEmpty()) return b.isEmpty() ? "Ninguno" : b;
        return b.isEmpty() ? a : a + " + " + b;
    }

    // ---------------------------------------------------------- leer archivos

    /** Lee un CSV de estaciones línea por línea y las mete al grafo. */
    private Conteo leerEstaciones(File archivo) throws IOException {
        Conteo c = new Conteo();
        try (BufferedReader br = abrir(archivo)) {
            String linea;
            int n = 0;
            boolean esEncabezado = true; // la primera línea con texto es el título de las columnas
            while ((linea = br.readLine()) != null) {
                n++;
                linea = sinBOM(linea).trim();
                if (linea.isEmpty()) continue;
                if (esEncabezado) { esEncabezado = false; continue; }
                String[] p = linea.split(",", -1); // separamos por comas
                if (p.length < 5) { c.omitir(n, "se esperaban 5 columnas"); continue; }
                String id = p[0].trim();
                if (id.isEmpty()) { c.omitir(n, "ID vacío"); continue; }
                int capacidad;
                try {
                    capacidad = Integer.parseInt(p[4].trim());
                    if (capacidad < 0) throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    c.omitir(n, "capacidad inválida");
                    continue;
                }
                try {
                    grafo.agregarEstacion(new Estacion(id, p[1].trim(), p[2].trim(), p[3].trim(), capacidad));
                    c.aceptados++;
                } catch (IllegalArgumentException ex) {
                    c.omitir(n, ex.getMessage()); // por ejemplo, ID repetido
                }
            }
        }
        return c;
    }

    /** Lee un CSV de conexiones línea por línea y las mete al grafo. */
    private Conteo leerConexiones(File archivo) throws IOException {
        Conteo c = new Conteo();
        try (BufferedReader br = abrir(archivo)) {
            String linea;
            int n = 0;
            boolean esEncabezado = true;
            while ((linea = br.readLine()) != null) {
                n++;
                linea = sinBOM(linea).trim();
                if (linea.isEmpty()) continue;
                if (esEncabezado) { esEncabezado = false; continue; }
                String[] p = linea.split(",", -1);
                if (p.length < 3) { c.omitir(n, "se esperaban 3 columnas"); continue; }
                String origen = p[0].trim();
                String destino = p[1].trim();
                if (origen.isEmpty() || destino.isEmpty()) { c.omitir(n, "estación vacía"); continue; }
                int tiempo;
                try {
                    tiempo = Integer.parseInt(p[2].trim());
                } catch (NumberFormatException ex) {
                    c.omitir(n, "tiempo inválido");
                    continue;
                }
                asegurarEstacion(origen);
                asegurarEstacion(destino);
                try {
                    grafo.agregarConexion(origen, destino, tiempo);
                    c.aceptados++;
                } catch (IllegalArgumentException ex) {
                    c.omitir(n, ex.getMessage()); // por ejemplo, tiempo negativo o conexión repetida
                }
            }
        }
        return c;
    }

    /** Si la estación no existe todavía, la crea en blanco para no perder la conexión. */
    private void asegurarEstacion(String id) {
        if (grafo.buscarEstacion(id) == null) grafo.agregarEstacion(new Estacion(id));
    }

    // -------------------------------------------------------- guardar archivos

    /** Escribe todas las estaciones en el CSV. */
    private void guardarEstaciones(File destino) throws IOException {
        File tmp = new File(destino.getPath() + ".tmp"); // archivo temporal
        try (PrintWriter out = abrirEscritura(tmp)) {
            out.println(ENCABEZADO_ESTACIONES);
            Estacion[] todas = grafo.getEstaciones();
            for (int i = 0; i < todas.length; i++) {
                Estacion e = todas[i];
                out.println(e.getId() + "," + e.getNombre() + "," + e.getLinea() + ","
                        + e.getZona() + "," + e.getCapacidad());
            }
            if (out.checkError()) throw new IOException("Error de escritura");
        }
        Files.move(tmp.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    /** Escribe todas las conexiones en el CSV. */
    private void guardarConexiones(File destino) throws IOException {
        File tmp = new File(destino.getPath() + ".tmp");
        try (PrintWriter out = abrirEscritura(tmp)) {
            out.println(ENCABEZADO_CONEXIONES);
            Estacion[] todas = grafo.getEstaciones();
            for (int i = 0; i < todas.length; i++) {
                Estacion e = todas[i];
                for (Conexion c = e.getPrimeraConexion(); c != null; c = c.getSiguiente()) {
                    // cada conexión está guardada en las dos estaciones; comparando los
                    // códigos nos aseguramos de escribirla solo una vez
                    if (e.getId().compareTo(c.getDestino().getId()) < 0) {
                        out.println(e.getId() + "," + c.getDestino().getId() + "," + c.getTiempo());
                    }
                }
            }
            if (out.checkError()) throw new IOException("Error de escritura");
        }
        Files.move(tmp.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    // ------------------------------------------------------------ ayudas

    /** Abre un archivo para leerlo (en UTF-8 para que salgan bien las tildes y la ñ). */
    private BufferedReader abrir(File f) throws IOException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8));
    }

    /** Abre un archivo para escribir en él (también en UTF-8). */
    private PrintWriter abrirEscritura(File f) throws IOException {
        return new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8));
    }

    /** Algunos programas (como Excel) ponen una marca invisible al inicio del archivo; aquí la quitamos. */
    private String sinBOM(String s) {
        return (s.length() > 0 && s.charAt(0) == '\uFEFF') ? s.substring(1) : s;
    }
}
