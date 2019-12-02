package languages.operators;

import languages.visitor.ExpVisitor;

public class StreamCollect extends StreamOp {

	public StreamCollect() {
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("collect");
		return builder.toString();
	}
	

}
