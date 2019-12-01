package languages.operators;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import languages.visitor.ExpVisitor;

public class FunctionCall extends Exp {

	private String id;
	private List<Exp> args;
	
	public FunctionCall(String id) {
		this.id = id;
		this.args = new LinkedList<Exp>();
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}
	@Override
	public String name() {
		return this.id;
	}

	public void add(Exp e) {
		this.args.add(e);
	}

	public String getId() {
		return this.id;
	}
	public List<Exp> getArgs() {
		return this.args;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("$");
		builder.append(this.id);
		builder.append("(");
		builder.append(String.join(", ", this.args.stream().map(e -> e.toString()).collect(Collectors.toList())));
		builder.append(")");
		return builder.toString();
	}

	
}
