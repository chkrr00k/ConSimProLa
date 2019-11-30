package languages.operators;

import languages.visitor.ExpVisitor;

public class PopExp extends Exp {
	
	private String variable;
	private String array;
	
	public PopExp(String variable, String array) {
		this.variable = variable;
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
	public String getVariable() {
		return this.variable;
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
		builder.append(this.variable);
		builder.append(" <- ");
		builder.append(this.array);
		return builder.toString();
	}
	

}
