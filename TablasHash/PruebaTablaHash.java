package paqueteH;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PruebaTablaHash {

	public static void main (String args[]){
		Instant fechaHoraActual = Instant.now(); 
		String random="";
		
		
		TablaHashFile tabla = new TablaHashFile(57,75,2,6);
		ElementoHash Ficha1 = new ElementoHash("Ficha1");
		ElementoHash Ficha2 = new ElementoHash("Ficha2");
		ElementoHash Ficha3 = new ElementoHash("Ficha3");
		ElementoHash Ficha4 = new ElementoHash("Ficha4");
		
		tabla.insertarEnTabla(Ficha1);
		tabla.insertarEnTabla(Ficha2);
		tabla.insertarEnTabla(Ficha3);
		tabla.insertarEnTabla(Ficha4);
		
		for(int i=0;i<100;i++)
		{
		random=generaTextoAleatorio(6,65,90);
		ElementoHash randomElem = new ElementoHash(random);
		tabla.insertarEnTabla(randomElem);
		}
		
		tabla.borrarEnTabla("Ficha1");
		tabla.borrarEnTabla("Ficha3");
		
		tabla.mostrarTabla();
		
		Instant fechaHoraActual2 = Instant.now(); 
		Duration tiempo= Duration.between(fechaHoraActual,fechaHoraActual2);
		System.out.println("La operacion se ha realizado en:" + tiempo.toMillis()+ " milisegundos");
	}
	
	public static char asciiToChar(final int ascii){
		return (char)ascii;		
	}
	
	public static String generaTextoAleatorio(int nCaracteres, int asciiI, int asciiF){
		String texto="";
		List<Integer> numeros = new ArrayList<>();
  	  	numeros.clear();
  	  	Random rnd = new Random();
	    rnd.ints(nCaracteres,asciiI,asciiF).forEach(numeros::add);
	      
	    for(int j=0;j<nCaracteres;j++){
	    	  char  letra = asciiToChar(numeros.get(j));
	    	  texto+=letra;
		}
	    return texto;
	}

}
