package paqueteP;
import java.io.*;

/*La clase ArbolB gestiona el fichero de índices en disco usando la estructura de ArbolB
 * Esta clase hereda de la clase ArchivoListaHuecos las utilidades de gestión 
 * de los huecos generados o suprimidos en el proceso de borrado e inserción de nodos del árbol y de los ficheros de datos.
 * Los nodos del árbol están constituidos por objetos de la clase Pagina, 
 * y contienen objetos de la clase Clave que soportan el valor de la clave y el número de registro del resto de los datos en el fichero de datos. 
 */
 

public class ArbolB extends ArchivoListaHuecos {
	private int posicionRaiz;
    private int orden;
    private Pagina paginaRaiz;
   
    public ArbolB(String nombre, int orden)throws FileNotFoundException, IOException {
        super(new Pagina(orden), nombre);
        paginaRaiz = new Pagina(orden);
        posicionRaiz = escribirRegistroLH(paginaRaiz);
        this.orden = orden;
        escribirOrdenyPosicionRaiz(orden, posicionRaiz);
    }

    public ArbolB(String nombre, String modo)throws FileNotFoundException, IOException {
        super(new Pagina(1), nombre, modo);
        leerOrdenyPosicionRaiz();
        super.setRegistro(new Pagina(orden));
        paginaRaiz = (Pagina)leerRegistroLH(posicionRaiz);
    }

    public int getPosicionRaiz(){
        return posicionRaiz;
    }

    public int getOrden(){
        return orden;
    }
     
    protected RegistroControlListaHuecos leerRegistroLH(int i) throws IOException {
        Pagina pagina = new Pagina(orden);
        if((long)i > super.numRegistros() || i < 1)
            i = (int)super.numRegistros();
        super.archivo.seek(i * super.registro.longitudRegistro());
        pagina.leer(super.archivo);
        return pagina;
    }

    private void escribirRegistroLH(Pagina pagina, int posicion)throws IOException {
        super.archivo.seek(posicion * pagina.longitudRegistro());
        pagina.escribir(super.archivo);
    }

    private int escribirRegistroLH(Pagina pagina) throws IOException {
        super.registro = pagina;
        int posicionRegistro = super.escribirRegistro();
        return posicionRegistro;
    }

    public void volcar() throws IOException {
        System.out.println((new StringBuilder("Arbol B de orden: ")).append(orden).append(". La raiz se encuentra en el registro: ").append(posicionRaiz).toString());
        super.volcar();
    }

    public boolean vacio(){
        return paginaRaiz.getNumeroDeClaves() == 0;
    }

    public int buscar(Clave clave)throws IOException {
        int posicionRegistro;
        if(vacio())
            posicionRegistro = -1;
        else
            posicionRegistro = buscar(clave, posicionRaiz);
        return posicionRegistro;
    }

    private int buscar(Clave clave, int posicion) throws IOException {
        int posicionRegistro;
        if(posicion != Pagina.SIN_HIJO){
            Pagina pagina = (Pagina)leerRegistroLH(posicion);
            int posClave = pagina.buscarDescendiente(clave);
            if(posClave < pagina.getNumeroDeClaves()) {
                if(pagina.getClave(posClave).comparaCon(clave) == 0)
                    posicionRegistro = pagina.getClave(posClave).getPosicion();
                else
                    posicionRegistro = buscar(clave, pagina.getHijo(posClave));
            } else {
                posicionRegistro = buscar(clave, pagina.getHijo(pagina.getNumeroDeClaves()));
            }
        } else{
            posicionRegistro = -1;
        }
        return posicionRegistro;
    }

    public void insertar(Clave clave) throws IOException {
        if(vacio()){
            paginaRaiz.insertarSoloClave(clave);
            escribirRegistroLH(paginaRaiz, posicionRaiz);
        } else {
            ResultadoPromocion resultado = insertar(clave, posicionRaiz);
            if(resultado.hayPromocion()){
                paginaRaiz = new Pagina(orden, resultado.getClave(), posicionRaiz, resultado.getHijoDerecha());
                posicionRaiz = escribirRegistroLH(paginaRaiz);
                escribirPosicionRaiz(posicionRaiz);
            }
        }
    }

