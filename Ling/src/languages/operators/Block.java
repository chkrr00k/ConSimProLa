package languages.operators;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import languages.visitor.ExpVisitor;

public class Block extends Instruction {

	private List<Instruction> lines;
		
	public Block() {
		this.lines = new LinkedList<Instruction>();
	}
	
	public void add(Instruction l){
		this.lines.add(l);
	}
	public List<Instruction> getLines() {
		return this.lines;
	}


	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

	@Override
	public String name() {
		return "{}";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		builder.append(String.join("", this.lines.stream().map(e -> e.toString()).collect(Collectors.toList())));
		builder.append("}");
		return builder.toString();
	}

	

}
