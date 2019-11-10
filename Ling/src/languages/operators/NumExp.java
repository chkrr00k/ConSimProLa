package languages.operators;

import languages.visitor.ExpVisitor;

public class NumExp extends Exp{
	private double val;

	public NumExp(double val2) {
		this.val = val2;
	}

	public double getVal() {
		return this.val;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.val);
		return builder.toString();
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

	@Override
	public String name() {
		return "" + this.val;
	}
	
}
