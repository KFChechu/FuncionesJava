//Gestiona los datos en la memoria principal de las entidades de la tabla Peliculas

package paqueteP;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Peliculas extends RegistroClave
{
	public static final int TAMANHO_TITULO = 30;
    public static final int TAMANHO_TIPO = 25;
    //tamaño del registro 30*2+ 25*2 +4 = 114
    private static final int TAMANHO_REGISTRO = 114;
    private String titulo;
    private String tipo;
    private int anho;

    public Peliculas(){
        setTitulo("");
        setTipo("");
        setAnho(0);
    }

    public Peliculas(int numReg, String titulo, String tipo, int anho){
        super(-2, numReg);
        setTitulo(titulo);
        setTipo(tipo);
        setAnho(anho);
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public String getTitulo(){
        return titulo;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    public String getTipo(){
        return tipo;
    }

    public void setAnho(int anho){
        this.anho = anho;
    }

    public int getAnho(){
        return anho;
    }

    public int longitudRegistro(){
        return TAMANHO_REGISTRO + super.longitudRegistro();
    }

    public void escribir(RandomAccessFile archivo)throws IOException{
        super.escribir(archivo);
        super.escribirCadena(getTitulo(), 30, archivo);
        super.escribirCadena(getTipo(), 25, archivo);
        archivo.writeInt(getAnho());
    }

    public void leer(RandomAccessFile archivo)throws IOException {
        super.leer(archivo);
        setTitulo(super.leerCadena(30, archivo));
        setTipo(super.leerCadena(25, archivo));
        setAnho(archivo.readInt());
    }

    public String toString(){
        return "Pelicula[control="+super.getControl()+", numReg="+super.getValorClave()+ "titulo="+getTitulo()+" tipo="+getTipo()+", numero paginas="+getAnho()+"]";
    }
}

