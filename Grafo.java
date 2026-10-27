package urbanroute;

/**
 * El grafo es la red completa del metro: las estaciones y cómo se conectan.
 * Cada estación guarda su propia lista de vecinas (lista de adyacencia).
 * Para guardar y buscar estaciones, el grafo le pide ayuda a la TablaHash.
 * Las conexiones van en los dos sentidos: si A conecta con B, B conecta con A.
 */
public class Grafo {
    private final TablaHash estaciones = new TablaHash(); // aquí viven todas las estaciones
    private int numConexiones;
    private boolean modificado; // true si hay cambios que todavía no se guardan en el archivo

    /**
     * Agrega una estación nueva a la red.
     *
     * @param e la estación
     * @throws IllegalArgumentException si es nula o ya existe una con ese código
     */
    public void agregarEstacion(Estacion e) {
        if (e == null) throw new IllegalArgumentException("Estación inválida.");
        if (!estaciones.insertar(e.getId(), e)) {
            throw new IllegalArgumentException("Ya existe una estación con ID " + e.getId() + ".");
        }
        modificado = true;
    }

    /**
     * Busca una estación por su código (rápido, gracias a la tabla hash).
     *
     * @param id el código de la estación
     * @return la estación o null si no existe
     */
    public Estacion buscarEstacion(String id) { return estaciones.buscar(id); }

    /**
     * Conecta dos estaciones que ya existen. La conexión se guarda en las
     * dos estaciones, porque se puede viajar en ambos sentidos.
     *
     * @param idOrigen  código de una estación
     * @param idDestino código de la otra
     * @param tiempo    minutos del viaje (tiene que ser mayor que 0)
     * @throws IllegalArgumentException si algún dato está mal o ya estaban conectadas
     */
    public void agregarConexion(String idOrigen, String idDestino, int tiempo) {
        // primero revisamos que todo esté bien, y si no, avisamos con un mensaje
        if (idOrigen.equals(idDestino)) throw new IllegalArgumentException("Origen y destino son la misma estación.");
        if (tiempo <= 0) throw new IllegalArgumentException("El tiempo debe ser un número positivo.");
        Estacion o = estaciones.buscar(idOrigen);
        Estacion d = estaciones.buscar(idDestino);
        if (o == null) throw new IllegalArgumentException("La estación " + idOrigen + " no existe.");
        if (d == null) throw new IllegalArgumentException("La estación " + idDestino + " no existe.");
        if (o.buscarConexionHacia(idDestino) != null) {
            throw new IllegalArgumentException("Ya existe una conexión entre " + idOrigen + " y " + idDestino + ".");
        }
        o.agregarConexion(d, tiempo);
        d.agregarConexion(o, tiempo);
        numConexiones++;
        modificado = true;
    }

    /**
     * Quita la conexión entre dos estaciones (las estaciones se quedan).
     *
     * @param idOrigen  código de una estación
     * @param idDestino código de la otra
     * @return true si existía y se borró
     */
    public boolean eliminarConexion(String idOrigen, String idDestino) {
        Estacion o = estaciones.buscar(idOrigen);
        Estacion d = estaciones.buscar(idDestino);
        if (o == null || d == null || !o.eliminarConexionHacia(idDestino)) return false;
        d.eliminarConexionHacia(idOrigen); // también se borra del otro lado
        numConexiones--;
        modificado = true;
        return true;
    }

    /**
     * Cierra una estación: la borra junto con todas sus conexiones, tanto
     * del grafo como de la tabla hash.
     *
     * @param id código de la estación
     * @return true si existía y se borró
     */
    public boolean eliminarEstacion(String id) {
        Estacion e = estaciones.buscar(id);
        if (e == null) return false;
        // primero la quitamos de la lista de cada vecina...
        for (Conexion c = e.getPrimeraConexion(); c != null; c = c.getSiguiente()) {
            c.getDestino().eliminarConexionHacia(id);
            numConexiones--;
        }
        // ...y después la sacamos de la tabla
        e.vaciarConexiones();
        estaciones.eliminar(id);
        modificado = true;
        return true;
    }

    /** Borra todas las conexiones pero deja las estaciones. */
    public void eliminarTodasLasConexiones() {
        Estacion[] todas = estaciones.aArreglo();
        for (int i = 0; i < todas.length; i++) todas[i].vaciarConexiones();
        numConexiones = 0;
    }

    /** Deja el grafo completamente vacío (se usa antes de cargar otro archivo). */
    public void limpiar() {
        estaciones.vaciar();
        numConexiones = 0;
        modificado = false;
    }

    /** @return arreglo con todas las estaciones */
    public Estacion[] getEstaciones() { return estaciones.aArreglo(); }
    /** @return cuántas estaciones hay */
    public int getNumEstaciones() { return estaciones.getTamano(); }
    /** @return cuántas conexiones hay (cada tramo cuenta una sola vez) */
    public int getNumConexiones() { return numConexiones; }
    /** @return true si hay cambios sin guardar en el archivo */
    public boolean isModificado() { return modificado; }
    /** @param modificado true si hay cambios pendientes de guardar */
    public void setModificado(boolean modificado) { this.modificado = modificado; }
}
