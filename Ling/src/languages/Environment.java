package languages;

import java.text.Collator;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.event.ListSelectionEvent;

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
		List<String> envKey = new LinkedList<String>(this.doubleVariables.keySet());
		Collections.sort(envKey, Collator.getInstance());
		StringBuilder builder = new StringBuilder();
		builder.append("Environment [\n");
		envKey.stream().map(k -> {
			return String.format("%-10s | %.2f\n", k, this.doubleVariables.get(k));
		}).forEach(i -> builder.append(i));
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
