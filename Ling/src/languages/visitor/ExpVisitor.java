package languages.visitor;

import languages.operators.*;

public abstract class ExpVisitor {
	public abstract double getResult();
	
	public abstract void visit(IfExp e);
	public abstract void visit(WhenExp e);

	public abstract void visit(WhileExp e);
	public abstract void visit(ForInstr e);

	public abstract void visit(Block e);
	
	public abstract void visit(PlusExp e);
	public abstract void visit(MinusExp e);
	public abstract void visit(MulExp e);
	public abstract void visit(DivExp e);
	public abstract void visit(PowExp e);
	public abstract void visit(NumExp e);
	public abstract void visit(ModExp e);
	
	public abstract void visit(RValExp e);
	public abstract void visit(RValArrayExp e);

	public abstract void visit(LValExp e);
	public abstract void visit(LValArrayExp e);

	public abstract void visit(SeqExp e);
	public abstract void visit(Line e);

	public abstract void visit(Program e);
	
	public abstract void visit(OrExp e);
	public abstract void visit(AndExp e);

	public abstract void visit(EqExp e);
	public abstract void visit(NeqExp e);
	public abstract void visit(GtExp e);
	public abstract void visit(GteExp e);
	public abstract void visit(LtExp e);
	public abstract void visit(LteExp e);
	public abstract void visit(NotExp e);

	public abstract void visit(AssignExp e);

	public abstract void visit(ObjAssignExp e);

	public abstract void visit(ArrayAssignExp e);

	public abstract void visit(ExpAssignExp e);

	public abstract void visit(DerefExp e);

	public abstract void visit(PopExp e);
	public abstract void visit(PushExp e);

	public abstract void visit(SizeExp e);

	public abstract void visit(LambdaExp e);
	public abstract void visit(FunctionExp e);
	public abstract void visit(ReturnOp e);

	public abstract void visit(FunctionCall e);

	public abstract void visit(StreamExp e);
	public abstract void visit(StreamReduce e);

	public abstract void visit(StreamFilter e);
	public abstract void visit(StreamCollect e);
	public abstract void visit(StreamMap e);

	public abstract void visit(IncludeOp e);

	public abstract void visit(PresenceExp e);
	
	
}
