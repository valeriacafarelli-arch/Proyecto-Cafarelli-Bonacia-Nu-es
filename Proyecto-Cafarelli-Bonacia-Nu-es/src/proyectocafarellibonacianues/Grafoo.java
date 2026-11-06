/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectocafarellibonacianues;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 *
 * @author PC
 */
public class Grafoo {
    
}
package proyectocafarellibonacianues;

import java.util.*;

public class grafo {
    private Vertice cabeza; 

    public grafo() {
        this.cabeza = null;
    }

    public void agregarEstacion(Estacion estacion) {
        Vertice nuevoVertice = new Vertice(estacion);
        if (cabeza == null) {
            cabeza = nuevoVertice;
        } else {
            Vertice actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevoVertice;
        }
    }


    public Vertice buscarVertice(String idEstacion) {
        Vertice actual = cabeza;
        while (actual != null) {
            if (actual.estacion.id_estacion.equals(idEstacion)) {
                return actual;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    public List<Estacion> dijkstra(String idOrigen, String idDestino) {
        Vertice verticeOrigen = buscarVertice(idOrigen);
        Vertice verticeDestino = buscarVertice(idDestino);

        if (verticeOrigen == null || verticeDestino == null) {
            System.out.println("Estación de origen o destino no encontrada.");
            return new ArrayList<>();
        }

        Map<Vertice, Integer> tiemposMinimos = new HashMap<>();
        Map<Vertice, Vertice> predecesores = new HashMap<>();
        

        PriorityQueue<NodoDistancia> cola = new PriorityQueue<>(Comparator.comparingInt(n -> n.tiempoAcumulado));

        Vertice actual = cabeza;
        while (actual != null) {
            tiemposMinimos.put(actual, Integer.MAX_VALUE);
            actual = actual.siguiente;
        }

        tiemposMinimos.put(verticeOrigen, 0);
        cola.add(new NodoDistancia(verticeOrigen, 0));

        Set<Vertice> visitados = new HashSet<>();

        while (!cola.isEmpty()) {
            NodoDistancia nodoActual = cola.poll();
            Vertice u = nodoActual.vertice;

            if (visitados.contains(u)) continue;
            visitados.add(u);

            if (u.equals(verticeDestino)) break;

            List<Conexion> conexionesSalientes = obtenerConexionesDe(u);

            for (Conexion conexion : conexionesSalientes) {
                Vertice v = conexion.Estacion_Destino;
                int pesoTiempo = conexion.Tiempo_Minutos;

                if (!visitados.contains(v)) {
                    int nuevoTiempo = tiemposMinimos.get(u) + pesoTiempo;

                    if (nuevoTiempo < tiemposMinimos.get(v)) {
                        tiemposMinimos.put(v, nuevoTiempo);
                        predecesores.put(v, u);
                        cola.add(new NodoDistancia(v, nuevoTiempo));
                    }
                }
            }
        }

        LinkedList<Estacion> camino = new LinkedList<>();
        Vertice paso = verticeDestino;
        
        if (predecesores.containsKey(paso) || paso.equals(verticeOrigen)) {
            while (paso != null) {
                camino.addFirst(paso.estacion);
                paso = predecesores.get(paso);
            }
        }

        return camino;
    }


    private List<Conexion> obtenerConexionesDe(Vertice vertice) {
        List<Conexion> resultado = new ArrayList<>();
        return resultado;
    }
    private static class NodoDistancia {
        Vertice vertice;
        int tiempoAcumulado;

        public NodoDistancia(Vertice vertice, int tiempoAcumulado) {
            this.vertice = vertice;
            this.tiempoAcumulado = tiempoAcumulado;
        }
    }
}