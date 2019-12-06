package languages.visitor;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.DoubleSupplier;
import java.util.function.Function;

import javax.management.RuntimeErrorException;

import languages.environment.Array;
import languages.environment.Complex;
import languages.environment.Environment;
import languages.environment.NativeFunction;
import languages.environment.StandardLibrary;
import languages.environment.Value;
import languages.environment.Valueable;
import languages.environment.Variable;
import languages.operators.AndExp;
import languages.operators.ArrayAssignExp;
import languages.operators.AssignExp;
import languages.operators.Block;
import languages.operators.ComplexAssignExp;
import languages.operators.DerefExp;
import languages.operators.DivExp;
import languages.operators.EqExp;
import languages.operators.ExpAssignExp;
import languages.operators.ForInstr;
import languages.operators.FunctionCall;
import languages.operators.FunctionExp;
import languages.operators.GtExp;
import languages.operators.GteExp;
import languages.operators.IfExp;
import languages.operators.IncludeOp;
import languages.operators.LValArrayExp;
import languages.operators.LValExp;
import languages.operators.LambdaExp;
import languages.operators.Line;
import languages.operators.LtExp;
import languages.operators.LteExp;
import languages.operators.MinusExp;
import languages.operators.ModExp;
import languages.operators.MulExp;
import languages.operators.NeqExp;
import languages.operators.NotExp;
import languages.operators.NumExp;
import languages.operators.ObjAssignExp;
import languages.operators.OpExp;
import languages.operators.OrExp;
import languages.operators.PlusExp;
import languages.operators.PopExp;
import languages.operators.PowExp;
import languages.operators.PresenceExp;
import languages.operators.Program;
import languages.operators.PushExp;
import languages.operators.RValArrayExp;
import languages.operators.RValExp;
import languages.operators.ReturnOp;
import languages.operators.SeqExp;
import languages.operators.SizeExp;
import languages.operators.StreamCollect;
import languages.operators.StreamExp;
import languages.operators.StreamFilter;
import languages.operators.StreamMap;
import languages.operators.StreamReduce;
import languages.operators.UnaryExp;
import languages.operators.WhenExp;
import languages.operators.WhileExp;

public class EvalExpVisitor extends ExpVisitor implements Cloneable{
	private Object value;
	private Environment env;
	private boolean stop;
	
	public EvalExpVisitor(Object value, Environment env) {
		this.value = value;
		this.env = env;
		this.stop = false;
	}

	public EvalExpVisitor() {
		this(0d, new Environment());
	}

	private EvalExpVisitor(EvalExpVisitor v) throws CloneNotSupportedException {
		this(0d, v.env.clone());
	}
	public EvalExpVisitor(String string) {
		this(0d, new Environment(string));
	}

	public EvalExpVisitor clone() throws CloneNotSupportedException{
		return new EvalExpVisitor(this);
	}

	public double getResult() {
		if(this.value instanceof String && this.env.has((String) this.value)){
			return this.env.getValue((String) this.value);
		}else if(this.value instanceof Value){
			return ((Value) this.value).getValue();
		}else if(this.value instanceof Variable){
			return Double.NaN;
		}else{
			return (double) this.value;
		}
	}
	public Object getValue(){
		return this.value;
	}
	public Environment getEnvironment(){
		return this.env;
	}

