import java.util.*;
public class ReadWrite {
	
	public static void main(String args[]){
		Scanner sc = new Scanner(System.in);	// Declaramos el escaner para obtener datos por teclado
		String numMatricula;					// String donde almacenamos el numMatricula
		String nombre;							// String donde almacenamos el nombre del alumno
		
			System.out.println("Introduzca el número de matricula:");
			numMatricula = sc.nextLine();
			if(numMatricula.length() > 1)
			{
				System.out.println("Introduzca el nombre del alumno:");
				nombre = sc.nextLine();
				System.out.println("Nombre: " + nombre.toUpperCase() + " -> Numero de matricula: " + numMatricula);
			}
			else
				System.out.println("Número de matrícula no válido");

		sc.close();
		System.out.println("Fin del programa.");;
	}
}
