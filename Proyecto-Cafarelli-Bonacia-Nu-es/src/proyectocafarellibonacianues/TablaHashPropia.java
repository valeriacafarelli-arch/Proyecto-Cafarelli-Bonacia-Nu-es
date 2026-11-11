/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectocafarellibonacianues;

/**
 *
 * @author VALERIA
 */
    // Tabla hash interna del grafo para buscar estaciones por ID en O(1)
    public  class TablaHashPropia {
        public EntradaHash[] cubetas;
        public int capacidad;

        public TablaHashPropia(int capacidad) {
            this.capacidad = capacidad;
            this.cubetas = new EntradaHash[capacidad];
        }

        // Funcion hash basica usando numero primo
        private int hash(String clave) {
            int h = 0;
            char[] chars = clave.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                h = (31 * h + chars[i]) % capacidad;
            }
            if (h < 0) {
                h += capacidad;
            }
            return h;
        }
        
        public void insertar(String clave, Vertice valor) {
            int pos = hash(clave);
            EntradaHash actual = cubetas[pos];
            while (actual != null) {
                if (actual.clave.equals(clave)) {
                    actual.valor = valor;
                    return;
                }
                actual = actual.siguiente;
            }
            EntradaHash nuevo = new EntradaHash(clave, valor);
            nuevo.siguiente = cubetas[pos];
            cubetas[pos] = nuevo;
        }

        public Vertice buscar(String clave) {
            int pos = hash(clave);
            EntradaHash actual = cubetas[pos];
            while (actual != null) {
                if (actual.clave.equals(clave)) {
                    return actual.valor;
                }
                actual = actual.siguiente;
            }
            return null;
        }
    }


