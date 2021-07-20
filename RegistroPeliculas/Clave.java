package paqueteP;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Clave{
	private int valor;
    private int posicion;

    public Clave(){
        valor = -1;
        posicion = -1;
    }

    public Clave(int valor){
        this.valor = valor;
        posicion = -1;
    }

    public Clave(int valor, int posicion){
        this.valor = valor;
        this.posicion = posicion;
    }

    public Clave(Clave clave){
        this(clave.valor, clave.posicion);
    }

    public int getValor(){
        return valor;
    }

    public int getPosicion(){
        return posicion;
    }

    public void leer(RandomAccessFile archivo)throws IOException{
        valor = archivo.readInt();
        posicion = archivo.readInt();
    }

    public void escribir(RandomAccessFile archivo)throws IOException {
        archivo.writeInt(valor);
        archivo.writeInt(posicion);
    }

    public int longitud(){
    	//Dos enteros necesitan 8 bytes
        return 8;
    }

    public int comparaCon(Clave clave){
        int valor;
        if(this.valor == clave.valor)
            valor = 0;
        else
        	if(this.valor < clave.valor)
        		valor = -1;
        	else
        		valor = 1;
        return valor;
    }
}
