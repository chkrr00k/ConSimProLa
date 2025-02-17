package languages.operators;

import languages.visitor.ExpVisitor;

public class MinusExp extends OpExp{
	public MinusExp(Exp l, Exp r) {
		super(l == null ? new NumExp(0d) : l, r);
	}
	@Override
	public String name() {
		return "-";
	}
	
	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}	
}

