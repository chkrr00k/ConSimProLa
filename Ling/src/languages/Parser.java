package languages;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import languages.operators.*;
/**
 * Grammar
 * 
 * PROGRAM ::= INSTRUCTION+
 * 
 * INSTRUCTION ::= BLOCK
 * INSTRUCTION ::= LINE
 * INSTRUCTION ::= IF
 * INSTRUCTION ::= WHILE
 * 
 * BLOCK ::= { INSTRUCTION+ }
 *  
 * LINE ::= SEQ ;
 * 
 * SEQ ::= EXP //BOEXP
 * SEQ ::= SEQ, EXP //SEQ, BOEXP
 * 
 * BOEXP ::= ANDEXP
 * BOEXP ::= BOEXP || ANDEXP
 * 
 * ANDEXP ::= RELEXP && ANDEXP
 * ANDEXP ::= RELEXP
 * 
 * RELEXP ::= RELEXP [<|<=|>|>=|==] EXP
 * RELEXP ::= EXP
 * 
 * EXP ::= TERM
 * EXP ::= EXP [+|-] TERM
 * EXP ::= ASSIGN
 * 
 * ASSIGN ::= IDENT = EXP
 * ASSIGN ::= IDENT = IF
 * ASSIGN ::= IDENT = WHILE //useless
 * 
 * IF ::= if BOEXP BLOCK else BLOCK
 * WHILE ::= while BOEXP BLOCK
 * 
 * TERM ::= POT
 * TERM ::= TERM [*|/|%] POT
 * 
 * POT ::= NEGEXP
 * POT ::= NEGEXP ^ POT
 * 
 * NEGEXP ::= FACTOR
 * NEGEXP ::= ! FACTOR
 * 
 * FACTOR ::= - FACTOR
 * FACTOR ::= num
 * FACTOR ::= ( SEQ )
 * FACTOR ::= $ IDENT
 * 
 * 
 * ->
 * OBJ ::= IDENT := ( OBJFIELDS )
 * 
 * OBJFIELDS ::= OBJFIELDDECL
 * OBJFIELDS ::= OBJFIELDS, OBJFIELDDECL
 * 
 * OBJFIELDDECL ::= FIELD => EXP
 * OBJFIELDDECL ::= FIELD => IF
 * 
 * OBJEXTRACTION ::= $ OBJ . FIELD
 * OBJASSIGN ::= OBJ . FIELD = EXP
 * OBJASSIGN ::= OBJ . FIELD = IF
 * 
 */
/*
 * definition
 * a := (value => 9, expr => (3 + $variable));
 * use
 * a.value = 2;
 * if $a.value { ... }
 */
public class Parser {

	private Optional<Token> currTok;
	private Scanner scanner;
	
	private final static String IF = "if";
	private final static String ELSE = "else";
	private final static String WHILE = "while";
	
	private final static String POW = "^";
	private final static String PLUS = "+";
	private final static String DIV = "/";
	private final static String MUL = "*";
	private final static String SUB = "-";
	private final static String MOD = "%";
	private final static String ASSIGN = "=";
	private final static String RVAL = "$";
	private final static String OPEN_PAR = "(";
	private final static String CLOSE_PAR = ")";
	private final static String OPEN_BLOCK = "{";
	private final static String CLOSE_BLOCK = "}";
	private final static String SEQ = ",";
	private final static String LINE = ";";
	
	private final static String NOT = "not";
	private final static String OR = "or";
	private final static String AND = "and";
	private final static String EQ = "==";
	private final static String NEQ = "!=";
	private final static String GT = ">";
	private final static String GTE = ">=";
	private final static String LT = "<";
	private final static String LTE = "<=";
	
	public Parser(Scanner s) throws Exception {
		this.scanner = s;
		this.currTok = this.scanner.getNextToken();
		
	}
	
	private void error(String msg) throws Exception{
		throw new Exception("Unexpected token:\n" + this.scanner.errorMsg() + "\nWas expected: " + msg);
	}
	
	public boolean isEmpty(){
		return !this.scanner.hasNext();
	}
	
	public Program parseProgram() throws Exception{
		Program result = new Program();
		while(this.scanner.hasNext()){
			result.add(this.parseInstruction());
		}
		return result;
		
	}
	
	private Instruction parseInstruction() throws Exception{
		Instruction result = null;
		if(this.currTok.get().equals(Parser.OPEN_BLOCK)){
			result = this.parseBlock();
		}else if(this.currTok.get().equals(Parser.IF)){
			this.currTok = this.scanner.getNextToken();
			result = this.parseIf();
		}else if(this.currTok.get().equals(Parser.WHILE)){
			this.currTok = this.scanner.getNextToken();
			result = this.parseWhile();
		}else{
			result = this.parseLine();
		}
		
		return result;
	}
	
