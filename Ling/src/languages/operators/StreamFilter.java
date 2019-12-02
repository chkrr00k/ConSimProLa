package languages.operators;

import languages.visitor.ExpVisitor;

public class StreamFilter extends StreamOp {

	private LambdaExp l;
	public StreamFilter(LambdaExp l) {
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
		builder.append("filter ");
		builder.append(this.l);
		return builder.toString();
	}
	

}
