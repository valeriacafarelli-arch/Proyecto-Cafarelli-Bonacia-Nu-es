/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectocafarellibonacianues;

/**
 *
 * @author VALERIA
 */

      // Nodo basico para la lista enlazada
    public class NodoLista {
        public Conexion dato;
        public NodoLista siguiente;

        public NodoLista(Conexion dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

