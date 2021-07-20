package paqueteH;

public class TablaHashFile {
	double factorCarga;
	int numElementosInsertados;
	int exploracion;
	FicheroHash fichero;
	int longitudClave;
	int nColisionesT=0;
	TablaHashFile(int tamanho, double factorCarga, int exploracion,int longitudClave){
		this.factorCarga=factorCarga;
		this.exploracion=exploracion;
		this.longitudClave=longitudClave;
		String relleno="";
		fichero = new FicheroHash("Claves.dir", longitudClave);
		fichero.vaciaFichero();
		for(int i=0;i< longitudClave; i++) relleno+="0"; 
	    for(int i=1;i<= tamanho; i++)
			fichero.escribeRegistro(relleno, i, true);
	}
	int getTamanhoTablaHash(){
		return fichero.dameNumeroRegistros();
	}
	//para claves alfanumericas y numericas
	int fHash(String clave){
		int valorHash=0;
		try{
			valorHash=Integer.parseInt(clave);
			valorHash =valorHash%getTamanhoTablaHash();
			if(valorHash ==0)
				valorHash++;
			return valorHash;
		}catch(Exception e){
			for(int i=0; i< clave.length(); i++){
				valorHash+= clave.charAt(i);
			}
			valorHash =valorHash%getTamanhoTablaHash();
			if(valorHash ==0)
				valorHash++;
			return valorHash;
		}
	}
	
	boolean posicionLibre(int posicion){
		  return fichero.posicionLibre(posicion);
	}

	void insertarEnTabla(ElementoHash elementoAux){
		if(numElementosInsertados >(int)(getTamanhoTablaHash() * (factorCarga /100))){
			System.out.println("Tabla casi llena; operacion de reHash");
			System.out.println("Numero insertados: "+numElementosInsertados);
			System.out.println("Numero posiciones total: "+getTamanhoTablaHash());
			reHash((int)(getTamanhoTablaHash()*1.5));
		}
	    
	    int posicion=fHash(elementoAux.getClave());
	    if(!posicionLibre(posicion)){
	    	posicion = exploracionHash(posicion,elementoAux.getClave());
	    }
		fichero.escribeRegistro(elementoAux.getClave(), posicion, false);
		numElementosInsertados++;
	    
	}
	
	ElementoHash leerTabla(int posicion){
		ElementoHash elemento = fichero.leeRegistro(posicion);
		return elemento;
	}
	
	void borrarEnTabla(String clave){ 
			int posicion=buscarEnTabla(clave);
			System.out.println( "@posicion: "+ posicion + " tamaño " +getTamanhoTablaHash());
			if(posicion > 0 && posicion <= getTamanhoTablaHash()){
				fichero.borraRegistro(posicion);
				System.out.println( "@borrarDeTabla:"+clave+" posicion= "+ posicion);
				numElementosInsertados--;
			}else
	    	   throw new ExcepcionHash("Imposible Borrar: No se encontro ese elemento");
	}
	int buscarEnTabla(String clave){  
		boolean encontrado=false;
		ElementoHash elementoAux=null;
		int posicion=fHash(clave);
	
		//System.out.println( "buscar:"+clave+" posicion= "+ posicion);
		elementoAux = fichero.leeRegistro(posicion);
		if(elementoAux != null && clave.equals(elementoAux.getClave())) encontrado=true;
	    while(!encontrado){
           posicion= exploracionHash(posicion,clave);
           //System.out.println("posicion " + posicion + " clave "+ clave);
           elementoAux=fichero.leeRegistro(posicion);
           //System.out.println("* " + elementoAux.getClave());
           if(elementoAux != null && clave.equals(elementoAux.getClave())){
        	   
        	   encontrado=true;
           }
		};
		
		if(encontrado){
			return posicion;
		}else
    	   throw new ExcepcionHash("No se encontro el elemento de clave " + clave);
	}
	
