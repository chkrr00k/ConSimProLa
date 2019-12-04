package languages.environment;

import java.text.Collator;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.event.ListSelectionEvent;

public class Environment implements Cloneable{
	private Map<String, Variable> doubleVariables;
	

	public Environment() {
		this.doubleVariables = new HashMap<String, Variable>();
	}
	
	public Environment(Map<String, Variable> d) {
		this.doubleVariables = new HashMap<String, Variable>();
		d.forEach((k, v) -> {
			this.doubleVariables.put(k, v.clone());
		});
	}

	public void add(String key, double value){
		this.doubleVariables.put(key, new Value(key,value));
	}
	public Variable add(String id, Variable v) {
		if(!id.equals(v.getName())){
			throw new IllegalArgumentException(id + " " + v);
		}
		this.doubleVariables.put(id, v);
		return v;
	}

	public double getValue(String key){
		Variable v = this.doubleVariables.get(key);
		if(v instanceof Valueable){
			return ((Valueable) v).getValue(); 
		}else{
			throw new IllegalStateException(key + " " + v);
		}
	}
	public Variable get(String key){
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
			Variable v = this.doubleVariables.get(k);
			if(v instanceof Value){
				return String.format("%-5s | %-10s | %.2f\n","(Val)", k, ((Value) v).getValue());
			}else{
				return String.format("%-5s | %-10s | %s\n",v instanceof Complex ? "(Com)":v instanceof Array? "(Arr)":v instanceof Function?"(Fun)":"(Unk)", k, v);
			}
		}).forEach(i -> builder.append(i));
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public Environment clone() throws CloneNotSupportedException {
		return new Environment(this.doubleVariables);
	}
	
}
