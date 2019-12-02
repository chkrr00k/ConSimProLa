package languages.operators;

import languages.visitor.ExpVisitor;

public abstract class StreamOp {
	public abstract void accept(ExpVisitor v);
}