	int buscarEnTabla_old(String clave){  
		boolean encontrado=false;
		ElementoHash elementoAux=null;
		int posicion=fHash(clave);
	
		elementoAux = fichero.leeRegistro(posicion);
		if(elementoAux ==null)
			System.out.println( "elemento nulo" );
			do{
				if(clave.equals(elementoAux.getClave())) encontrado=true;
		   if(encontrado==false){
			   posicion= exploracionHash(posicion,clave);
			   elementoAux = fichero.leeRegistro(posicion);
		   }
			}while(!clave.equals(elementoAux.getClave())&& encontrado);			
		if(encontrado){
			return posicion;
		}else
    	   throw new ExcepcionHash("No se encontro el elemento de clave " + clave);
	}

	//@SuppressWarnings("unused")
	void reHash(int tamanho){
		if(numElementosInsertados > tamanho)
			throw new ExcepcionHash("Los elementos insertados no caben en la nueva tabla");
		
		try{
			String relleno="";
			for(int i=0; i< longitudClave; i++) relleno+="0"; 
	
			ElementoHash elementoTemp[] = new ElementoHash[getTamanhoTablaHash()];
			for(int i=0; i< elementoTemp.length; i++){
				elementoTemp[i]=null;
			}
			mostrarTabla();
			System.out.println("==INICIO REHASH========================================================================");
			System.out.println("Rehash tabla temp "+ numElementosInsertados + " tam ori"+  getTamanhoTablaHash() + " elementos hasta " + tamanho);
			int cont=0;
			for(int i=1; i<= getTamanhoTablaHash(); i++){
				ElementoHash elemento=fichero.leeRegistro(i);
				if(elemento != null){
					elementoTemp[i-1]=elemento;
					cont++;
				}
			}
			System.out.println("Recuperados "+ cont + " elementos");
		    fichero.getFichero().setLength(0);
		    fichero.vaciaFichero();
		    numElementosInsertados=0;
		    nColisionesT=0;
		   		    
		    for(int i=1;i<= tamanho; i++)
				fichero.escribeRegistro(relleno, i, true);
		    cont=0;
		    for (int i=0;i< elementoTemp.length; i++){
		    	if(elementoTemp[i] !=null){ 
		    		ElementoHash elemento=elementoTemp[i];
		    		insertarEnTabla(elemento);
					cont++;
		    	}
			}
		    System.out.println("recuperados " + cont + " elementos ");
		    System.out.println("==FIN REHASH========================================================================");
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	}
		
	int exploracionHash(int posicion, String clave){
		int nColisiones=0;
		try{
	    if(exploracion ==1){ //Exploracion lineal clasica
			while(!posicionLibre(posicion) || posicion < 0){
			   posicion= (posicion + nColisiones) % getTamanhoTablaHash();
			   if(posicion >= getTamanhoTablaHash()){
					posicion-=getTamanhoTablaHash();
			   }else if( posicion ==0){
				   posicion= (posicion + nColisiones) % getTamanhoTablaHash();
			   }
			   nColisiones++;
			}
			if(nColisiones > 0) System.out.println("N. colisiones: "+ nColisiones);
	    }else{ // Exploracion cuadratica 
		    while(!posicionLibre(posicion) || posicion < 0){
		    	
		    	posicion =(posicion+ nColisiones * nColisiones)% getTamanhoTablaHash();
		    	if(posicion >= getTamanhoTablaHash()){
		    		posicion-=getTamanhoTablaHash();
		    	}else if( posicion ==0){
		    		posicion =(posicion + nColisiones * nColisiones) % getTamanhoTablaHash();
				}
		    	nColisiones++;
		    }
		    if(nColisiones > 0) System.out.println("N. colisiones: "+ nColisiones);
	    }
		}catch(Exception e){
			e.printStackTrace();
		}
		nColisionesT +=nColisiones;
		//System.out.println("Posición libre lograda "  + posicion);
	    return posicion;
	}

	void mostrarTabla(){
	   System.out.println("Elementos de la tabla:");
	   ElementoHash elementoAux=null;
	   int cont=0;
	   for(int i=1; i<=getTamanhoTablaHash(); i++){
		  elementoAux =null;
		  elementoAux=fichero.leeRegistro(i); 
		  if(elementoAux != null){
		    System.out.println("Elemento reg: "+ i +" "+elementoAux.getClave());
		    cont++;
		  }
		    
	   }
	   System.out.println("Listados: "+ cont+ "elementos con " + nColisionesT + " colisiones "); 
	}
}
