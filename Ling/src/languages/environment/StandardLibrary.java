package languages.environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.DoubleSupplier;
import java.util.function.Function;

public class StandardLibrary {
	public final static Map<String, DoubleSupplier> supplier = new HashMap<String, DoubleSupplier>();
	public final static Map<String, Function<Double, Double>> functions = new HashMap<String, Function<Double, Double>>();
	static{
		StandardLibrary.supplier.put("rand", () -> {
			return new Random().nextDouble();
		});
		StandardLibrary.supplier.put("time", () -> {
			return System.currentTimeMillis();
		});
		
		StandardLibrary.supplier.put("PI", () -> {
			return Math.PI;
		});
		StandardLibrary.supplier.put("E", () -> {
			return Math.E;
		});
		StandardLibrary.supplier.put("NaN", () -> {
			return Double.NaN;
		});
		StandardLibrary.supplier.put("INFINITY", () -> {
			return Double.POSITIVE_INFINITY;
		});
		StandardLibrary.supplier.put("NEG_INFINITY", () -> {
			return Double.NEGATIVE_INFINITY;
		});
		
		StandardLibrary.functions.put("isNaN", (i) -> {
			return Double.isNaN(i)? 0d: 1d;
		});
		StandardLibrary.functions.put("isInfinite", (i) -> {
			return Double.isInfinite(i)? 0d: 1d;
		});
		StandardLibrary.functions.put("ceil", (i) -> {
			return Math.ceil(i);
		});
		StandardLibrary.functions.put("ceil", (i) -> {
			return Math.ceil(i);
		});
		StandardLibrary.functions.put("floor", (i) -> {
			return Math.floor(i);
		});
		StandardLibrary.functions.put("round", (i) -> {
			return new Double(Math.round(i));
		});
		StandardLibrary.functions.put("abs", (i) -> {
			return Math.abs(i);
		});
		StandardLibrary.functions.put("sin", (i) -> {
			return Math.sin(i);
		});
		StandardLibrary.functions.put("cos", (i) -> {
			return Math.cos(i);
		});
		StandardLibrary.functions.put("tan", (i) -> {
			return Math.tan(i);
		});
		StandardLibrary.functions.put("asin", (i) -> {
			return Math.asin(i);
		});
		StandardLibrary.functions.put("acos", (i) -> {
			return Math.acos(i);
		});
		StandardLibrary.functions.put("atan", (i) -> {
			return Math.atan(i);
		});
		StandardLibrary.functions.put("log", (i) -> {
			return Math.log(i);
		});
		StandardLibrary.functions.put("log10", (i) -> {
			return Math.log10(i);
		});
		StandardLibrary.functions.put("exp", (i) -> {
			return Math.exp(i);
		});
	}

}
