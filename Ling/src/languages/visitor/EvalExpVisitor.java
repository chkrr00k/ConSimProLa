package languages.visitor;

import java.util.function.BiFunction;
import java.util.function.Function;

import languages.Environment;
import languages.operators.AndExp;
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
import languages.operators.RValExp;
import languages.operators.SeqExp;
import languages.operators.UnaryExp;
import languages.operators.WhileExp;

public class EvalExpVisitor extends ExpVisitor {
	private Object value;
	private Environment env = new Environment();
	
	public EvalExpVisitor() {
		this.value = 0d;
	}

	public double getResult() {
		if(this.value instanceof String && this.env.has((String) this.value)){
			return this.env.get((String) this.value);
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
			this.value = this.env.get(e.getName());
		}else{
			throw new RuntimeException("Invalid identifier: " + e.getName());
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
		this.env.add(id, (double) this.value);
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
		e.getFields().forEach(f -> f.accept(this));
	}

	@Override
	public void visit(Field e) {
		e.getValue().accept(this);
		if(!e.isNested()){
			this.env.add(e.getName(), (double) this.value);
		}
	}

}
