package languages.operators;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import languages.visitor.ExpVisitor;

public class StreamExp extends Exp {

	private String arr;
	private List<StreamOp> op;
	public StreamExp(String arr) {
		this.arr = arr;
		this.op = new LinkedList<StreamOp>();
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

	@Override
	public String name() {
		return "stream";
	}

	public void add(StreamOp o) {
		this.op.add(o);
	}

	public String getArr() {
		return this.arr;
	}
	public List<StreamOp> getOp() {
		return this.op;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("stream ");
		builder.append(this.arr);
		builder.append(" ");
		builder.append(String.join(" then ", this.op.stream().map(e -> e.toString()).collect(Collectors.toList())));
		return builder.toString();
	}
	

}