	private void helper(OpExp e, BiFunction<Double, Double, Double> f){
		double arg1, arg2;
		e.getLeft().accept(this);
		arg1 = this.getResult();
		e.getRight().accept(this);
		arg2 = this.getResult();
		this.value = f.apply(arg1, arg2);
	}
	private void helper(UnaryExp e, Function<Double, Double> f){
		double arg1;
		e.getTarget().accept(this);
		arg1 = this.getResult();
		if(Double.isNaN(arg1)){
			throw new IllegalArgumentException(arg1 + " is not an value");
		}
		this.value = f.apply(arg1);
	}
	private void resolve(String name, Variable a){
		String[] dec = name.split("\\.");
		if(this.env.get(dec[0]) instanceof Value || this.env.get(dec[0]) instanceof Array){
			this.env.add(dec[0], a);
		}else{
			Complex c = (Complex) this.env.get(dec[0]);
			if(dec.length - 1 > 0){
				for(String s: Arrays.copyOfRange(dec, 1, dec.length - 1)){
					c = (Complex) c.getField(s);
				}
				c.setField(dec[dec.length - 1], a);
			}else{
				a = a.clone();
				a.setName(dec.length == 1? name : dec[1]);
				this.env.add(name, a);
			}
		}
		
	}
	private Variable resolve(String name){
		String[] dec = name.split("\\.");
		Complex c = (Complex) this.env.get(dec[0]);
		if(dec.length - 1 > 0){
			for(String s: Arrays.copyOfRange(dec, 1, dec.length - 1)){
				c = (Complex) c.getField(s);
			}
		}
		return dec.length > 1? c.getField(dec[1]): c.getField(dec[dec.length - 1]);
	}
	
	@Override
	public void visit(PlusExp e) {
		if(this.stop){
			return;
		}
		this.helper(e, (a, b) -> {
			return a + b;
		});
	}
	@Override
	public void visit(MinusExp e) {
		if(this.stop){
			return;
		}
		this.helper(e, (a, b) -> {
			return a - b;
		});
	}
	@Override
	public void visit(MulExp e) {
		if(this.stop){
			return;
		}
		this.helper(e, (a, b) -> {
			return a * b;
		});
	}
	@Override
	public void visit(DivExp e) {
		if(this.stop){
			return;
		}
		this.helper(e, (a, b) -> {
			return a / b;
		});
	}
	@Override
	public void visit(PowExp e) {
		if(this.stop){
			return;
		}
		this.helper(e, (a, b) -> {
			return Math.pow(a, b);
		});
	}
	@Override
	public void visit(NumExp e) {
		if(this.stop){
			return;
		}
		this.value = e.getVal();
	}
	@Override
	public void visit(ModExp e) {
		if(this.stop){
			return;
		}
		this.helper(e, (a, b) -> {
			return a % b;
		});
	}
	@Override
	public void visit(RValExp e) {
		if(this.stop){
			return;
		}
		if(this.env.has(e.getName())){
			try{
				this.value = this.env.get(e.getName());
			}catch(IllegalStateException ex){
				this.value = this.env.get(e.getName());
			}
		}else if(e.getName().contains(".")){
			this.value = this.resolve(e.getName());
		}else{
			throw new RuntimeException("Invalid identifier: " + e.getName());
		}
	}
	@Override
	public void visit(RValArrayExp e) {
		if(this.stop){
			return;
		}
		if(this.env.has(e.getId())){
			Array a = (Array) this.env.get(e.getId());
			e.getIndex().accept(this);
			int index;
			if(this.value instanceof Double){
				index = ((Double) this.value).intValue();
			}else if(this.value instanceof Value){
				index = ((Value) this.value).getValue().intValue();
			}else{
				throw new IllegalArgumentException(this.value + " is not a valid index");
			}
			
			this.value = a.get(index).clone();

		}else{
			throw new RuntimeException("Invalid array extraction: " + e);
		}
	}

	@Override
	public void visit(LValExp e) {
		if(this.stop){
			return;
		}
		this.value = e.getName();
	}
	@Override
	public void visit(LValArrayExp e) {
		if(this.stop){
			return;
		}
		if(this.env.has(e.getId())){
			Array a = (Array) this.env.get(e.getId());
			e.getIndex().accept(this);
			if(this.value instanceof Value){
				this.value = ((Value) this.value).getValue();
			}
			this.value = a.get(((Double) this.value).intValue());
		}else{
			throw new RuntimeException("Invalid array extraction: " + e);
		}
	}

