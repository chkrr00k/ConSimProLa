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
 * INSTRUCTION ::= FUNCDELC
 * INSTRUCTION ::= RETURN
 * INSTRUCTION ::= FOR
 * INSTRUCTION ::= WHEN
 * INSTRUCTION ::= IMPORT
 * 
 * IMPORT ::= import IDENT ;
 * 
 * BLOCK ::= { INSTRUCTION+ }
 *  
 * LINE ::= SEQ ;
 * 
 * SEQ ::= ARRPUSH
 * SEQ ::= SEQ, ARRPUSH
 * 
 * ARRPUSH ::= BOEXP -> IDENT // #2 is an array
 * ARRPUSH ::= BOEXP
 * 
 * BOEXP ::= ANDEXP
 * BOEXP ::= BOEXP || ANDEXP
 * 
 * ANDEXP ::= RELEXP && ANDEXP
 * ANDEXP ::= RELEXP
 * 
 * RELEXP ::= RELEXP [<|<=|>|>=|==|!=] EXP
 * RELEXP ::= EXP
 * 
 * EXP ::= TERM
 * EXP ::= EXP [+|-] TERM
 * EXP ::= ASSIGN
 * EXP ::= OBJ
 * 
 * ASSIGN ::= IDENT = EXP
 * ASSIGN ::= IDENT = IF
 * ASSIGN ::= IDENT = WHILE //useless
 * ASSIGN ::= IDENT [ EXP ] = EXP
 * ASSIGN ::= IDENT <- IDENT // #2 is an array
 * ASSIGN ::= STREAM
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
 * FACTOR ::= $ ARRAY [ EXP ]
 * FACTOR ::= size IDENT //ident is an array
 * FACTOR ::= & IDENT 
 * 
 * 
 * 
 * OBJFIELDS ::= OBJFIELDDECL
 * OBJFIELDS ::= OBJFIELDS, OBJFIELDDECL
 * 
 * OBJFIELDDECL ::= FIELD => EXP
 * OBJFIELDDECL ::= FIELD => IF
 * OBJFIELDDECL ::= => EXP
 * 
 * ~>
 * OBJEXTRACTION ::= $ OBJ . FIELD
 * OBJASSIGN ::= OBJ . FIELD = EXP
 * OBJASSIGN ::= OBJ . FIELD = IF
 * ~>
 * 
 * OBJ ::= IDENT := ( OBJFIELDS )
 * ARRAY ::= IDENT := [ ]
 * ARRAY ::= IDENT := [ ELEMENTS ]
 * ->
 * CREAT ::= IDENT := OBJ
 * OBJ ::= [ ]
 * OBJ ::= [ ELEMENTS ]
 * OBJ ::= { INNEROBJ }
 * OBJ ::= num
 * INNEROBJ ::= IDENT : OBJ
 * ->
 * 
 * ELEMENTS ::= ELEMENT, ELEMENTS
 * ELEMENTS ::= ELEMENT
 * 
 * FUNCDECL ::= fun IDENT LAMBDA
 * 
 * LAMBDA ::= ( ELEMENTS ) BLOCK
 * 
 * RETURN ::= return IDENT ;
 * RETURN ::= return BOEXP ;
 * RETURN ::= return ;
 * 
 * FUNCALL ::= IDENT ( ELEMENTS )
 * FUNCALL ::= IDENT ( )
 * 
 * FOR ::= for IDENT in IDENT BLOCK
 * 
 * STREAM ::= stream IDENT STREAMOPS STREAMEND
 * STREAMOPS ::= STREAMOP then STREAMOPS
 * STREAMOPS ::= STREAMOP
 * 
 * STREAMOP ::= map LAMBDA
 * STREAMOP ::= filter LAMBDA
 * STREAMEND ::= collect
 * STREAMEND ::= reduce LAMBDA
 * 
 * WHEN ::= when { CONDITIONS }
 * CONDITIONS ::= CONDITION CONDITIONS
 * CONDITIONS ::= CONDITION
 * CONDITION ::= BOEXP then BLOCK
 */

