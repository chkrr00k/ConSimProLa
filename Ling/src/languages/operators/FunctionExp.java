package languages.operators;

import languages.visitor.ExpVisitor;

public class FunctionExp extends Instruction {

	private LambdaExp inner;
	private String name;
	
	public FunctionExp() {
		
	}
	public void accept(ExpVisitor v){
		v.visit(this);
	}
	public String name(){
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void add(LambdaExp l) {
		this.inner = l;
	}
	public LambdaExp getInner() {
		return this.inner;
	}
	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("fun ");
		builder.append(this.name);
		builder.append(this.inner);
		return builder.toString();
	}
	
	

}
