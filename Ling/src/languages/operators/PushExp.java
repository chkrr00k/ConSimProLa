package languages.operators;

import languages.Token;
import languages.visitor.ExpVisitor;

public class PushExp extends Exp {

	private Exp result;
	private String array;
	
	public PushExp(Exp result, String array) {
		this.result = result;
		this.array = array;
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

	@Override
	public String name() {
		return "<-";
	}

	public Exp getResult() {
		return this.result;
	}
	public String getArray() {
		return this.array;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.result);
		builder.append(" -> ");
		builder.append(this.array);
		return builder.toString();
	}
	

}