public class Parser {

	private Optional<Token> currTok;
	private Scanner scanner;
	
	@Followed(Followed.DelimType.DELIM)
	private final static String IF = "if";
	@Followed(Followed.DelimType.DELIM)
	private final static String ELSE = "else";
	@Followed(Followed.DelimType.DELIM)
	private final static String WHILE = "while";
	@Followed(Followed.DelimType.DELIM)
	private final static String FOR = "for";
	@Followed(Followed.DelimType.DELIM)
	private final static String IN = "in";
	@Followed(Followed.DelimType.DELIM)
	private final static String WHEN = "when";
	
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
	
	@Followed(Followed.DelimType.DELIM)
	private final static String NOT = "not";
	@Followed(Followed.DelimType.DELIM)
	private final static String OR = "or";
	@Followed(Followed.DelimType.DELIM)
	private final static String AND = "and";
	private final static String EQ = "==";
	private final static String NEQ = "!=";
	private final static String GT = ">";
	private final static String GTE = ">=";
	private final static String LT = "<";
	private final static String LTE = "<=";
	
	private final static String OBJASSIGN = ":=";
	private final static String FIELDASSIGN = ":";
	
	private final static String DEREFERENCING = "&";
	private final static String OPEN_ARR = "[";
	private final static String CLOSE_ARR = "]";
	private final static String ARR_POP = "<-";
	private final static String ARR_PUSH = "->";
	@Followed(Followed.DelimType.DELIM)
	private final static String ARR_SIZE = "size";
	
	@Followed(Followed.DelimType.DELIM)
	private final static String FUNC = "fun";
	@Followed(Followed.DelimType.DELIM)
	private final static String RETURN = "return";
	
	@Followed(Followed.DelimType.DELIM)
	private final static String STREAM = "stream";
	@Followed(Followed.DelimType.DELIM)
	private static final String THEN = "then";
	@Followed(Followed.DelimType.DELIM)
	private final static String MAP = "map";
	@Followed(Followed.DelimType.DELIM)
	private final static String FILTER = "filter";
	@Followed(Followed.DelimType.DELIM)
	private final static String COLLECT = "collect";
	@Followed(Followed.DelimType.DELIM)
	private final static String REDUCE = "reduce";
	
	@Followed(Followed.DelimType.DELIM)
	private final static String IMPORT = "import";
	private static final String PRESENCE = "?";
	
	public Parser(Scanner s) throws Exception {
		this.scanner = s;
		this.currTok = this.scanner.getNextToken();
	}
	
	private void error(String msg) throws Exception{
//		System.err.println("Unexpected token:\n" + this.scanner.errorMsg() + "\nWas expected: " + msg);
		throw new Exception("Unexpected token:\n" + this.scanner.errorMsg() + "\nWas expected: " + msg);
	}
	
	public boolean isEmpty(){
		return !this.scanner.hasNext();
	}
	
	public Program parseProgram() throws Exception{
		Program result = new Program();
		while(this.scanner.hasNext()){
			Instruction i = this.parseInstruction();
			if(i != null){
				result.add(i);
			}
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
		}else if(this.currTok.get().equals(Parser.FUNC)){
			this.currTok = this.scanner.getNextToken();
			result = this.parseFunction();
		}else if(this.currTok.get().equals(Parser.RETURN)){
			this.currTok = this.scanner.getNextToken();
			result = this.parseReturn();
		}else if(this.currTok.get().equals(Parser.FOR)){
			this.currTok = this.scanner.getNextToken();
			result = this.parseFor();
		}else if(this.currTok.get().equals(Parser.WHEN)){
			this.currTok = this.scanner.getNextToken();
			result = this.parseWhen();
		}else if(this.currTok.get().equals(Parser.IMPORT)){
			this.currTok = this.scanner.getNextToken();
			result = this.parseImport();
		}else{
			result = this.parseLine();
		}
		
		return result;
	}
	private Instruction parseImport() throws Exception {
		IncludeOp result = null;
		if(this.currTok.isPresent() && this.currTok.get().isIdentifier()){
			result = new IncludeOp(this.currTok.get().get());
			this.currTok = this.scanner.getNextToken();
		}
		if(!this.currTok.get().equals(Parser.LINE)){
			this.error(Parser.LINE);
		}else{
			this.currTok = this.scanner.getNextToken();
		}
		return result;
	}

