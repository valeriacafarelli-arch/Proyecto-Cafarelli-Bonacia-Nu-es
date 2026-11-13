/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectocafarellibonacianues;
/**
 *
 * @author PC
 */

public class Grafoo {
    private Vertice cabeza;
    private TablaHashPropia tablaVertices;
    private int totalVertices;

    public Grafoo() {
        this.cabeza = null;
        this.tablaVertices = new TablaHashPropia(101);
        this.totalVertices = 0;
    }

    // Agrega estacion a la lista de vertices y a la tabla hash
    public void agregarEstacion(Estacion estacion) {
        if (estacion == null) {
            throw new IllegalArgumentException("La estacion no puede ser nula");
        }

        String id = (estacion.id_estacion != null) ? estacion.id_estacion : estacion.id_estacion;

        if (tablaVertices.buscar(id) != null) {
            throw new IllegalArgumentException("La estacion ya existe: " + id);
        }

        Vertice nuevo = new Vertice(estacion);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Vertice aux = cabeza;
            while (aux.siguiente != null) {
                aux = aux.siguiente;
            }
            aux.siguiente = nuevo;
        }

        tablaVertices.insertar(id, nuevo);
        totalVertices++;
    }

    // Valida que existan origen y destino antes de crear la conexion
    public void agregarConexion(String idOrigen, String idDestino, int tiempoMinutos) {
        Vertice origen = buscarVertice(idOrigen);
        Vertice destino = buscarVertice(idDestino);

        if (origen == null) {
            throw new IllegalArgumentException("No existe el origen: " + idOrigen);
        }
        if (destino == null) {
            throw new IllegalArgumentException("No existe el destino: " + idDestino);
        }
        if (tiempoMinutos <= 0) {
            throw new IllegalArgumentException("El tiempo debe ser mayor a 0");
        }

        Conexion con = new Conexion(origen, destino, tiempoMinutos);
        origen.adyacentes.agregar(con);
        destino.adyacentes.agregar(con);
    }

    // Busqueda en O(1) usando la tabla interna
    public Vertice buscarVertice(String idEstacion) {
        if (idEstacion == null) {
            return null;
        }
        return tablaVertices.buscar(idEstacion);
    }

    // Dijkstra usando arreglos simples y for tradicional
    public Lista dijkstra(String idOrigen, String idDestino) {
        Vertice vOrigen = buscarVertice(idOrigen);
        Vertice vDestino = buscarVertice(idDestino);

        if (vOrigen == null || vDestino == null) {
            throw new IllegalArgumentException("Origen o destino no encontrados");
        }

        Vertice[] nodos = new Vertice[totalVertices];
        int[] tiempos = new int[totalVertices];
        Vertice[] padres = new Vertice[totalVertices];
        boolean[] visitados = new boolean[totalVertices];

        Vertice aux = cabeza;
        int i = 0;
        int idxOrigen = -1;
        int idxDestino = -1;

        
        while (aux != null) {
            nodos[i] = aux;
            tiempos[i] = Integer.MAX_VALUE;
            padres[i] = null;
            visitados[i] = false;

            if (aux.equals(vOrigen)) {
                idxOrigen = i;
            }
            if (aux.equals(vDestino)) {
                idxDestino = i;
            }

            aux = aux.siguiente;
            i++;
        }

        tiempos[idxOrigen] = 0;

        for (int paso = 0; paso < totalVertices; paso++) {
            int menor = Integer.MAX_VALUE;
            int u = -1;

            // Buscar el nodo con menor tiempo acumulado no visitado
            for (int j = 0; j < totalVertices; j++) {
                if (!visitados[j] && tiempos[j] < menor) {
                    menor = tiempos[j];
                    u = j;
                }
            }

            if (u == -1 || u == idxDestino) {
                break;
            }

            visitados[u] = true;
            Vertice verticeActual = nodos[u];

            // Revisar vecinos
            ListaPropia conexiones = verticeActual.adyacentes;
            for (int k = 0; k < conexiones.getTamano(); k++) {
                Conexion con = (Conexion) conexiones.obtener(k);
                Vertice vecino = con.Estacion_Destino;
                int peso = con.Tiempo_Minutos;

                int v = buscarIndice(nodos, vecino);
                if (v != -1 && !visitados[v]) {
                    if (tiempos[u] != Integer.MAX_VALUE) {
                        int nuevoTiempo = tiempos[u] + peso;
                        if (nuevoTiempo < tiempos[v]) {
                            tiempos[v] = nuevoTiempo;
                            padres[v] = verticeActual;
                        }
                    }
                }
            }
        }

        // Armar el camino hacia atras
        Lista camino = new Lista();
        int actualIdx = idxDestino;

        if (tiempos[actualIdx] != Integer.MAX_VALUE) {
            while (actualIdx != -1) {
                camino.agregarAlInicio(nodos[actualIdx].estacion);
                Vertice padre = padres[actualIdx];
                actualIdx = (padre != null) ? buscarIndice(nodos, padre) : -1;
            }
        }

        return camino;
    }

    private int buscarIndice(Vertice[] arreglo, Vertice vertice) {
        for (int i = 0; i < arreglo.length; i++) {
            if (arreglo[i] != null && arreglo[i].equals(vertice)) {
                return i;
            }
        }
        return -1;
    }

    public Lista obtenerTodasLasEstaciones() {
        Lista lista = new Lista();
        Vertice aux = cabeza;
        while (aux != null) {
            lista.agregar(aux.estacion);
            aux = aux.siguiente;
        }
        return lista;
    }

    public Vertice getCabeza() {
        return cabeza;
    }
    
    public void eliminarVertice(String idEstacion) {
        Vertice verticeAEliminar = buscarVertice(idEstacion);
        if (verticeAEliminar == null) {
            throw new IllegalArgumentException("La estacion no existe: " + idEstacion);
        }

        Vertice aux = cabeza;
        while (aux != null) {
            aux.adyacentes.eliminar(idEstacion);
            aux = aux.siguiente;
        }

        if (cabeza.equals(verticeAEliminar)) {
            cabeza = cabeza.siguiente;
        } else {
            Vertice actual = cabeza;
            while (actual.siguiente != null && !actual.siguiente.equals(verticeAEliminar)) {
                actual = actual.siguiente;
            }
            if (actual.siguiente != null) {
                actual.siguiente = actual.siguiente.siguiente;
            }
        }

        tablaVertices.eliminar(idEstacion);
        totalVertices--;
    }
    
    public String calcularHubs(){
        return "";
    }

    public int getTotalVertices() {
        return totalVertices;
    }
}
