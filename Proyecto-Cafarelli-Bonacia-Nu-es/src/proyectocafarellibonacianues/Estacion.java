/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectocafarellibonacianues;

/**
 *
 * @author VALERIA
 */
//Declaramos las variables que guardan las características de la estación, es decir, la clave, el nombre de la estación, la línea de transporte, donde se ubica y la cantidad de pasajeros máxima que hay disponible.
public class Estacion {
    String id_estacion;
    String nombre;
    String linea;
    String zona;
    int Capacidad_Pasajeros_Hora;
      public Estacion (String id_estacion, String nombre, String linea, String zona, int Capacidad_Pasajeros_Hora){
          this.nombre = nombre;
          this.id_estacion = id_estacion;
          this.Capacidad_Pasajeros_Hora = Capacidad_Pasajeros_Hora;
          this.linea = linea;
          this.zona = zona;    
      } 
           
}

