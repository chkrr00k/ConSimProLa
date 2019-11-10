package languages;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class Scanner{

	private int index; // how many chars we did
	private int nextPos; // the length of the next token
	private String s; // the input string
	private String current; // the current scanned token
	
	private AScanner as;
	
	public Scanner(String input, List<String> delim){
		this.as = new AScanner(input, delim);
		this.index = 0;
		this.nextPos = 0;
		this.s = input;
		this.current = "";
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
	
	public Optional<Token> getNextToken() throws Exception {
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

	public boolean hasNext() {
		return this.as.hasNext();
	}
}
