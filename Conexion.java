package urbanroute;

/**
 * Una conexión es el "camino" entre dos estaciones que están una al lado de
 * la otra, con los minutos que se tarda en ir de una a otra.
 * También sirve como eslabón de la lista de conexiones de cada estación:
 * cada conexión sabe cuál es la siguiente.
 */
public class Conexion {
    private final Estacion origen;
    private final Estacion destino;
    private final int tiempo;
    private Conexion siguiente; // la próxima conexión de la lista (null si es la última)

    /**
     * Crea una conexión.
     *
     * @param origen  estación de donde sale
     * @param destino estación a donde llega
     * @param tiempo  minutos que tarda el viaje
     */
    public Conexion(Estacion origen, Estacion destino, int tiempo) {
        this.origen = origen;
        this.destino = destino;
        this.tiempo = tiempo;
    }

    /** @return la estación de donde sale */
    public Estacion getOrigen() { return origen; }

    /** @return la estación a donde llega */
    public Estacion getDestino() { return destino; }

    /** @return los minutos del viaje */
    public int getTiempo() { return tiempo; }

    /** @return la siguiente conexión de la lista (o null si no hay más) */
    public Conexion getSiguiente() { return siguiente; }

    /** @param siguiente la conexión que va después de esta en la lista */
    public void setSiguiente(Conexion siguiente) { this.siguiente = siguiente; }
}
