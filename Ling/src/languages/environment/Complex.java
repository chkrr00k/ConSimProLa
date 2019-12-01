package languages.environment;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Complex extends Variable implements Valueable{
	private Optional<Value> v;
	private List<Variable> fields;
	
	public Complex(String name) {
		super(name);
		this.fields = new LinkedList<Variable>();
		this.v = Optional.empty();
	}

	private Complex(Complex c) {
		this(c.getName());
		c.fields.forEach(f -> this.fields.add(f.clone()));
		this.v = c.v;
	}

	public boolean hasValue(){
		return this.v.isPresent();
	}
	public Double getValue() {
		return this.v.get().getValue();
	}
	public List<Variable> getFields() {
		return this.fields;
	}
	public void setValue(Value v) {
		this.v = Optional.of(v);
	}
	@Override
	public void setValue(Double value) {
		if(this.v.isPresent()){
			this.v.get().setValue(value);
		}else{
			this.setValue(new Value("", value));
		}
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
		builder.append(super.getName());
		builder.append(":=(");
		if(this.v.isPresent()){
			builder.append("=>");
			builder.append(this.v.get());
			if(this.fields.size() > 0){
				builder.append(", ");
			}
		}
		builder.append(String.join(",", this.fields.stream().map(e -> e.getName() + "=>" + e).collect(Collectors.toList())));
		builder.append(")");
		return builder.toString();
	}

	@Override
	public Variable clone() {
		return new Complex(this);
	}

	
}
