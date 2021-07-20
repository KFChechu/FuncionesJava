// Gestiona en las tablas los espacios vacios procedentes de la eliminacion de registros
package paqueteP;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RegistroControlListaHuecos extends Registro{
    public static final int FIN_LISTA = -1;
    public static final int REGISTRO_OCUPADO = -2;
    private static final int TAMANIO_REGISTRO = 4;
    private int control;


    public RegistroControlListaHuecos(){
        setControl(-2);
    }

    public RegistroControlListaHuecos(int control){
        setControl(control);
    }

    public void setControl(int control){
        this.control = control;
    }

    public int getControl(){
        return control;
    }

    public int longitudRegistro(){
        return TAMANIO_REGISTRO + super.longitudRegistro();
    }

    public boolean estaOcupado(){
        return control == -2;
    }

    public void escribir(RandomAccessFile archivo)throws IOException{
        archivo.writeInt(getControl());
    }

    public void leer(RandomAccessFile archivo) throws IOException{
        setControl(archivo.readInt());
    }

    public String toString(){
        return "RegistroControlListaHuecos [control="+getControl()+"]";
    }
}
