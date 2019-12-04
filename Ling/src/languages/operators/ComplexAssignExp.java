package languages.operators;

public abstract class ComplexAssignExp extends Exp {
	protected String id;
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
	
}
