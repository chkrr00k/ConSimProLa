package languages.operators;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import languages.visitor.ExpVisitor;

public class ObjAssignExp extends ComplexAssignExp {

	private Map<String,ComplexAssignExp> fields;	
	
	public ObjAssignExp() {
		this.fields = new HashMap<String,ComplexAssignExp>();
		super.id = id;
	}
	public void add(String name, ComplexAssignExp complexAssignExp){
		this.fields.put(name, complexAssignExp);
	}
	
	@Override
	public void accept(ExpVisitor v) {
		v.visit(this);
	}

	public Map<String, ComplexAssignExp> getFields() {
		return this.fields;
	}
	@Override
	public String name() {
		return ":=";
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		builder.append(String.join(",", this.fields.keySet()
				.stream().map(e -> e.toString() + ":" + this.fields.get(e).toString()).collect(Collectors.toList())));
		builder.append("}");
		return builder.toString();
	}

	
	
}
