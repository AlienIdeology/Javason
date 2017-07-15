package com.github.alienideology.javason.reflect;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * An annotation for excluding fields on serializing and deserializing.
 * 
 * @author AlienIdeology
 */
@Retention(RUNTIME)
@Target(value ={FIELD})
public @interface Exclude {

	boolean excludeOnSerialize() default true;
	
	boolean excludeOnDeserialize() default true;
	
}
