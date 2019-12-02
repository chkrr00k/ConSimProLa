package languages.operators;

import languages.visitor.ExpVisitor;

public class ForInstr extends Instruction {
	private String id;
	private String arr;
	private Block posBlock;
	
	public ForInstr(String id, String arr, Block posBlock) {
		this.id = id;
		this.posBlock = posBlock;
		this.arr = arr;
	}

	public String getId() {
		return this.id;
	}
	public Block getPosBlock() {
		return this.posBlock;
	}
	public String getArr() {
		return this.arr;
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

	@Override
	public String name() {
		return "for";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("for ");
		builder.append(this.id);
		builder.append(" in ");
		builder.append(this.arr);
		builder.append(this.posBlock);
		return builder.toString();
	}
	

}
