package languages.operators;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import languages.visitor.ExpVisitor;

public class ArrayAssignExp extends ComplexAssignExp {

	private List<Exp> elements;
	
	public ArrayAssignExp() {
		this.elements = new LinkedList<Exp>();
	}

	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

	@Override
	public String name() {
		return super.id;
	}

	public void add(Exp element) {
		this.elements.add(element);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if(super.isTopLevel()){
			builder.append(super.getId());
			builder.append(" := ");
		}
		builder.append("[");
		if(this.elements.size() > 0){
			builder.append(String.join(",", this.elements.stream().map(e -> e.toString()).collect(Collectors.toList())));
		}
		builder.append("]");
		return builder.toString();
	}

	public List<Exp> getElements() {
		return this.elements;
	}
	
	

}
