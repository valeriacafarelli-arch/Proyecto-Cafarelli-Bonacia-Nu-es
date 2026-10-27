/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectocafarellibonacianues;

/**
 *
 * @author valeriacafarelli-arch
 */
public class Estacion {
    private String id;
    private String nombre;
    private String linea;
    private String zona;
    private int capacidad;
    private Conexion primeraConexion; 
    private int grado; 

    public Estacion(String id, String nombre, String linea, String zona, int capacidad) {
        this.id = id;
        this.nombre = nombre;
        this.linea = linea;
        this.zona = zona;
        this.capacidad = capacidad;
    }

    public Estacion(String id) {
        this(id, id, "Sin línea", "Sin zona", 0);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public Conexion getPrimeraConexion() {
        return primeraConexion;
    }

    public void setPrimeraConexion(Conexion primeraConexion) {
        this.primeraConexion = primeraConexion;
    }

    public int getGrado() {
        return grado;
    }

    public void setGrado(int grado) {
        this.grado = grado;
    }


    public Conexion buscarConexionHacia(String idDestino) {
        for (Conexion c = primeraConexion; c != null; c = c.getSiguiente()) {
            if (c.getDestino().getId().equals(idDestino)) return c;
        }
        return null;
    }

    public boolean agregarConexion(Estacion destino, int tiempo) {
        if (buscarConexionHacia(destino.getId()) != null) return false;
        Conexion nueva = new Conexion(this, destino, tiempo);
        nueva.setSiguiente(primeraConexion); 
        primeraConexion = nueva;             
        grado++;
        return true;
    }

    public boolean eliminarConexionHacia(String idDestino) {
        Conexion anterior = null;
        for (Conexion c = primeraConexion; c != null; c = c.getSiguiente()) {
            if (c.getDestino().getId().equals(idDestino)) {
                if (anterior == null) primeraConexion = c.getSiguiente();
                else anterior.setSiguiente(c.getSiguiente());
                grado--;
                return true;
            }
            anterior = c;
        }
        return false;
    }

    public void vaciarConexiones() {
        primeraConexion = null;
        grado = 0;
    }

    @Override
    public String toString() { return id + " - " + nombre; }
}