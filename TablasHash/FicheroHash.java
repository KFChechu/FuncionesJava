package paqueteH;
import java.io.*;
public class FicheroHash{
    private RandomAccessFile miTabla;
    private int numeroRegistros;
    private int longitudClave;
    private int longitudR;
        
    String tabla;
    FicheroHash(String tabla, int longitudClave){
        try{
            miTabla= new RandomAccessFile(tabla,"rw");
            this.tabla=tabla;
            this.longitudClave=longitudClave;
            longitudR=longitudClave*2+4;
        }catch( IOException e){
            e.getMessage(); 
        }
        
    }
    public void vaciaFichero(){
    	try{
    	    miTabla.setLength(0);
    	}catch(Exception e){
    	    e.printStackTrace();	
    	}
    }
    public int dameNumeroRegistros(){
    	try{
    		long longitud= miTabla.length();
    		if (longitud >= longitudR)
    			numeroRegistros= (int)(longitud/longitudR);
    		else 
    			numeroRegistros=0;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return numeroRegistros;
    	
    }
    
    public ElementoHash leeRegistro(int nRegistro){
        String valorClave = "";
        boolean borrado=false;
        if(nRegistro < 1 || nRegistro > dameNumeroRegistros())
        	return null;
        try{
	        miTabla.seek((nRegistro-1)*longitudR);
	        int estadoI=miTabla.readInt();
	        for(int i =0; i < longitudClave; i++){
	            valorClave += miTabla.readChar();
	        }
	        
	        if(estadoI==0) borrado = false; else borrado=true;
        }catch(Exception e){
        	e.printStackTrace();
        }
        
    	if (borrado == true)
    		return null;
    	else
    		return (new ElementoHash(valorClave));
    }
        
    public String muestraRegistros(){
        String texto = "";
        try{
	        numeroRegistros= dameNumeroRegistros();
	        for(int i=1; i<= numeroRegistros; i++){
	        	ElementoHash r= leeRegistro(i);
	        	texto += "R="+ i +" "+ r.verElementoHash();
	        }
        }catch(Exception e){
        	e.printStackTrace();
        }
        return texto;
        
    }
    
    boolean posicionLibre(int posicion){
    	ElementoHash elemento=null;
    	
    	try{
    		elemento= leeRegistro(posicion);
    		
    	}catch( Exception e){
    		e.getMessage(); 
    	}
    	if(elemento != null){
    		return false;
    	}else{
    		return true;
    	}
    		
    }
    
    public void escribeRegistro(String valorClave,  int nRegistro, boolean borrado){
    	try{
    		if(nRegistro==0 || nRegistro > (this.dameNumeroRegistros()+1)){
    			//throw new ExcepcionHash("Numero registro fuera de rango");
    			System.out.println("Numero registro fuera de rango" + nRegistro +
    					 " "+valorClave + " " +this.dameNumeroRegistros());
    		}
    			
	        miTabla.seek((nRegistro-1)*(longitudR));
	        if(borrado) miTabla.writeInt(1); else miTabla.writeInt(0);
	        for(int i=0; i< longitudClave; i++){
	            if(i < longitudClave )
	                miTabla.writeChar(valorClave.charAt(i));
	            else
	                miTabla.writeChar(' ');
	        }
 
    	}catch(Exception e){
        	e.printStackTrace();
        }
    }
    
    public void borraRegistro(int nRegistro){
    	
    	try{
    		miTabla.seek((nRegistro-1)*(longitudR));
    		miTabla.writeInt(1);
    		
     	}catch(Exception e){
        	e.printStackTrace();
        }
    }
    
    RandomAccessFile getFichero(){
    	return miTabla;
    }
    
    public void cierraTabla() throws IOException{
        miTabla.seek(0);
        miTabla.close();
    }
}