	private Block parseBlock() throws Exception {
		Block result = new Block();
		Instruction innerExp = null;
		this.currTok = this.scanner.getNextToken();
		while(!this.currTok.get().equals(Parser.CLOSE_BLOCK)){
//			innerExp = this.parseLine();
			innerExp = this.parseInstruction();
			if(innerExp != null){
				result.add(innerExp);
			}
		}
		this.currTok = this.scanner.getNextToken();
		return result;
	}
	
	private Line parseLine() throws Exception{
		Exp result = this.parseSeq();
		if(this.currTok.isPresent()){
			if(this.currTok.get().equals(Parser.LINE)){
				this.currTok = this.scanner.getNextToken();
				return new Line(result);
			}else{
				this.error(Parser.LINE);
				return null;
			}
		}else{
			this.error(Parser.LINE);
		}
		return null;
	}
	
	private Exp parseSeq() throws Exception{
		Exp result = this.parseBoExp();
		while(this.currTok.isPresent()){
			if(this.currTok.get().equals(Parser.SEQ)){
				this.currTok = this.scanner.getNextToken();
				Exp n = parseBoExp();
				if(n != null){
					result = new SeqExp(result, n);
				}else{
					this.error(Parser.SEQ);
					return null;
				}
			}else{
				return result;
			}
		}
		return result;
	}
	
	private Exp parseIf() throws Exception{
		if(this.currTok.isPresent()){
			Exp cond = this.parseBoExp();
			Block posBlock = this.parseBlock();
			IfExp ie = new IfExp(cond, posBlock);
			if(this.currTok.isPresent() && this.currTok.get().equals(Parser.ELSE)){
				this.currTok = this.scanner.getNextToken();
				if(this.currTok.isPresent()){
					Block negBlock = this.parseBlock();
					ie.addElse(negBlock);
				}
			}
			return ie;
		}else{
			this.error(Parser.IF);
		}
		return null;
	}
	private Exp parseWhile() throws Exception{
		if(this.currTok.isPresent()){
			Exp cond = this.parseBoExp();
			Block posBlock = this.parseBlock();
			return new WhileExp(cond, posBlock);
		}else{
			this.error(Parser.WHILE);
		}
		return null;
	}
	
	public Exp parseBoExp() throws Exception{
		Exp result = this.parseAndExp();
		
		while(this.currTok.isPresent()){
			if(this.currTok.get().equals(Parser.OR)){
				this.currTok = this.scanner.getNextToken();
				Exp nextTerm = this.parseAndExp();
				if(nextTerm != null){
					result = new OrExp(result, nextTerm);
				}else{
					this.error("<or expression>");
				}
			}else{
				return result;
			}
		}
		
		return result;
	}
	public Exp parseAndExp() throws Exception{
		Exp result = this.parseRelExp();
		
		while(this.currTok.isPresent()){
			if(this.currTok.get().equals(Parser.AND)){
				this.currTok = this.scanner.getNextToken();
				Exp nextTerm = this.parseRelExp();
				if(nextTerm != null){
					result = new AndExp(result, nextTerm);
				}else{
					this.error("<and expression>");
				}
			}else{
				return result;
			}
		}
		
		return result;
	}
	public Exp parseRelExp() throws Exception{
		Exp result = this.parseExp();
		
		while(this.currTok.isPresent()){
			if(this.currTok.get().equals(Parser.EQ)){
				this.currTok = this.scanner.getNextToken();
				Exp nextTerm = this.parseExp();
				if(nextTerm != null){
					result = new EqExp(result, nextTerm);
				}else{
					this.error("<comparison expression>");
				}
			}else if(this.currTok.get().equals(Parser.NEQ)){
				this.currTok = this.scanner.getNextToken();
				Exp nextTerm = this.parseExp();
				if(nextTerm != null){
					result = new NeqExp(result, nextTerm);
				}else{
					this.error("<comparison expression>");
				}
			}else if(this.currTok.get().equals(Parser.GT)){
				this.currTok = this.scanner.getNextToken();
				Exp nextTerm = this.parseExp();
				if(nextTerm != null){
					result = new GtExp(result, nextTerm);
				}else{
					this.error("<comparison expression>");
				}
			}else if(this.currTok.get().equals(Parser.GTE)){
				this.currTok = this.scanner.getNextToken();
				Exp nextTerm = this.parseExp();
				if(nextTerm != null){
					result = new GteExp(result, nextTerm);
				}else{
					this.error("<comparison expression>");
				}
			}else if(this.currTok.get().equals(Parser.LT)){
				this.currTok = this.scanner.getNextToken();
				Exp nextTerm = this.parseExp();
				if(nextTerm != null){
					result = new LtExp(result, nextTerm);
				}else{
					this.error("<comparison expression>");
				}
			}else if(this.currTok.get().equals(Parser.LTE)){
				this.currTok = this.scanner.getNextToken();
				Exp nextTerm = this.parseExp();
				if(nextTerm != null){
					result = new LteExp(result, nextTerm);
				}else{
					this.error("<comparison expression>");
				}
			}else{
				return result;
			}
		}
		
		return result;
	}
	
