package languages;

public class Token {
	
	private String tk;
	
	public Token(String tk){
		this.tk = tk;
	}

	public boolean isIntNumber() {
		try{
			Integer.parseInt(this.tk);
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}
	public boolean isNumber() {
		try{
			Double.parseDouble(this.tk);
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}

	public boolean equals(String s) {
		return s.equals(this.tk);
	}

	public int asInt() {
		return Integer.parseInt(this.tk);
	}
	public double asDouble() {
		return Double.parseDouble(this.tk);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.tk);
		return builder.toString();
	}

	public boolean isIdentifier() {
		return this.tk.matches("\\w+") && !Parser.getAcceptedSeparators().contains(this.tk);
	}



}
