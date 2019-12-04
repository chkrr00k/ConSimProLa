package languages.operators;

import languages.visitor.ExpVisitor;

public class ExpAssignExp extends ComplexAssignExp {

	private Exp bo;
	public ExpAssignExp(Exp bo) {
		this.bo = bo;
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

	public Exp getBo() {
		return this.bo;
	}

	@Override
	public String name() {
		return "";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.bo);
		return builder.toString();
	}
	

}
