package languages.operators;

import languages.visitor.ExpVisitor;

public class PresenceExp extends Exp {
	private String id;
	public PresenceExp(String id) {
		this.id = id;
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

	@Override
	public String name() {
		return "?";
	}
	public String getId() {
		return this.id;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("?");
		builder.append(this.id);
		return builder.toString();
	}
	

}
