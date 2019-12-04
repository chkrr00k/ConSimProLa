package languages.operators;

import java.util.List;
import java.util.LinkedList;

import languages.visitor.ExpVisitor;
@Deprecated
public class Field {

	private List<String> base;
	private String id;
	private Exp value;
	public final boolean isTopValue;

	public Field(String id, Exp value) {
		this.id = id;
		this.value = value;
		this.base = new LinkedList<String>();
		this.isTopValue = false;
	}


	public Field(Exp value) {
		this.value = value;
		this.isTopValue = true;
		this.base = new LinkedList<String>();
		this.id = "";
	}


	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.base + (this.id.isEmpty() ? "" :  "." + this.id);
	}
	public Exp getValue() {
		return this.value;
	}
	public List<String> getBase() {
		return this.base;
	}
	public String getParent() {
		return this.base.get(0);
	}


	public void addBase(String base) {
		this.base.add(base);
		if(this.isNested()){
		//	((ObjAssignExp) this.getValue()).addParent(base);
		}
		
	}
	public boolean isNested(){
		return this.getValue() instanceof ObjAssignExp;
	}
	public boolean isValue(){
		return this.id.isEmpty();
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
