package sistemasinteligentes;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Trabajo Practico Obligatorio - Arbol de Decision c4.5 (version simplificada)
 * Repetto Francisco FAI-2548 y Rivera Malena FAI-2516
**/

/* ------------------------------
 * DATOS A TENER EN CUENTA: 
/* ------------------------------
 * Hemos definido que la profundidad maxima del arbol sea de 8
 * Se utiliza Gain Ratio = (Información / SplitInfo)
 
 * Con el fin de simplificarlo se utilizó también umbrales fijos en un Map
    - Umbral 0 para los binarios
    - Umbral 2 para el atributo "cuentas piernas"
 
 
 * Hay un método para predecir, otro para entrenar y otro para imprimir
 */
public class ArbolC4_5 {

    
    private Nodo raiz; //la raiz del arbol
    private final Map<String,Integer> indiceAtributo; //guarda los atributos dentro de un Map para dps no tener que buscarlos
    private final String[] atributos; //lista de Atributos (todos menos el atributo objetivo: tipo)
    private final int indiceClase; //Índice de la columna que contiene la clase en la instancia
    private final Map<String,Double> umbrales; //HashMap que guarda los umbrales
    private final int profundidadMaxima = 8; //para guardar la profundidad q puede tener el arbol (se puede cambiar)

    
    
    
    /*---------------- CONSTRUCTOR ----------------*/
    public ArbolC4_5(String[] atributosSinClase, int indiceClase, Map<String,Double> umbrales) {
        this.atributos = atributosSinClase;
        this.indiceClase  = indiceClase;
        this.umbrales  = umbrales;
        this.indiceAtributo  = new HashMap<>();
        for (int i = 0; i < atributosSinClase.length; i++) 
                indiceAtributo.put(atributosSinClase[i], i);
    }

    
    
    /*---------------- METODO PARA ENTRENAR ----------------*/
    
    //Se pedia que el arbol se entrene con un 80% del dataset,
    public void entrenar(List<String[]> filas){ 
        raiz = construir(filas, 0);
    }

    //Metodo para construir recursivamente el arbol, se le pasan los datos (subconjunto)y la profundidadActual
    //devuelve el nodo "raiz" del subarbol resultante
    private Nodo construir(List<String[]> datos, int profundidadActual){
        
        if (datos.isEmpty()) //caso base, no hay datos
            return new Nodo("DESCONOCIDO");
        
        //si todas las instancias comparten la misma clase
        String unica = mismaClase(datos);
        if (unica != null) 
            return new Nodo(unica); //se devuelve la hoja
        
        //si ya nos excedemos de la profundidad maxima
        if (profundidadActual >= profundidadMaxima) 
            return new Nodo(claseMayoritaria(datos)); //devolvemos un nodo con la clase mas comun

        
        //A partir de aca empieza la parte de elegir el atributo con mejor ratio
        String mejorA = null; //se guarda el nombre del mejor atributo, por ahora: ninguno
        double mejorGR = -1; //se guarda el mejor gain ratio, por ahora: -1
        List<String[]> mejorIzquierda = null,  //subconjunto izquierdo, umbral <=
                       mejorDerecha = null;   // subconjunto derecho, umbral <
        
        
        for (String atributo : atributos) {
            double um = umbrales.get(atributo); //obtenemos el umbral de ese atributo
            int indecito = indiceAtributo.get(atributo); //en que columna esta ese atributo en la fila csv?? (porEjemplo: cuantas piernas --> columna 12)
            
            //cada una d estas listas es para poner los datos segun la particion
            List<String[]> izq = new ArrayList<>(), 
                           der = new ArrayList<>();
            
            for (String[] datito:datos){ 
                double v = Double.parseDouble(datito[indecito]); 
                if(v<=um) //comparamos segun el umbral y evaluamo
                    izq.add(datito); 
                else 
                    der.add(datito);
            }
            
            if(izq.isEmpty()||der.isEmpty()) //si una particion queda vacia, el slipInfo no es valido
                continue; //por lo tanto se sigue con el otro datito

            //aca se hacen las comparaciones, nos quedamos con el mejor ratio
            //el q tenga mejor ratio se lo guarda como atributo.
            double gr = gainRatio(datos, izq, der);
            if(gr>mejorGR){
                mejorGR=gr;
                mejorA=atributo;
                mejorIzquierda=izq; 
                mejorDerecha=der; 
            }
        }
        
        //si no encontró un mejor atributo (todos tenian un slipt invalido)
        if (mejorA==null) return new Nodo(claseMayoritaria(datos)); //se devuelve una hoja

        
        //pero si se encontro se crea un nodo interno
        Nodo n = new Nodo(mejorA, umbrales.get(mejorA)); //se crea nuevo nodo con nombre, y umbral
        
        //se hace recursivo y se suma en uno la profundida
        n.izq = construir(mejorIzquierda, profundidadActual+1);
        n.der = construir(mejorDerecha, profundidadActual+1);
        return n;
    }
    
