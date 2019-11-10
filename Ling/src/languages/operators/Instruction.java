package languages.operators;

import languages.visitor.ExpVisitor;

public abstract class Instruction {

	public abstract void accept(ExpVisitor v);
	public abstract String name();

}
