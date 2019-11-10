package languages.operators;

import languages.visitor.ExpVisitor;

public class OrExp extends OpExp {

	public OrExp(Exp l, Exp r) {
	super(l, r);
}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

	@Override
	public String name() {
		return "or";
	}

}
