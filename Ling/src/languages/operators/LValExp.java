package languages.operators;

import languages.visitor.ExpVisitor;

public class LValExp extends Exp{

	private String name;

	public LValExp(String name) {
		this.name = name;
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
		builder.append(this.name);
		return builder.toString();
	}
	
	

}
