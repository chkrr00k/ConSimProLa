package languages.operators;

import languages.visitor.ExpVisitor;

public class ModExp extends OpExp {

	public ModExp(Exp l, Exp r) {
		super(l, r);
	}

	@Override
	public String name() {
		return "%";
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);

	}

}
