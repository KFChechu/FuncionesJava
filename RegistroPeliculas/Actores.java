package paqueteP;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Actores extends RegistroClave
{
	public static final int TAMANIO_TITULO = 30;
	public static final int TAMANIO_TIPO = 25;
	private String nombre;
	private String pais;
	
    public Actores(){
        setNombre("");
        setPais("");
    }

    public Actores(int codActor, String nombre, String pais){
        super(-2, codActor);
        setNombre(nombre);
        setPais(pais);
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getNombre(){
        return nombre;
    }

    public void setPais(String pais){
        this.pais = pais;
    }

    public String getPais(){
        return pais;
    }

    public int longitudRegistro(){
        return 110 + super.longitudRegistro();
    }

    public void escribir(RandomAccessFile archivo) throws IOException {
        super.escribir(archivo);
        super.escribirCadena(getNombre(), 30, archivo);
        super.escribirCadena(getPais(), 25, archivo);
    }

    public void leer(RandomAccessFile archivo)throws IOException {
        super.leer(archivo);
        setNombre(super.leerCadena(30, archivo));
        setPais(super.leerCadena(25, archivo));
    }

    public String toString(){
        return super.getControl()+";"+super.getValorClave()+";"+getNombre()+";"+getPais();
    }
}

