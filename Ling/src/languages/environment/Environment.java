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
	private Map<String, Variable> variables;
	private String name;
	

	public Environment(String name) {
		this(name, new HashMap<String, Variable>());
	}
	public Environment() {
		this(new HashMap<String, Variable>());
	}
	public Environment(Map<String, Variable> d) {
		this("Main", d);
	}
	public Environment(String name, Map<String, Variable> d) {
		this.variables = new HashMap<String, Variable>();
		d.forEach((k, v) -> {
			this.variables.put(k, v.clone());
		});
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Variable add(String key, double value){
		if(this.has(key) && this.get(key).isImmutable()){
			throw new IllegalAccessError("You can't assign values defined immutable: " + key);
		}
		Value v = null;
		this.variables.put(key, v = new Value(key,value));
		return v;
	}
	public Variable add(String id, Variable v) {
		if(!id.equals(v.getName())){
			throw new IllegalArgumentException(id + " " + v);
		}
		if(this.has(id) && this.get(id).isImmutable()){
			throw new IllegalAccessError("You can't assign values defined immutable: " + id);
		}
		this.variables.put(id, v);
		return v;
	}
 @Deprecated
	public double getValue(String key){
		Variable v = this.variables.get(key);
		if(v instanceof Valueable){
			return ((Valueable) v).getValue(); 
		}else{
			throw new IllegalStateException(key + " " + v);
		}
	}
	public Variable get(String key){
		return this.variables.get(key);
	}
	public boolean has(String key){
		return this.variables.containsKey(key);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		List<String> envKey = new LinkedList<String>(this.variables.keySet());
		Collections.sort(envKey, Collator.getInstance());
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("Environment (%-10s) is \n", this.name));
		builder.append(String.format("%-2s | %-5s | %-10s | %s\n","K", "Type", "Name", "Value"));
		builder.append(String.format("%-2s | %-5s | %-10s | %s\n","--", "-----", "----------", "--------------------------------------->"));
		envKey.stream().map(k -> {
			Variable v = this.variables.get(k);
			if(v instanceof Value){
				return String.format("%-2s | %-5s | %-10s | %.2f\n",v.isImmutable() ? "St" :"Mt","(Val)", k, ((Value) v).getValue());
			}else{
				return String.format("%-2s | %-5s | %-10s | %s\n", v.isImmutable() ? "St" :"Mt",v instanceof Complex ? "(Com)":v instanceof Array? "(Arr)":v instanceof Function?"(Fun)":"(Unk)", k, v);
			}
		}).forEach(i -> builder.append(i));
		
		return builder.toString();
	}
	
	@Override
	public Environment clone() throws CloneNotSupportedException {
		return new Environment(this.variables);
	}

	
}
