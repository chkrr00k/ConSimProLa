package languages.environment;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Complex extends Variable {
	private List<Variable> fields;
	
	public Complex(String name) {
		super(name);
		this.fields = new LinkedList<Variable>();
	}

	private Complex(Complex c) {
		this(c.getName());
		c.fields.forEach(f -> this.fields.add(f.clone()));
	}
	public List<Variable> getFields() {
		return this.fields;
	}
	public Variable addField(Variable f) {
		this.fields.add(f);
		return f;
	}
	public boolean hasFields(Variable f) {
		return this.hasFields(f.getName());
	}
	public boolean hasFields(String f) {
		return this.fields.stream().anyMatch((e) -> e.getName().equals(f));
	}
	public Variable getField(String b) {
		if(this.hasFields(b)){
			for(Variable v: this.fields){
				if(v.getName().equals(b)){
					return v;
				}
			}
		}
		throw new IllegalStateException(b + " was not defined");
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		builder.append(String.join(",", this.fields.stream().map(e -> e.getName() + ":" + e).collect(Collectors.toList())));
		builder.append("}");
		return builder.toString();
	}

	@Override
	public Variable clone() {
		return new Complex(this);
	}

	
}
