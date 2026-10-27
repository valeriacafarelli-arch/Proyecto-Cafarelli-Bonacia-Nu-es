/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectocafarellibonacianues;

/**
 *
 * @author valeriacafarelli-arch
 */

public class Conexion {
    private Estacion origen;
    private Estacion destino;
    private int tiempo;
    private Conexion siguiente;

    public Conexion(Estacion origen, Estacion destino, int tiempo) {
        this.origen = origen;
        this.destino = destino;
        this.tiempo = tiempo;
    }

    public Estacion getOrigen() {
        return origen;
    }

    public void setOrigen(Estacion origen) {
        this.origen = origen;
    }

    public Estacion getDestino() {
        return destino;
    }

    public void setDestino(Estacion destino) {
        this.destino = destino;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public Conexion getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Conexion siguiente) {
        this.siguiente = siguiente;
    }

}