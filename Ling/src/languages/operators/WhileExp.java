package languages.operators;

import languages.visitor.ExpVisitor;

public class WhileExp extends Exp {

	private Exp cond;
	private Block posCond;

	public WhileExp(Exp cond, Block posCond) {
		this.cond = cond;
		this.posCond = posCond;
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

	public Exp getCond() {
		return this.cond;
	}
	public Block getPosCond() {
		return this.posCond;
	}

	@Override
	public String name() {
		return "while";
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("while (");
		builder.append(this.cond);
		builder.append("){");
		builder.append(this.posCond);
		builder.append("}");
		return builder.toString();
	}
	

}
