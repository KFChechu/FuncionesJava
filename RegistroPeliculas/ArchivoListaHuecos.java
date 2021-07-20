// Gestiona en el fichero de datos la parte del control de huecos

package paqueteP;
import java.io.*;

public class ArchivoListaHuecos extends Archivo {

    public ArchivoListaHuecos(RegistroControlListaHuecos registro, String nombreArchivo, String modo)
        throws FileNotFoundException {
        super(registro, nombreArchivo, modo);
    }

    public ArchivoListaHuecos(RegistroControlListaHuecos registro, String nombreArchivo)
        throws FileNotFoundException {
        super(registro, nombreArchivo);
        try {
            super.archivo.seek(0L);
            registro.setControl(-1);
            escribirRegistroLH(registro);
        }
        catch(IOException ioe)
        {
            System.out.println("Error fatal en el segundo constructor de la clase ArchivoListaHuecos");
            System.exit(-1);
        }
    }

    public void leerRegistro(int posicion) throws IOException {
        if((long)posicion > numRegistros() || posicion < 1)
            posicion = (int)numRegistros();
        super.leerRegistro(posicion);
    }

    protected RegistroControlListaHuecos leerRegistroLH(int posicion)
        throws IOException
    {
        RegistroControlListaHuecos registroAux = new RegistroControlListaHuecos();
        if((long)posicion > numRegistros() || posicion < 1)
            posicion = (int)numRegistros();
        super.archivo.seek(posicion * super.registro.longitudRegistro());
        registroAux.leer(super.archivo);
        return registroAux;
    }

    public int escribirRegistro() throws IOException {
        int cabeceraLH = leerControl(0);
        int posicionRegistro;
        if(cabeceraLH == -1){
            posicionRegistro = (int)numRegistros() + 1;
        } else {
            int controlSiguiente = leerControl(cabeceraLH);
            posicionRegistro = cabeceraLH;
            escribirControl(0, controlSiguiente);
        }
        super.escribirRegistro(posicionRegistro);
        return posicionRegistro;
    }

    protected void escribirRegistroLH(RegistroControlListaHuecos registroLH) throws IOException {
        registroLH.escribir(super.archivo);
    }

    public void borrarRegistro(int posicion) throws IOException {
        if(posicion > 0 && (long)posicion <= numRegistros()) {
            RegistroControlListaHuecos registroAux = leerRegistroLH(posicion);
            if(registroAux.estaOcupado()) {
                escribirControl(posicion, leerControl(0));
                escribirControl(0, posicion);
            }
        }
    }

    public long numRegistros() throws IOException {
        return super.archivo.length() / (long)super.registro.longitudRegistro() - 1L;
    }

    public void volcar() throws IOException {
        super.archivo.seek(0L);
        int cabeceraLH = super.archivo.readInt();
        System.out.println((new StringBuilder("Cabecera de la lista de huecos: ")).append(cabeceraLH).append(". Registros en uso(ocupados+libres): ").append(numRegistros()).toString());
        for(int pos = 1; (long)pos <= numRegistros(); pos++){
            leerRegistro(pos);
            System.out.println((new StringBuilder("Registro ")).append(pos).append(";   ").append(super.registro.toString()).toString());
        }

        System.out.println("****************** FIN DEL VOLCADO ***********************");
    }

    private void escribirControl(int posicion, int valor) throws IOException {
        RegistroControlListaHuecos regAux = new RegistroControlListaHuecos(valor);
        super.archivo.seek(posicion * super.registro.longitudRegistro());
        escribirRegistroLH(regAux);
    }

    private int leerControl(int posicion) throws IOException {
        RegistroControlListaHuecos regAux = new RegistroControlListaHuecos();
        super.archivo.seek(posicion * super.registro.longitudRegistro());
        regAux.leer(super.archivo);
        return regAux.getControl();
    }
}
