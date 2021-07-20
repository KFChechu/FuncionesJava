package paqueteH;
public class ExcepcionHash extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public ExcepcionHash(){
		super("Error Hash");
	}
	public ExcepcionHash(String mensaje){
		super(mensaje);
	}
}
