package languages.operators;

public abstract class ComplexAssignExp extends Exp {
	protected String id;
	protected Exp index;
	private boolean array = false;
	private boolean top = false;
	
	public String getId() {
		return this.id;
	}

	public void setName(String id) {
		this.id = id;
	}
	public boolean isTopLevel(){
		return this.top;
	}
	public boolean setTopLevel(){
		return this.top = true;
	}

	public void setIndex(Exp index) {
		this.array = true;
		this.index = index;
	}
	public Exp getIndex() {
		return this.index;
	}
	public boolean isArray(){
		return this.array;
	}
	
}
