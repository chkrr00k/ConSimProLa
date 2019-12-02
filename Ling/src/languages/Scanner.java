package languages;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Queue;

public class Scanner{

	private int index; // how many chars we did
	private int nextPos; // the length of the next token
	private String s; // the input string
	private String current; // the current scanned token
	
	private AScanner as;
	private Queue<NextInfo> next;
	
	public Scanner(String input, List<Match> list){
		this.as = new AScanner(input, list);
		this.index = 0;
		this.nextPos = 0;
		this.s = input;
		this.current = "";
		this.next = new LinkedList<Scanner.NextInfo>();
	}

	public String errorMsg(){
		char[] spaces = new char[this.index];
		Arrays.fill(spaces, ' ');
		char[] errors = new char[this.current.trim().length()];
		Arrays.fill(errors, '^');
		
		StringBuilder builder = new StringBuilder();
		builder.append(this.s.substring(
				this.index >= 10 ? this.index - 10 : this.index,
				this.index + 10 >= this.s.length() - 1 ?  this.s.length() : this.index + 10));
		builder.append("\n");
		
		builder.append(new String(spaces).substring(this.index >= 10 ? this.index - 10 : this.index));
		builder.append(new String(errors));
		return builder.toString();
	}
	public Optional<Token> peek(){
		try{
			if(this.next.size() == 0){
				String result = "";
				int index = this.nextPos;
				int nextPos = 0;
				String current = "";
				NextInfo n = null;
				do{
					result = this.as.get();
					index += nextPos;
					nextPos = result.length();
					current = result;
				}while(result.trim().isEmpty());
				n = new NextInfo(new Token(result.trim()), nextPos, index, current);
				this.next.add(n);
				return Optional.of(n.t);
			}else{
				return Optional.of(this.next.toArray(new NextInfo[this.next.size()])[0].t);
			}
		}catch(Exception e){
			return Optional.empty();
		}
	}
	public Optional<Token> getNextToken() throws Exception {
		if(this.next.size() > 0){
			NextInfo n = this.next.poll();
			this.index += n.index;
			this.nextPos = n.nextPos;
			this.current = n.t.get();
			return Optional.of(n.t);
		}else{
			try{
				String result = "";
				do{
					result = this.as.get();
					this.index += this.nextPos;
					this.nextPos = result.length();
					this.current = result;
				}while(result.trim().isEmpty());
				return Optional.of(new Token(result.trim()));
			}catch(NoSuchElementException e){
				return Optional.empty();
			}
		}
	}

	public boolean hasNext() {
		return this.as.hasNext();
	}
	
	private class NextInfo{
		public final Token t;
		public final int nextPos;
		public final int index;
		public final String current;
		public NextInfo(Token t, int nextPos, int index, String current) {
			this.t = t;
			this.nextPos = nextPos;
			this.index = index;
			this.current = current;
		}
	}
	
	public static void main(String[] args) throws Exception {
		String t = "( if $x or 2 {";
		Scanner a = new Scanner(t, Parser.getAcceptedSeparators());
		while(a.hasNext()){
			System.out.println(a.getNextToken());
		}
	}
}
