package com.github.alienideology.javason;

/**
 * A handler for serializing and deserializing objects.
 * 
 * @author AlienIdeology
 *
 * @param <T> The object to serialize to.
 */
public class SerializeHandler<T> {

	public void onSerialize(JsonObject json) {}
	
	public void onDeserialize(T t) {}
	
}