	private Exp parseExp() throws Exception{
		Exp result = this.parseTerm();
		while(this.currTok.isPresent()){
			if(this.currTok.get().equals(Parser.PLUS)){
				this.currTok = this.scanner.getNextToken();
				Exp nextTerm = this.parseTerm();
				if(nextTerm != null){
					result = new PlusExp(result, nextTerm);
				}else{
					this.error("<value>");
				}
			}else if(this.currTok.get().equals(Parser.SUB)){
				this.currTok = this.scanner.getNextToken();
				Exp nextTerm = this.parseTerm();
				if(nextTerm != null){
					result = new MinusExp(result, nextTerm);
				}else{
					this.error("<value>");
				}
			}else if(this.currTok.get().isIdentifier()){
				String id = this.currTok.get().toString();
				this.currTok = this.scanner.getNextToken();
				if(this.currTok.isPresent() && this.currTok.get().equals(Parser.ASSIGN)){
					Exp rVal = null;
					this.currTok = this.scanner.getNextToken();
					if(this.currTok.get().equals(Parser.IF)){
						this.currTok = this.scanner.getNextToken();
						rVal = this.parseIf();
					}else if(this.currTok.get().equals(Parser.WHILE)){
						this.currTok = this.scanner.getNextToken();
						rVal = this.parseWhile();
					}else{
						rVal = this.parseExp();
					}
					result = new AssignExp(new LValExp(id), rVal);
				}else{
					this.error(Parser.ASSIGN);
				}
			}else{
				return result;
			}
		}
		return result;
	}

	private Exp parseTerm() throws Exception {
		Exp result = this.parsePot();
		while(this.currTok.isPresent()){
			if(this.currTok.get().equals(Parser.MUL)){
				this.currTok = this.scanner.getNextToken();
				Exp nextFactor = this.parsePot();
				if(nextFactor != null){
					result = new MulExp(result, nextFactor);
				}else{
					this.error("<value>");
				}
			}else if(this.currTok.get().equals(Parser.DIV)){
				this.currTok = this.scanner.getNextToken();
				Exp nextFactor = this.parsePot();
				if(nextFactor != null){
					result = new DivExp(result, nextFactor);
				}else{
					this.error("<value>");
				}
			}else if(this.currTok.get().equals(Parser.MOD)){
				this.currTok = this.scanner.getNextToken();
				Exp nextFactor = this.parsePot();
				if(nextFactor != null){
					result = new ModExp(result, nextFactor);
				}else{
					this.error("<value>");
				}
			}else{
				return result;
			}
		}
		return result;
	}
	
	private Exp parsePot() throws Exception {
//		Exp result = parseFactor();
		Exp result = parseNot();
		if(this.currTok.isPresent()){
			if(this.currTok.get().equals(Parser.POW)) {
				this.currTok = scanner.getNextToken();
				Exp nextFactor = this.parsePot();
				if(nextFactor != null){
					result = new PowExp(result, nextFactor);
				}else{
					this.error("<value>");
				}
			}else {
				return result;
			} 
		}
		return result;   
	}
	
	private Exp parseNot() throws Exception{
		if(this.currTok.isPresent()){
			if(this.currTok.get().equals(Parser.NOT)) {
				
				this.currTok = scanner.getNextToken();
				Exp result = this.parseFactor();
				if(result != null){
					return new NotExp(result);
				}else{
					this.error("<value or variable>");
				}
			}else {
				return this.parseFactor();
			} 
		}
		this.error("<value or variable>");
		return null;   
	}

	private Exp parseFactor() throws Exception {
		if(this.currTok.get().equals(Parser.OPEN_PAR)){
			this.currTok = this.scanner.getNextToken();
			Exp innerExp = this.parseSeq();
			if(this.currTok.get().equals(Parser.CLOSE_PAR)){
				this.currTok = this.scanner.getNextToken();
				return innerExp;
			}else{
				this.error(Parser.CLOSE_PAR);
			}
		}else if(this.currTok.get().equals(Parser.RVAL)){
			this.currTok = this.scanner.getNextToken();
			String id = this.currTok.get().toString();
			this.currTok = this.scanner.getNextToken();
			return new RValExp(id);
		}else if(this.currTok.get().isNumber()){
			double val = this.currTok.get().asDouble();
			this.currTok = this.scanner.getNextToken();
			return new NumExp(val);
		}
		return null;
	}

	public final static List<String> getAcceptedSeparators(){
		return Arrays.stream(Parser.class.getDeclaredFields())
				.filter(f -> {
					return Modifier.isStatic(f.getModifiers());
				}).map(e -> {
					try{
						return "" + e.get(Parser.class);
					}catch(Exception e1){
						return "";
					}
				}).collect(Collectors.toList());
	}
	public final static Parser of(String s) throws Exception{
		Scanner sc = new Scanner(s,  Parser.getAcceptedSeparators());
		return new Parser(sc);
	}
}