    //Devuelve la clase si todas las instancias la comparten
    private String mismaClase(List<String[]> datito){
        String c=datito.get(0)[indiceClase]; 
        for(int i=1;i<datito.size();i++) 
            if(!c.equals(datito.get(i)[indiceClase])){
                c=null;
                break; //para salir del for
            }
        return c;
    }
    
    private String claseMayoritaria(List<String[]> d){
        Map<String,Integer> conteo =new HashMap<>(); 
        for(String[] datito:d){
            String clasecita= datito[indiceClase];
            conteo.put(clasecita,conteo.getOrDefault(clasecita,0)+1); //si no hay ningun conteo de esa clase ponemos eso pra q tire 0 y no null
        }
        //devuelve la clase con mayor cantidad de conteo
    return Collections.max(conteo.entrySet(),Map.Entry.comparingByValue()).getKey(); 
    }
    
     
    //
    private double entropia(List<String[]> d) {
        Map<String,Integer> conteo = new HashMap<>();
        for (String[] datito : d) {
            String clasecita = datito[indiceClase];
            conteo.put(clasecita, conteo.getOrDefault(clasecita,0) + 1);
        }
        double total = d.size();
        double entropia = 0;
        for (int n : conteo.values()) {
            double p = n / total;
            entropia -= p * Math.log(p) / Math.log(2);
        }
        return entropia;
    }
    
     
     // ----- Gain Ratio = InformationGanada / SplitInfo
    private double gainRatio(List<String[]> tot,List<String[]> izq,List<String[]> der){
        double gr;
        double gain = informacionGanada(tot, izq, der);
        double splitInfo = splitInfo(izq.size(), der.size(), tot.size());
        if(splitInfo==0) // evita división por cero
            gr=0;
        gr= gain / splitInfo;
        
        return gr;
    }

    private double informacionGanada(List<String[]> tot,List<String[]> izq,List<String[]> der){
        double eTot=entropia(tot);
        double p=(double)izq.size()/tot.size();
        return eTot - (p*entropia(izq)+(1-p)*entropia(der));
    }

    private double splitInfo(int nIzq,int nDer,int total){
        double pIzq=(double)nIzq/total, pDer=(double)nDer/total;
        double si=0;
        if(pIzq>0) si -= pIzq*Math.log(pIzq)/Math.log(2);
        if(pDer>0) si -= pDer*Math.log(pDer)/Math.log(2);
        return si;
    }


    
    
    
/*TERMINAMOS DE CONSTRUIR EL ARBOL*/

/* AHORA PASAMOS A LA PARTE DE PREDICCION PARA EL 20% RESTANTE DEL DATASET*/
   
    public String predecir(String[] fila) { 
        Nodo a = raiz;
        //descendemos hasta llegar a una hoja
        while(!a.esHoja()){
            //buscamos la columna del atributo del nodo actual
            double v = Double.parseDouble(fila[indiceAtributo.get(a.atributo)]); //se pasa a double
            a = (v<=a.umbral)? a.izq : a.der; //Elegimos la rama segun el umbral
        }
        return a.clase;
    }


    
    
    
   
    /*---------------- IMPRESION RECURSIVA----------------*/
    public void imprimir() { 
        imprimirRec(raiz, ""); 
    }
    
    private void imprimirRec(Nodo n, String pref){
        if(n==null) 
            System.out.println("Arbol terminado");; //termina el algoritmo
        
        if(n.esHoja()){
            System.out.println(pref + "→ [Clase: " + n.clase + "]");
        }else{
            System.out.println(pref + "["+n.atributo+" ≤ " + n.umbral + "]");
            imprimirRec(n.izq, pref + "  ├─ ");
            imprimirRec(n.der, pref + "  └─ ");
        }
    }

  
}

