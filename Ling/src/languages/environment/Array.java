package languages.environment;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Array extends Variable {
	private List<Variable> elements;
	
	public Array(String name) {
		super(name);
		this.elements = new LinkedList<Variable>();
	}

	public void add(Variable value) {
		this.elements.add(value);
	}
	public Variable get(int i){
		return this.elements.get(i);
	}
	public void remove(int i) {
		this.elements.remove(i);
	}

	public void push(Double value) {
		this.elements.add(0, new Value(value));
	}

	public List<Variable> getAll(){
		return this.elements;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.getName());
		builder.append(":= [");
		builder.append(String.join(",", this.elements.stream().map(e -> e.toString()).collect(Collectors.toList())));
		builder.append("]");
		return builder.toString();
	}
	

}
