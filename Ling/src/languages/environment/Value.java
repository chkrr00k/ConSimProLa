package languages.environment;

public class Value extends Variable implements Valueable{
	private Double value;

	
	public Value(String name, Double value) {
		super(name);
		this.value = value;
	}
	public Value(String name) {
		this(name, 0d);
	}

	public Value(Double value) {
		this("", value);
	}
	private Value(Value v) {
		this(v.getName(), new Double(v.value));
	}
	public Double getValue() {
		return this.value;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.value);
		
		return builder.toString();
	}
	@Override
	public void setValue(Double value) {
		this.value = value;
	}
	@Override
	public Variable clone() {
		return new Value(this);
	}

	
	
}
