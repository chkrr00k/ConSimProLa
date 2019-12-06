package languages.main;

import java.util.Arrays;

import languages.Parser;
import languages.environment.Function;
import languages.environment.Value;
import languages.environment.Valueable;
import languages.operators.Program;
import languages.visitor.EvalExpVisitor;
import languages.visitor.ExpVisitor;

public class Main {
	public static void main(String[] args) {
		String e = ""
				+ "fun array(length) {"
				+ " result := [];"
				+ " while size result < $length {"
				+ "  0 -> result;"
				+ " }"
				+ " return result;"
				+ "}"
				+ "fun insertAt(input, element, index){"
				+ " tmp := [];"
				+ " i := 0;"
				+ " while $i <= $index {"
				+ "  el <- input;"
				+ "  i := $i + 1;"
				+ "  $el -> tmp;"
				+ " }"
				+ " $element -> input;"
				+ " while size tmp {"
				+ "  el <- tmp;"
				+ "  $el -> input;"
				+ " }"
				+ " return input;"
				+ "}"
				+ "fun copy(input){"
				+ " return input;"
				+ "}"
				+ "fun arrayOf(obj, len){"
				+ " result := [];"
				+ " while size result <= $len {"
				+ "  &obj -> result;"
				+ " }"
				+ " return result;"
				+ "}"
				+ "fun range(start, stop, step){"
				+ " res := [];"
				+ " i := $stop;"
				+ " tmp := $stop;"
				+ " while $i > $start {"
				+ "  tmp = $tmp - $step;"
				+ "  i := $i - 1;"
				+ "  $tmp -> res;"
				+ " }"
				+ " return res;"
				+ "}"
				+ ""//stdlib
				+ "test = 2;"
				+ "if $test {"
				+ "y = 0;"
				+ "b = if $y {"
				+ " x = 3 , y = 2 + 8, ( z := $x - $y, 9 );"
				+ " x = $z -9;"
				+ " g = $x^$z;"
				+ " $g;"
				+ "} else {"
				+ " x = 4;"
				+ " k = if $x or 2 {"
				+ "  y = 2;"
				+ "  f = if $y {"
				+ "   $y;"
				+ "  };"
				+ " };"
				+ "} + 5;"
				+ "i = 3, a = 0;"
				+ "g = 9;"
				+ "while $i {"
				+ " i = $i - 1;"
				+ " a = 2 + $a;"
				+ "}"
				+ "h = 3/2;"
				+ "}else{"
				+ " a = 9;"
				+ "}"
				+ "{"
				+ " q = 3;"
				+ " if $q {"
				+ "  t = 9;"
				+ " }"
				+ "}"
				+ "a1 := [1,2,3,4,5,6];"
				+ "pt = 4;"
				+ "p := $array($pt);"
				+ "a1 = $insertAt($a1, 4, 2);"
				+ "a2 = $copy($a1);"
				+ "2 -> a2;"
				+ "cc := {g : 8};"
				+ "cc2 = $copy(&cc);"
				+ "z21 = $arrayOf(&cc, 4);"
				+ "c = $z21[3];"
				+ "c.f = 9;"
				+ "def := [];"
				+ "$array -> def;"
				+ "new <- def;"
				+ "wwwwe = $new(5);"
				+ "k := [];"
				+ "for i in $wwwwe {"
				+ " $i + 4 -> k;"
				+ "}"
				+ ""
				+ "dd = $range(2, 5, 1);"
				+ "sum = stream dd reduce (a, b) { return $a + $b; };"
				+ "dd2 = stream dd filter (a) { return $a > 3; } "
				+ "then reduce (a, b) { ttt = $a + $b; return ttt; };"
				+ "cccccc = 6;"
				+ ""
				+ "dd3 := stream dd filter(a) { t = $a > 3; return t; }"
				+ "then map(e) { return ($e + 1);} then collect;"
				+ "when {"
				+ " (3) then {"
				+ "  e = 9;"
				+ " }"
				+ " 0 then { e = 6;}"
				+ "}"
				+ "i = 9;"
				+ "i = 10;"
				+ "import NaN;"
				+ "g := {p : $NaN()};"
				+ "y = 0;"
		;

		Program r;
		
		try{
			
			ExpVisitor v = new EvalExpVisitor();

			Parser p = Parser.of(e);
			
			r = p.parseProgram();
		
			
			System.out.println("input:\t\t" + r);
			
			r.accept(v);

			System.out.println("result:\t\t" + v.getResult());
			System.out.println("result (Obj):\t" + ((EvalExpVisitor) v).getValue());
			System.out.println(((EvalExpVisitor) v).getEnvironment());
			System.out.println(p.isEmpty()? "Correct" : "Incorrect");
		}catch(Exception e1){
			System.err.println(e1.getMessage());
			e1.printStackTrace();
		}
	}
}
