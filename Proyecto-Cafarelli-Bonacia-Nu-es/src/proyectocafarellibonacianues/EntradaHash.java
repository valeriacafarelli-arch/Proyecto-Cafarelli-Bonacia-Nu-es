/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectocafarellibonacianues;

/**
 *
 * @author VALERIA
 */
    // Nodo para la tabla hash con encadenamiento
    public class EntradaHash {
        String clave;
        Vertice valor;
        EntradaHash siguiente;

        public EntradaHash(String clave, Vertice valor) {
            this.clave = clave;
            this.valor = valor;
            this.siguiente = null;
        }
        
    }
