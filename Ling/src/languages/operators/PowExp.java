package languages.operators;

import languages.visitor.ExpVisitor;

public class PowExp extends OpExp{
	public PowExp(Exp l, Exp r) {
		super(l, r);
	}
	@Override
	public String name() {
		return "^";
	}
	
	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}
}
