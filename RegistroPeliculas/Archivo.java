// Gestiona en el fichero de datos la parte de los datos de las entidades

package paqueteP;
import java.io.*;

public class Archivo {
	protected Registro registro;
    protected RandomAccessFile archivo;

    public Archivo(Registro registro, String nombreArchivo, String modo)
        throws FileNotFoundException
    {
        archivo = null;
        File archivoFisico = new File(nombreArchivo);
        this.registro = registro;
        if(!archivoFisico.exists()){
            throw new FileNotFoundException("Error. El fichero indicado no existe");
        } else {
            archivo = new RandomAccessFile(nombreArchivo, modo);
            return;
        }
    }

    public Archivo(Registro registro, String nombreArchivo)
        throws FileNotFoundException
    {
        archivo = null;
        File archivoFisico = new File(nombreArchivo);
        setRegistro(registro);
        if(archivoFisico.exists())
            archivoFisico.delete();
        archivo = new RandomAccessFile(nombreArchivo, "rw");
    }

    public void cerrarArchivo() throws IOException {
        archivo.close();
    }

    public Registro getRegistro()   {
        return registro;
    }

    public void setRegistro(Registro registro){
        this.registro = registro;
    }

    protected void escribirRegistro(int posicion)  throws IOException {
        archivo.seek(posicion * registro.longitudRegistro());
        registro.escribir(archivo);
    }

    public void leerRegistro(int posicion) throws IOException
    {
        if((long)posicion > numRegistros())
            archivo.seek(archivo.length() - (long)registro.longitudRegistro());
        else
            archivo.seek(posicion * registro.longitudRegistro());
        registro.leer(archivo);
    }

    public long numRegistros() throws IOException {
        long num = archivo.length() / (long)registro.longitudRegistro();
        return num;
    }

    public void volcar() throws IOException {
        int pos = 0;
        archivo.seek(0L);
        while(archivo.getFilePointer() < archivo.length()) {
            leerRegistro(pos);
            System.out.println(registro.toString());
            pos++;
        }
    }
   
}
