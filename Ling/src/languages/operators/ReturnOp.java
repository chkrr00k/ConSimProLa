package languages.operators;

import languages.visitor.ExpVisitor;

public class ReturnOp extends Instruction {

	private String id;
	private Exp exp;
	private boolean ex;
	
	public ReturnOp(String id) {
		this.id = id;
		this.exp = null;
		this.ex = false;
	}

	public ReturnOp(Exp e) {
		this.exp = e;
		this.id = null;
		this.ex = true;
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}
	public boolean hasExp(){
		return this.ex;
	}
	@Override
	public String name() {
		return this.id;
	}
	public Exp getExp() {
		return this.exp;
	}

	public String getId() {
		return this.id;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("return ");
		builder.append(this.id);
		builder.append(";");
		return builder.toString();
	}
	

}
