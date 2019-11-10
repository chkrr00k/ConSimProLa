package languages.operators;

import languages.visitor.ExpVisitor;

public class NeqExp extends OpExp {

	public NeqExp(Exp l, Exp r) {
		super(l, r);
	}

	@Override
	public String name() {
		return "!=";
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

}
