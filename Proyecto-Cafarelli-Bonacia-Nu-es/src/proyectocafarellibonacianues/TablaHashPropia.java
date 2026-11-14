/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectocafarellibonacianues;

/**
 *
 * @author alanabonacia-lgtm
 */

    public  class TablaHashPropia {
        public EntradaHash[] estaciones;
        public int capacidad;

        public TablaHashPropia(int capacidad) {
            this.capacidad = capacidad;
            this.estaciones = new EntradaHash[capacidad];
        }
//Toma el texto del ID de la estación, calcula su código hash de Java, lo vuelve positivo en elcaso de que de negativo y le aplica un módulo (% capacidad) para asegurar que encaje exactamente en una posición válida del arreglo
//Se utiliza el 31 como factor multiplicador para combinar los caracteres. Se elige el 31 porque es un número primo impar y tiene una propiedad matemática,evitando Al ser un número primo, ayuda a que los resultados de la función hash se distribuyan de manera mucho más uniforme a lo largo de la tabla, evitando que distintas palabras generen el mismo código
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
    //Si dos IDs diferentes caen por casualidad en el mismo número de casillero (colisión), no se sobreescriben: se enlazan uno detrás de otro utilizando el nodo EntradaHash    
        public void insertar(String clave, Vertice valor) {
            int pos = hash(clave);
            EntradaHash actual = estaciones[pos];
            while (actual != null) {
                if (actual.clave.equals(clave)) {
                    actual.valor = valor;
                    return;
                }
                actual = actual.siguiente;
            }
            EntradaHash nuevo = new EntradaHash(clave, valor);
            nuevo.siguiente = estaciones[pos];
            estaciones[pos] = nuevo;
        }

        public Vertice buscar(String clave) {
            int pos = hash(clave);
            EntradaHash actual = estaciones[pos];
            while (actual != null) {
                if (actual.clave.equals(clave)) {
                    return actual.valor;
                }
                actual = actual.siguiente;
            }
            return null;
        }
        public void eliminar(String clave){
            int pos = hash (clave);
            EntradaHash actual = estaciones[pos];
            if (actual.clave.equals(clave)) {
                    estaciones[pos] = actual.siguiente;
                }
            else {
                while (!actual.siguiente.clave.equals(clave)){
                    actual = actual.siguiente;
                }
                actual.siguiente = actual.siguiente.siguiente;
            }
        }
    }


