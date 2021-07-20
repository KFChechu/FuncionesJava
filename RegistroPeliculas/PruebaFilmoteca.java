package paqueteP;
import java.io.*;

public class PruebaFilmoteca extends Object{

	public PruebaFilmoteca(){
		try{
			String FicheroActores="F:/Informatica UPM/Curso 2016-2017/Workspace/TerceraPracticaFicheros/src/paqueteP/Actores.txt";
			String FicheroPeliculas="F:/Informatica UPM/Curso 2016-2017/Workspace/TerceraPracticaFicheros/src/paqueteP/Peliculas.txt";
		
			Actores Act= new Actores(1,"Jhon", "England");
			Peliculas Pel = new Peliculas(1, "Terminator", "Accion", 1990);
			Peliculas_Actores Pel_Act = new Peliculas_Actores(1,1,1);
			
			GestorTabla TablaActores= new GestorTabla("Actores",1,Act);
			GestorTabla TablaPeliculas= new GestorTabla("Peliculas",1,Pel);
			GestorTabla TablaPeliculas_Actores= new GestorTabla("TablaPeliculas_Actores",1,Pel_Act);
			
			
			
			leeFicheroActores(TablaActores,FicheroActores);
			leeFicheroPeliculas(TablaPeliculas,TablaPeliculas_Actores,FicheroPeliculas);
			System.out.println("Lectura de ficheros completada\n");
			
			TablaPeliculas.listar(TablaActores, TablaPeliculas_Actores);
			System.out.println("Listado de TablaPeliculas completado\n");
			
			TablaActores.getArchivoDatos().volcar();
			System.out.println("Volcado de TablaActores(datos) completado\n");
			
			TablaActores.getArbolB().volcar();
			System.out.println("Volcado de TablaActores(indice) completado\n");

		}catch (Exception e){
			System.out.println("Error de entrada/salida sobre PruebaActores: "+e.getMessage());
			e.printStackTrace();
		}
		
	}
	
    static int leeFicheroActores(GestorTabla gestorA, String fichero){
    	int numeroActores=0;
    	String texto="";
 	    try{
 	      FileReader flE=new FileReader(fichero);
 	      BufferedReader fE=new BufferedReader(flE);
 	      while(texto != null){
 	         texto = fE.readLine();
 	         if(texto != null){
 	           String [] elementos= texto.split(";"); 
 	           Actores registroA = new Actores(Integer.parseInt(elementos[0]), elementos[1], elementos[2]);
 	           gestorA.insertar(registroA);
 	           numeroActores++;
 	         }
 	      }
 	      fE.close();
 	  }catch(IOException e){
 	       System.out.println("Error en el fichero");
 	  }
      return numeroActores;
    }
    
    static int leeFicheroPeliculas(GestorTabla gestorP,GestorTabla gestorPA, String fichero){
    	 String texto="";
   	   int resul=0;
   	   int secuencia=5000;
   	  int secuenciaPA=10000;
   	   try{
   	      FileReader flE=new FileReader(fichero);
   	      BufferedReader fE=new BufferedReader(flE);
   	      while(texto != null){
   	         texto = fE.readLine();
   	         if(texto != null){
   	           if(!isInteger(texto.substring(0,1))){ 
   	        	  String [] elementos= texto.split(";");
   	        	 secuencia++;
   	        	 //System.out.println("Insertada pelicula " + secuencia+" " +elementos[0] + " " +Integer.parseInt(elementos[2]) );
   	        	  Peliculas registroP = new Peliculas(secuencia, elementos[0], elementos[1],Integer.parseInt(elementos[2]));
   	        	  gestorP.insertar(registroP);
   	        	  resul++;
   	           }else{
   	        	  //System.out.println("Insertado actor "+texto.trim()+ " peli="+ secuencia +" actor=" + Integer.parseInt(texto.trim()));
   	        	  Peliculas_Actores registroPA = new Peliculas_Actores(secuenciaPA++,secuencia,Integer.parseInt(texto.trim()));
   	        	  gestorPA.insertar(registroPA);
   	           }
   	         }
   	      }
   	      fE.close();
   	  }catch(Exception e){
   	       System.out.println(e.getMessage());
   	       e.printStackTrace();
   	  }
   	  return resul;
    }
    
    static boolean isInteger( String input ){
       try{      
          Integer.parseInt( input );
          return true;
       } catch( Exception e ){
          return false;
       }
    }
	
	public static void main(String[] args) {
		new PruebaFilmoteca();
	}
}

