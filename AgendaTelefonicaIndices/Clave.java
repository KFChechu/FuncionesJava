package tablaAgenda;

class Clave{
    private
	String  clave;
	int direccion;
Clave() {
	clave = "";
	direccion = 0;
}

Clave (String c) {
	clave = c;
	direccion= 0;
}

Clave (String c, int d) {
	clave = c;
	direccion= d;
}

int getDire(){
return direccion;
}
void setDire(int dir){
	direccion=dir;
}

String getClave(){
	return clave;
}

void setClave(String cla){
	clave= cla;
}

boolean tieneDatos() {
	boolean resul = false;
	if (clave != "" && direccion != 0)
		resul = true;
	return resul;
}

}