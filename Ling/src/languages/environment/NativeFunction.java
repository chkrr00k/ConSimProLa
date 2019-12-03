package languages.environment;

import java.util.List;
import java.util.function.DoubleSupplier;
import java.util.function.Function;


public class NativeFunction extends languages.environment.Function {

	private DoubleSupplier ds;
	private Function<Double, Double> fs;
	private boolean args;
	public NativeFunction(String name, DoubleSupplier f) {
		super(null, null, null);
		super.setName(name);
		this.ds = f;
		this.args = false;
	}
	public NativeFunction(String name, Function<Double, Double> f) {
		super(null, null, null);
		super.setName(name);
		this.fs = f;
		this.args = true;
	}
	@Override
	public Object apply(List<Variable> args) throws CloneNotSupportedException {
		if(this.args){
			return new Value(this.fs.apply(((Valueable) args.get(0)).getValue()));
		}else{
			return ds.getAsDouble();
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("() {<native code>}");
		return builder.toString();
	}
	
}
