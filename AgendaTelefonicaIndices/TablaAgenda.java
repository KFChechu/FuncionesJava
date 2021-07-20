package tablaAgenda;

import java.io.*;
public class TablaAgenda{
    private RandomAccessFile miTabla;		//En miTabla se guardan los registros
    private int numeroRegistros;			
    private final int longitudR = 52;	// Tamaño del registro
    private final int longitudT = 20;	// Tamaño del nombre
    private Indice indice= new Indice();
    String tabla;
    
    TablaAgenda(String tabla){
        try{
            miTabla= new RandomAccessFile(tabla,"rw");	
            this.tabla=tabla;
            numeroRegistros= dameNumeroRegistros();		//se pasan el NUMERO DE REGISTROS
        }catch( IOException e){
                e.getMessage(); 
        }

    }
    
    Indice getIndice(){
    	return indice;
    }

    void setIndice(NodoIndice nodo){
    	indice.inicio=nodo;
    }
    
    public int dameNumeroRegistros(){					
    	File fichero = new File(tabla);
    	long longitud= fichero.length();
    	if (longitud >= longitudR)
    		numeroRegistros= (int)longitud/longitudR;
    	else 
    		numeroRegistros=0;
        return numeroRegistros;
    }
    
    public Registro leeRegistro(int nRegistro) throws IOException{
        String nombre = "";
        int edad;
        long telefono;
        miTabla.seek((nRegistro-1)*longitudR);
        edad = miTabla.readInt();
        for(int i =0; i<longitudT; i++)
                nombre += miTabla.readChar();
        
        telefono = miTabla.readLong();
        return (new Registro(nombre,edad,telefono));
    }
    
    public Registro leeRegistro(String clave)
    {
    	String nombre = "";
    	boolean encontrado=false;
        int edad=0,x=1;
        long telefono=0;
        try{
        do{
        miTabla.seek((x-1)*longitudR);
        edad = miTabla.readInt();
        for(int i =0; i<longitudT; i++)
                nombre += miTabla.readChar();
        
        if(clave==nombre)
        	encontrado=true;
   	
        telefono = miTabla.readLong();
        x++;
        }
        while(x<dameNumeroRegistros() && !encontrado);
        }catch( IOException e){
            e.getMessage();
        }
        if(encontrado)	
        return (new Registro(nombre,edad,telefono));
        else 
        	return (new Registro("",0,0));
    }
    
    public String MuestraRegistros() throws IOException{
    	String nombre;
        int edad;
        long telefono;
        String texto="";
        int nRegistro=1;
        
        if(dameNumeroRegistros()<1)
        	texto="No hay registros disponibles";
        else
        	do{
        	nombre="";
	        miTabla.seek((nRegistro-1)*longitudR);
	        edad = miTabla.readInt();
	        for(int i =0; i<longitudT; i++)
	                nombre += miTabla.readChar();
	        telefono = miTabla.readLong();
	        nRegistro++;
	        texto += "Reg: " + (nRegistro-1) + " Nombre: " +nombre + " Edad: " + String.valueOf(edad) + " Telefono: " +telefono + "\n";
	        } while(nRegistro<=numeroRegistros);
        return texto;
    }
    
    public void escribeRegistro(String nombre,int edad,long telefono,int nRegistro) throws IOException{
        int longitud = nombre.length();
        miTabla.seek((nRegistro-1)*longitudR);
        miTabla.writeInt(edad);
        for(int i=0; i< longitudT; i++){
            if(i < longitud )
                miTabla.writeChar(nombre.charAt(i));
            else
                miTabla.writeChar(' ');
        }
        miTabla.writeLong(telefono);
        dameNumeroRegistros();
    }
    
    public void borraRegistro(int nRegistro) throws IOException{ //Copiando el último registro en el registro a borrar y truncar el fichero
    	Registro registroAux=leeRegistro(dameNumeroRegistros());
    	indice.eliminarClave(registroAux.dameNombre());
    	int longitud = registroAux.dameNombre().length();
        miTabla.seek((nRegistro-1)*longitudR);
        miTabla.writeInt(registroAux.dameEdad());
        for(int i=0; i< longitudT; i++){
            if(i < longitud )
                miTabla.writeChar(registroAux.dameNombre().charAt(i));
            else
                miTabla.writeChar(' ');
        }
        miTabla.writeLong(registroAux.dameTelefono());
        numeroRegistros--;
        miTabla.setLength(longitudR*numeroRegistros);
        dameNumeroRegistros();
    }
    
    
    public void cierraTabla() throws IOException{
        miTabla.seek(0);
        miTabla.close();
    }
    
}
