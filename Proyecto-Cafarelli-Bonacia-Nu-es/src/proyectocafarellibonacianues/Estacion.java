/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectocafarellibonacianues;

/**
 *
 * @author VALERIA
 */
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

