/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tetris;

import java.io.Serializable;

public class puntos implements Serializable{
    
    private int edad;
    private String nombre;
    
    public puntos(int edad, String nombre){
        this.edad= edad;
        this.nombre=nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void mostrarDatos(){
        System.out.println("Nombre: "+ nombre);
        System.err.println("Edad: "+ edad);
    }
}
