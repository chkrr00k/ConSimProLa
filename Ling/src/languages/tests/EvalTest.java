package languages.tests;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import languages.Parser;
import languages.environment.Array;
import languages.environment.Environment;
import languages.environment.Value;
import languages.environment.Variable;
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
	public void testMul() throws Exception {
		Environment e = this.test("expected = 2 * 4;");
		assertTrue((((Value) e.get("expected")).getValue().equals(8d)));
	}
	@Test
	public void testDiv() throws Exception {
		Environment e = this.test("expected = 2 / 4;");
		assertTrue((((Value) e.get("expected")).getValue().equals(0.5d)));
	}
	@Test
	public void testMod() throws Exception {
		Environment e = this.test("expected = 4 % 2;");
		assertTrue((((Value) e.get("expected")).getValue().equals(0d)));
	}
	@Test
	public void testMinus() throws Exception {
		Environment e = this.test("expected = 2 - 4;");
		assertTrue((((Value) e.get("expected")).getValue().equals(-2d)));
	}
	@Test
	public void testPot() throws Exception {
		Environment e = this.test("expected = 4 ^ 2;");
		assertTrue((((Value) e.get("expected")).getValue().equals(16d)));
	}
	@Test
	public void testGte() throws Exception {
		Environment e = this.test("expected = 2 >= 4;");
		assertTrue((((Value) e.get("expected")).getValue().equals(0d)));
	}
	@Test
	public void testGte2() throws Exception {
		Environment e = this.test("expected = 4 >= 2;");
		assertTrue((((Value) e.get("expected")).getValue().equals(4d)));
	}
	@Test
	public void testGt() throws Exception {
		Environment e = this.test("expected = 2 > 4;");
		assertTrue((((Value) e.get("expected")).getValue().equals(0d)));
	}
	@Test
	public void testGt2() throws Exception {
		Environment e = this.test("expected = 4 > 2;");
		assertTrue((((Value) e.get("expected")).getValue().equals(4d)));
	}
	@Test
	public void testLte() throws Exception {
		Environment e = this.test("expected = 4 <= 2;");
		assertTrue((((Value) e.get("expected")).getValue().equals(0d)));
	}
	@Test
	public void testLte2() throws Exception {
		Environment e = this.test("expected = 2 <= 4;");
		assertTrue((((Value) e.get("expected")).getValue().equals(2d)));
	}
	@Test
	public void testLt() throws Exception {
		Environment e = this.test("expected = 4 < 2;");
		assertTrue((((Value) e.get("expected")).getValue().equals(0d)));
	}
	@Test
	public void testLt2() throws Exception {
		Environment e = this.test("expected = 2 < 4;");
		assertTrue((((Value) e.get("expected")).getValue().equals(2d)));
	}
	@Test
	public void testEq() throws Exception {
		Environment e = this.test("expected = 2 == 4;");
		assertTrue((((Value) e.get("expected")).getValue().equals(0d)));
	}
	@Test
	public void testEq2() throws Exception {
		Environment e = this.test("expected = 4 == 4;");
		assertTrue((((Value) e.get("expected")).getValue().equals(4d)));
	}
	@Test
	public void testNeq() throws Exception {
		Environment e = this.test("expected = 2 != 4;");
		assertTrue((((Value) e.get("expected")).getValue().equals(2d)));
	}
	@Test
	public void testNeq2() throws Exception {
		Environment e = this.test("expected = 4 != 4;");
		assertTrue((((Value) e.get("expected")).getValue().equals(0d)));
	}
	@Test
	public void testNeq3() throws Exception {
		Environment e = this.test("expected = 0 != 4;");
		assertTrue((((Value) e.get("expected")).getValue().equals(1d)));
	}
	@Test
	public void testNot() throws Exception {
		Environment e = this.test("expected = not 4;");
		assertTrue((((Value) e.get("expected")).getValue().equals(0d)));
	}
	@Test
	public void testNot2() throws Exception {
		Environment e = this.test("expected = not 0;");
		assertTrue((((Value) e.get("expected")).getValue().equals(1d)));
	}
	@Test
	public void testAnd() throws Exception {
		Environment e = this.test("expected = 1 and 1;");
		assertTrue((((Value) e.get("expected")).getValue().equals(1d)));
	}
	@Test
	public void testAnd2() throws Exception {
		Environment e = this.test("expected = 0 and 1;");
		assertTrue((((Value) e.get("expected")).getValue().equals(0d)));
	}
	@Test
	public void testAnd3() throws Exception {
		Environment e = this.test("expected = 0 and 0;");
		assertTrue((((Value) e.get("expected")).getValue().equals(0d)));
	}
	@Test
	public void testOr() throws Exception {
		Environment e = this.test("expected = 1 or 1;");
		assertTrue((((Value) e.get("expected")).getValue().equals(2d)));
	}
	@Test
	public void testOr2() throws Exception {
		Environment e = this.test("expected = 0 or 1;");
		assertTrue((((Value) e.get("expected")).getValue().equals(1d)));
	}
	@Test
	public void testOr3() throws Exception {
		Environment e = this.test("expected = 0 or 0;");
		assertTrue((((Value) e.get("expected")).getValue().equals(0d)));
	}
	@Test
	public void testOr4() throws Exception {
		Environment e = this.test("expected = 1 or -1;");
		assertTrue((((Value) e.get("expected")).getValue().equals(2d)));
	}
	@Test
	public void testPar() throws Exception {
		Environment e = this.test("expected = (9);");
		assertTrue((((Value) e.get("expected")).getValue().equals(9d)));
	}
	@Test
	public void testIfAss() throws Exception {
		Environment e = this.test("expected = if 1 { 9;};");
		assertTrue((((Value) e.get("expected")).getValue().equals(9d)));
	}
	@Test
	public void testIfAss2() throws Exception {
		Environment e = this.test("expected = if 0 { 9;};");
		assertTrue((((Value) e.get("expected")).getValue().equals(0d)));
	}
	@Test
	public void testIfElseAss() throws Exception {
		Environment e = this.test("expected = if 1 { 9;}else{4;};");
		assertTrue((((Value) e.get("expected")).getValue().equals(9d)));
	}
	@Test
	public void testIfElseAss2() throws Exception {
		Environment e = this.test("expected = if 0 { 9;}else{4;};");
		assertTrue((((Value) e.get("expected")).getValue().equals(4d)));
	}
	public void testWhileAss() throws Exception {
		Environment e = this.test("times = 3; res = 0; expected = while $times { res = $res + 2;};");
		assertTrue((((Value) e.get("expected")).getValue().equals(4d)));
		assertTrue((((Value) e.get("expected")).getValue().equals(6d)));
	}
	public void test() throws Exception {
		Environment e = this.test("times = 3; res = 0; expected = while $times { res = $res + 2;};");
		assertTrue((((Value) e.get("expected")).getValue().equals(4d)));
		assertTrue((((Value) e.get("expected")).getValue().equals(6d)));
	}
	@Test
	public void testSeq() throws Exception {
		Environment e = this.test("expected = 2, test = 4;");
		assertTrue((((Value) e.get("expected")).getValue().equals(2d)));
		assertTrue((((Value) e.get("test")).getValue().equals(4d)));
	}
	@Test
	public void testUnaryMinus() throws Exception {
		Environment e = this.test("expected = -2;");
		assertTrue((((Value) e.get("expected")).getValue().equals(-2d)));
	}
	@Test
	public void testArr() throws Exception {
		Environment e = this.test("expected := [1,2,3];");
		assertEquals(3, ((Array) e.get("expected")).getAll().size());
	}
	@Test
	public void testPush() throws Exception {
		Environment e = this.test("expected := [1,2,3]; 3 -> expected;");
		assertEquals(4, ((Array) e.get("expected")).getAll().size());
	}
	@Test
	public void testPushExp() throws Exception {
		Environment e = this.test("el = 4; expected := [1,2,3]; $el -> expected;");
		assertEquals(4, ((Array) e.get("expected")).getAll().size());
	}
	@Test
	public void testPushArr() throws Exception {
		Environment e = this.test("expected := [1,2,3]; $expected -> expected;");
		assertEquals(4, ((Array) e.get("expected")).getAll().size());
		assertEquals(3, ((Array) ((Array) e.get("expected")).get(0)).getAll().size());
	}
	@Test
	public void testPop() throws Exception {
		Environment e = this.test("expected := [1,2,3]; el <- expected;");
		assertEquals(2, ((Array) e.get("expected")).getAll().size());
		assertTrue((((Value) e.get("el")).getValue().equals(1d)));
	}
	@Test
	public void testSize() throws Exception {
		Environment e = this.test("expected := [1,2,3]; el = size expected;");
		assertEquals(3, ((Array) e.get("expected")).getAll().size());
		assertTrue((((Value) e.get("el")).getValue().equals(3d)));
	}
	@Test
	public void testFunction() throws Exception {
		Environment e = this.test(""
				+ "fun array(length) {"
				+ " result := [];"
				+ " while size result < $length {"
				+ "  0 -> result;"
				+ " }"
				+ " return result;"
				+ "}"
				+ "expected = $array(4);");
		assertEquals(4, ((Array) e.get("expected")).getAll().size());
	}
	@Test
	public void testFunction2() throws Exception {
		Environment e = this.test(""
				+ "fun insertAt(input, element, index){"
				+ " tmp := [];"
				+ " i = 0;"
				+ " while $i < $index {"
				+ "  el <- input;"
				+ "  i = $i + 1;"
				+ "  $el -> tmp;"
				+ " }"
				+ " $element -> input;"
				+ " while size tmp {"
				+ "  el <- tmp;"
				+ "  $el -> input;"
				+ " }"
				+ " return input;"
				+ "}"
				+ "expected := [1,2,3,4];"
				+ "expected = $insertAt($expected, 4, 2);");
		assertEquals(5, ((Array) e.get("expected")).getAll().size());
		assertTrue(((Value) ((Array) e.get("expected")).get(2)).getValue().equals(4d));
	}
	@Test
	public void testFunctionCopy() throws Exception {
		Environment e = this.test(""
				+ "fun copy(input){"
				+ " return input;"
				+ "}"
				+ "cc := (=>3, f => 4);"
				+ "cc2 = $copy(&cc);"
				+ "");
	}
	@Test
	public void testRecursion() throws Exception {
		Environment e = this.test(
				"fun array(in){"
				+ " if not $in {"
				+ "  res := [];"
				+ "  return res;"
				+ " }else{"
				+ "  res = $array($in - 1);"
				+ "  0 -> res;"
				+ "  return res;"
				+ " }"
				+ "}"
				+ "expected = $array(4);");
		assertEquals(4, ((Array) e.get("expected")).getAll().size());
	}
}
