package tablaAgenda;

//Clase main
public class Agenda{
    public static void main(String argumentos[]) throws Exception{
        TablaAgenda tabla=new TablaAgenda("Datos.dir");
        LectorTeclado lt = new LectorTeclado();
        char opcion;
    	Menu menu= new Menu();  
    	do{ 
    	   opcion = menu.dameOpcion();
    	   switch (opcion){
    	    case '1': //Da de alta un contacto
    	    	String nombre=lt.leeTexto("Nombre");
    	        int edad =lt.leeEntero("Edad: ");
    	        long telefono = lt.leeEnteroLargo("Telefono");
    	        System.out.println("Antes:");
    	        int nR = tabla.dameNumeroRegistros();
    	        nR++;
    	        tabla.escribeRegistro(nombre,edad,telefono,nR);
    	        System.out.println("Registro insertado");
    	    	break;    
    	    case '2': //Da de baja un contacto.
    	    	System.out.println("Borrar contactos");
    	    	if(tabla.dameNumeroRegistros()>0)
    	    	{
    	    	System.out.println(tabla.MuestraRegistros());
    	    	int nReg = lt.leeEntero("Introduzca el número de registro a borrar");
    	    	tabla.borraRegistro(nReg);
    	    	System.out.println("Registro borrado");
    	    	}
    	    	else
    	    		System.out.println("Ningún registro encontrado");
    	    	 break;    
    		case '3':
    			System.out.println("Lista contactos");
    			System.out.println(tabla.MuestraRegistros());
    			System.out.println("Registros listados");
    			break;
    		case '4':
    			System.out.println("Salir");
    			tabla.cierraTabla();
    			break;
    		default:
    		 System.out.println("La opcion es incorreta");
    	    break;
    	   }
    	 }while(opcion != '4');
    	 System.out.println("Fin de la aplicación");
    }
}