	private Instruction parseWhen() throws Exception {
		if(this.currTok.isPresent()){
			WhenExp we = new WhenExp();
			if(this.currTok.isPresent() && this.currTok.get().equals(Parser.OPEN_BLOCK)){
				this.currTok = this.scanner.getNextToken();
				while(this.currTok.isPresent()){
					if(this.currTok.isPresent() && this.currTok.get().equals(Parser.CLOSE_BLOCK)){
						this.currTok = this.scanner.getNextToken();
						return we;
					}else{
						Exp cond = this.parseArrPush();
						if(this.currTok.isPresent() && this.currTok.get().equals(Parser.THEN)){
							this.currTok = this.scanner.getNextToken();
						}else{
							this.error(Parser.THEN);
						}
						Block posBlock = this.parseBlock();
						we.add(cond, posBlock);
					}
				}
			}else{
				this.error(Parser.OPEN_BLOCK);
			}
		}else{
			this.error(Parser.WHEN);
		}
		return null;
	}

	private Instruction parseFor() throws Exception {
		if(this.currTok.isPresent()){
			if(this.currTok.isPresent() && this.currTok.get().isIdentifier()){
				String id = this.currTok.get().get();
				this.currTok = this.scanner.getNextToken();
				if(this.currTok.isPresent() && this.currTok.get().equals(Parser.IN)){
					this.currTok = this.scanner.getNextToken();
					if(this.currTok.isPresent() && this.currTok.get().isIdentifier()){
						String arr = this.currTok.get().get();
						this.currTok = this.scanner.getNextToken();
						Block posBlock = this.parseBlock();
						return new ForInstr(id, arr, posBlock);
					}else{
						this.error("<ident>");
					}
				}else{
					this.error(Parser.IN);
				}
			}else{
				this.error("<ident>");
			}
		}else{
			this.error(Parser.FOR);
		}
		return null;
	}

	private Instruction parseReturn() throws Exception {
		ReturnOp result = null;
		if(this.currTok.isPresent() && this.currTok.get().isIdentifier()){
			result = new ReturnOp(this.currTok.get().get());
			this.currTok = this.scanner.getNextToken();
		}else{
			result = new ReturnOp(this.parseBoExp());
		}
		if(!this.currTok.get().equals(Parser.LINE)){
			this.error(Parser.LINE);
		}else{
			this.currTok = this.scanner.getNextToken();
		}
		return result;
	}

