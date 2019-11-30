package languages;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class AScanner {

	private StringTokenizer strTok;
	
	private Optional<String> subToken;
	private Queue<String> next;
	
	private List<String> keywords;
	
		
	public AScanner(String inner, List<String> delims) {
		this.keywords = delims.stream().filter(d -> {
			// return d.length() > 1;
			return true;
		}).collect(Collectors.toList());
		
		this.strTok = new StringTokenizer(inner, " "/* + String.join("", delims.stream().filter(d -> {
			return d.length() == 1;
		}).collect(Collectors.toList()))*/, true);
		
		this.subToken = Optional.empty();
		this.next = new LinkedList<String>();
	}
	public String get() throws Exception{
		return this.get(false);
	}
	private String get(boolean force) throws Exception{
		if(!force && this.next.size() > 0){
			return this.next.poll();
		}else{
			String newTok = "";
			if(this.subToken.isPresent()){ // if we stored a non finished token	
				Optional <Integer> mid = this.keywords.stream().filter(k -> {
					return this.subToken.get().startsWith(k);
				}).map(k -> {
					return k.length();
				}).max(Integer::compare);
					if(mid.isPresent()){
						String result = this.subToken.get().substring(0, mid.get());
						newTok = this.subToken.get().substring(mid.get());
						if(newTok.length() == 0){
							this.subToken = Optional.empty();
						}else{
							this.subToken = Optional.of(newTok);
						}
						return result;
					}// if we are here the current token is a single and can be returned;
					
				mid = this.keywords.stream().map((k) ->{
					return this.subToken.get().indexOf(k);
				}).filter(v -> {
					return v > 0;
				}).min(Integer::compare); // gets the minimum index of the nearest token
				if(mid.isPresent()){ // if it found one
					newTok = this.subToken.get().substring(0, mid.get());
					if(newTok.length() == 0){ // token is finished
						this.subToken = Optional.empty();
					}else{
						this.subToken = Optional.of(this.subToken.get().substring(mid.get()));
					}
					return newTok;
				}else{
					newTok = this.subToken.get();
					this.subToken = Optional.empty();
					return newTok;
				}
			}else{ // we ask for a new token
				String tok = this.strTok.nextToken();
				if(this.keywords.stream().anyMatch((e -> {
					return tok.contains(e);
				}))){ // if there is a keyword in the token
					this.subToken = Optional.of(tok);
					return this.get(); // recursive call!
				}else{
					return tok;
				}
			}
		}
	}
	public Optional<String> peek() throws Exception{
		try{
			if(this.next.size() >= 1){
				return Optional.of(this.next.toArray(new String[this.next.size()])[0]);
			}else if(this.next.size() == 0){
				String n = this.get(true);
				this.next.add(n);
				return Optional.of(n);
			}else{
				return Optional.empty();
			}
		}catch(NoSuchElementException e){
			return Optional.empty();
		}
	}
	public boolean hasNext() {
		return this.subToken.isPresent() || this.strTok.hasMoreTokens() || this.next.size() > 0;
	}
	
	public static void main(String[] args) throws Exception {
		String t = "( z = $x - $y, 9 == 3 ) x=x+1";
		AScanner a = new AScanner(t, Parser.getAcceptedSeparators());
		while(a.hasNext()){
			System.out.println(a.get() + "\t" + a.peek());
		}
	}
}
