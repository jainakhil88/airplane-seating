package org.ss.demo.airplane.seating.util;

public final class Util {

	private static final String REGEX_FOR_SQUARE_BRACKET_COMMA = "[ \\[ \\] ,  \\s{1,}]";

	public static boolean doesInputContainAllNumbers(String input) {
		String parsedInput = input.replaceAll(REGEX_FOR_SQUARE_BRACKET_COMMA, "").trim();
		return parsedInput.chars().allMatch(Character::isDigit);
	}
	
}
