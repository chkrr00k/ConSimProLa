package languages.operators;

import languages.visitor.ExpVisitor;

public class Field {

	private String base;
	private String id;
	private Exp value;
	public final boolean isTopValue;

	public Field(String id, Exp value) {
		this.id = id;
		this.value = value;
		this.base = "";
		this.isTopValue = false;
	}


	public Field(Exp value) {
		this.value = value;
		this.isTopValue = true;
		this.base = "";
		this.id = "";
	}


	public String getName() {
		return this.base + (this.id.isEmpty() ? "" :  "." + this.id);
	}
	public Exp getValue() {
		return this.value;
	}
	public String getBase() {
		return this.base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public boolean isNested(){
		return this.getValue() instanceof ObjAssignExp;
	}
	
	public void accept(ExpVisitor v) {
		v.visit(this);
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.id);
		builder.append("=>");
		builder.append(this.value);
		return builder.toString();
	}
	

}
