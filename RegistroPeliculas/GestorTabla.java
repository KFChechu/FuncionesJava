// Gestiona el acceso a los ficheros de datos y claves conjuntamente

package paqueteP;
import java.io.FileNotFoundException;
import java.io.IOException;

/*
 * La clase  GestorTabla tiene las utilidades de altas, bajas, buscar y mostrar  de los archivos de datos e índices 
 * de forma coordinada en ambas tablas.
 */

public class GestorTabla{
	private ArchivoListaHuecos archivoDatos;
    private ArbolB arbolB;

    public GestorTabla(String nombreTabla, int orden, RegistroClave registro)
        throws FileNotFoundException, IOException{
        archivoDatos = new ArchivoListaHuecos(registro, (new StringBuilder(String.valueOf(nombreTabla))).append(".dat").toString());
        arbolB = new ArbolB((new StringBuilder(String.valueOf(nombreTabla))).append(".btree").toString(), orden);
    }

    public GestorTabla(String nombre, String modo, RegistroClave registro)
    		throws FileNotFoundException, IOException{
        archivoDatos = new ArchivoListaHuecos(registro, (new StringBuilder(String.valueOf(nombre))).append(".dat").toString(), modo);
        arbolB = new ArbolB((new StringBuilder(String.valueOf(nombre))).append(".btree").toString(), modo);
    }

    public ArbolB getArbolB(){
        return arbolB;
    }

    public ArchivoListaHuecos getArchivoDatos(){
        return archivoDatos;
    }

    public RegistroClave buscar(int valorClave) throws IOException {
        RegistroClave registro = null;
        Clave clave = new Clave(valorClave);
        int posicionEnDatos = arbolB.buscar(clave);
        if(posicionEnDatos != -1){
            archivoDatos.leerRegistro(posicionEnDatos);
            registro = (RegistroClave)archivoDatos.getRegistro();
        }
        return registro;
    }

    public boolean borrar(int valorClave)throws IOException{
        boolean borrado = false;
        Clave clave = new Clave(valorClave);
        int posicionEnDatos = arbolB.buscar(clave);
        if(posicionEnDatos != -1){
            borrado = true;
            arbolB.borrar(clave);
            archivoDatos.borrarRegistro(posicionEnDatos);
        }
        return borrado;
    }

    public boolean insertar(RegistroClave registro)throws IOException {
        boolean insertado = false;
        Clave clave = new Clave(registro.getValorClave());
        int posicion = arbolB.buscar(clave);
        if(posicion == -1){
            insertado = true;
            archivoDatos.setRegistro(registro);
            int posicionEnDatos = archivoDatos.escribirRegistro();
            arbolB.insertar(new Clave(registro.getValorClave(), posicionEnDatos));
        }
        return insertado;
    }

    public void cerrar() throws IOException {
        arbolB.cerrarArchivo();
        archivoDatos.cerrarArchivo();
    }
    
    public void listar()throws IOException{
    	if (!getArbolB().vacio()){
			this.ordenCentral(getArbolB().getPosicionRaiz());
		}
		else
		{
			System.out.println("El arbol esta vacio");
		}
	}
    
    /*Lista logica de los datos de la tabla principal y los de la tabla secundaria enlazados con la principal*/
    public void listar(GestorTabla tSecundaria, GestorTabla tEnlace)throws IOException{
		if (!getArbolB().vacio()){
			this.ordenCentral2(getArbolB().getPosicionRaiz(),tSecundaria, tEnlace);
		}else{
			System.out.println("El arbol esta vacio");
		}
	}
    
    private void ordenCentral2(int posicion,GestorTabla tSecundaria, GestorTabla tEnlace) throws IOException {
	   int posicionRegistro;
	   if (posicion != -1){
		   Pagina pagina = (Pagina)getArbolB().leerRegistroLH(posicion); 
	       for (int i=0; i< pagina.getNumeroDeClaves(); i++) 
	       {	
	    	   ordenCentral2(pagina.getHijo(i), tSecundaria, tEnlace);
	    	   posicionRegistro = pagina.getClave(i).getPosicion();
			   getArchivoDatos().leerRegistro(posicionRegistro);
			   RegistroClave registro= (RegistroClave)getArchivoDatos().getRegistro();
			   String codigosTSecundaria = tEnlace.ordenCentralBuscar(tEnlace.getArbolB().getPosicionRaiz(),registro.getValorClave(),"");
			   
	    	   System.out.println(getArchivoDatos().getRegistro().toString());
	    	   String listaCodigosTSecundaria []= codigosTSecundaria.split(";");
	    	   for(int j=0;j< listaCodigosTSecundaria.length; j++){
	    		   tSecundaria.ordenCentralClave(tSecundaria.getArbolB().getPosicionRaiz(),Integer.parseInt(listaCodigosTSecundaria[j]));
	    	   }   
	 	    	  
	    	   
	       }
	       ordenCentral2(pagina.getHijo(pagina.getNumeroDeClaves()), tSecundaria, tEnlace);
	   }
	}
    