	private LambdaExp parseLambda() throws Exception{
		LambdaExp result = new LambdaExp();
		if(this.currTok.get().equals(Parser.OPEN_PAR)){
			this.currTok = this.scanner.getNextToken();
			boolean done = false;
			while(this.currTok.isPresent() && !done){
				if(this.currTok.get().equals(Parser.SEQ)){
					this.currTok = this.scanner.getNextToken();
					continue;
				}else if(this.currTok.get().equals(Parser.CLOSE_PAR)){
					this.currTok = this.scanner.getNextToken();
					done = true;
				}else{
					result.addArgument(this.currTok.get().get());
					this.currTok = this.scanner.getNextToken();
				}
			}
			if(this.currTok.get().equals(Parser.OPEN_BLOCK)){
				Block b = this.parseBlock();
				result.add(b);
			}else{
				this.error(Parser.OPEN_BLOCK);
			}
		}else{
			this.error(Parser.OPEN_PAR);
		}
		return result;
	}
	private Instruction parseFunction() throws Exception {
		FunctionExp result = new FunctionExp();
		if(this.currTok.isPresent()){
			String name = this.currTok.get().get();
			result.setName(name);
			this.currTok = this.scanner.getNextToken();
			result.add(this.parseLambda());

		}else{
			this.error("<identifier>");
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
		Exp result = this.parseArrPush();
		while(this.currTok.isPresent()){
			if(this.currTok.get().equals(Parser.SEQ)){
				this.currTok = this.scanner.getNextToken();
				Exp n = parseArrPush();
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
			Exp cond = this.parseArrPush();
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
			Exp cond = this.parseArrPush();
			Block posBlock = this.parseBlock();
			return new WhileExp(cond, posBlock);
		}else{
			this.error(Parser.WHILE);
		}
		return null;
	}
	private Exp parseArrPush() throws Exception{
		Exp result = this.parseBoExp();
		 if(this.currTok.isPresent() && this.currTok.get().equals(Parser.ARR_PUSH)){
			 this.currTok = this.scanner.getNextToken();
			 result = new PushExp(result, this.currTok.get().get());
			 this.currTok = this.scanner.getNextToken();
		 }
		return result;
	}
	private Exp parseBoExp() throws Exception{
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
	private Exp parseAndExp() throws Exception{
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
	private Exp parseRelExp() throws Exception{
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
				boolean array = false;
				String id = this.currTok.get().toString();
				Optional<Token> t = Optional.empty();
				Exp index = null;
				if(((t = this.scanner.peek()).isPresent()) && t.get().equals(Parser.OPEN_ARR)){
					this.currTok = this.scanner.getNextToken(); // it's the "["
					this.currTok = this.scanner.getNextToken();
					index = this.parseExp();
					if(this.currTok.isPresent() && this.currTok.get().equals(Parser.CLOSE_ARR)){
						this.currTok = this.scanner.getNextToken();
						array = true;
					}else{
						this.error(Parser.CLOSE_ARR);
					}
				}else{
					this.currTok = this.scanner.getNextToken();		
				}
				if(this.currTok.isPresent() && this.currTok.get().equals(Parser.ASSIGN)){					
					Exp rVal = null;
					this.currTok = this.scanner.getNextToken();
					if(this.currTok.get().equals(Parser.IF)){
						this.currTok = this.scanner.getNextToken();
						rVal = this.parseIf();
					}else if(this.currTok.get().equals(Parser.WHILE)){
						this.currTok = this.scanner.getNextToken();
						rVal = this.parseWhile();
					}else if(this.currTok.get().equals(Parser.STREAM)){
						this.currTok = this.scanner.getNextToken();
						rVal = this.parseStream();
					}else{
						rVal = this.parseBoExp();
					}
					result = new AssignExp(array ? new LValArrayExp(id, index) : new LValExp(id), rVal);
				}else if(this.currTok.isPresent() && this.currTok.get().equals(Parser.OBJASSIGN)){
					this.currTok = this.scanner.getNextToken();
					ComplexAssignExp res = this.parseObj();
					res.setName(id);
					res.setTopLevel();
					return res;
				}else if(this.currTok.isPresent() && this.currTok.get().equals(Parser.ARR_POP)){
					this.currTok = this.scanner.getNextToken();
					if(this.currTok.isPresent() && this.currTok.get().isIdentifier()){
						result = new PopExp(id, this.currTok.get().get());
					}
					this.currTok = this.scanner.getNextToken();
				}else{
					this.error(Parser.ASSIGN + " or " + Parser.OBJASSIGN);
				}
			}else{
				return result;
			}
		}
		return result;
	}
	
	private Exp parseStream() throws Exception {
		if(this.currTok.isPresent()){
			String arr = this.currTok.get().get();
			StreamExp so = new StreamExp(arr);
			this.currTok = this.scanner.getNextToken();
			while(this.currTok.isPresent()){
				if(this.currTok.isPresent() && this.currTok.get().equals(Parser.REDUCE)){
					this.currTok = this.scanner.getNextToken();
					if(this.currTok.isPresent()){
						so.add(this.parseReduce());
						return so;
					}
				}else if(this.currTok.isPresent() && this.currTok.get().equals(Parser.COLLECT)){
						so.add(this.parseCollect());
						this.currTok = this.scanner.getNextToken();
						return so;
				}else if(this.currTok.isPresent() && this.currTok.get().equals(Parser.MAP)){
					this.currTok = this.scanner.getNextToken();
					if(this.currTok.isPresent()){
						so.add(this.parseMap());
					}
				}else if(this.currTok.isPresent() && this.currTok.get().equals(Parser.FILTER)){
					this.currTok = this.scanner.getNextToken();
					if(this.currTok.isPresent()){
						so.add(this.parseFilter());
					}
				}else{
					this.error(Parser.REDUCE + " " + Parser.COLLECT + " " + Parser.MAP + " " + Parser.FILTER);
				}
				
				if(this.currTok.isPresent() && this.currTok.get().equals(Parser.THEN)){
					this.currTok = this.scanner.getNextToken();
				}
			}
		}else{
			this.error("<ident>");
		}
		return null;
	}

	private StreamOp parseMap() throws Exception {
		LambdaExp l = this.parseLambda();
		return new StreamMap(l);
	}
	private StreamOp parseFilter() throws Exception {
		LambdaExp l = this.parseLambda();
		return new StreamFilter(l);
	}
	private StreamOp parseReduce() throws Exception {
		LambdaExp l = this.parseLambda();
		return new StreamReduce(l);
	}
	private StreamOp parseCollect() throws Exception {
		return new StreamCollect();
	}
// { a : 3, b : [], c : {}}
	private ComplexAssignExp parseObj() throws Exception{
		if(this.currTok.get().equals(Parser.OPEN_BLOCK)){
			ObjAssignExp resultmp = new ObjAssignExp();
			this.currTok = this.scanner.getNextToken();
			while(this.currTok.isPresent()){
				if(this.currTok.get().equals(Parser.SEQ)){
					this.currTok = this.scanner.getNextToken();
					continue;
				}else{
					String id = this.currTok.get().get();
					this.currTok = this.scanner.getNextToken();
					if(this.currTok.isPresent() && this.currTok.get().equals(Parser.FIELDASSIGN)){
						this.currTok = this.scanner.getNextToken();
						ComplexAssignExp inner = this.parseObj();
						resultmp.add(id, inner);
					}else{
						this.error(Parser.FIELDASSIGN);
					}

					if(this.currTok.get().equals(Parser.CLOSE_BLOCK)){
						this.currTok = this.scanner.getNextToken();
						return resultmp;
					}else{
						this.currTok = this.scanner.getNextToken();
					}
				}
			}
			return resultmp;
		}else if(this.currTok.get().equals(Parser.OPEN_ARR)){
			this.currTok = this.scanner.getNextToken();
			ArrayAssignExp resultmp = new ArrayAssignExp();
			while(this.currTok.isPresent()){
				if(this.currTok.get().equals(Parser.SEQ)){
					this.currTok = this.scanner.getNextToken();
					continue;
				}else if(this.currTok.get().equals(Parser.CLOSE_ARR)){
					this.currTok = this.scanner.getNextToken();
					return resultmp;
				}else{
					ComplexAssignExp inner = this.parseObj();
					resultmp.add(inner);
				}
			}
		}else{
			Exp bo = this.parseBoExp();
			return new ExpAssignExp(bo);
		}
		return null;
	}

	/*private Field parseField() throws Exception{
		Field result = null;
		while(this.currTok.isPresent()){
			if(this.currTok.get().isIdentifier()){
				String id = this.currTok.get().toString();
				this.currTok = this.scanner.getNextToken();
				if(this.currTok.isPresent() && this.currTok.get().equals(Parser.FIELDASSIGN)){
					Exp rVal = null;
					this.currTok = this.scanner.getNextToken();
					if(this.currTok.get().equals(Parser.IF)){
						this.currTok = this.scanner.getNextToken();
						rVal = this.parseIf();
					}else{
						rVal = this.parseExp();
					}
					result = new Field(id, rVal);
				}else if(this.currTok.isPresent() && this.currTok.get().equals(Parser.FIELDOBJASSIGN)){
					Exp rVal = null;
					this.currTok = this.scanner.getNextToken();
					rVal = this.parseObj(id);
					result = new Field(id, rVal);
				}else{
					this.error(Parser.FIELDASSIGN);
				}
			}else if(this.currTok.get().equals(Parser.FIELDASSIGN)){
				this.currTok = this.scanner.getNextToken();
				Exp val;
				if(this.currTok.get().equals(Parser.IF)){
					this.currTok = this.scanner.getNextToken();
					val = this.parseIf();
				}else{
					val = this.parseExp();
				}
				result = new Field(val);
			}else{
				return result;
			}
		}
		return result;
	}*/

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
			Optional<Token> t = Optional.empty();
			if(((t = this.scanner.peek()).isPresent()) && t.get().equals(Parser.OPEN_ARR)){
				this.currTok = this.scanner.getNextToken(); // it's the "["
				this.currTok = this.scanner.getNextToken();
				Exp index = this.parseExp();
				if(this.currTok.isPresent() && this.currTok.get().equals(Parser.CLOSE_ARR)){
					this.currTok = this.scanner.getNextToken();
					return new RValArrayExp(id, index);
				}else{
					this.error(Parser.CLOSE_ARR);
				}
			}else if(t.isPresent() && t.get().equals(Parser.OPEN_PAR)){
				this.currTok = this.scanner.getNextToken(); // it's the "("
				FunctionCall fc = new FunctionCall(id);
				this.currTok = this.scanner.getNextToken();
				boolean done = false;
				while(this.currTok.isPresent() && !done){
					if(this.currTok.get().equals(Parser.SEQ)){
						this.currTok = this.scanner.getNextToken();
						continue;
					}else if(this.currTok.get().equals(Parser.CLOSE_PAR)){
						this.currTok = this.scanner.getNextToken();
						done = true;
					}else{
						fc.add(this.parseBoExp());
					}
				}
				return fc;
			}else{
				this.currTok = this.scanner.getNextToken();
				return new RValExp(id);
			}
		}else if(this.currTok.get().isNumber()){
			double val = this.currTok.get().asDouble();
			this.currTok = this.scanner.getNextToken();
			return new NumExp(val);
		}else if(this.currTok.get().equals(Parser.ARR_SIZE)){
			this.currTok = this.scanner.getNextToken();
			if(this.currTok.isPresent() && this.currTok.get().isIdentifier()){
				String id = this.currTok.get().get();
				this.currTok = this.scanner.getNextToken();
				return new SizeExp(id);
			}
		}else if(this.currTok.get().equals(Parser.DEREFERENCING)){
			this.currTok = this.scanner.getNextToken();
			if(this.currTok.isPresent() && this.currTok.get().isIdentifier()){
				String id = this.currTok.get().get();
				this.currTok = this.scanner.getNextToken();
				return new DerefExp(id);
			}
		}else if(this.currTok.get().equals(Parser.PRESENCE)){
			this.currTok = this.scanner.getNextToken();
			if(this.currTok.isPresent() && this.currTok.get().isIdentifier()){
				String id = this.currTok.get().get();
				this.currTok = this.scanner.getNextToken();
				return new PresenceExp(id);
			}
		}
		return null;
	}

	public final static List<Match> getAcceptedSeparators(){
		return Arrays.stream(Parser.class.getDeclaredFields())
				.filter(f -> {
					return Modifier.isStatic(f.getModifiers());
				}).map(e -> {
					try{
						if(e.isAnnotationPresent(Followed.class)){
							return new Match(e.get(Parser.class).toString() + "", e.getAnnotation(Followed.class).value());
						}else{
							return new Match(e.get(Parser.class) + "");
						}
					}catch(Exception e1){
						return null;
					}
				}).collect(Collectors.toList());
	}
	public final static Parser of(String s) throws Exception{
		Scanner sc = new Scanner(s,  Parser.getAcceptedSeparators());
		return new Parser(sc);
	}
}
