/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectocafarellibonacianues;

/**
 *
 * @author VALERIA
 */
//Definimos el punto de partida y el punto de llegada utilizando objetos de tipo Vertice que representan las estaciones dentro del grafo. El int Tiempo_Minutos represeta el tiempo que va a tardar en llegar del punto de partida al punto de llegada.
public class Conexion {
    Vertice Estacion_Origen;
    Vertice Estacion_Destino;
    int Tiempo_Minutos;
        public Conexion (Vertice Estacion_Origen, Vertice Estacion_Destino, int Tiempo_Minutos) {
            this.Tiempo_Minutos = Tiempo_Minutos;
            this.Estacion_Destino = Estacion_Destino;
            this.Estacion_Origen = Estacion_Origen;
        }
}