	@Override
	public void visit(AssignExp e) {
		if(this.stop){
			return;
		}
		e.getLeft().accept(this);
		if(e.getLeft() instanceof LValExp){
			String id = ((LValExp) e.getLeft()).getName();
			e.getRight().accept(this);
			
			if(this.env.has(id)){
				if(this.value instanceof Double){
				//	((Value) v).setValue((double) this.value);
					this.env.add(id, (Variable) new Value(id, (double) this.value));
				}else if(this.value instanceof Variable){
					((Variable) this.value).setName(id);
					this.env.add(id, (Variable) this.value);
				}else{
					System.err.println("Error!");
				}
			}else if(id.contains(".")){//FIXME
				if(this.value instanceof Double){
					this.value = new Value((double) this.value);
				}
				this.resolve(id, (Variable) this.value);
			}else{
				if(this.value instanceof Variable){
					((Variable) this.value).setName(id);
					this.env.add(id, (Variable) this.value);
				}else{
					
					this.env.add(id, (double) this.value);
				}
			}
		}else if(e.getLeft() instanceof LValArrayExp){//FIXME
			Variable v = (Variable) this.value;
			e.getRight().accept(this);
			
			if(this.value instanceof Value){
				this.value = ((Value) this.value).getValue();
			}
			if(this.value instanceof Double){
				((Valueable) v).setValue((double) this.value);
			}else if(this.value instanceof Complex || this.value instanceof Array || this.value instanceof languages.environment.Function){
				Variable c = (Variable) this.value;
				((LValArrayExp) e.getLeft()).getIndex().accept(this);
				int index;
				if(this.value instanceof Double){
					index = ((Double) this.value).intValue();
				}else if(this.value instanceof Value){
					index = ((Value) this.value).getValue().intValue();
				}else{
					throw new IllegalArgumentException(this.value + " is not a valid index");
				}
				((Array) this.env.get(((LValArrayExp) e.getLeft()).getId())).replace(c, index);
			}else{
				throw new IllegalArgumentException("wrong type in array: " + this.value.getClass());
			}
		}else{
			System.err.println("Invalid Operand (AssingExp)");
		}
	}

	@Override
	public void visit(SeqExp e) {
		if(this.stop){
			return;
		}
		e.getLeft().accept(this);
		e.getRight().accept(this);		
	}

	@Override
	public void visit(Line e) {
		if(this.stop){
			return;
		}
		e.getExp().accept(this);
	}

	@Override
	public void visit(Block e) {
		if(this.stop){
			return;
		}
		e.getLines().forEach(l -> {
			l.accept(this);
		});
	}

	@Override
	public void visit(IfExp e) {
		if(this.stop){
			return;
		}
		e.getCond().accept(this);
//		Object tmpVal = this.value;
		if(this.value instanceof Value){
			this.value = ((Value) this.value).getValue();
		}
		if((double)this.value != 0d){
			e.getPosCond().accept(this);
		}else if(e.getNegCond().isPresent()){
			e.getNegCond().get().accept(this);
		}
//		this.value = tmpVal;
	}

	@Override
	public void visit(WhenExp e) {
		if(this.stop){
			return;
		}
		e.getPairs().forEach(el -> {
			el.getKey().accept(this);
			if(this.value instanceof Value){
				this.value = ((Value) this.value).getValue();
			}
			if((double)this.value != 0d){
				el.getValue().accept(this);
			}
		});
	}

	@Override
	public void visit(WhileExp e) {
		if(this.stop){
			return;
		}
		e.getCond().accept(this);
		if(this.value instanceof Value){
			this.value = ((Value) this.value).getValue();
		}
		while((double)this.value != 0d){
			e.getPosCond().accept(this);
			if(this.stop){
				return;
			}
			e.getCond().accept(this);
			if(this.value instanceof Value){
				this.value = ((Value) this.value).getValue();
			}
		}
	}

