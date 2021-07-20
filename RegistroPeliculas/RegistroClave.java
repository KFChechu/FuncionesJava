// Gestiona en los ficheros la parte de la clave que identifica a una entidad de datos
package paqueteP;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RegistroClave extends RegistroControlListaHuecos{
	private int valorClave;
    public RegistroClave(){
        setValorClave(0);
    }

    public RegistroClave(int control, int valorClave){
        //super(control);
        setValorClave(valorClave);
    }

    public void setValorClave(int valorClave){
        this.valorClave = valorClave;
    }

    public int getValorClave(){
        return valorClave;
    }

    public int longitudRegistro(){
        return 4 + super.longitudRegistro();
    }

    public void escribir(RandomAccessFile archivo)throws IOException {
        super.escribir(archivo);
        archivo.writeInt(getValorClave());
    }

    public void leer(RandomAccessFile archivo)throws IOException {
        super.leer(archivo);
        setValorClave(archivo.readInt());
    }

    public String toString(){
        return "RegistroClave * [control="+super.getControl()+", valorClave = "+getValorClave()+"]";
    }
}
