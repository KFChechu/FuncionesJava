package tablaAgenda;
import java.io.IOException;

public class Indice {
    NodoIndice inicio;
    
    Indice (){
        Clave clave=new Clave("inicio",0);		//clave+direccion inicial
        inicio=new NodoIndice(clave, null);		//Inicia el nodo con CLAVE y apunta a null
        inicio.sig=inicio;						//Apunta a inicio
    }
    
    
    Boolean estaVacia() {
		boolean resul = false;
		if (inicio != null)
			resul = inicio.sig == inicio;
		else
			resul = true;
		return resul;
    }
    
    Boolean insertarClave(Clave cla) {
    	Boolean resul = false;
    	if (cla.tieneDatos()) {
            inicio.info = cla;
            NodoIndice nuevo, anterior = inicio, actual = inicio.sig;
            while (actual.info.getClave().compareTo(cla.getClave()) < 0) {
            	anterior = actual;
            	actual = actual.sig;
            }
            if (actual == inicio
                || actual.info.getClave().compareTo(cla.getClave()) > 0) {
                resul = true;
                nuevo = new NodoIndice(cla, actual);
                anterior.sig = nuevo;
            }
        }
        return resul;
    }
    boolean eliminarClave(String cla) {
		boolean resul = false;
		Clave clave = new Clave();
		clave.setClave(cla);
		inicio.info.setClave(cla);
		NodoIndice anterior = inicio, actual = inicio.sig;
		while (actual.info.getClave().compareTo(cla) < 0) {
			anterior = actual;
			actual = actual.sig;
		}
		if (actual != inicio && actual.info.getClave().compareTo(cla) == 0) {
			resul = true;
			anterior.sig = actual.sig;
		}
		return resul;
    }
    
    Clave buscarClave(String t) {
		Clave clave = new Clave(), resul=null;
		clave.setClave(t);
		inicio.info = clave;
		NodoIndice actual = inicio.sig;
		while (actual.info.getClave().compareTo(t) < 0){
			actual = actual.sig;
	        }
		if (actual != inicio && actual.info.getClave().trim().toLowerCase().equals(t.trim().
				toLowerCase())==true) {
			resul = actual.info;
		}
		return resul;
    }

    void mostrarListaIndices() {
		NodoIndice aux;
		System.out.println( "Estado de la lista: " );
		aux = inicio;
		do{
	        if (aux != inicio )System.out.println(aux.info.getClave());
	        aux = aux.sig;
		}while (aux != inicio);
		System.out.println();
    }
    
    void  crearIndices( TablaAgenda tabla)throws IOException {
    	
    	int i=0;
    	while (tabla.dameNumeroRegistros() < i) 
    	{
    		Clave clave=new Clave(tabla.leeRegistro(i).dameNombre(),i);
    		insertarClave(clave);
    		i++;
    	}
    	
    }
}
