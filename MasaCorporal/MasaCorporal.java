import java.io.*;
public class MasaCorporal {
	 public static void main(String args[]){
	        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
	        boolean error=false;
	        do{
	            try{
	                error=false;
	                System.out.println("Teclea el peso(kg): ");
	                String texto= teclado.readLine();
	                float peso = Integer.parseInt(texto);
	                System.out.println("Teclea la estatura(cm): ");
	                texto= teclado.readLine();
	                float estatura = Integer.parseInt(texto);
	                if (estatura == 0)
	                    throw new ArithmeticException();
	                if (peso < 40 || peso >200 || estatura<50 || estatura>250) {
	                    throw new Exception();
	                }
	                float imc=peso/((estatura/100) * (estatura/100));
	                System.out.printf("El indice de masa corporal es: " + imc );
	            
	            }catch (NumberFormatException e){
	                System.out.println("Error: en la conversión.");
	                error=true;
	            }catch (IOException e) {
	                System.out.println("Error en la lectura.");
	                error=true;
	            }catch (ArithmeticException e){
	                System.out.println("Error: División por cero.");
	                error=true;
	            }catch (Exception e){
	                System.out.println("Error en el rango de los datos.");
	                error=true;
	            }
	        }while(error);
	    }
}
