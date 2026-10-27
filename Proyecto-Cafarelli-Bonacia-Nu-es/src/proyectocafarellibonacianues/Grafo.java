/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectocafarellibonacianues;

/**
 *
 * @author valeriacafarelli-arch
 */

public class Grafo {

    private TablaHash estaciones = new TablaHash();
    private int numConexiones = 0;
    private boolean modificado = false;

    public void agregarEstacion(Estacion e) {
        if (e == null) {
            throw new IllegalArgumentException("Estación inválida.");
        }
        if (!estaciones.insertar(e.getId(), e)) {
            throw new IllegalArgumentException("Ya existe una estación con ID " + e.getId() + ".");
        }
        modificado = true;
    }

    public Estacion buscarEstacion(String id) {
        return estaciones.buscar(id);
    }

    public void agregarConexion(String idOrigen, String idDestino, int tiempo) {
        if (idOrigen.equals(idDestino)) {
            throw new IllegalArgumentException("Origen y destino son la misma estación.");
        }
        if (tiempo <= 0) {
            throw new IllegalArgumentException("El tiempo debe ser un número positivo.");
        }
        Estacion o = estaciones.buscar(idOrigen);
        Estacion d = estaciones.buscar(idDestino);
        if (o == null) {
            throw new IllegalArgumentException("La estación " + idOrigen + " no existe.");
        }
        if (d == null) {
            throw new IllegalArgumentException("La estación " + idDestino + " no existe.");
        }
        if (o.buscarConexionHacia(idDestino) != null) {
            throw new IllegalArgumentException("Ya existe una conexión entre " + idOrigen + " y " + idDestino + ".");
        }
        o.agregarConexion(d, tiempo);
        d.agregarConexion(o, tiempo);
        numConexiones++;
        modificado = true;
    }

    public boolean eliminarConexion(String idOrigen, String idDestino) {
        Estacion o = estaciones.buscar(idOrigen);
        Estacion d = estaciones.buscar(idDestino);
        if (o == null || d == null || !o.eliminarConexionHacia(idDestino)) {
            return false;
        }
        d.eliminarConexionHacia(idOrigen);
        numConexiones--;
        modificado = true;
        return true;
    }

    public boolean eliminarEstacion(String id) {
        Estacion e = estaciones.buscar(id);
        if (e == null) {
            return false;
        }
        for (Conexion c = e.getPrimeraConexion(); c != null; c = c.getSiguiente()) {
            c.getDestino().eliminarConexionHacia(id);
            numConexiones--;
        }
        e.vaciarConexiones();
        estaciones.eliminar(id);
        modificado = true;
        return true;
    }

    public void eliminarTodasLasConexiones() {
        Estacion[] todas = estaciones.aArreglo();
        for (int i = 0; i < todas.length; i++) {
            todas[i].vaciarConexiones();
        }
        numConexiones = 0;
    }

    public void limpiar() {
        estaciones.vaciar();
        numConexiones = 0;
        modificado = false;
    }

    public Estacion[] getEstaciones() {
        return estaciones.aArreglo();
    }

    public int getNumEstaciones() {
        return estaciones.getTamano();
    }

    public int getNumConexiones() {
        return numConexiones;
    }

    public boolean isModificado() {
        return modificado;
    }

    public void setModificado(boolean modificado) {
        this.modificado = modificado;
    }
}