package languages.tests;
import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import languages.Parser;
import languages.environment.Array;
import languages.environment.Complex;
import languages.environment.Environment;
import languages.environment.Function;
import languages.environment.NativeFunction;
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
	Program r;
	@Before
	public void setUp() throws Exception {
		v = new EvalExpVisitor();
	}

	@After
	public void tearDown() throws Exception {
		assertTrue(p.isEmpty());
	}
	private Environment test(String code) throws Exception{
		
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
	public void testSize2() throws Exception {
		Environment e = this.test("expected = 3; el = size expected;");
		assertTrue((((Value) e.get("el")).getValue().equals(1d)));
	}
	@Test
	public void testSize3() throws Exception {
		Environment e = this.test("expected := {a : 4;b:2}; el = size expected;");
		assertTrue((((Value) e.get("el")).getValue().equals(2d)));
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
				+ "expected = $array(4);"
				);
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
				+ "cc := {e:4, z:6};"
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
	@Test
	public void testHigerOrder() throws Exception {
		Environment e = this.test(
				"fun get(in){"
				+ " fun result(i){"
				+ "  return $i > $in;"
				+ " }"
				+ " return &result;"
				+ "}"
				+ "comp = $get(4);"
				+ "expected = $comp(9);");
		assertTrue((((Value) e.get("expected")).getValue().equals(9d)));
	}
/*	@Test
	public void testApplication() throws Exception {
		Environment e = this.test(
				"fun visit(client){"
				+ " client.visited = $client.visited + 1;"
				+ " return &client;"
				+ "}"
				+ "report := [];"
				+ "client := (id => 0, visited => 0);"
				+ "client = $visit(&client);"
				+ "&client -> report;");
	}*/
	@Test
	public void testObj() throws Exception {
		Environment e = this.test(
				"obj := {a : 3, b : [], c : [1,2,3], d :{e : 4}};"
				+ "obj2 := 5;"
				+ "obj3 := [1,2,3,4];"
				+ "obj4 := {inn : [{e:4}]};"
				+ "c = $obj4;");
	}
	@Test
	public void testNativeInterface() throws Exception {
		Environment e = this.test(
				"import rand;"
				+ "import time;"
				+ "expected = $rand();"
				+ "timestamp = $time();");
		assertTrue(e.get("rand") instanceof NativeFunction);
	}
	@Test
	public void testIntegerCheck() throws Exception {
		Environment e = this.test(
				"fun isInt(input){"
				+ " res = ($input % 1) == 0;"
				+ " return res;"
				+ "}"
				+ "expected = $isInt(1);"
				+ "expected2 = $isInt(6.32);");
		assertTrue((((Value) e.get("expected")).getValue().equals(1d)));
		assertTrue((((Value) e.get("expected2")).getValue().equals(0d)));
	}
	@Test
	public void testFunctions() throws Exception {
		Environment e = this.test(
				"import ceil;"
				+ "import floor;"
				+ "import abs;"
				+ "import round;"
				+ "expected = $ceil(12.34);"
				+ "expected1 = $floor(12.34);"
				+ "expected2 = $abs(-12.34);"
				+ "expected3 = $round(12.34);");
		assertTrue((((Value) e.get("expected")).getValue().equals(13d)));
		assertTrue((((Value) e.get("expected1")).getValue().equals(12d)));
		assertTrue((((Value) e.get("expected2")).getValue().equals(12.34d)));
		assertTrue((((Value) e.get("expected3")).getValue().equals(12d)));
	}
	@Test
	public void testSortArray() throws Exception {
		Environment e = this.test(
				""
				+ "fun swap(input, p1, p2){"
				+ " tmp = $input[$p1];"
				+ " input[$p1] = $input[$p2];"
				+ " input[$p2] = $tmp;"
				+ " return &input;"
				+ "}"
				+ "fun posMax(v, n){"
				+ " i = 0;"
				+ " max = 0;"
				+ " while $i < $n {"
				+ "  if $v[$max] < $v[$i]{"
				+ "   max = $i;"
				+ "  }"
				+ " i = $i + 1;"
				+ " }"
				+ " return $max;"
				+ "}"
				+ "fun naive(v, n){"
				+ " p = 0;"
				+ " while $n > 1 {"
				+ "  p = $posMax(&v, $n);"
				+ "  if $p < $n - 1 {"
				+ "   v = $swap(&v, $p, $n - 1);"
				+ "  }"
				+ "  n = $n - 1;"
				+ " }"
				+ " return &v;"
				+ "}"
				+ "a := [5,4,3,2,1];"
				+ "expected = $naive(&a, size a);");
		assertEquals(5, ((Array) e.get("expected")).getAll().size());
		assertTrue(((Value) ((Array) e.get("expected")).get(0)).getValue().equals(1d));
		assertTrue(((Value) ((Array) e.get("expected")).get(1)).getValue().equals(2d));
		assertTrue(((Value) ((Array) e.get("expected")).get(2)).getValue().equals(3d));
		assertTrue(((Value) ((Array) e.get("expected")).get(3)).getValue().equals(4d));
		assertTrue(((Value) ((Array) e.get("expected")).get(4)).getValue().equals(5d));
	}
	@Test
	public void testFunctionSwap() throws Exception {
		Environment e = this.test(
				""
				+ "fun swap(input, p1, p2){"
				+ " tmp = $input[$p1];"
				+ " input[$p1] = $input[$p2];"
				+ " input[$p2] = $tmp;"
				+ " return &input;"
				+ "}"
				+ "a := [5,4,3,2,1];"
				+ "expected = $swap(&a, 2, 4);");
		assertEquals(5, ((Array) e.get("expected")).getAll().size());
		assertTrue(((Value) ((Array) e.get("expected")).get(0)).getValue().equals(5d));
		assertTrue(((Value) ((Array) e.get("expected")).get(1)).getValue().equals(4d));
		assertTrue(((Value) ((Array) e.get("expected")).get(2)).getValue().equals(1d));
		assertTrue(((Value) ((Array) e.get("expected")).get(3)).getValue().equals(2d));
		assertTrue(((Value) ((Array) e.get("expected")).get(4)).getValue().equals(3d));
	}
	@Test
	public void testSwap() throws Exception {
		Environment e = this.test(
				"p1 = 2;"
				+ "p2 = 4;"
				+ "e := [5,4,3,2,1];"
				+ " tmp = $e[$p1];"
				+ " e[$p1] = $e[$p2];"
				+ " e[$p2] = $tmp;");
		assertEquals(5, ((Array) e.get("e")).getAll().size());
		assertTrue(((Value) ((Array) e.get("e")).get(0)).getValue().equals(5d));
		assertTrue(((Value) ((Array) e.get("e")).get(1)).getValue().equals(4d));
		assertTrue(((Value) ((Array) e.get("e")).get(2)).getValue().equals(1d));
		assertTrue(((Value) ((Array) e.get("e")).get(3)).getValue().equals(2d));
		assertTrue(((Value) ((Array) e.get("e")).get(4)).getValue().equals(3d));
	}
	@Test
	public void testIndirectSwap() throws Exception {
		Environment e = this.test(
				"p1 := 2;"
				+ "p2 := 4;"
				+ "e := [5,4,3,2,1];"
				+ " tmp1 = $e[$p1];"
				+ " tmp2 = $e[$p2];"
				+ " e[$p1] = $tmp2;"
				+ " e[$p2] = $tmp1;");
		assertEquals(5, ((Array) e.get("e")).getAll().size());
		assertTrue(((Value) ((Array) e.get("e")).get(0)).getValue().equals(5d));
		assertTrue(((Value) ((Array) e.get("e")).get(1)).getValue().equals(4d));
		assertTrue(((Value) ((Array) e.get("e")).get(2)).getValue().equals(1d));
		assertTrue(((Value) ((Array) e.get("e")).get(3)).getValue().equals(2d));
		assertTrue(((Value) ((Array) e.get("e")).get(4)).getValue().equals(3d));
	}
	@Test
	public void testVarToArray() throws Exception {
		Environment e = this.test(
				"tmp = 2;"
				+ "p2 = 4;"
				+ "e := [5,4,3,2,1];"
				+ " e[$p2] = $tmp;");
		assertEquals(5, ((Array) e.get("e")).getAll().size());
		assertTrue(((Value) ((Array) e.get("e")).get(0)).getValue().equals(5d));
		assertTrue(((Value) ((Array) e.get("e")).get(1)).getValue().equals(4d));
		assertTrue(((Value) ((Array) e.get("e")).get(2)).getValue().equals(3d));
		assertTrue(((Value) ((Array) e.get("e")).get(3)).getValue().equals(2d));
		assertTrue(((Value) ((Array) e.get("e")).get(4)).getValue().equals(2d));
	}
	@Test
	public void testArrayToArray() throws Exception {
		Environment e = this.test(
				"p1 = 2;"
				+ "p2 = 4;"
				+ "e := [5,4,3,2,1];"
				+ " e[$p1] = $e[$p2];"
				);
		assertEquals(5, ((Array) e.get("e")).getAll().size());
		assertTrue(((Value) ((Array) e.get("e")).get(0)).getValue().equals(5d));
		assertTrue(((Value) ((Array) e.get("e")).get(1)).getValue().equals(4d));
		assertTrue(((Value) ((Array) e.get("e")).get(2)).getValue().equals(1d));
		assertTrue(((Value) ((Array) e.get("e")).get(3)).getValue().equals(2d));
		assertTrue(((Value) ((Array) e.get("e")).get(4)).getValue().equals(1d));
	}
	@Test
	public void testLValArrayExp() throws Exception {
		Environment e = this.test(
				"c = 11;"
				+ "i = 0;"
				+ "a := [5,4,3,2,1];"
				+ "a[$i] = $c;");
		assertEquals(5, ((Array) e.get("a")).getAll().size());
		assertTrue(((Value) ((Array) e.get("a")).get(0)).getValue().equals(11d));
		assertTrue(((Value) ((Array) e.get("a")).get(1)).getValue().equals(4d));
		assertTrue(((Value) ((Array) e.get("a")).get(2)).getValue().equals(3d));
		assertTrue(((Value) ((Array) e.get("a")).get(3)).getValue().equals(2d));
		assertTrue(((Value) ((Array) e.get("a")).get(4)).getValue().equals(1d));
	}
	@Test
	public void testRValArrayExp() throws Exception {
		Environment e = this.test(""
				+ "i = 3;"
				+ "a := [5,4,3,2,1];"
				+ "c = $a[$i];");
		assertTrue((((Value) e.get("c")).getValue().equals(2d)));
	}
	@Test
	public void testArrayObject() throws Exception {
		Environment e = this.test(""
				+ "i = 1;"
				+ "a := [{q:2}, {q:4}];"
				+ "c = $a[$i];"
				+ "c = $c.q;");
		assertTrue((((Value) e.get("c")).getValue().equals(4d)));
	}
	@Test
	public void testArrayObjectInserting() throws Exception {
		Environment e = this.test(""
				+ "i = 2;"
				+ "c := {q : 50};"
				+ "a := [{q:2}, {q:4}, 0];"
				+ "a[$i] = $c;");
		assertTrue(e.get("c") instanceof Complex);
		assertTrue(((Array) e.get("a")).get(2) instanceof Complex);
	}
	@Test
	public void testArrayArrayInserting() throws Exception {
		Environment e = this.test(""
				+ "i = 2;"
				+ "c := [1,2,3];"
				+ "a := [{q:2}, {q:4}, 0];"
				+ "a[$i] = $c;");
		assertTrue(e.get("c") instanceof Array);
		assertTrue(((Array) e.get("a")).get(2) instanceof Array);
	}
	@Test
	public void testArrayFunctionInserting() throws Exception {
		Environment e = this.test(""
				+ "fun s(){}"
				+ "i = 2;"
				+ "a := [{q:2}, {q:4}, 0];"
				+ "a[$i] = $s;");
		assertTrue(e.get("s") instanceof Function);
		assertTrue(((Array) e.get("a")).get(2) instanceof Function);
	}
	@Test
	public void testObjAssign() throws Exception {
		Environment e = this.test(""
				+ "o := {a:4};"
				+ "o.a = 9;"
				+ ""
				+ "");
		Complex c = (Complex) e.get("o");
		assertEquals(9d, (double)((Value) c.getField("a")).getValue(), 0);
	}
	@Test
	public void testObjArrayAssign() throws Exception {
		Environment e = this.test(""
				+ "o := {a:4};"
				+ "o.a := [6];"
				+ "v = $o.a;"
				+ "4 -> o.a;"
				+ "k <- o.a;"
				+ "4 -> o.a;"
				+ "");
		Complex c = (Complex) e.get("o");
		assertEquals(4d, (double)((Value) e.get("k")).getValue(), 0);
		assertTrue(c.getField("a") instanceof Array);
		assertEquals(2, ((Array) c.getField("a")).getAll().size());
	}
	@Test
	public void testForObj() throws Exception {
		Environment e = this.test(""
				+ "o := {a:4, c:9};"
				+ "z := [];"
				+ "for el in o{"
				+ " $el -> z;"
				+ "}"
				+ "");
		assertEquals(2, ((Array) e.get("z")).getAll().size());
	}
	@Test
	public void testZero() throws Exception {
		Environment e = this.test(""
				+ "fun zero(obj){"
				+ " for el in obj{"
				+ "  el = 0;"
				+ " }"
				+ " return obj;"
				+ "}"
				+ "o := {a:4, c:9};"
				+ "e = $zero($o);"
				+ "");
	}
}

