package languages.operators;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import languages.visitor.ExpVisitor;

public class ObjAssignExp extends ComplexAssignExp {

	private List<Field> fields;	
	
	public ObjAssignExp(String id) {
		this.fields = new LinkedList<Field>();
		super.id = id;
	}
	public void add(Field field){
		this.fields.add(field);
		if(field.isNested()){
			((ObjAssignExp) field.getValue()).addParent(this.id);
		}else{
			field.addBase(super.id);
		}
	}

	public void addParent(String base) {
		for(Field f : this.fields){
			f.addBase(base);
		}
	}
	public List<Field> getFields() {
		return this.fields;
	}

	
	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

	@Override
	public String name() {
		return ":=";
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.id);
		builder.append(" := (");
		builder.append(String.join(",", this.fields.stream().map(e -> e.toString()).collect(Collectors.toList())));
		builder.append(")");
		return builder.toString();
	}

	
	
}
