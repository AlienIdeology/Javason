package com.github.alienideology.javason;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * A pure json array with a generic type, easier for enhanced for loop.
 * 
 * @author AlienIdeology
 *
 * @param <E> The generic element type.
 */
@SuppressWarnings("unchecked")
public class PureJsonArray<E> implements Iterable<E> {
	
	private JsonArray array;
	
	/**
	 * Create a new empty json array.
	 */
	public PureJsonArray() {
		this.array = new JsonArray();
	}
	
	/**
	 * Create a new pure array from an existed {@link JsonArray} with the specified {@link Class} type.
	 * This will ignore all elements with different type than the specified class.
	 * 
	 * @param array The json array.
	 * @param clazz The specified class.
	 */
	public PureJsonArray(JsonArray array, Class<E> clazz) {
		this();
		// TODO: Convert objects
		for (Object obj : array) {
			if (clazz.isInstance(obj)) {
				this.array.add(obj);
			} else if (clazz.isPrimitive()) {
				if (clazz.isInstance(Number.class) && obj instanceof Number) {
					this.array.add(obj);
				}
			} else if (clazz.isInstance(String.class)) {
				this.array.add(String.valueOf(obj));
			}
		}
	}
	
	/**
	 * Create a pure array from a {@link Collection}.
	 * 
	 * @param collection The collection.
	 */
	public PureJsonArray(Collection<E> collection) {
		this.array = new JsonArray(collection);
	}
	
	/**
	 * Create a pure array from an array of generic objects.
	 * 
	 * @param array The array of generic objects.
	 */
	public PureJsonArray(E[] array) {
		this.array = new JsonArray(array);
	}
	
	/**
	 * Get an object from a given index.
	 * @see JsonArray#get(int)
	 * 
	 * @param index The index.
	 * @return The object found.
	 */
	public E get(int index) {
		return (E) array.get(index);
	}
	
	/**
	 * Add an element to the array.
	 * @see JsonArray#add(Object)
	 * 
	 * @param obj The element to add.
	 * @return This PureJsonArray, easier for chaining.
	 */
	public PureJsonArray<E> add(E obj) {
		array.add(obj);
		return this;
	}
	
	/**
	 * Add or replace an element at the given index.
	 * @see JsonArray#put(int, Object)
	 * 
	 * @param index The index.
	 * @param obj The element.
	 * @return This PureJsonArray, easier for chaining.
	 */
	public PureJsonArray<E> put(int index, E obj) {
		array.put(index, obj);
		return this;
	}
	
	/**
	 * Remove a value at a given index.
	 * @see JsonArray#remove(int)
	 * 
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @return The object removed.
	 */
	public E remove(int index) {
		return (E) array.remove(index);
	}
	
	/**
	 * Remove a value.
	 * @see JsonArray#remove(Object)
	 * 
	 * @param object The object to search for.
	 * @return The object removed.
	 */
	public E remove(E object) {
		return (E) array.remove(object);
	}
	
	/**
	 * Get the size, or length, of this array.
	 * 
	 * @return The size.
	 */
	public int size() {
		return array.size();
	}
	
	/**
	 * Check if the array is empty or not.
	 * 
	 * @return True if the array is empty.
	 */
	public boolean isEmpty() {
		return array.isEmpty();
	}
	
	/**
	 * Clear the json array, removing all elements.
	 * 
	 * @return This PureJsonArray, easier for chaining.
	 */
	public PureJsonArray<E> clear() {
		array.clear();
		return this;
	}
	
	/**
	 * Get this pure array as a json array.
	 * 
	 * @return The json array.
	 */
	public JsonArray getAsJsonArray() {
		return array;
	}
	
	/**
	 * Get this pure json array as an array.
	 * 
	 * @return The array.
	 */
	public E[] getAsArray() {
		return (E[]) array.getAsArray();
	}
	
	/**
	 * Get this pure json array as a {@link Collection}.
	 * 
	 * @return The collection.
	 */
	public Collection<E> getAsCollection() {
		return (Collection<E>) array.getAsCollection();
	}

	@Override
	public void forEach(Consumer<? super E> action) {
		array.forEach((Consumer<? super Object>) action);
	}

	@Override
	public Iterator<E> iterator() {
		return (Iterator<E>) array.iterator();
	}

	@Override
	public Spliterator<E> spliterator() {
		return (Spliterator<E>) array.spliterator();
	}
	
	/**
	 * Construct a json string from this json array with no indentation.
	 * 
	 * @return The json string.
	 */
	@Override
	public String toString() {
		return toString(0);
	}
	
	/**
	 * Construct a json string from this json array with the given indentation.
	 * 
	 * @param indent The amount of white spaces to indent.
	 * @return The json string.
	 */
	public String toString(int indent) {
		return array.toString(indent);
	}

}
