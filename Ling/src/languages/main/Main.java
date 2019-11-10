package languages.main;

import languages.Parser;
import languages.operators.Instruction;
import languages.operators.Program;
import languages.visitor.EvalExpVisitor;
import languages.visitor.ExpVisitor;

public class Main {
	public static void main(String[] args) {
//		String e = "0.1 -1*4-(((1-1)^ 5)*1)^1 ";
		String e = "test = 2;"
				+ "if $test {"
				+ "y = 0;"
				+ "b = if $y {"
				+ " x = 3 , y = 2 + 8, ( z = $x - $y, 9 );"
				+ " x = $z -9;"
				+ " g = $x^$z;"
				+ " $g;"
				+ "} else {"
				+ " x = 4;"
				+ " k = if (1) {"
				+ "  y = 2;"
				+ "  f = if $y {"
				+ "   $y;"
				+ "  };"
				+ " };"
				+ "} + 5;"
				+ "i = 3, a = 0;"
				+ "g = while $i {"
				+ " i = $i - 1;"
				+ " a = 2 + $a;"
				+ "};"
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
				+ "w = 9;";
		
		Program r;
		
		try{
			
			ExpVisitor v = new EvalExpVisitor();

			Parser p = Parser.of(e);
			r = p.parseProgram();
			
			System.out.println("input:\t\t" + r);
			
			r.accept(v);

			System.out.println("result:\t\t" + v.getResult());
			System.out.println("env:\t\t" + ((EvalExpVisitor) v).getEnvironment());
			System.out.println(p.isEmpty()? "Correct" : "Incorrect");
		}catch(Exception e1){
			System.err.println(e1.getMessage());
			e1.printStackTrace();
		}
	}
}
