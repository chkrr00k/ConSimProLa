package languages.operators;

import languages.visitor.ExpVisitor;

public class AssignExp extends OpExp{

	private boolean immutable;
	public AssignExp(Exp l, Exp r) {
		this(l, r, false);
	}

	public AssignExp(Exp l, Exp r, boolean b) {
		super(l, r);
		this.immutable = b;
	}

	@Override
	public String name() {
		return "=";
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}
	public boolean isImmutable() {
		return this.immutable;
	}
	

}
