package languages.operators;

import languages.visitor.ExpVisitor;

public class RValArrayExp extends Exp {

	private String id;
	private Exp index;
	
	public RValArrayExp(String id, Exp index) {
		this.id = id;
		this.index = index;
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

	@Override
	public String name() {
		return this.id;
	}

	public String getId() {
		return this.id;
	}

	public Exp getIndex(){
		return this.index;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.id);
		builder.append("[");
		builder.append(this.index);
		builder.append("]");
		return builder.toString();
	}
	

}
