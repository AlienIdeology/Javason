package com.github.alienideology.javason.reflect;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * An annotation for specifying the field or parameter's name when serializing or deserializing.
 * 
 * @author AlienIdeology
 */
@Retention(RUNTIME)
@Target(value ={FIELD, PARAMETER})
public @interface Serialization {

	String key() default "";
	
}
