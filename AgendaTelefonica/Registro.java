package tablaAgenda;

public class Registro{
	private String nombre;
	private int edad;
	private long telefono;
	Registro(String nom,int nEdad,long telefono){
		nombre = nom;
		edad = nEdad;
		this.telefono = telefono;
	}
	public String muestraRegistro(){
		return nombre +" "+ edad+ " "+ telefono+ "\n";
	}
	public String dameNombre(){
		return nombre;
	}
	public long dameTelefono(){
		return telefono;
	}
	public int dameEdad(){
		return edad;
	}
}
