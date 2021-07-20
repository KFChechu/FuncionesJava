//Gestiona los datos en la memoria principal de las entidades de la tabla de enlace Pelicula_Actores
package paqueteP;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Peliculas_Actores extends RegistroClave
{
	public static final int TAMANIO_TITULO = 4;
    public static final int TAMANIO_TIPO = 4;
    private static final int TAMANIO_REGISTRO = 8;
    private int codPelicula;
    private int codActor;
    public Peliculas_Actores(){
        setCodPelicula(0);
        setCodActor(0);
    }

    public Peliculas_Actores(int numReg, int codPelicula, int codActor)
    {
        super(-2, numReg);
        setCodPelicula(codPelicula);
        setCodActor(codActor);
    }

    public void setCodActor(int codActor){
        this.codActor = codActor;
    }

    public void setCodPelicula(int codPelicula){
        this.codPelicula = codPelicula;
    }

    public int getCodPelicula(){
        return codPelicula;
    }
    public int getCodActor(){
        return codActor;
    }

    public int longitudRegistro(){
        return TAMANIO_REGISTRO + super.longitudRegistro();
    }

    public void escribir(RandomAccessFile archivo) throws IOException {
        super.escribir(archivo);
        archivo.writeInt(getCodPelicula());
        archivo.writeInt(getCodActor());
    }

    public void leer(RandomAccessFile archivo) throws IOException {
        super.leer(archivo);
        setCodPelicula(archivo.readInt());
        setCodActor(archivo.readInt());
        
    }

    public String toString(){
         return super.getControl()+ ";"+super.getValorClave()+";"+getCodPelicula()+";"+ getCodActor();
    }
    
    public String getValor(int i){
    	if(i==1)
    		return getCodPelicula()+"";
    	else
    		return getCodActor()+"";
    }
}

