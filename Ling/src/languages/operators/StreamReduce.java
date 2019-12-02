package languages.operators;

import languages.visitor.ExpVisitor;

public class StreamReduce extends StreamOp {

	private LambdaExp l;
	public StreamReduce(LambdaExp l) {
		this.l = l;
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

	public LambdaExp getL() {
		return this.l;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("reduce ");
		builder.append(this.l);
		return builder.toString();
	}
	

}