	@Override
	public void visit(ForInstr e) {
		if(this.stop){
			return;
		}
		Variable old = null;
		if(this.env.has(e.getId())){
			old = this.env.get(e.getId());
		}
		e.getArr().accept(this);
		
		if(this.value instanceof Variable){
			Variable c = (Variable) this.value;
			if(c instanceof Array){
				Array current = (Array) c;
				Variable tmp = null;
				for(Variable v : current.getAll()){
					if(this.stop){
						return;
					}
					tmp = v.clone();
					tmp.setName(e.getId());
					this.env.add(e.getId(), tmp);
					e.getPosBlock().accept(this);
				}
			}else if(c instanceof Complex){
				Complex current = (Complex) c;
				Variable tmp = null;
				for(Variable v : current.getFields()){
					if(this.stop){
						return;
					}
					tmp = v.clone();
					tmp.setName(e.getId());
					this.env.add(e.getId(), tmp);
					e.getPosBlock().accept(this);
				}
			}else if(c instanceof languages.environment.Function || c instanceof Value){
				Variable tmp = null;
				if(this.stop){
					return;
				}
				tmp = c.clone();
				tmp.setName(e.getId());
				this.env.add(e.getId(), tmp);
				e.getPosBlock().accept(this);

			}
		}else if(this.value instanceof Double){
			this.env.add(e.getId(), (double)this.value);
			e.getPosBlock().accept(this);
		}else{
			throw new RuntimeException(e.getArr() + " was not defined");
		}
		if(old != null){
			this.env.add(e.getId(), old);
		}
	}

	@Override
	public void visit(Program e) {
		if(this.stop){
			return;
		}
		e.getInstructions().forEach(i -> {
			i.accept(this);
		});
	}

	@Override
	public void visit(OrExp e) {
		if(this.stop){
			return;
		}
		this.helper(e, (l, r) -> {
			return Math.abs(l) + Math.abs(r);
		});
	}

	@Override
	public void visit(AndExp e) {
		if(this.stop){
			return;
		}
		this.helper(e, (l, r) -> {
			return Math.abs(l) * Math.abs(r);
		});
	}

	@Override
	public void visit(EqExp e) {
		if(this.stop){
			return;
		}
		this.helper(e, (l, r) -> {
			return Double.compare(l, r) == 0 ? l == 0? 1 : l : 0d;
		});
	}

	@Override
	public void visit(NeqExp e) {
		if(this.stop){
			return;
		}
		this.helper(e, (l, r) -> {
			return Double.compare(l, r) == 0 ? 0d : l == 0? 1 : l;
		});
	}

	@Override
	public void visit(GtExp e) {
		if(this.stop){
			return;
		}
		this.helper(e, (l, r) -> {
			return l > r ? l == 0? 1 : l : 0d;
		});
	}

	@Override
	public void visit(GteExp e) {
		if(this.stop){
			return;
		}
		this.helper(e, (l, r) -> {
			return l >= r ? l == 0? 1 : l : 0d;
		});
	}

	@Override
	public void visit(LtExp e) {
		if(this.stop){
			return;
		}
		this.helper(e, (l, r) -> {
			return l < r ? l == 0 ? 1 : l : 0d;
		});
	}

	@Override
	public void visit(LteExp e) {
		if(this.stop){
			return;
		}
		this.helper(e, (l, r) -> {
			return l <= r ? l == 0 ? 1 : l : 0d;
		});
	}

	@Override
	public void visit(NotExp e) {
		if(this.stop){
			return;
		}
		this.helper(e, (t) -> {
			return t != 0d ? 0d : 1d;
		});
	}

	@Override
	public void visit(ObjAssignExp e) {
		if(this.stop){
			return;
		}
		Complex c = new Complex(e.getId());
		Map<String, ComplexAssignExp> f = e.getFields();
		f.keySet().stream().forEach(el ->{
			f.get(el).accept(this);
			((Variable) this.value).setName(el);
			c.addField((Variable) this.value);
		});
		if(e.isTopLevel()){
			this.resolve(e.getId(), c);
		//	this.env.add(e.getId(), c);
		}
		this.value = c;
	}

