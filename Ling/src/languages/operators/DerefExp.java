package languages.operators;

import languages.visitor.ExpVisitor;

public class DerefExp extends Exp {
	private String id;
	
	public DerefExp(String id) {
		this.id = id;
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

	@Override
	public String name() {
		return this.id;
	}

	public String getId() {
		return this.id;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("&");
		builder.append(this.id);
		return builder.toString();
	}
	

}
