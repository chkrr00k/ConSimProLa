package languages.operators;

import languages.visitor.ExpVisitor;

public class RValExp extends Exp{

	private String name;
		
	public RValExp(String name) {
		this.name = name;
	}

	public double getValue() {
		return 2;
	}
	public String getName() {
		return this.name;
	}


	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

	@Override
	public String name() {
		return this.getName();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("$");
		builder.append(this.name);
		return builder.toString();
	}
	

}