	@Override
	public void visit(ArrayAssignExp e) {
		if(this.stop){
			return;
		}
		Array a = new Array(e.getId());
		e.getElements().stream().map(el -> {
			el.accept(this);
			return (Variable) this.value;
		}).forEach(el -> {
			a.add(el);
		});
		if(e.isTopLevel()){
			this.resolve(e.getId(), a);
//			this.env.add(e.getId(), a);
		}
		this.value = a;
	}

	@Override
	public void visit(ExpAssignExp e) {
		if(this.stop){
			return;
		}
		e.getBo().accept(this);
		Variable v = null;
		if(this.value instanceof Value){
			v = (Value) this.value;
		}else if(this.value instanceof Double){
			v = new Value((double)this.value);
		}else{
			v = (Variable) this.value;
		}
		v.setName(e.getId());
		if(e.isTopLevel()){
			this.resolve(e.getId(), v);
		//	this.env.add(e.getId(), v);
		}
		this.value = v;
		
	}

	@Override
	public void visit(DerefExp e) {
		if(this.env.has(e.getId())){
			this.value = this.env.get(e.getId());
		}else{
			throw new RuntimeException(e.getId() + "was not defined");
		}
	}

	@Override
	public void visit(PopExp e) {
		if(this.stop){
			return;
		}
		if(this.env.has(e.getArray())){
			Array a = (Array) this.env.get(e.getArray());
			Variable v =  a.get(0);
			a.remove(0);
			v.setName(e.getVariable());
			this.env.add(e.getVariable(), v);
//			this.value = ((Valueable) v).getValue();
			this.value = v;
		}else{
			Array a = (Array) this.resolve(e.getArray());
			Variable v =  a.get(0);
			a.remove(0);
			v.setName(e.getVariable());
			this.env.add(e.getVariable(), v);
			this.resolve(e.getArray(), a);
			//throw new RuntimeException("Not defined: " + e.getArray());
		}
	}

	@Override
	public void visit(PushExp e) {
		if(this.stop){
			return;
		}
		if(this.env.has(e.getArray())){
			Array a = (Array) this.env.get(e.getArray());
			e.getResult().accept(this);
			if(this.value instanceof Double){
				a.push((Double)this.value);
			}else{
				a.push((Variable)this.value);
			}
			
			
		}else{
			Array a = (Array) this.resolve(e.getArray());
			e.getResult().accept(this);
			if(this.value instanceof Double){
				a.push((Double)this.value);
			}else{
				a.push((Variable)this.value);
			}
			this.resolve(e.getArray(), a);
			//throw new RuntimeException("Not defined: " + e.getArray());
		}
	}

	@Override
	public void visit(SizeExp e) {
		if(this.stop){
			return;
		}
		if(this.env.has(e.getId())){
			if(this.env.get(e.getId()) instanceof Array){
				Array a = (Array) this.env.get(e.getId());
				this.value = new Double(a.getAll().size());
			}else if(this.env.get(e.getId()) instanceof Value){
				this.value = new Double(1);
			}else if(this.env.get(e.getId()) instanceof Complex){
				this.value = new Double(((Complex)this.env.get(e.getId())).getFields().size());
			}else{
				throw new IllegalArgumentException("I don't know what " + e.getId() + "is.");
			}
		}else{
			throw new RuntimeException("Not defined: " + e.getId());
		}
	}

	@Override
	public void visit(FunctionExp e) {
		e.getInner().accept(this);
		languages.environment.Function f = (languages.environment.Function) this.value;
		f.setName(e.getName());
		this.env.add(e.getName(), f);
	}

