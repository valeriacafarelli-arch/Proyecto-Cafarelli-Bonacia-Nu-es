/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectocafarellibonacianues;

/**
 *
 * @author VALERIA
 */
    
    // Lista enlazada simple para no usar ArrayList ni LinkedList
    public class ListaPropia {
        private NodoLista primero;
        private int tamano;

        public ListaPropia() {
            this.primero = null;
            this.tamano = 0;
        }



        public void agregar(Object dato) {
            NodoLista nuevo = new NodoLista(dato);
            if (primero == null) {
                primero = nuevo;
            } else {
                NodoLista aux = primero;
                while (aux.siguiente != null) {
                    aux = aux.siguiente;
                }
                aux.siguiente = nuevo;
            }
            tamano++;
        }

        public void agregarAlInicio(Object dato) {
            NodoLista nuevo = new NodoLista(dato);
            nuevo.siguiente = primero;
            primero = nuevo;
            tamano++;
        }

        public Object obtener(int indice) {
            if (indice < 0 || indice >= tamano) {
                throw new IndexOutOfBoundsException("Indice fuera de rango");
            }
            NodoLista actual = primero;
            for (int i = 0; i < indice; i++) {
                actual = actual.siguiente;
            }
            return actual.dato;
        }

        public int getTamano() {
            return tamano;
        }
    }
