package urbanroute;

/**
 * Nuestra tabla hash. Sirve para encontrar una estación rapidísimo usando
 * su código (ej: "PZA_VZLA") sin tener que recorrer toda la lista.
 *
 * Cómo funciona: tenemos un arreglo de "cajitas". A cada código le hacemos
 * una cuenta matemática (la función hash) que nos dice en cuál cajita va.
 * Si dos códigos caen en la misma cajita (colisión), se guardan uno detrás
 * del otro en una lista enlazada. Cuando se llena demasiado, la tabla se
 * agranda sola para que siga siendo rápida.
 */
public class TablaHash {

    /** Un eslabón de la lista que vive dentro de cada cajita. */
    private static class Nodo {
        final String clave;      // el código de la estación
        final Estacion valor;    // la estación en sí
        Nodo siguiente;          // el siguiente que cayó en la misma cajita

        Nodo(String clave, Estacion valor, Nodo siguiente) {
            this.clave = clave;
            this.valor = valor;
            this.siguiente = siguiente;
        }
    }

    private static final int CAPACIDAD_INICIAL = 31;         // cajitas al empezar
    private static final double FACTOR_CARGA_MAXIMO = 0.75;  // si se llena más de 75%, se agranda

    private Nodo[] cubetas; // las cajitas
    private int tamano;     // cuántas estaciones hay guardadas

    /** Crea una tabla vacía. */
    public TablaHash() {
        cubetas = new Nodo[CAPACIDAD_INICIAL];
    }

    /**
     * La cuenta que convierte un código en el número de una cajita.
     * Va letra por letra multiplicando por 31 y sumando, y al final se
     * saca el resto para que el número quede dentro del arreglo.
     *
     * @param clave     el código a convertir
     * @param capacidad cuántas cajitas hay
     * @return el número de cajita (entre 0 y capacidad-1)
     */
    private int indice(String clave, int capacidad) {
        int h = 0;
        for (int i = 0; i < clave.length(); i++) {
            h = 31 * h + clave.charAt(i);
        }
        return (h & 0x7FFFFFFF) % capacidad; // el & evita números negativos
    }

    /**
     * Guarda una estación en la tabla.
     *
     * @param clave el código de la estación
     * @param valor la estación
     * @return false si ya había una con ese código
     */
    public boolean insertar(String clave, Estacion valor) {
        if (buscar(clave) != null) return false;
        if (tamano + 1 > cubetas.length * FACTOR_CARGA_MAXIMO) redimensionar();
        int i = indice(clave, cubetas.length);
        cubetas[i] = new Nodo(clave, valor, cubetas[i]); // se pone de primera en su cajita
        tamano++;
        return true;
    }

    /**
     * Busca una estación por su código.
     *
     * @param clave el código a buscar
     * @return la estación, o null si no existe
     */
    public Estacion buscar(String clave) {
        if (clave == null) return null;
        // vamos directo a la cajita y solo revisamos lo que hay ahí
        for (Nodo n = cubetas[indice(clave, cubetas.length)]; n != null; n = n.siguiente) {
            if (n.clave.equals(clave)) return n.valor;
        }
        return null;
    }

    /**
     * @param clave el código a revisar
     * @return true si ese código está en la tabla
     */
    public boolean contiene(String clave) { return buscar(clave) != null; }

    /**
     * Saca una estación de la tabla.
     *
     * @param clave el código de la estación
     * @return true si estaba y se borró
     */
    public boolean eliminar(String clave) {
        if (clave == null) return false;
        int i = indice(clave, cubetas.length);
        Nodo anterior = null;
        for (Nodo n = cubetas[i]; n != null; n = n.siguiente) {
            if (n.clave.equals(clave)) {
                // saltamos el nodo para que la lista ya no lo incluya
                if (anterior == null) cubetas[i] = n.siguiente;
                else anterior.siguiente = n.siguiente;
                tamano--;
                return true;
            }
            anterior = n;
        }
        return false;
    }

    /** Borra todo y deja la tabla como nueva. */
    public void vaciar() {
        cubetas = new Nodo[CAPACIDAD_INICIAL];
        tamano = 0;
    }

    /** @return cuántas estaciones hay guardadas */
    public int getTamano() { return tamano; }

    /**
     * Da todas las estaciones en un arreglo (sirve para recorrerlas todas,
     * por ejemplo al dibujar el grafo o al guardar el archivo).
     *
     * @return arreglo con todas las estaciones (sin un orden en particular)
     */
    public Estacion[] aArreglo() {
        Estacion[] resultado = new Estacion[tamano];
        int k = 0;
        for (int i = 0; i < cubetas.length; i++) {
            for (Nodo n = cubetas[i]; n != null; n = n.siguiente) {
                resultado[k++] = n.valor;
            }
        }
        return resultado;
    }

    /**
     * Hace la tabla el doble de grande y reacomoda todo, porque al cambiar
     * el número de cajitas cada código puede caer en una cajita distinta.
     */
    private void redimensionar() {
        Nodo[] nuevas = new Nodo[cubetas.length * 2 + 1];
        for (int i = 0; i < cubetas.length; i++) {
            Nodo n = cubetas[i];
            while (n != null) {
                Nodo sig = n.siguiente; // guardamos el siguiente antes de mover este
                int j = indice(n.clave, nuevas.length);
                n.siguiente = nuevas[j];
                nuevas[j] = n;
                n = sig;
            }
        }
        cubetas = nuevas;
    }
}
