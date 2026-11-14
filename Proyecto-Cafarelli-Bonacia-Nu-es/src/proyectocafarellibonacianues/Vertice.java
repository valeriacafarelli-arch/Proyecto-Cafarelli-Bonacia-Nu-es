/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectocafarellibonacianues;

/**
 *
 * @author VALERIA
 */
//Guarda la información de la estación, un puntero para enlazarse con la siguiente estación, y su propia lista de conexiones
public class Vertice {
    Vertice siguiente;
    Estacion estacion;
    ListaPropia adyacentes;
        public Vertice (Estacion estacion) {
            siguiente = null;
            this.estacion = estacion;
            adyacentes = new ListaPropia();

        }
            
}
