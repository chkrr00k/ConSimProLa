package languages.operators;

import languages.visitor.ExpVisitor;

public class StreamMap extends StreamOp {

	private LambdaExp l;
	public StreamMap(LambdaExp l) {
		this.l = l;
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}
	public LambdaExp getL() {
		return this.l;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("map ");
		builder.append(this.l);
		return builder.toString();
	}
}
