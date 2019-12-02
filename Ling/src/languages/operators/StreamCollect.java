package languages.operators;

import languages.visitor.ExpVisitor;

public class StreamCollect extends StreamOp {

	public StreamCollect() {
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

}
