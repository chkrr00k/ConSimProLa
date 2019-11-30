package languages.operators;

import languages.visitor.ExpVisitor;

public class SizeExp extends Exp {

	private String id;
	public SizeExp(String id) {
		this.id = id;
	}

	@Override
	public String name() {
		return this.id;
	}

	public String getId() {
		return this.id;
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
		builder.append("size ");
		builder.append(this.id);
		return builder.toString();
	}
	
	

}
