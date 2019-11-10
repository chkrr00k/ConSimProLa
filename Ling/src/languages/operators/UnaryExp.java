package languages.operators;

public abstract class UnaryExp extends Exp {
	
	private Exp target;
	
	protected UnaryExp(Exp target){
		this.target = target;
	}
	
	public abstract String name();
	
	public String toString(){ 
		return this.name() + " " + this.target.toString() + " )";
	}

	public Exp getTarget() {
		return this.target;
	}

}
