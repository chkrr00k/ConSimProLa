package languages.tests;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import languages.Parser;
import languages.environment.Environment;
import languages.environment.Value;
import languages.operators.Program;
import languages.visitor.EvalExpVisitor;
import languages.visitor.ExpVisitor;


public class EvalTest {
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	EvalExpVisitor v;
	Parser p;
	@Before
	public void setUp() throws Exception {
		v = new EvalExpVisitor();
	}

	@After
	public void tearDown() throws Exception {
		assertTrue(p.isEmpty());
	}
	private Environment test(String code) throws Exception{
		Program r;
		p = Parser.of(code);
		r = p.parseProgram();		
		r.accept(v);
		
		return v.getEnvironment();
	}
	@Test
	public void testPlus() throws Exception {
		Environment e = this.test("expected = 2 + 4;");
		assertTrue((((Value) e.get("expected")).getValue().equals(6d)));
	}
	@Test
	public void testMinus() throws Exception {
		Environment e = this.test("expected = 2 - 4;");
		assertTrue((((Value) e.get("expected")).getValue().equals(-2d)));
	}


}
