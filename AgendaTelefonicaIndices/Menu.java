package tablaAgenda;

public class Menu{
   char opcion;
   LectorTeclado teclado;
   String texto;
   Menu(){
      teclado = new LectorTeclado();
   }
   public char dameOpcion(){
      System.out.println("\nOpciones de la Agenda\n");
      System.out.println("1.-Altas");
      System.out.println("2.-Bajas");
      System.out.println("3.-Listados");
      System.out.println("4.-Salir");
      texto = teclado.leeTexto("Selecciona una opción");
      opcion = texto.charAt(0);     
      return opcion;
   }
}