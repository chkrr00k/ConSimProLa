package languages.environment;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import languages.operators.Block;
import languages.visitor.EvalExpVisitor;

public class Function extends Variable {

	private EvalExpVisitor visitor;
	private Block b;
	private List<String> args;
	
	public Function(EvalExpVisitor v, Block b, List<String> arguments) {
		super("");
		this.visitor = v;
		this.b = b;
		this.args = arguments;
	}
	
	private Function(Function f) {
		this(f.visitor, f.b, new LinkedList<String>(f.args));
	}

	public Object apply(List<Variable> args) throws CloneNotSupportedException{
		args = args.stream().map(e -> e.clone()).collect(Collectors.toList());
		this.combine(this.args, args).forEach((k, v) -> {
			v.setName(k);
			this.visitor.getEnvironment().add(k, v);
		});
		EvalExpVisitor v = this.visitor.clone();
		v.getEnvironment().add(this.getName(), this);
		v.visit(this.b);
		return v.getValue();
	}
	private final <K, V> Map<K, V> combine(List<K> keys, List<V> values) {
	    Iterator<K> keyIter = keys.iterator();
	    Iterator<V> valIter = values.iterator();
	    return IntStream.range(0, keys.size()).boxed()
	            .collect(Collectors.toMap(i -> keyIter.next(), i -> valIter.next()));
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		builder.append(String.join(",", this.args));
		builder.append(")");
		builder.append(this.b);
		return builder.toString();
	}

	@Override
	public Variable clone() {
		return new Function(this);
	}

}
