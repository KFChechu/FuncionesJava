//Soporta la estructura de un nodo en el Arbol B
package paqueteP;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Pagina extends RegistroControlListaHuecos{
	public static int SIN_HIJO = -1;
    /*el atributo orden es la mitad del orden del arbol*/
	private int orden;
    private Clave claves[];
    private int hijos[];
    private int numeroDeClaves;
    
    public Pagina(int orden){
        super.setControl(-2);
        this.orden = orden;
        claves = new Clave[2 * orden];
        for(int i = 0; i < claves.length; i++)
            claves[i] = new Clave(0);

        hijos = new int[2 * orden + 1];
        for(int i = 0; i < hijos.length; i++)
            hijos[i] = SIN_HIJO;

        numeroDeClaves = 0;
    }

    public Pagina(int orden, Clave clave, int izquierda, int derecha){
        this(orden);
        claves[0] = new Clave(clave);
        hijos[0] = izquierda;
        hijos[1] = derecha;
        numeroDeClaves = 1;
    }

    public Pagina(Pagina pagina){
        super.setControl(pagina.getControl());
        orden = pagina.orden;
        numeroDeClaves = pagina.numeroDeClaves;
        claves = new Clave[pagina.claves.length];
        hijos = new int[pagina.hijos.length];
        for(int i = 0; i < claves.length; i++)
        {
            claves[i] = new Clave(pagina.claves[i]);
            hijos[i] = pagina.hijos[i];
        }

        hijos[claves.length] = pagina.hijos[claves.length];
    }

    public int getHijo(int posicion){
        return hijos[posicion];
    }

    public void setHijo(int posicion, int hijo){
        hijos[posicion] = hijo;
    }

    public Clave getClave(int posicion){
        return new Clave(claves[posicion]);
    }

    public void setClave(int posicion, Clave clave){
        claves[posicion] = new Clave(clave);
    }

    public int getNumeroDeClaves(){
        return numeroDeClaves;
    }

    public void setNumeroDeClaves(int numeroDeClaves){
        this.numeroDeClaves = numeroDeClaves;
    }

    public boolean vacia(){
        boolean estaVacia;
        if(numeroDeClaves == 0)
            estaVacia = true;
        else
            estaVacia = false;
        return estaVacia;
    }

    public boolean llena(){
        boolean estaLlena;
        if(numeroDeClaves == 2 * orden)
            estaLlena = true;
        else
            estaLlena = false;
        return estaLlena;
    }

    public void leer(RandomAccessFile archivo) throws IOException{
        super.leer(archivo);
        for(int i = 0; i < hijos.length; i++)
            hijos[i] = archivo.readInt();

        numeroDeClaves = archivo.readInt();
        for(int i = 0; i < claves.length; i++)
            claves[i].leer(archivo);

    }

    public void escribir(RandomAccessFile archivo) throws IOException{
        super.escribir(archivo);
        for(int i = 0; i < hijos.length; i++)
            archivo.writeInt(hijos[i]);

        archivo.writeInt(numeroDeClaves);
        for(int i = 0; i < claves.length; i++)
            claves[i].escribir(archivo);

    }

    public int longitudRegistro(){
        return super.longitudRegistro() + claves.length * (new Clave()).longitud() + hijos.length * 4 + 4;
    }

    public int buscarDescendiente(Clave clave){
        int posicion;
        for(posicion = 0; posicion < numeroDeClaves && claves[posicion].comparaCon(clave) == -1; posicion++);
        return posicion;
    }

    public String toString(){
        String cadena = new String("");
        if(!super.estaOcupado())
            cadena = (new StringBuilder(String.valueOf(cadena))).append("Registro vacio, control: ").append(super.getControl()).toString();
        else if(vacia()){
            cadena = "Pagina sin claves";
        } else {
            cadena = (new StringBuilder(String.valueOf(cadena))).append("Claves: [").toString();
            for(int i = 0; i < numeroDeClaves; i++)
                cadena = (new StringBuilder(String.valueOf(cadena))).append(claves[i].getValor()).append("|").append(claves[i].getPosicion()).append(",").toString();

            cadena = (new StringBuilder(String.valueOf(cadena))).append("], Hijos: [").toString();
            for(int i = 0; i <= numeroDeClaves; i++)
                cadena = (new StringBuilder(String.valueOf(cadena))).append(hijos[i]).append(",").toString();

            cadena = (new StringBuilder(String.valueOf(cadena))).append("], Numero de claves: ").append(numeroDeClaves).toString();
        }
        return cadena;
    }

    public boolean esHoja(){
        return hijos[0] == SIN_HIJO;
    }

    public void insertarSoloClave(Clave clave){
        int posicion;
        for(posicion = 0; posicion < numeroDeClaves && claves[posicion].comparaCon(clave) == -1; posicion++);
        if(posicion == numeroDeClaves || claves[posicion].comparaCon(clave) != 0)
        {
            for(int i = numeroDeClaves - 1; i >= posicion; i--)
                claves[i + 1] = claves[i];

            claves[posicion] = new Clave(clave);
            numeroDeClaves++;
        }
    }

    public void insertarClaveEHijo(Clave clave, int hijo){
        int i;
        for(i = 0; claves[i].comparaCon(clave) == -1 && i < numeroDeClaves; i++);
        if(claves[i].comparaCon(clave) != 0){
            for(int j = numeroDeClaves - 1; j >= i; j--)
            {
                claves[j + 1] = claves[j];
                hijos[j + 2] = hijos[j + 1];
            }

            claves[i] = new Clave(clave);
            numeroDeClaves++;
            hijos[i + 1] = hijo;
        }
    }

    public void insertarClaveEHijoEnCero(Clave clave, int hijo){
        for(int j = numeroDeClaves - 1; j >= 0; j--)
        {
            claves[j + 1] = claves[j];
            hijos[j + 2] = hijos[j + 1];
        }

        hijos[1] = hijos[0];
        claves[0] = new Clave(clave);
        hijos[0] = hijo;
        numeroDeClaves++;
    }

    public void FundirClaveyPaginaDerecha(Clave clavePadre, Pagina paginaDerecha){
        claves[numeroDeClaves] = clavePadre;
        hijos[numeroDeClaves + 1] = paginaDerecha.getHijo(0);
        int i = numeroDeClaves + 1;
        for(int j = 0; j < paginaDerecha.getNumeroDeClaves(); j++)
        {
            claves[i] = paginaDerecha.getClave(j);
            hijos[i + 1] = paginaDerecha.getHijo(j + 1);
            i++;
        }

        numeroDeClaves = numeroDeClaves + 1 + paginaDerecha.getNumeroDeClaves();
    }

    public void eliminaClavesEHijos(int posicion){
        for(int i = posicion; i < numeroDeClaves; i++){
            setClave(i, new Clave());
            setHijo(i + 1, SIN_HIJO);
        }

        setNumeroDeClaves(posicion);
    }

    public void borrarClave(int posicion){
        for(int i = posicion; i < numeroDeClaves - 1; i++)
            claves[i] = claves[i + 1];

        claves[numeroDeClaves - 1] = new Clave();
        numeroDeClaves--;
    }

    public void borrarHijo(int posicion){
        for(int i = posicion; i < numeroDeClaves; i++)
            hijos[i] = hijos[i + 1];

        hijos[numeroDeClaves] = SIN_HIJO;
    }

}