    private ResultadoPromocion insertar(Clave clave, int posicion) throws IOException {
        ResultadoPromocion resultado = new ResultadoPromocion();
        Pagina paginaActual = (Pagina)leerRegistroLH(posicion);
        int posicionHijo = paginaActual.buscarDescendiente(clave);
        if(posicionHijo == paginaActual.getNumeroDeClaves() || paginaActual.getClave(posicionHijo).comparaCon(clave) != 0)
            if(paginaActual.esHoja())
            {
                if(paginaActual.llena())
                {
                    resultado = promocionaClave(clave, paginaActual, posicion, Pagina.SIN_HIJO);
                } else
                {
                    paginaActual.insertarSoloClave(clave);
                    escribirRegistroLH(paginaActual, posicion);
                }
            } else
            {
                resultado = insertar(clave, paginaActual.getHijo(posicionHijo));
                if(resultado.hayPromocion())
                    if(paginaActual.llena())
                    {
                        resultado = promocionaClave(resultado.getClave(), paginaActual, posicion, resultado.getHijoDerecha());
                    } else
                    {
                        paginaActual.insertarClaveEHijo(resultado.getClave(), resultado.getHijoDerecha());
                        escribirRegistroLH(paginaActual, posicion);
                        resultado = new ResultadoPromocion();
                    }
            }
        return resultado;
    }

    private ResultadoPromocion promocionaClave(Clave clave, Pagina pagina, int numRegistroPagina, int registroDerecha)
        throws IOException
    {
        ResultadoPromocion resultado = new ResultadoPromocion();
        int posicionClave = pagina.buscarDescendiente(clave);
        Pagina paginaDerecha = new Pagina(orden);
        int mitad = pagina.getNumeroDeClaves() / 2;
        int j = 0;
        Clave claveAPromocionar;
        if(posicionClave < mitad)
        {
            for(int i = mitad; i < pagina.getNumeroDeClaves(); i++)
            {
                paginaDerecha.setClave(j, pagina.getClave(i));
                paginaDerecha.setHijo(j, pagina.getHijo(i));
                j++;
            }

            paginaDerecha.setHijo(j, pagina.getHijo(pagina.getNumeroDeClaves()));
            paginaDerecha.setNumeroDeClaves(mitad);
            claveAPromocionar = pagina.getClave(mitad - 1);
            pagina.eliminaClavesEHijos(mitad - 1);
            pagina.insertarClaveEHijo(clave, registroDerecha);
        } else
        if(posicionClave > mitad)
        {
            for(int i = mitad + 1; i < pagina.getNumeroDeClaves(); i++)
            {
                paginaDerecha.setClave(j, pagina.getClave(i));
                paginaDerecha.setHijo(j, pagina.getHijo(i));
                j++;
            }

            paginaDerecha.setHijo(j, pagina.getHijo(pagina.getNumeroDeClaves()));
            paginaDerecha.setNumeroDeClaves(mitad - 1);
            claveAPromocionar = pagina.getClave(mitad);
            pagina.eliminaClavesEHijos(mitad);
            paginaDerecha.insertarClaveEHijo(clave, registroDerecha);
        } else
        {
            for(int i = mitad; i < pagina.getNumeroDeClaves(); i++)
            {
                paginaDerecha.setClave(j, pagina.getClave(i));
                paginaDerecha.setHijo(j + 1, pagina.getHijo(i + 1));
                j++;
            }

            paginaDerecha.setHijo(0, registroDerecha);
            paginaDerecha.setNumeroDeClaves(mitad);
            claveAPromocionar = clave;
            pagina.eliminaClavesEHijos(mitad);
        }
        escribirRegistroLH(pagina, numRegistroPagina);
        int posicionDerecha = escribirRegistroLH(paginaDerecha);
        resultado = new ResultadoPromocion(claveAPromocionar, posicionDerecha);
        return resultado;
    }

    public void borrar(Clave clave)
        throws IOException
    {
        if(!vacio())
        {
            Clave claveEnHoja = eliminaOCambia(clave, posicionRaiz);
            if(claveEnHoja != null)
                clave = claveEnHoja;
            reequilibrar(clave, posicionRaiz, -1);
            paginaRaiz = (Pagina)leerRegistroLH(posicionRaiz);
            if(paginaRaiz.getNumeroDeClaves() == 0 && !paginaRaiz.esHoja())
            {
                int antiguaRaiz = posicionRaiz;
                posicionRaiz = paginaRaiz.getHijo(0);
                paginaRaiz = (Pagina)leerRegistroLH(posicionRaiz);
                escribirPosicionRaiz(posicionRaiz);
                super.borrarRegistro(antiguaRaiz);
            }
        }
    }

