/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectocafarellibonacianues;

/**
 *
 * @author valeriacafarelli-arch
 */

public class TablaHash {

    private class Nodo {
        String clave;
        Estacion valor;
        Nodo siguiente;

        Nodo(String clave, Estacion valor, Nodo siguiente) {
            this.clave = clave;
            this.valor = valor;
            this.siguiente = siguiente;
        }
    }

    private Nodo[] cubetas;
    private int tamano;

    public TablaHash() {
        cubetas = new Nodo[31];
        tamano = 0;
    }

    private int indice(String clave, int capacidad) {
        int hash = Math.abs(clave.hashCode());
        return hash % capacidad;
    }

    public boolean insertar(String clave, Estacion valor) {
        if (buscar(clave) != null) {
            return false;
        }
        if (tamano + 1 > cubetas.length * 0.75) {
            redimensionar();
        }
        int i = indice(clave, cubetas.length);
        cubetas[i] = new Nodo(clave, valor, cubetas[i]);
        tamano++;
        return true;
    }

    public Estacion buscar(String clave) {
        if (clave == null) {
            return null;
        }
        int i = indice(clave, cubetas.length);
        Nodo actual = cubetas[i];
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                return actual.valor;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    public boolean contiene(String clave) {
        return buscar(clave) != null;
    }

    public boolean eliminar(String clave) {
        if (clave == null) {
            return false;
        }
        int i = indice(clave, cubetas.length);
        Nodo actual = cubetas[i];
        Nodo anterior = null;
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                if (anterior == null) {
                    cubetas[i] = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }
                tamano--;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return false;
    }

    public void vaciar() {
        cubetas = new Nodo[31];
        tamano = 0;
    }

    public int getTamano() {
        return tamano;
    }

    public Estacion[] aArreglo() {
        Estacion[] resultado = new Estacion[tamano];
        int k = 0;
        for (int i = 0; i < cubetas.length; i++) {
            Nodo actual = cubetas[i];
            while (actual != null) {
                resultado[k] = actual.valor;
                k++;
                actual = actual.siguiente;
            }
        }
        return resultado;
    }

    private void redimensionar() {
        Nodo[] nuevas = new Nodo[cubetas.length * 2 + 1];
        for (int i = 0; i < cubetas.length; i++) {
            Nodo actual = cubetas[i];
            while (actual != null) {
                Nodo siguiente = actual.siguiente;
                int j = indice(actual.clave, nuevas.length);
                actual.siguiente = nuevas[j];
                nuevas[j] = actual;
                actual = siguiente;
            }
        }
        cubetas = nuevas;
    }
}