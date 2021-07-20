package paqueteH;

public class TablaHashMemo {
	ElementoHash  tablaHash[];
	double factorCarga;
	int numElementosInsertados;
	private int tamanhoTablaHash;
	int exploracion;
	FicheroHash fichero;
	int longitudClave;
	int nColisionesT=0;
	TablaHashMemo(int tamanho, double factorCarga, int exploracion,int longitudClave){
		tamanhoTablaHash=tamanho;
		this.factorCarga=factorCarga;
		this.exploracion=exploracion;
		tablaHash = new ElementoHash[tamanhoTablaHash];
		this.longitudClave=longitudClave;
	}
		
	int getTamanhoTablaHash(){
		return tablaHash.length;
	}
	int fHash(String clave){
		int valorHash=0;
		try{ //para claves numericas enteras
			valorHash=Integer.parseInt(clave);
			valorHash =valorHash%getTamanhoTablaHash();
			//System.out.println("elemento "+clave+" codigo hash "+valorHash);
			return valorHash;
		}catch(Exception e){ //para claves alfanumericas
			for(int i=0; i< clave.length(); i++){
				valorHash+= clave.charAt(i);
			}
			valorHash =valorHash%getTamanhoTablaHash();
			//System.out.println("elemento* "+clave+" codigo hash "+valorHash);
			return valorHash;
		}
	}

	boolean posicionLibre(int posicion){
	  if (tablaHash[posicion]== null){
		 return true;
	  }else{
		 return false;
	  }

	}

	void insertarEnTabla(ElementoHash elementoAux){
		if(numElementosInsertados >(int)(getTamanhoTablaHash() * (factorCarga /100))){
			System.out.println("Tabla casi llena; operacion de reHash");
			reHash((int)(getTamanhoTablaHash()*1.5));
		}
	    int posicion=fHash(elementoAux.getClave());
	    if(!posicionLibre(posicion)){
	    	System.out.println("colision en: "+posicion);
	    	posicion = exploracionHash(posicion);
	    }
		System.out.println("InsertaTabla:" +elementoAux.getClave() + " posicion= "+ posicion );
		tablaHash[posicion]=elementoAux;
		numElementosInsertados++;
	}

	void borrarEnTabla(String clave){ // throws ExcepcionHash{
		int posicion=buscarEnTabla(clave);
		
		if(posicion > 0 && posicion <= getTamanhoTablaHash()){
			tablaHash[posicion]=null;
			numElementosInsertados--;
		}else
    	   throw new ExcepcionHash("Imposible Borrar: No se encontro ese elemento");    
	}

	int buscarEnTabla(String clave){  
		boolean encontrado=false;
		ElementoHash elementoAux=null;
		int posicion=fHash(clave);

		elementoAux = leerTabla(posicion);
		if(elementoAux != null)
     	   if(clave.equals(elementoAux.getClave())) encontrado=true;
		
		while( !encontrado){
           posicion= exploracionHash(posicion);
           elementoAux=tablaHash[posicion];
           if(elementoAux != null)
        	   if(clave.equals(elementoAux.getClave())) encontrado=true;
		}			
		if(encontrado){
			return posicion;
		}else
    	   throw new ExcepcionHash("No se encontro el elemento de clave " + clave);
	}

	void reHash(int tamanho){
		if(numElementosInsertados > tamanho)
			throw new ExcepcionHash("Los elementos insertados no caben en la nueva tabla");

		tamanhoTablaHash=tamanho;
		//Tabla Vieja con tamanho viejo
		ElementoHash [] tablaVieja=new ElementoHash[getTamanhoTablaHash()];
		tablaVieja=tablaHash;

		// Tabla Nueva con nuevo tamanho
		tablaHash=new ElementoHash[tamanho];

		for (int i=0;i< tablaVieja.length;i++){
		   if(tablaVieja[i]!= null){
			  ElementoHash elementoAux = tablaVieja[i];
			  insertarEnTabla(elementoAux);
		   }
		}
	}
	
	int exploracionHash(int posicion){
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
		System.out.println("Posición libre lograda "  + posicion);
	    return posicion;
	}

	ElementoHash leerTabla(int posicion){
		ElementoHash elemento = tablaHash[posicion];
		return elemento;
	}
	
	void mostrarTabla(){
       int cont=0;
	   System.out.println("Elementos de la tabla:");
	   ElementoHash elementoAux=null;
	   for(int i=0; i<getTamanhoTablaHash(); i++){
		   elementoAux =null;
		  if(tablaHash[i] != null)
				  elementoAux = tablaHash[i];
			  
		  if(elementoAux != null){
		    System.out.println("Elemento: "+ i +" "+elementoAux.getClave());
		    cont++;
		  }
	   }
	   System.out.println("Listados: "+ cont+ "elementos con " + nColisionesT + " colisiones "); 
	}
}
