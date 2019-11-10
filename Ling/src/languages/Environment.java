package languages;

import java.util.HashMap;
import java.util.Map;

public class Environment {
	private Map<String, Double> doubleVariables;

	public Environment() {
		this.doubleVariables = new HashMap<String, Double>();
	}
	
	public void add(String key, double value){
		this.doubleVariables.put(key, value);
	}
	public double get(String key){
		return this.doubleVariables.get(key);
	}
	public boolean has(String key){
		return this.doubleVariables.containsKey(key);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Environment [numerics=");
		builder.append(this.doubleVariables);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