    private Clave eliminaOCambia(Clave clave, int posicion)
        throws IOException
    {
        Clave c = null;
        if(posicion != Pagina.SIN_HIJO)
        {
            Pagina paginaActual = (Pagina)leerRegistroLH(posicion);
            int posicionBajada = paginaActual.buscarDescendiente(clave);
            if(posicionBajada < paginaActual.getNumeroDeClaves())
            {
                if(paginaActual.getClave(posicionBajada).comparaCon(clave) == 0)
                {
                    if(paginaActual.esHoja())
                    {
                        paginaActual.borrarClave(posicionBajada);
                        escribirRegistroLH(paginaActual, posicion);
                    } else
                    {
                        int posicionMenorDeLasMayores = paginaMenorDeLasMayores(paginaActual.getHijo(posicionBajada + 1));
                        Pagina menorDeLasMayores = (Pagina)leerRegistroLH(posicionMenorDeLasMayores);
                        c = menorDeLasMayores.getClave(0);
                        paginaActual.setClave(posicionBajada, c);
                        menorDeLasMayores.borrarClave(0);
                        escribirRegistroLH(paginaActual, posicion);
                        escribirRegistroLH(menorDeLasMayores, posicionMenorDeLasMayores);
                    }
                } else
                {
                    c = eliminaOCambia(clave, paginaActual.getHijo(posicionBajada));
                }
            } else
            {
                c = eliminaOCambia(clave, paginaActual.getHijo(paginaActual.getNumeroDeClaves()));
            }
        }
        return c;
    }

    private int paginaMenorDeLasMayores(int posicion)
        throws IOException
    {
        Pagina paginaActual = (Pagina)leerRegistroLH(posicion);
        int numeroRegistro;
        if(paginaActual.esHoja())
            numeroRegistro = posicion;
        else
            numeroRegistro = paginaMenorDeLasMayores(paginaActual.getHijo(0));
        return numeroRegistro;
    }

    private void reequilibrar(Clave clave, int posicion, int posicionPadre) throws IOException {
        Pagina paginaActual =(Pagina)leerRegistroLH(posicion);
        int posicionBajada = paginaActual.buscarDescendiente(clave);
        if(!paginaActual.esHoja())
            if(posicionBajada < paginaActual.getNumeroDeClaves())
            {
                if(paginaActual.getClave(posicionBajada).comparaCon(clave) == 0)
                    reequilibrar(clave, paginaActual.getHijo(posicionBajada + 1), posicion);
                else
                    reequilibrar(clave, paginaActual.getHijo(posicionBajada), posicion);
            } else
            {
                reequilibrar(clave, paginaActual.getHijo(posicionBajada), posicion);
            }
        paginaActual = (Pagina)leerRegistroLH(posicion);
        if(posicionPadre != -1 && paginaActual.getNumeroDeClaves() < orden && !robarClave(paginaActual, posicion, clave, posicionPadre))
            juntar(paginaActual, posicion, clave, posicionPadre);
    }

    private boolean robarClave(Pagina paginaActual, int posicionActual, Clave clave, int posicionPadre)
        throws IOException {
        boolean seHaRobado = false;
        Pagina paginaPadre = (Pagina)leerRegistroLH(posicionPadre);
        int posicionBajada = paginaPadre.buscarDescendiente(clave);
        if(posicionBajada < paginaPadre.getNumeroDeClaves() && paginaPadre.getClave(posicionBajada).comparaCon(clave) == 0)
            posicionBajada++;
        int derecha = posicionBajada + 1;
        int izquierda = posicionBajada - 1;
        if(derecha <= paginaPadre.getNumeroDeClaves()){
            int posicionDerecha = paginaPadre.getHijo(derecha);
            Pagina paginaDerecha =(Pagina) leerRegistroLH(posicionDerecha);
            if(paginaDerecha.getNumeroDeClaves() > orden){
                Clave claveRobada = paginaDerecha.getClave(0);
                int hijoRobado = paginaDerecha.getHijo(0);
                paginaDerecha.borrarHijo(0);
                paginaDerecha.borrarClave(0);
                escribirRegistroLH(paginaDerecha, posicionDerecha);
                Clave clavePadre = paginaPadre.getClave(posicionBajada);
                paginaPadre.setClave(posicionBajada, claveRobada);
                escribirRegistroLH(paginaPadre, posicionPadre);
                paginaActual.insertarClaveEHijo(clavePadre, hijoRobado);
                escribirRegistroLH(paginaActual, posicionActual);
                seHaRobado = true;
            }
        }
        if(!seHaRobado && izquierda >= 0){
            int posicionIzquierda = paginaPadre.getHijo(izquierda);
            Pagina paginaIzquierda =(Pagina) leerRegistroLH(posicionIzquierda);
            if(paginaIzquierda.getNumeroDeClaves() > orden){
                Clave claveRobada = paginaIzquierda.getClave(paginaIzquierda.getNumeroDeClaves() - 1);
                int hijoRobado = paginaIzquierda.getHijo(paginaIzquierda.getNumeroDeClaves());
                paginaIzquierda.borrarHijo(paginaIzquierda.getNumeroDeClaves());
                paginaIzquierda.borrarClave(paginaIzquierda.getNumeroDeClaves() - 1);
                escribirRegistroLH(paginaIzquierda, posicionIzquierda);
                Clave clavePadre = paginaPadre.getClave(posicionBajada - 1);
                paginaPadre.setClave(posicionBajada - 1, claveRobada);
                escribirRegistroLH(paginaPadre, posicionPadre);
                paginaActual.insertarClaveEHijoEnCero(clavePadre, hijoRobado);
                escribirRegistroLH(paginaActual, posicionActual);
                seHaRobado = true;
            }
        }
        return seHaRobado;
    }

