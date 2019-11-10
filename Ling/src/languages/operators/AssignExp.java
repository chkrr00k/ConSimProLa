package languages.operators;

import languages.visitor.ExpVisitor;

public class AssignExp extends OpExp{

	public AssignExp(Exp l, Exp r) {
		super(l, r);
	}

	@Override
	public String name() {
		return "=";
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

}
