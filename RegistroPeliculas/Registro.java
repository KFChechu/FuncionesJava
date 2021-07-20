// Clase abstracta de la cual derivan las clases de escritura y lectura en los ficheros*/
package paqueteP;
import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class Registro {

	public Registro(){
    }

    public int longitudRegistro(){
        return 0;
    }

    public abstract void escribir(RandomAccessFile randomaccessfile) throws IOException;

    public abstract void leer(RandomAccessFile randomaccessfile) throws IOException;

    public String toString() {
        return "Registro ";
    }

    protected String leerCadena(int longitud, RandomAccessFile archivo) throws IOException {
        char cadena[] = new char[longitud];
        for(int cuenta = 0; cuenta < cadena.length; cuenta++)
            cadena[cuenta] = archivo.readChar();

        return (new String(cadena)).replace('\0', ' ');
    }

    protected void escribirCadena(String cadena, int longitud, RandomAccessFile archivo)
        throws IOException {
        StringBuffer bufer = null;
        if(cadena != null)
            bufer = new StringBuffer(cadena);
        else
            bufer = new StringBuffer(longitud);
        bufer.setLength(longitud);
        archivo.writeChars(bufer.toString());
    }
}
