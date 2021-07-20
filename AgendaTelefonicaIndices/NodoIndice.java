package tablaAgenda;

public class NodoIndice{
    Clave info;
    NodoIndice sig;

   NodoIndice(Clave cla) {
		info = cla;
		sig = null;
	}

	NodoIndice(Clave elemento, NodoIndice n) {
		info = elemento;
		sig = n;
	}
}