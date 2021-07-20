package paqueteH;
class ElementoHash {
	private String clave;
		
	ElementoHash (String clave){
		this.clave=clave;
	}

	String getClave(){
		return clave;
	}

	void setFicha (String clave){
		this.clave=clave;
	}

	String verElementoHash(){
		return getClave();
	}
}
