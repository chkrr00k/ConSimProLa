package languages.visitor;

import languages.operators.*;

public abstract class ExpVisitor {
	public abstract double getResult();
	
	public abstract void visit(IfExp e);
	public abstract void visit(Block e);
	public abstract void visit(MinusExp e);
	public abstract void visit(MulExp e);
	public abstract void visit(DivExp e);
	public abstract void visit(PowExp e);
	public abstract void visit(NumExp e);
	public abstract void visit(ModExp e);
	public abstract void visit(RValExp e);
	public abstract void visit(LValExp e);
	public abstract void visit(AssignExp e);
	public abstract void visit(SeqExp e);
	public abstract void visit(Line e);
	public abstract void visit(PlusExp e);
	public abstract void visit(WhileExp e);
	public abstract void visit(Program e);
	
	
}
