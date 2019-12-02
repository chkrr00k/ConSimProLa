package languages.visitor;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.management.RuntimeErrorException;

import languages.environment.Array;
import languages.environment.Complex;
import languages.environment.Environment;
import languages.environment.Value;
import languages.environment.Valueable;
import languages.environment.Variable;
import languages.operators.AndExp;
import languages.operators.ArrayAssignExp;
import languages.operators.AssignExp;
import languages.operators.Block;
import languages.operators.DerefExp;
import languages.operators.DivExp;
import languages.operators.EqExp;
import languages.operators.Field;
import languages.operators.ForInstr;
import languages.operators.FunctionCall;
import languages.operators.FunctionExp;
import languages.operators.GtExp;
import languages.operators.GteExp;
import languages.operators.IfExp;
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
import languages.operators.Program;
import languages.operators.PushExp;
import languages.operators.RValArrayExp;
import languages.operators.RValExp;
import languages.operators.ReturnOp;
import languages.operators.SeqExp;
import languages.operators.SizeExp;
import languages.operators.UnaryExp;
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
	public EvalExpVisitor clone() throws CloneNotSupportedException{
		return new EvalExpVisitor(this);
	}

	public double getResult() {
		if(this.value instanceof String && this.env.has((String) this.value)){
			return this.env.getValue((String) this.value);
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
	private Variable resolve(String name){
		String[] dec = name.split("\\.");
		Complex c = (Complex) this.env.get(dec[0]);
		for(String s: Arrays.copyOfRange(dec, 1, dec.length - 1)){
			c = (Complex) c.getField(s);
		}
		return c.getField(dec[dec.length - 1]);
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
				this.value = this.env.getValue(e.getName());
			}catch(IllegalStateException ex){
				this.value = this.env.get(e.getName());
			}
		}else if(e.getName().contains(".")){
			this.value = ((Valueable) this.resolve(e.getName())).getValue();
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
			if(a.get(((Double) this.value).intValue()) instanceof Value){
				this.value = ((Value) a.get(((Double) this.value).intValue())).getValue();
			}else{
				this.value = a.get(((Double) this.value).intValue());
			}
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
				Variable v = this.env.get(id);
				if(this.value instanceof Complex){
					this.env.add(id, (Variable) this.value);
				}
				if(v instanceof Complex){
					((Complex) v).setValue((Double) this.value);
				}else if(v instanceof Value){
					((Value) v).setValue((Double) this.value);
				}else if(v instanceof Array){
					((Variable) this.value).setName(id);
					this.env.add(id, (Variable) this.value);
				}
			}else if(id.contains(".")){
				((Valueable) this.resolve(id)).setValue((Double) this.value);
			}else{
				if(this.value instanceof Variable){
					((Variable) this.value).setName(id);
					this.env.add(id, (Variable) this.value);
				}else if(this.value instanceof Complex){
					this.env.add(id, (Variable) this.value);
				}else{
					this.env.add(id, (double) this.value);
				}
			}
		}else if(e.getLeft() instanceof LValArrayExp){
			Variable v = (Variable) this.value;
			e.getRight().accept(this);
			((Valueable) v).setValue((Double) this.value);
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
		if((double)this.value != 0d){
			e.getPosCond().accept(this);
		}else if(e.getNegCond().isPresent()){
			e.getNegCond().get().accept(this);
		}
//		this.value = tmpVal;
	}

	@Override
	public void visit(WhileExp e) {
		if(this.stop){
			return;
		}
		e.getCond().accept(this);
		while((double)this.value != 0d){
			e.getPosCond().accept(this);
			if(this.stop){
				return;
			}
			e.getCond().accept(this);
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
		if(this.env.has(e.getArr())){
			Array current = (Array) this.env.get(e.getArr());
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
//		e.getFields().forEach(f -> f.accept(this));
		
//		this.env.add(e.getId(), new Complex(e.getId()));
		e.getFields().forEach(f -> f.accept(this));
		this.value = 0d;
	}

	@Override
	public void visit(ArrayAssignExp e) {
		if(this.stop){
			return;
		}
		Array a = (Array) this.env.add(e.getId(), new Array(e.getId()));
		e.getElements().stream().forEach(el -> {
			el.accept(this);
			a.add(new Value((Double) this.value));
		});
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
	public void visit(Field e) {
		if(this.stop){
			return;
		}
/*		e.getValue().accept(this);
		if(!e.isNested()){
			this.env.add(e.getName(), (double) this.value);
		}*/
		
		e.getValue().accept(this);
		if(!e.isNested()){
			Complex c = null;
			if(e.getBase().size() == 1){

				if(!this.env.has(e.getParent()) || !(this.env.get(e.getParent()) instanceof Complex)){
					this.env.add(e.getParent(), new Complex(e.getParent()));
				}
				c = (Complex) this.env.get(e.getParent());
			}else{
				
				List<String> bs = e.getBase();
				Collections.reverse(bs);
				if(this.env.has(bs.get(0))){
					c = (Complex) this.env.get(bs.get(0));
				}else{
					c = new Complex(bs.get(0));
					this.env.add(c.getName(), c);
				}
				for(String b : bs.subList(1, bs.size())){
					if(c.hasFields(b)){
						c = (Complex) c.getField(b);
					}else{
						c = (Complex) c.addField(new Complex(b));
					}
				}
			}
			if(e.isValue()){
				c.setValue(new Value(e.getId(), (double)this.value));
			}else{
				c.addField(new Value(e.getId(), (double)this.value));
			}
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
			throw new RuntimeException("Not defined: " + e.getArray());
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
			throw new RuntimeException("Not defined: " + e.getArray());
		}
	}

	@Override
	public void visit(SizeExp e) {
		if(this.stop){
			return;
		}
		if(this.env.has(e.getId())){
			Array a = (Array) this.env.get(e.getId());
			this.value = new Double(a.getAll().size());
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

}
