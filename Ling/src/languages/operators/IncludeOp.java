package languages.operators;

import languages.visitor.ExpVisitor;

public class IncludeOp extends Instruction {

	private String name;
	public IncludeOp(String name) {
		this.name = name;
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

	@Override
	public String name() {
		return "import";
	}

	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("import ");
		builder.append(this.name);
		return builder.toString();
	}
	

}
