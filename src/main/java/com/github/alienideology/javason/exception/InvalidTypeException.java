package com.github.alienideology.javason.exception;

/**
 * Exception for invalid getter type.
 * 
 * @author AlienIdeology
 */
public class InvalidTypeException extends RuntimeException {

	public InvalidTypeException(Class<?> expect, Class<?> get) {
		super("Expecting " + expect.getSimpleName() + " as the value, get " + get.getSimpleName());
	}

}