	@Override
	public void visit(LambdaExp e) {
		try{
			EvalExpVisitor v = this.clone();
			this.value = new languages.environment.Function(v, e.getB(), e.getArguments());
		}catch(CloneNotSupportedException e1){
		}
	}

	@Override
	public void visit(ReturnOp e) {
		if(e.hasExp()){
			e.getExp().accept(this);
			if(this.value instanceof Double){
				this.value = new Value((double)this.value);
			}
		}else{
			this.value = this.env.get(e.getId());
		}
		this.stop = true;
	}

	@Override
	public void visit(FunctionCall e) {
		languages.environment.Function f = (languages.environment.Function) this.env.get(e.getId());
		List<Variable> args = new LinkedList<Variable>();
		e.getArgs().forEach(el -> {
			el.accept(this);
			if(!(this.value instanceof Variable)){
				this.value = new Value((Double)this.value);
			}
			args.add((Variable) this.value);
		});
		try{
			this.value = f.apply(args);
		}catch(CloneNotSupportedException e1){
			this.value = 0d;
		}
	}

	@Override
	public void visit(StreamExp e) {
		if(this.env.has(e.getArr())){
			this.value = ((Array) this.env.get(e.getArr())).clone();
			e.getOp().stream().forEach(o -> o.accept(this));
		}else{
			throw new RuntimeException(e.getArr() + " was never defined");
		}
	}

	@Override
	public void visit(StreamReduce e) {
		Array a = (Array) this.value;
		List<Variable> args = new LinkedList<Variable>();
		e.getL().accept(this);
		languages.environment.Function f = (languages.environment.Function) this.value;
		this.value = new Value(0d);
		for(int i = 0; i < a.getAll().size(); i++){
			args.add((Variable) this.value);
			args.add(a.get(i));
			try{
				this.value = f.apply(args);
			}catch(CloneNotSupportedException e1){
				this.value = 0d;
			}
			
			args.clear();
		}
	}

	@Override
	public void visit(StreamFilter e) {
		Array a = (Array) this.value;
		List<Variable> args = new LinkedList<Variable>();
		e.getL().accept(this);
		languages.environment.Function f = (languages.environment.Function) this.value;
		for(int i = 0; i < a.getAll().size(); i++){
			args.add(a.get(i));
			try{
				this.value = f.apply(args);
			}catch(CloneNotSupportedException e1){
				this.value = 0d;
			}
			if(((Valueable) this.value).getValue() == 0d){
				a.remove(i);
			}
			args.clear();
		}
		this.value = a;
		
	}

	@Override
	public void visit(StreamCollect e) {
		Array a = (Array) this.value;
		this.value = a;
		
	}

	@Override
	public void visit(StreamMap e) {
		Array a = (Array) this.value, result = new Array(a.getName());
		List<Variable> args = new LinkedList<Variable>();
		e.getL().accept(this);
		languages.environment.Function f = (languages.environment.Function) this.value;
		this.value = new Value(0d);
		for(int i = 0; i < a.getAll().size(); i++){
			args.add(a.get(i));
			try{
				this.value = f.apply(args);
			}catch(CloneNotSupportedException e1){
				this.value = 0d;
			}
			result.add((Variable) this.value);
			args.clear();
		}
		this.value = result;
	}

	@Override
	public void visit(IncludeOp e) {
		DoubleSupplier ds = StandardLibrary.supplier.get(e.getName());
		if(ds != null){
			this.env.add(e.getName(), new NativeFunction(e.getName(), ds));
			
		}else{
			Function<Double, Double> fs = StandardLibrary.functions.get(e.getName());
			if(fs != null){
				this.env.add(e.getName(), new NativeFunction(e.getName(), fs));
			}else{
				throw new RuntimeException("Native function " +  e.getName() + " can't be found");
			}
		}
	}

	@Override
	public void visit(PresenceExp e) {
		if(this.env.has(e.getId())){
			this.value = this.env.get(e.getId());
		}else{
			this.value = new Value(0d);
		}
	}

}
