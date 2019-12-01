package languages.environment;

public abstract class Variable implements Cloneable{
	private String name;
	private boolean immutable;
	private boolean local;
	
	protected Variable(String name) {
		this.name = name;
		this.immutable = false;
		this.local = false;
	}

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;		
	}

	public boolean isImmutable() {
		return this.immutable;
	}
	public boolean isLocal() {
		return this.local;
	}
	public void setImmutable(boolean immutable) {
		this.immutable = immutable;
	}
	public void setLocal(boolean local) {
		this.local = local;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(!(obj instanceof Variable)){
			return false;
		}
		Variable other = (Variable) obj;
		if(this.name == null){
			if(other.name != null){
				return false;
			}
		}else if(!this.name.equals(other.name)){
			return false;
		}
		return true;
	}
	@Override
	public abstract Variable clone();
	
}
