package languages.operators;

import languages.visitor.ExpVisitor;

public class AndExp extends OpExp {

	public AndExp(Exp l, Exp r) {
		super(l, r);
	}

	@Override
	public String name() {
		return "and";
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

}
