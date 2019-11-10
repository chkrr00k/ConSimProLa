package languages.operators;

import languages.visitor.ExpVisitor;

public class NotExp extends UnaryExp {

	public NotExp(Exp target) {
		super(target);
	}

	@Override
	public String name() {
		return "!";
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

}
