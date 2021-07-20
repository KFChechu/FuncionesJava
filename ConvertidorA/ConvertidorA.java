import java.io.*;
public class ConvertidorA {
	public static void main(String args[])throws IOException{
			try{
		String ficheroInicial="F:/Informatica UPM/Curso 2016-2017/Workspace/TercerProyectoJava/src/TercerProyectoJava.txt";
		String ficheroFinal="F:/Informatica UPM/Curso 2016-2017/Workspace/TercerProyectoJava/src/TercerProyectoJava2.txt";
				int contador=0;
				FileReader flE =new FileReader(ficheroInicial);
				FileWriter flS =new FileWriter(ficheroFinal);
				
				BufferedReader fE=new BufferedReader(flE);
				BufferedWriter fS=new BufferedWriter(flS);
				
				String texto="";
				
				while(texto != null){
					texto=	fE.readLine();
					if(texto!=null){
						contador += texto.length() - texto.replace("a", "").length();
						texto=texto.replace('a','@');
						fS.write(texto);
						fS.newLine();
					}
				}
				
	System.out.println("Copia completada. El caracter 'a' fue sustituido " + contador + " veces por el caracter '@'.");
				fE.close();
				fS.close();
			}catch(IOException e){
				System.out.println("Error en la lectura o escritura de ficheros");
			}

	}
}
