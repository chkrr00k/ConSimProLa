package languages.operators;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.util.Pair;
import languages.visitor.ExpVisitor;

public class WhenExp extends Instruction {

	private List<Pair<Exp, Block>> instr;
	
	public WhenExp() {
		super();
		this.instr = new LinkedList<Pair<Exp,Block>>();
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

	@Override
	public String name() {
		return "when";
	}

	public void add(Exp cond, Block posBlock) {
		this.instr.add(new Pair<Exp, Block>(cond, posBlock));
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("when { ");
		builder.append(String.join("", this.instr.stream().map(e -> e.getKey() + " then " + e.getValue()).collect(Collectors.toList())));
		builder.append(" }");
		return builder.toString();
	}

	public List<Pair<Exp, Block>> getPairs() {
		return this.instr;
	}
	

}
