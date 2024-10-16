package edu.austincollege.acvote.unit;

/**
 * Home of convenient string utilities used in testing.   Recommended
 * to use static import into your test class.
 * 
 * @author mahiggs
 *
 */
public class StringUtil {

	
	/**
	 * Receives a string with embedded single quotes and replaces with double quotes
	 * so programmers don't have to escape double quotes when double quotes are essential.
	 * <p>
	 * This is especially useful when dealing with literal strings in testing code.
	 * 
	 * @param anyStr
	 * @return string with single quotes replaced with double quotes.
	 */
	public static String dquote(String anyStr) {
		if (anyStr == null) return null;
		return anyStr.replaceAll("[']", "\"");
	}
	
	
}
