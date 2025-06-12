/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemasinteligentes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author male_
 */
public class TestC4_5 {
        public static void main(String[] args) throws IOException {
             File csv = new File("C:\\Users\\male_\\OneDrive\\Documentos\\NetBeansProjects\\JavaApplication1\\src\\sistemasInteligentes\\src\\sistemasinteligentes\\Zoo_Dataset (4).csv");
             if(!csv.exists()) //si no existe el csv tira error
                 throw new IOException("CSV no encontrado: ");
           
             //en el caso de que si exista pasamos a cargar el csv
            List<String[]> filas = new ArrayList<>(); 
               String[] cab;
                 try(Scanner sc =new Scanner(csv)){
                     cab=sc.nextLine().split(","); 
                     while(sc.hasNextLine()) 
                         filas.add(sc.nextLine().split(",")); 
                 }
            Collections.shuffle(filas, new Random(42));
            int indiceClase = cab.length-1; 
            int corte=(int)(filas.size()*0.8);
            List<String[]> train = filas.subList(0,corte); 
            List<String[]> test = filas.subList(corte, filas.size());
            
            ArbolC4_5 modelo = new ArbolC4_5(Arrays.copyOf(cab, cab.length-1), indiceClase, cargarUmbrales());
              modelo.entrenar(train);
            int ok=0; 
            for(String[] f:test) 
                if(modelo.predecir(f).equals(f[indiceClase])) 
                    ok++; double acc=100.0*ok/test.size();
                    
        System.out.printf("Instancias test: %d  |  Aciertos: %d  |  Precisión: %.2f %%\n", test.size(), ok, acc);
        System.out.println("\nÁrbol generado:\n----------------");
        modelo.imprimir();
     }
        
        private static Map<String,Double> cargarUmbrales(){
        Map<String,Double> umbralcito=new HashMap<>();
        
        umbralcito.put("Tiene pelo",0.0); 
        umbralcito.put("Tiene plumas",0.0); 
        umbralcito.put("Pone huevos",0.0); 
        umbralcito.put("Produce leche",0.0);
        umbralcito.put("Puede volar",0.0); 
        umbralcito.put("Vive en el agua",0.0); 
        umbralcito.put("Es depredador",0.0); 
        umbralcito.put("Tiene dientes",0.0);
        umbralcito.put("Es vertebrado",0.0); 
        umbralcito.put("Respira aire",0.0); 
        umbralcito.put("Es venenoso",0.0); 
        umbralcito.put("Tiene aletas",0.0);
        umbralcito.put("Cuantas piernas",2.0); 
        umbralcito.put("Tiene cola",0.0); 
        umbralcito.put("Es domestico",0.0); 
        umbralcito.put("Tiene tamanio parecido a un gato",0.0);
        
        return umbralcito;
    }
   }

