package com.github.alienideology.javason;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Immutable, or unmodifiable json array.
 * This extends {@link JsonArray} and throw {@link UnsupportedOperationException}s for methods.
 * 
 * @author AlienIdeology
 */
public class ImmutableJsonArray extends JsonArray {

	@Override
	public <T> Collection<T> toCollection(Collection<T> t) {
		return Collections.unmodifiableCollection(super.toCollection(t));
	}

	@Override
	public JsonArray add(Object object) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("The JSON array is immutable!");
	}

	@Override
	public JsonArray add(Object[] objects) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("The JSON array is immutable!");
	}

	@Override
	public JsonArray put(int index, Object object) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("The JSON array is immutable!");
	}

	@Override
	public Object remove(int index) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("The JSON array is immutable!");
	}

	@Override
	public Object remove(Object object) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("The JSON array is immutable!");
	}

	@Override
	public JsonArray clear() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("The JSON array is immutable!");
	}
	
	@Override
	public Collection<Object> getAsCollection() {
		return Collections.unmodifiableCollection(super.getAsCollection());
	}

	@Override
	public void forEach(Consumer<? super Object> arg0) {
		getAsCollection().forEach(arg0);
	}

	@Override
	public Iterator<Object> iterator()  {
		return getAsCollection().iterator();
	}

	@Override
	public Spliterator<Object> spliterator() {
		return getAsCollection().spliterator();
	}
	
}
