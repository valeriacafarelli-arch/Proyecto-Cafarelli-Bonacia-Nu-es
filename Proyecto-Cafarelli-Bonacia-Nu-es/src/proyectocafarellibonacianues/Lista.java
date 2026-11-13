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
    public class Lista {
        private Nodo primero;
        private int tamano;

        public Lista() {
            this.primero = null;
            this.tamano = 0;
        }



        public void agregar(Estacion dato) {
            Nodo nuevo = new Nodo(dato);
            if (primero == null) {
                primero = nuevo;
            } else {
                Nodo aux = primero;
                while (aux.siguiente != null) {
                    aux = aux.siguiente;
                }
                aux.siguiente = nuevo;
            }
            tamano++;
        }

        public void agregarAlInicio(Estacion dato) {
            Nodo nuevo = new Nodo(dato);
            nuevo.siguiente = primero;
            primero = nuevo;
            tamano++;
        }

        public Object obtener(int indice) {
            if (indice < 0 || indice >= tamano) {
                throw new IndexOutOfBoundsException("Indice fuera de rango");
            }
            Nodo actual = primero;
            for (int i = 0; i < indice; i++) {
                actual = actual.siguiente;
            }
            return actual.dato;
        }

        public int getTamano() {
            return tamano;
        }
        
        public void eliminar(String id){
            if(primero != null){
                Nodo aux = primero;
                if(aux.dato.id_estacion.equals(id)){
                    primero = primero.siguiente;
                }else{
                    while(aux.siguiente != null && !aux.siguiente.dato.id_estacion.equals(id)){
                        aux = aux.siguiente;
                    }
                    if(aux.siguiente != null){
                        aux.siguiente = aux.siguiente.siguiente;
                    }
                }
            }
        }
    }