    private String  ordenCentralBuscar(int posicion,int valorClave, String result) throws IOException {
	   int posicionRegistro;
	   if (posicion != -1) 
	   {
		   Pagina pagina = (Pagina)getArbolB().leerRegistroLH(posicion); 
	       for (int i=0; i< pagina.getNumeroDeClaves(); i++) 
	       {
	    	   result=ordenCentralBuscar(pagina.getHijo(i),valorClave,result);
	    	   posicionRegistro = pagina.getClave(i).getPosicion();
			   getArchivoDatos().leerRegistro(posicionRegistro);
			   //RegistroClave registro= (RegistroClave)getArchivoDatos().getRegistro();
			   String valores[] = getArchivoDatos().getRegistro().toString().split(";");
			   if(valorClave==Integer.parseInt(valores[2])){
				   result +=valores[3]+";";
			   }
	       }
	       result=ordenCentralBuscar(pagina.getHijo(pagina.getNumeroDeClaves()),valorClave,result);
	   }
	   return result;
	}
    
  /** Recorre el arbol B con orden central, volcando por salida estandar, por cada clave visitada el contenido 
    * del registro de datos asociado a la misma.
    * @param posicion Numero de registro en el que se encuentra almacenada la pagina del arbol B.
    */		
	private void ordenCentral(int posicion) throws IOException
	{
	   int posicionRegistro;
	   if (posicion != -1) 
	   {
		   Pagina pagina = (Pagina)getArbolB().leerRegistroLH(posicion); 
	       for (int i=0; i< pagina.getNumeroDeClaves(); i++) 
	       {
	    	   ordenCentral(pagina.getHijo(i));
	    	   posicionRegistro = pagina.getClave(i).getPosicion();
	    	   
			   getArchivoDatos().leerRegistro(posicionRegistro);
	    	   System.out.println("Lista orden central" +getArchivoDatos().getRegistro().toString());
	       }
	       ordenCentral(pagina.getHijo(pagina.getNumeroDeClaves()));
	   }
	}
	
	private void ordenCentralClave(int posicion, int clave) throws IOException
	{
	   int posicionRegistro;
	   if (posicion != -1) 
	   {
		   Pagina pagina = (Pagina)getArbolB().leerRegistroLH(posicion); 
	       for (int i=0; i< pagina.getNumeroDeClaves(); i++) 
	       {
	    	   ordenCentralClave(pagina.getHijo(i), clave);
	    	   posicionRegistro = pagina.getClave(i).getPosicion();
			   getArchivoDatos().leerRegistro(posicionRegistro);
			   String valores[] = getArchivoDatos().getRegistro().toString().split(";");
			   if(clave == Integer.parseInt(valores[1]))
	    	      System.out.println(getArchivoDatos().getRegistro().toString());
	       }
	       ordenCentralClave(pagina.getHijo(pagina.getNumeroDeClaves()),clave);
	   }
	}

	public void listar(int valorClave1, int valorClave2)throws IOException{
		if (!getArbolB().vacio()){
			if (valorClave1 > valorClave2) System.out.println("No hay claves en el rango. Clave1 mayor que Clave2");				
		    else  this.listarRecursivo(getArbolB().getPosicionRaiz(), valorClave1, valorClave2);	
		}
		else System.out.println("El arbol esta vacio");
	}
	
	private void listarRecursivo(int posicion, int valorClave1, int valorClave2) throws IOException {
		boolean ok = true;
		if (posicion != -1){
			Pagina pagina =  (Pagina)this.getArbolB().leerRegistroLH(posicion);
			int i=0;
			while( i < pagina.getNumeroDeClaves() && ok){
				if(pagina.getClave(i).getValor() >= valorClave1){
						if(pagina.getClave(i).getValor() > valorClave1){
							if(pagina.getClave(i).getValor() >= valorClave2) ok = false;
							listarRecursivo(pagina.getHijo(i), valorClave1, valorClave2);
						}
						if(pagina.getClave(i).getValor() <= valorClave2){
							this.imprimirClave(pagina.getClave(i).getPosicion());
					}
				}
				i++;
			}
			if(ok) listarRecursivo(pagina.getHijo(pagina.getNumeroDeClaves()), valorClave1, valorClave2);
		}
	}
	
	private void imprimirClave(int posicion) throws IOException{
		this.getArchivoDatos().leerRegistro(posicion);
		System.out.println(this.getArchivoDatos().getRegistro().toString());
	}
}
