package languages;

import languages.Followed.DelimType;

public class Match{
	public final String token;
	public final DelimType next;
	
	public Match(String token, DelimType next) {
		this.token = token;
		this.next = next;
	}
	public Match(String token) {
		this.token = token;
		this.next = DelimType.EVERYTHING;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.token);
		builder.append(" ");
		builder.append(this.next);
		return builder.toString();
	}
	
}
