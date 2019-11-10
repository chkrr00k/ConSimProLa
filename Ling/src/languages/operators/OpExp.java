package languages.operators;

public abstract class OpExp extends Exp {
	private Exp left, right;
	protected OpExp( Exp l, Exp r){ 
		this.left=l; 
		this.right=r;
	}
	
	public abstract String name();
	
	public Exp getLeft() {
		return this.left;
	}
	public Exp getRight() {
		return this.right;
	}

	public String toString(){ 
		return "( " + this.left.toString() + " " + this.name() + " " + this.right.toString() + " )";
	}
}
