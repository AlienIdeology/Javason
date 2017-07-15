package com.github.alienideology.javason;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 * Central utility class for the Json parser Javason.
 * 
 * @author AlienIdeology
 */
public class Javason {
	
	/**
	 * A list of allowed escape characters in a json string.
	 */
	public final static List<Character> ALLOWED_ESCAPE_CHARACTERS = Arrays.asList('\"', '\\', '/', '\b', '\f', '\n', '\r', '\t');
	
	/**
	 * Get the number from a string.
	 * 
	 * @param number The string to create number from.
	 * @return The number created.
	 * @throws NumberFormatException 
	 * 		If the string is not a valid number.
	 */
	public static Number getNumberFromString(String number) throws NumberFormatException {
		BigDecimal num = new BigDecimal(number); 
		
		// Double
		if (num.doubleValue() % 1 != 0) {
			// BigDecimal
			if (num.doubleValue() > Double.MAX_VALUE || num.doubleValue() < Double.MIN_VALUE) {
				return num;
			}
			return num.doubleValue();
		// Long
		} else if (num.toBigIntegerExact().intValue() > Integer.MAX_VALUE || num.toBigIntegerExact().intValue() < Integer.MIN_VALUE) {
			// BigInteger
			if (num.longValueExact() > Long.MAX_VALUE || num.longValueExact() < Long.MIN_VALUE) {
				return BigInteger.valueOf(num.longValueExact());
			}
			return num.longValue();
		// Integer
		} else {
			return num.intValue();
		}
	}
	
	/**
	 * Check if a number is valid.
	 * @see Double#isInfinite()
	 * @see Double#isNaN()
	 * @see Float#isInfinite()
	 * @see Float#isNaN()
	 * 
	 * @param num The number to check with.
	 * @return True if the number is valid.
	 */
	public static boolean isNumberValid(Number num) {
		if (num != null) {
			if ((num instanceof Double) && (Double.isInfinite((Double) num) || Double.isNaN((Double) num))) {
				return false;
			} else if ((num instanceof Float) && (Float.isInfinite((Float) num) || Float.isNaN((Float) num))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Validate a given number. See {@link #isNumberValid(Number)} for the number validation.
	 * 
	 * @param num The number to check.
	 * @throws IllegalArgumentException
	 * 		If the number is not valid.
	 */
	public static void validateNumber(Number num) throws IllegalArgumentException {
		if (!isNumberValid(num)) {
			throw new IllegalArgumentException("The number is not valid!");
		}
	}
	
}
