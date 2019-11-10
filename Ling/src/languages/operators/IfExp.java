package languages.operators;

import java.util.Optional;

import languages.visitor.ExpVisitor;

public class IfExp extends Exp {

	private Exp cond;
	private Block posCond;
	private Optional<Block> negCond;

	public IfExp(Exp cond, Block posCond) {
		this.cond = cond;
		this.posCond = posCond;
		this.negCond = Optional.empty();
	}
	public void addElse(Block negCond){
		this.negCond = Optional.of(negCond);
	}
	
	public Exp getCond() {
		return this.cond;
	}
	public Block getPosCond() {
		return this.posCond;
	}
	public Optional<Block> getNegCond() {
		return this.negCond;
	}
	
	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

	@Override
	public String name() {
		return "if";
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("if(");
		builder.append(this.cond);
		builder.append(")");
		builder.append(this.posCond);
		if(this.negCond.isPresent()){ 
			builder.append("else");
			builder.append(this.negCond.get());
		}
		return builder.toString();
	}
	
	

}
