/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemasinteligentes;

/**
 *
 * @author male_
 */
public class Nodo {
     String atributo;  // nombre del atributo (null si es hoja)
        double umbral;  // corte (viene por defecto)
        Nodo izq, der;   // ramas
        String clase;          // clase predicha si es hoja

        Nodo(String atributo, double umbral) { 
            this.atributo = atributo; 
            this.umbral = umbral; 
        }
        
        Nodo(String clase){
            this.clase = clase; 
        }
        
        boolean esHoja(){
            return clase != null;
        }
    }

