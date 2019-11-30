package languages.visitor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

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
import languages.operators.DivExp;
import languages.operators.EqExp;
import languages.operators.Field;
import languages.operators.GtExp;
import languages.operators.GteExp;
import languages.operators.IfExp;
import languages.operators.LValExp;
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
import languages.operators.PowExp;
import languages.operators.Program;
import languages.operators.RValArrayExp;
import languages.operators.RValExp;
import languages.operators.SeqExp;
import languages.operators.UnaryExp;
import languages.operators.WhileExp;

public class EvalExpVisitor extends ExpVisitor {
	private Object value;
	private Environment env;
	
	
	public EvalExpVisitor(Object value, Environment env) {
		this.value = value;
		this.env = env;
	}

	public EvalExpVisitor() {
		this(0d, new Environment());
	}

	public double getResult() {
		if(this.value instanceof String && this.env.has((String) this.value)){
			return this.env.getValue((String) this.value);
		}else{
			return (double) this.value;
		}
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
		this.helper(e, (a, b) -> {
			return a + b;
		});
	}
	@Override
	public void visit(MinusExp e) {
		this.helper(e, (a, b) -> {
			return a - b;
		});
	}
	@Override
	public void visit(MulExp e) {
		this.helper(e, (a, b) -> {
			return a * b;
		});
	}
	@Override
	public void visit(DivExp e) {
		this.helper(e, (a, b) -> {
			return a / b;
		});
	}
	@Override
	public void visit(PowExp e) {
		this.helper(e, (a, b) -> {
			return Math.pow(a, b);
		});
	}
	@Override
	public void visit(NumExp e) {
		this.value = e.getVal();
	}
	@Override
	public void visit(ModExp e) {
		this.helper(e, (a, b) -> {
			return a % b;
		});
	}
	@Override
	public void visit(RValExp e) {
		if(this.env.has(e.getName())){
			this.value = this.env.getValue(e.getName());
		}else if(e.getName().contains(".")){
			this.value = ((Valueable) this.resolve(e.getName())).getValue();
		}else{
			throw new RuntimeException("Invalid identifier: " + e.getName());
		}
	}
	@Override
	public void visit(RValArrayExp e) {
		System.out.println(e);
		if(this.env.has(e.getId())){
			Array a = (Array) this.env.get(e.getId());
			e.getIndex().accept(this);
			this.value = ((Value) a.get(((Double) this.value).intValue())).getValue();
		}else{
			throw new RuntimeException("Invalid array extraction: " + e);
		}
	}

	@Override
	public void visit(LValExp e) {
		this.value = e.getName();
	}
	@Override
	public void visit(AssignExp e) {
		e.getLeft().accept(this);
		String id = ((LValExp) e.getLeft()).getName();
		e.getRight().accept(this);
		//XXX
		if(this.env.has(id)){
			Variable v = this.env.get(id);
			if(v instanceof Complex){
				((Complex) v).setValue((Double) this.value);
			}else if(v instanceof Value){
				((Value) v).setValue((Double) this.value);
			}
		}else if(id.contains(".")){
			((Valueable) this.resolve(id)).setValue((Double) this.value);
		}else{
			this.env.add(id, (double) this.value);
		}
	}

	@Override
	public void visit(SeqExp e) {
		e.getLeft().accept(this);
		e.getRight().accept(this);		
	}

	@Override
	public void visit(Line e) {
		e.getExp().accept(this);
	}

	@Override
	public void visit(Block e) {
		e.getLines().forEach(l -> {
			l.accept(this);
		});
	}

	@Override
	public void visit(IfExp e) {
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
		e.getCond().accept(this);
		while((double)this.value != 0d){
			e.getPosCond().accept(this);
			e.getCond().accept(this);
		}
	}

	@Override
	public void visit(Program e) {
		e.getInstructions().forEach(i -> {
			i.accept(this);
		});
	}

	@Override
	public void visit(OrExp e) {
		this.helper(e, (l, r) -> {
			return Math.abs(l) + Math.abs(r);
		});
	}

	@Override
	public void visit(AndExp e) {
		this.helper(e, (l, r) -> {
			return Math.abs(l) * Math.abs(r);
		});
	}

	@Override
	public void visit(EqExp e) {
		this.helper(e, (l, r) -> {
			return Double.compare(l, r) == 0 ? l : 0d;
		});
	}

	@Override
	public void visit(NeqExp e) {
		this.helper(e, (l, r) -> {
			return Double.compare(l, r) == 0 ? 0d : l;
		});
	}

	@Override
	public void visit(GtExp e) {
		this.helper(e, (l, r) -> {
			return l > r ? l : 0d;
		});
	}

	@Override
	public void visit(GteExp e) {
		this.helper(e, (l, r) -> {
			return l >= r ? l : 0d;
		});
	}

	@Override
	public void visit(LtExp e) {
		this.helper(e, (l, r) -> {
			return l < r ? l : 0d;
		});
	}

	@Override
	public void visit(LteExp e) {
		this.helper(e, (l, r) -> {
			return l <= r ? l : 0d;
		});
	}

	@Override
	public void visit(NotExp e) {
		this.helper(e, (t) -> {
			return t != 0d ? 0d : 1d;
		});
	}

	@Override
	public void visit(ObjAssignExp e) {
//		e.getFields().forEach(f -> f.accept(this));
		
//		this.env.add(e.getId(), new Complex(e.getId()));
		e.getFields().forEach(f -> f.accept(this));
		this.value = 0d;
	}

	@Override
	public void visit(ArrayAssignExp e) {
		Array a = (Array) this.env.add(e.getId(), new Array(e.getId()));
		e.getElements().stream().forEach(el -> {
			el.accept(this);
			a.add(new Value((Double) this.value));
		});
	}

	@Override
	public void visit(Field e) {
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

}
