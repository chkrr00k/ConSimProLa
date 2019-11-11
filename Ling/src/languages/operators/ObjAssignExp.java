package languages.operators;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import languages.visitor.ExpVisitor;

public class ObjAssignExp extends Exp {

	private List<Field> fields;
	private String id;
	
	
	public ObjAssignExp(String id) {
		this.fields = new LinkedList<Field>();
		this.id = id;
	}
	public void add(Field field){
		this.fields.add(field);
		if(field.isNested()){
			((ObjAssignExp) field.getValue()).addParent(this.id);
		}else{
			field.setBase(this.id);
		}
	}

	private void addParent(String base) {
		for(Field f : this.fields){
			f.setBase(base + "." + f.getBase());
		}
	}
	public List<Field> getFields() {
		return this.fields;
	}
	public String getId() {
		return this.id;
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
		builder.append(id);
		builder.append(" := (");
		builder.append(String.join(",", this.fields.stream().map(e -> e.toString()).collect(Collectors.toList())));
		builder.append(")");
		return builder.toString();
	}

	
	
}
