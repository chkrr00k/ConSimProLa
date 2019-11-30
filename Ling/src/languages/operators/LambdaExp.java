package languages.operators;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import languages.visitor.ExpVisitor;

public class LambdaExp {

	private List<String> arguments;
	private Block b;
	
	public LambdaExp(){
		this.arguments = new LinkedList<String>();
	}
	public void addArgument(String string) {
		this.arguments.add(string);
	}

	public void add(Block b) {
		this.b = b;
	}

	public List<String> getArguments() {
		return this.arguments;
	}
	public Block getB() {
		return this.b;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		builder.append(String.join(", ", this.arguments.stream().map(e -> e.toString()).collect(Collectors.toList())));
		builder.append(")");
		builder.append(this.b);
		return builder.toString();
	}
	public void accept(ExpVisitor v){
		v.visit(this);
	}
	

}