    private void juntar(Pagina paginaActual, int posicionActual, Clave clave, int posicionPadre)
        throws IOException {
        Pagina paginaPadre = (Pagina)leerRegistroLH(posicionPadre);
        int posicionBajada = paginaPadre.buscarDescendiente(clave);
        if(posicionBajada < paginaPadre.getNumeroDeClaves() && paginaPadre.getClave(posicionBajada).comparaCon(clave) == 0)
            posicionBajada++;
        int derecha = posicionBajada + 1;
        int izquierda = posicionBajada - 1;
        if(derecha <= paginaPadre.getNumeroDeClaves())
        {
            Clave clavePadre = paginaPadre.getClave(posicionBajada);
            int posicionDerecha = paginaPadre.getHijo(derecha);
            Pagina paginaDerecha = (Pagina)leerRegistroLH(posicionDerecha);
            paginaPadre.borrarHijo(posicionBajada + 1);
            paginaPadre.borrarClave(posicionBajada);
            escribirRegistroLH(paginaPadre, posicionPadre);
            paginaActual.FundirClaveyPaginaDerecha(clavePadre, paginaDerecha);
            escribirRegistroLH(paginaActual, posicionActual);
            super.borrarRegistro(posicionDerecha);
        } else
        {
            Clave clavePadre = paginaPadre.getClave(posicionBajada - 1);
            int posicionIzquierda = paginaPadre.getHijo(izquierda);
            Pagina paginaIzquierda = (Pagina)leerRegistroLH(posicionIzquierda);
            paginaPadre.borrarHijo(posicionBajada);
            paginaPadre.borrarClave(posicionBajada - 1);
            escribirRegistroLH(paginaPadre, posicionPadre);
            paginaIzquierda.FundirClaveyPaginaDerecha(clavePadre, paginaActual);
            escribirRegistroLH(paginaIzquierda, posicionIzquierda);
            super.borrarRegistro(posicionActual);
        }
    }

    private void escribirPosicionRaiz(int posicionRaiz) throws IOException {
        super.archivo.seek(8L);
        super.archivo.writeInt(posicionRaiz);
    }

    private void escribirOrdenyPosicionRaiz(int orden, int posicionRaiz)throws IOException {
        super.archivo.seek(4L);
        super.archivo.writeInt(orden);
        super.archivo.writeInt(posicionRaiz);
    }

    private void leerOrdenyPosicionRaiz() throws IOException {
        super.archivo.seek(4L);
        orden = super.archivo.readInt();
        posicionRaiz = super.archivo.readInt();
    }

    class ResultadoPromocion {
    	private boolean promocion;
        private Clave clave;
        private int hijoDerecha;
        
        ResultadoPromocion(){
            super();
            promocion = false;
            clave = null;
            hijoDerecha = Pagina.SIN_HIJO;
        }

        ResultadoPromocion(Clave clave, int hijoDerecha){
            super();
            promocion = true;
            this.clave = clave;
            this.hijoDerecha = hijoDerecha;
        }
        
        public boolean hayPromocion(){
            return promocion;
        }

        public Clave getClave(){
            return clave;
        }

        public int getHijoDerecha(){
            return hijoDerecha;
        }
    }
}
