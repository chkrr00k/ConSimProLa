package languages.operators;

import languages.visitor.ExpVisitor;

public class Line extends Instruction {

	private Exp e;
	
	public Line(Exp e) {
		this.e = e;
	}

	@Override
	public String name() {
		return ";";
	}

	@Override
	public String toString() {
		return e + " " + this.name();
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

	public Exp getExp() {
		return this.e;
	}

}
