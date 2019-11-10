package languages.operators;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import languages.visitor.ExpVisitor;

public class Program {
	private List<Instruction> instructions;

	public Program() {
		this.instructions = new LinkedList<Instruction>();
	}
	
	public List<Instruction> getInstructions() {
		return this.instructions;
	}
	public void add(Instruction i){
		this.instructions.add(i);
	}
	
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

	public String name() {
		return "{}";
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		builder.append(String.join("", this.instructions.stream().map(e -> e.toString()).collect(Collectors.toList())));
		builder.append("}");
		return builder.toString();
	}
	
	
}
