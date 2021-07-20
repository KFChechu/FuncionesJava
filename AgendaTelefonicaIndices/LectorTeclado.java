package tablaAgenda;

import java.io.*;
public class LectorTeclado{
 BufferedReader teclado;
 LectorTeclado(){
  InputStreamReader f=new InputStreamReader(System.in);
  teclado = new BufferedReader(f);  
 }
 public String leeTexto(String mensaje){
  System.out.println(mensaje);
  try{
    return teclado.readLine();
  }catch (Exception e){
    System.out.println(e.getMessage());
  }
  return null;
 } 
 public int leeEntero(String mensaje){
	  System.out.println(mensaje);
	  try{
	    return Integer.parseInt(teclado.readLine());
	  }catch (Exception e){
	    System.out.println(e.getMessage());
	  }
	  return -1;
 } 
 public long leeEnteroLargo(String mensaje){
	  System.out.println(mensaje);
	  try{
	    return Long.parseLong(teclado.readLine());
	  }catch (Exception e){
	    System.out.println(e.getMessage());
	  }
	  return -1;
	 } 
} 