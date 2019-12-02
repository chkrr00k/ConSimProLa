package languages;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD}) 
public @interface Followed {
	
	public enum DelimType{
		NONE, EVERYTHING, DELIM;
	}

	public DelimType value() default DelimType.EVERYTHING;
	
	
}
