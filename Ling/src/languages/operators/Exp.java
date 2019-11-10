package languages.operators;

import languages.visitor.ExpVisitor;

abstract public class Exp extends Instruction{
	public abstract void accept(ExpVisitor v);
}
