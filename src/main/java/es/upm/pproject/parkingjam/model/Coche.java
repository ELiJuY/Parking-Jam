package es.upm.pproject.parkingjam.model;

public class Coche {
	private char id;
	private boolean isRedCar;
	
	private Casilla[] casillas;
	private boolean horizontal;
	
	
	public Coche(char id, Casilla[] casillas, boolean horizontal){
		this.id = id;
		this.horizontal = horizontal;
		this.casillas=casillas;
		setRedCar(false);
	}
	

	public void setCasillas(Casilla[] casillas) {
		this.casillas = casillas;
	}


	public char getId() {
		return id;
	}


	public void setId(char id) {
		this.id = id;
	}





	public Casilla[] getCasillas() {
		return this.casillas;
	}


	

	public int getSize() {
		return this.casillas.length;
	}

	public boolean isHorizontal() {
		return horizontal;
	}


	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
	}


	public boolean isRedCar() {
		return isRedCar;
	}


	public final void setRedCar(boolean isRedCar) {
		this.isRedCar = isRedCar;
	}


	
	
	
	
}

