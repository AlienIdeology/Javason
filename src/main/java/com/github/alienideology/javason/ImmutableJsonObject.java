package com.github.alienideology.javason;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Immutable, or unmodifiable json object.
 * This extends {@link JsonObject} and throw {@link UnsupportedOperationException}s for methods.
 * 
 * @author AlienIdeology
 */
public class ImmutableJsonObject extends JsonObject {

	@Override
	public JsonObject put(String key, Object value) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("The JSON object is immutable!");
	}

	@Override
	public Object remove(String key) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("The JSON object is immutable!");
	}

	@Override
	public Entry<String, Object> removeByValue(Object value) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("The JSON object is immutable!");
	}

	@Override
	public JsonObject clear() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("The JSON object is immutable!");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public HashMap<String, Object> getAsMap() {
		return (HashMap) Collections.unmodifiableMap(super.getAsMap());
	}
	
}
