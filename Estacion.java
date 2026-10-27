package urbanroute;

/**
 * Una estación del metro. Guarda sus datos (nombre, línea, zona...) y
 * también la lista de estaciones que tiene al lado (sus conexiones).
 * Esa lista la armamos nosotras con nodos enlazados, sin usar ArrayList
 * ni nada parecido, porque el proyecto no lo permite.
 */
public class Estacion {
    private final String id;
    private String nombre;
    private String linea;
    private String zona;
    private int capacidad;
    private Conexion primeraConexion; // el inicio de la lista de conexiones
    private int grado;                // cuántas conexiones tiene (para saber cuáles son los hubs)

    /**
     * Crea una estación con todos sus datos.
     *
     * @param id        código corto único, ej: PZA_VZLA
     * @param nombre    nombre completo
     * @param linea     línea del metro (Linea 1, Linea 2...)
     * @param zona      zona de la ciudad
     * @param capacidad cuántos pasajeros por hora aguanta
     */
    public Estacion(String id, String nombre, String linea, String zona, int capacidad) {
        this.id = id;
        this.nombre = nombre;
        this.linea = linea;
        this.zona = zona;
        this.capacidad = capacidad;
    }

    /**
     * Crea una estación "en blanco" con solo el ID. Se usa cuando el archivo
     * de conexiones nombra una estación que no estaba en el archivo de
     * estaciones: así no se pierde la conexión.
     *
     * @param id código de la estación
     */
    public Estacion(String id) {
        this(id, id, "Sin línea", "Sin zona", 0);
    }

    /** @return el código único de la estación */
    public String getId() { return id; }
    /** @return el nombre */
    public String getNombre() { return nombre; }
    /** @return la línea del metro */
    public String getLinea() { return linea; }
    /** @return la zona de la ciudad */
    public String getZona() { return zona; }
    /** @return pasajeros por hora */
    public int getCapacidad() { return capacidad; }
    /** @return la primera conexión de la lista (null si no tiene ninguna) */
    public Conexion getPrimeraConexion() { return primeraConexion; }
    /** @return cuántas conexiones directas tiene */
    public int getGrado() { return grado; }

    /**
     * Busca si esta estación está conectada con otra.
     *
     * @param idDestino código de la otra estación
     * @return la conexión si existe, o null si no
     */
    public Conexion buscarConexionHacia(String idDestino) {
        // recorremos la lista de una en una hasta encontrarla
        for (Conexion c = primeraConexion; c != null; c = c.getSiguiente()) {
            if (c.getDestino().getId().equals(idDestino)) return c;
        }
        return null;
    }

    /**
     * Agrega una conexión nueva (la pone de primera en la lista).
     *
     * @param destino la estación vecina
     * @param tiempo  minutos del viaje
     * @return false si ya estaban conectadas (no se agrega dos veces)
     */
    public boolean agregarConexion(Estacion destino, int tiempo) {
        if (buscarConexionHacia(destino.getId()) != null) return false;
        Conexion nueva = new Conexion(this, destino, tiempo);
        nueva.setSiguiente(primeraConexion); // la nueva apunta a la que era primera
        primeraConexion = nueva;             // y ahora la nueva es la primera
        grado++;
        return true;
    }

    /**
     * Borra la conexión con otra estación.
     *
     * @param idDestino código de la estación vecina
     * @return true si la encontró y la borró
     */
    public boolean eliminarConexionHacia(String idDestino) {
        Conexion anterior = null;
        for (Conexion c = primeraConexion; c != null; c = c.getSiguiente()) {
            if (c.getDestino().getId().equals(idDestino)) {
                // "saltamos" la conexión para que la lista ya no pase por ella
                if (anterior == null) primeraConexion = c.getSiguiente();
                else anterior.setSiguiente(c.getSiguiente());
                grado--;
                return true;
            }
            anterior = c;
        }
        return false;
    }

    /** Borra todas las conexiones de la estación (queda aislada). */
    public void vaciarConexiones() {
        primeraConexion = null;
        grado = 0;
    }

    @Override
    public String toString() { return id + " - " + nombre; }
}
