package com.github.alienideology.javason;

import java.math.BigDecimal;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

import com.github.alienideology.javason.exception.InvalidTypeException;
import com.github.alienideology.javason.exception.JsonException;

public class JsonArray implements Iterable<Object> {
	
	private List<Object> array;
	
	/**
	 * Creates an empty JsonArray.
	 */
	public JsonArray() {
		this.array = new ArrayList<>();
	}
	
	/**
	 * Create a JsonArray from a json source.
	 * 
	 * @param json The json source.
	 */
	public JsonArray(String json) {
		this.array = new ArrayList<>(new JsonParser(json).parseArray().getAsCollection());
	}
	
	/**
	 * Create a JsonArray from the given collection.
	 * 
	 * @param collection The collection.
	 */
	public JsonArray(Collection<?> collection) {
		this.array = new ArrayList<>(collection);
	}
	
	/**
	 * Create a JsonArray from the given array.
	 * 
	 * @param array The array.
	 */
	public <T> JsonArray(T[] array) {
		this.array = new ArrayList<>();
		for (T element : array) {
			this.array.add(element);
		}
	}
	
	/**
	 * Create an array by serializing this JsonArray.
	 * 
	 * @param clazz The class for the elements in the array.
	 * @return The array created.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public Object[] toNewArray(Class<Object> clazz) throws InstantiationException, IllegalAccessException {
		// TODO: Serialize JSON Array -> New Java Array
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * Create a collection by serializing this JsonArray.
	 * 
	 * @param clazz The class for the elements in the collection.
	 * @return The collection created.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public Collection<Object> toNewCollection(Class<Object> clazz) throws InstantiationException, IllegalAccessException {
		// TODO: Serialize JSON Array -> New Java Array
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * Override an existed array.
	 * 
	 * @param t The array.
	 * @return The array overwritten.
	 */
	public <T> T[] toArray(T[] t) {
		// TODO: Serialize JSON Array -> Existed Java Array
		return t;
	}
	
	/**
	 * Override an existed collection.
	 * 
	 * @param t The collection.
	 * @return The collection overwritten.
	 */
	public <T> Collection<T> toCollection(Collection<T> t) {
		// TODO: Serialize JSON Array -> Existed Java Collection
		return t;
	}
	
	/**
	 * Creates a {@link PureJsonArray} from this json array, ignoring all elements with different type than the specified class.
	 * 
	 * @param clazz The class type of the pure json array.
	 * @return The pure json array created.
	 */
	public <T> PureJsonArray<T> toPureJsonArray(Class<T> clazz) {
		return new PureJsonArray<T>(this, clazz);
	}
	
	/**
	 * Creates a {@link JsonObject} with one entry: the given key and this array as the matching value.
	 * 
	 * @param key The string key.
	 * @return The json object created.
	 */
	public JsonObject toJsonObject(String key) {
		return new JsonObject().put(key, this);
	}
	
	/**
	 * Get an object from a given index.
	 * 
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @return The object.
	 */
	// Nullable
	public Object get(int index) {
		return array.get(index);
	}
	
	/**
	 * Get the integer value from a given index.
	 * Possible conversions:
	 * <ul>
	 * 		<li>If the value is a {@link Long}, then it will be converted to integer using {@link Math#toIntExact(long)}.</li>
	 * 		<li>If the value is a {@link Number}, then it will be converted using {@link Number#intValue()}.</li>
	 * 		<li>If the value is a {@link String}, then it will be parsed using {@link Integer#parseInt(String)}.</li>
	 * </ul>
	 *
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @return The integer converted.
	 * @throws JsonException
	 * 		If the value at the given index is null.
	 * @throws InvalidTypeException
	 * 		If the value is not listed above, or if the {@link String} cannot be converted to an {@link Integer}. See {@link NumberFormatException}.
	 */
	public int getInt(int index) throws JsonException, InvalidTypeException {
		Object val = array.get(index);
		if (val instanceof Long) {
			return Math.toIntExact((Long) val);
		} else if (val instanceof Number) {
			return ((Number) val).intValue();
		} else if (val instanceof String) {
			try {
				return Integer.parseInt((String) val);
			} catch (NumberFormatException nfe) {
				throw new InvalidTypeException(Integer.class, String.class);
			}
		} else if (val == null) {
			throw new JsonException("The value at index " + index + " is null!");
		} else {
			throw new InvalidTypeException(Integer.class, val.getClass());
		}
	}

	/**
	 * Get the integer value at a given index by invoking {@link #getInt(int)}.
	 * This returns null if a {@link JsonException} is thrown.
	 *
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @return The integer converted.
	 * @throws InvalidTypeException
	 * 		See {@link #getInt(int)}.
	 */
	// Nullable
	public Integer getIntOpt(int index) throws InvalidTypeException {
		try {
			return getInt(index);
		} catch (JsonException ignored) {}
		return null;
	}

	/**
	 * Get the double value from a given index.
	 * Possible conversions:
	 * <ul>
	 * 		<li>If the value is a {@link Number}, then it will be converted using {@link Number#doubleValue()}.</li>
	 * 		<li>If the value is a {@link String}, then it will be parsed using {@link Double#parseDouble(String)}.</li>
	 * </ul>
	 *
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @return The double converted.
	 * @throws JsonException
	 * 		If the value at the given index is null.
	 * @throws InvalidTypeException
	 * 		If the value is not listed above, or if the {@link String} cannot be converted to an {@link Double}. See {@link NumberFormatException}.
	 */
	public double getDouble(int index) throws JsonException, InvalidTypeException {
		Object val = array.get(index);
		if (val instanceof Number) {
			return ((Number) val).doubleValue();
		} else if (val instanceof String) {
			try {
				return Double.parseDouble((String) val);
			} catch (NumberFormatException nfe) {
				throw new InvalidTypeException(Double.class, String.class);
			}
		} else if (val == null) {
			throw new JsonException("The value at index " + index + " is null!");
		} else {
			throw new InvalidTypeException(Double.class, val.getClass());
		}
	}

	/**
	 * Get the double value at a given index by invoking {@link #getDouble(int)}.
	 * This returns null if a {@link JsonException} is thrown.
	 *
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @return The double converted.
	 * @throws InvalidTypeException
	 * 		See {@link #getDouble(int)}.
	 */
	// Nullable
	public Double getDoubleOpt(int index) throws InvalidTypeException {
		try {
			return getDouble(index);
		} catch (JsonException ignored) {}
		return null;
	}

	/**
	 * Get the float value from a given index.
	 * Possible conversions:
	 * <ul>
	 * 		<li>If the value is a {@link Number}, then it will be converted using {@link Number#floatValue()}.</li>
	 * 		<li>If the value is a {@link String}, then it will be parsed using {@link Float#parseFloat(String)}.</li>
	 * </ul>
	 *
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @return The float converted.
	 * @throws JsonException
	 * 		If the value at the given index is null.
	 * @throws InvalidTypeException
	 * 		If the value is not listed above, or if the {@link String} cannot be converted to an {@link Float}. See {@link NumberFormatException}.
	 */
	public float getFloat(int index) throws JsonException, InvalidTypeException {
		Object val = array.get(index);
		if (val instanceof Number) {
			return ((Number) val).floatValue();
		} else if (val instanceof String) {
			try {
				return Float.parseFloat((String) val);
			} catch (NumberFormatException nfe) {
				throw new InvalidTypeException(Float.class, String.class);
			}
		} else if (val == null) {
			throw new JsonException("The value at index " + index + " is null!");
		} else {
			throw new InvalidTypeException(Float.class, val.getClass());
		}
	}

	/**
	 * Get the float value at a given index by invoking {@link #getFloat(int)}.
	 * This returns null if a {@link JsonException} is thrown.
	 *
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @return The float converted.
	 * @throws InvalidTypeException
	 * 		See {@link #getFloat(int)}.
	 */
	// Nullable
	public Float getFloatOpt(int index) throws InvalidTypeException {
		try {
			return getFloat(index);
		} catch (JsonException ignored) {}
		return null;
	}

	/**
	 * Get the long value from a given index.
	 * Possible conversions:
	 * <ul>
	 * 		<li>If the value is a {@link Number}, then it will be converted using {@link Number#longValue()}.</li>
	 * 		<li>If the value is a {@link String}, then it will be parsed using {@link Long#parseLong(String)}.</li>
	 * </ul>
	 *
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @return The long converted.
	 * @throws JsonException
	 * 		If the value at the given index is null.
	 * @throws InvalidTypeException
	 * 		If the value is not listed above, or if the {@link String} cannot be converted to an {@link Long}. See {@link NumberFormatException}.
	 */
	public long getLong(int index) throws JsonException, InvalidTypeException {
		Object val = array.get(index);
		if (val instanceof Number) {
			return ((Number) val).longValue();
		} else if (val instanceof String) {
			try {
				return Long.parseLong((String) val);
			} catch (NumberFormatException nfe) {
				throw new InvalidTypeException(Long.class, String.class);
			}
		} else if (val == null) {
			throw new JsonException("The value at index " + index + " is null!");
		} else {
			throw new InvalidTypeException(Long.class, val.getClass());
		}
	}

	/**
	 * Get the long value at a given index by invoking {@link #getLong(int)}.
	 * This returns null if a {@link JsonException} is thrown.
	 *
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @return The long converted.
	 * @throws InvalidTypeException
	 * 		See {@link #getLong(int)}.
	 */
	// Nullable
	public Long getLongOpt(int index) throws InvalidTypeException {
		try {
			return getLong(index);
		} catch (JsonException ignored) {}
		return null;
	}

	/**
	 * Get a number from a given index.
	 * Possible conversions:
	 * <ul>
	 * 		<li>If the value is a {@link Number}, return.</li>
	 * 		<li>If the value is a {@link String}, then it will be parsed using {@link Javason#getNumberFromString(String)}.</li>
	 * </ul>
	 *
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @return The number converted, or null if the value at the given index is null.
	 * @throws NumberFormatException
	 * 		If the {@link String} cannot be converted to a {@link Number}.
	 * @throws InvalidTypeException
	 * 		If the value is not a listed above.
	 */
	// Nullable
	public Number getNum(int index) throws NumberFormatException,  InvalidTypeException {
		Object val = array.get(index);
		if (val instanceof Number) {
			return (Number) val;
		} else if (val instanceof String) {
			return Javason.getNumberFromString((String) val);
		} else if (val == null) {
			return null;
		} else {
			throw new InvalidTypeException(Number.class, val.getClass());
		}
	}

	/**
	 * Get the big integer value from a given index.
	 * Possible conversions:
	 * <ul>
	 * 		<li>If the value is a {@link BigInteger}, return.</li>
	 * 		<li>If the value if a {@link BigDecimal}, then it will be converted using {@link BigDecimal#toBigIntegerExact()}.</li>
	 * 		<li>If the value is a {@link Number}, then it will be converted using the {@link BigInteger(String)} constructor and {@link Number#toString()}.</li>
	 * 		<li>If the value is a {@link String}, then it will be parsed using the {@link BigInteger(String)} constructor.</li>
	 * </ul>
	 *
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @return The big integer converted, or {@code null} if the value at the given index is null.
	 * @throws InvalidTypeException
	 * 		If the value is not listed above, or if the {@link String} cannot be converted to an {@link BigInteger}. See {@link NumberFormatException}.
	 */
	// Nullable
	public BigInteger getBigInteger(int index) {
		Object val = array.get(index);
		if (val instanceof BigInteger) {
			return (BigInteger) val;
		} else if (val instanceof BigDecimal) {
			return ((BigDecimal) val).toBigInteger();
		} else if (val instanceof Number) {
			return new BigInteger(((Number) val).toString());
		} else  if (val instanceof String) {
			try {
				return new BigInteger((String) val);
			} catch (NumberFormatException nfe) {
				throw new InvalidTypeException(BigInteger.class, String.class);
			}
		} else if (val == null) {
			return null;
		} else {
			throw new InvalidTypeException(BigInteger.class, val.getClass());
		}
	}

	/**
	 * Get the big decimal value from a given index.
	 * Possible conversions:
	 * <ul>
	 * 		<li>If the value is a {@link BigDecimal}, return.</li>
	 * 		<li>If the value if a {@link BigInteger}, then it will be converted using {@link BigDecimal#valueOf(double)} and {@link BigInteger#doubleValue()}.</li>
	 * 		<li>If the value is a {@link Number}, then it will be converted using the {@link BigDecimal(String)} constructor and {@link Number#toString()}.</li>
	 * 		<li>If the value is a {@link String}, then it will be parsed using the {@link BigDecimal(String)} constructor.</li>
	 * </ul>
	 *
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @return The big decimal converted, or {@code null} if the value at the given index is null.
	 * @throws InvalidTypeException
	 * 		If the value is not listed above, or if the {@link String} cannot be converted to an {@link BigDecimal}. See {@link NumberFormatException}.
	 */
	// Nullable
	public BigDecimal getBigDecimal(int index) {
		Object val = array.get(index);
		if (val instanceof BigDecimal) {
			return (BigDecimal) val;
		} else if (val instanceof BigInteger) {
			return BigDecimal.valueOf(((BigInteger) val).doubleValue());
		} else if (val instanceof Number) {
			return new BigDecimal(((Number) val).toString());
		} else  if (val instanceof String) {
			try {
				return new BigDecimal((String) val);
			} catch (NumberFormatException nfe) {
				throw new InvalidTypeException(BigDecimal.class, String.class);
			}
		} else if (val == null) {
			return null;
		} else {
			throw new InvalidTypeException(BigDecimal.class, val.getClass());
		}
	}

	/**
	 * Get the boolean value from a given index.
	 * Possible conversions:
	 * <ul>
	 * 		<li>If the value is a {@link Boolean}, return.</li>
	 * 		<li>If the value is a {@link String}, then it will be parsed using {@link Boolean#parseBoolean(String)}.</li>
	 * </ul>
	 *
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @return The boolean converted.
	 * @throws JsonException
	 * 		If the value at the given index is null.
	 * @throws InvalidTypeException
	 * 		If the value is not listed above.
	 */
	public boolean getBoolean(int index) throws JsonException, InvalidTypeException {
		Object val = array.get(index);
		if (val instanceof Boolean) {
			return (boolean) val;
		} else if (val instanceof String) {
			try {
				return Boolean.parseBoolean((String) val);
			} catch (NumberFormatException nfe) {
				throw new InvalidTypeException(Boolean.class, String.class);
			}
		} else if (val == null) {
			throw new JsonException("The value at index " + index + " is null!");
		} else {
			throw new InvalidTypeException(Boolean.class, val.getClass());
		}
	}

	/**
	 * Get the boolean value from a given key by invoking {@link #getBoolean(int)}.
	 * This returns null if a {@link JsonException} is thrown.
	 *
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @return The boolean converted.
	 * @throws InvalidTypeException
	 * 		See {@link #getBoolean(int)}.
	 */
	// Nullable
	public Boolean getBooleanOpt(int index) throws InvalidTypeException {
		try {
			return getBoolean(index);
		} catch (JsonException ignored) {} 
		return null;
	}
	
	/**
	 * Get the string value from a given index.
	 * This uses {@link String#valueOf(Object)} to convert an object.
	 * 
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @return The string value of any object.
	 */
	// Nullable
	public String getString(int index) {
		return array.get(index) == null ? null : String.valueOf(array.get(index));
	}
	
	/**
	 * Get the JsonObject from a given index.
	 * 
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @return The json object, or {@code null} if the value at the given index is null.
	 * @throws InvalidTypeException 
	 * 		If the value is not a {@link JsonObject}.
	 */
	// Nullable
	public JsonObject getObject(int index) throws InvalidTypeException {
		Object val = array.get(index);
		
		if (val instanceof JsonObject) {
			return (JsonObject) val;
		} else if (val == null) {
			return null;
		} else {
			throw new InvalidTypeException(JsonObject.class, val.getClass());
		}
	}
	
	/**
	 * Get the JsonArray from a given index.
	 * 
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @return The json array, or {@code null} if the value at the given index is null.
	 * @throws InvalidTypeException 
	 * 		If the value is not a {@link JsonArray}.
	 */
	// Nullable
	public JsonArray getArray(int index) throws InvalidTypeException {
		Object val = array.get(index);
		
		if (val instanceof JsonArray) {
			return (JsonArray) val;
		} else if (val == null) {
			return null;
		} else {
			throw new InvalidTypeException(JsonArray.class, val.getClass());
		}
	}
 	
	/**
	 * Check if this json array contains a given value.
	 * 
	 * @param object The object to check with.
	 * @return True if the json array contains the value.
	 */
	public boolean contains(Object object) {
		return array.contains(object);
	}
	
	/**
	 * Check if the value at a given index is not {@code null}.
	 * This also validate the index, so it is not possible to throw {@link IndexOutOfBoundsException}.
	 * 
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @return True if the json object contains the key, and the value is not {@code null}.
	 */
	public boolean notNull(int index) {
		return index >= 0 && index < array.size() && array.get(index) != null;
	}
	
	/**
	 * Add a value to the array.
	 * 
	 * @param object The value to add.
	 * @return This JsonArray, easier for chaining.
	 * @throws IllegalArgumentException
	 * 		If the object is a {@link Double} or {@link Float}, and the number is not valid. See {@link Javason#isNumberValid(Number)}.
	 */
	public JsonArray add(Object object) throws IllegalArgumentException {
		if (object instanceof Number) {
			Javason.validateNumber((Number) object);
		} else if (object.getClass().isArray()) {
			array.add(new JsonArray((Object[]) object));
		} else if (object instanceof Collection) {
			array.add(new JsonArray((Collection<?>) object));
	 	} else {
	 		array.add(object);
		}
		return this;
	}
	
	/**
	 * Add an {@link Integer} value to the array.
	 * 
	 * @param value The integer to add.
	 * @return This JsonArray, easier for chaining.
	 */
	public JsonArray add(int value) {
		array.add(value);
		return this;
	}
	
	/**
	 * Add a {@link Double} value to the array.
	 * 
	 * @param value The double to add.
	 * @return This JsonArray, easier for chaining.
	 * @throws IllegalArgumentException
	 * 		If the number is not valid. See {@link Javason#isNumberValid(Number)}.
	 */
	public JsonArray add(double value) throws IllegalArgumentException {
		array.add(value);
		return this;
	}
	
	/**
	 * Add a {@link Float} value to the array.
	 * 
	 * @param value The float to add.
	 * @return This JsonArray, easier for chaining.
	 * @throws IllegalArgumentException
	 * 		If the number is not valid. See {@link Javason#isNumberValid(Number)}.
	 */
	public JsonArray add(float value) throws IllegalArgumentException {
		array.add(value);
		return this;
	}
	
	/**
	 * Add a {@link Long} value to the array.
	 * 
	 * @param value The long to add.
	 * @return This JsonArray, easier for chaining.
	 */
	public JsonArray add(long value) {
		return add(value);
	}
	
	/**
	 * Add a {@link Boolean} value to the array.
	 * 
	 * @param value The boolean to add.
	 * @return This JsonArray, easier for chaining.
	 */
	public JsonArray add(boolean value) {
		return add(value);
	}
	
	/**
	 * Add a {@link String} value to the array.
	 * 
	 * @param value The string to add.
	 * @return This JsonArray, easier for chaining.
	 */
	public JsonArray add(String value) {
		return add(value);
	}
	
	/**
	 * Add a {@link JsonObject} to the array.
	 * 
	 * @param value The json object to add.
	 * @return This JsonArray, easier for chaining.
	 */
	public JsonArray add(JsonObject value) {
		return add(value);
	}
	
	/**
	 * Add an array of {@link Object}s to the array.
	 * 
	 * @param value The value to add.
	 * @return This JsonArray, easier for chaining.
	 */
	public JsonArray add(Object[] value) {
		return add(new JsonArray(value));
	}
	
	/**
	 * Add a {@link JsonArray} to the array.
	 * 
	 * @param value The json array to add.
	 * @return This JsonArray, easier for chaining.
	 */
	public JsonArray add(JsonArray value) {
		return add(value);
	}
	
	/**
	 * Add a {@link Collection} of {@link Object}s to the array.
	 * 
	 * @param value The collection to add.
	 * @return This JsonArray, easier for chaining.
	 */
	public JsonArray add(Collection<Object> value) {
		return add(new JsonArray(value));
	}
	
	/**
	 * Add or replace the {@link Object} at a given index.
	 * If the index is larger or equal to the {@link #size()}, then the object will be added.
	 * If not, the object will be replaced.
	 * 
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @param object The object to add or replace.
	 * @return This JsonArray, easier for chaining.
	 * @throws IllegalArgumentException
	 * 			If the object is a {@link Double} or {@link Float}, and the number is not valid. See {@link Javason#isNumberValid(Number)}.
	 */
	public JsonArray put(int index, Object object) throws IllegalArgumentException {
		if (object instanceof Number) {
			Javason.validateNumber((Number) object);
		}
		
		if (index < 0) index = 0;
		if (index >= array.size()) {
			array.add(object);
		} else {
			array.set(index, object);
		}
		return this;
	}
	
	/**
	 * Add or replace the {@link Integer} at a given index.
	 * If the index is larger or equal to the {@link #size()}, then the object will be added.
	 * If not, the object will be replaced.
	 * 
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @param value The integer to add or replace.
	 * @return This JsonArray, easier for chaining.
	 */
	public JsonArray put(int index, int value) {
		return put(index, value);
	}
	
	/**
	 * Add or replace the {@link Double} at a given index.
	 * If the index is larger or equal to the {@link #size()}, then the object will be added.
	 * If not, the object will be replaced.
	 * 
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @param value The double to add or replace.
	 * @return This JsonArray, easier for chaining.
	 * @throws IllegalArgumentException
	 * 			If the number is not valid. See {@link Javason#isNumberValid(Number)}.
	 */
	public JsonArray put(int index, double value) throws IllegalArgumentException {
		return put(index, value);
	}
	
	/**
	 * Add or replace the {@link Float} at a given index.
	 * If the index is larger or equal to the {@link #size()}, then the object will be added.
	 * If not, the object will be replaced.
	 * 
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @param value The float to add or replace.
	 * @return This JsonArray, easier for chaining.
	 * @throws IllegalArgumentException
	 * 			If the number is not valid. See {@link Javason#isNumberValid(Number)}.
	 */
	public JsonArray put(int index, float value) throws IllegalArgumentException {
		return put(index, value);
	}
	
	/**
	 * Add or replace the {@link Long} at a given index.
	 * If the index is larger or equal to the {@link #size()}, then the object will be added.
	 * If not, the object will be replaced.
	 * 
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @param value The long to add or replace.
	 * @return This JsonArray, easier for chaining.
	 */
	public JsonArray put(int index, long value) {
		return put(index, value);
	}
	
	/**
	 * Add or replace the {@link Boolean} at a given index.
	 * If the index is larger or equal to the {@link #size()}, then the object will be added.
	 * If not, the object will be replaced.
	 * 
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @param value The boolean to add or replace.
	 * @return This JsonArray, easier for chaining.
	 */
	public JsonArray put(int index, boolean value) {
		return put(index, value);
	}
	
	/**
	 * Add or replace the {@link String} at a given index.
	 * If the index is larger or equal to the {@link #size()}, then the object will be added.
	 * If not, the object will be replaced.
	 * 
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @param value The string to add or replace.
	 * @return This JsonArray, easier for chaining.
	 */
	public JsonArray put(int index, String value) {
		return put(index, value);
	}
	
	/**
	 * Add or replace the {@link JsonObject} at a given index.
	 * If the index is larger or equal to the {@link #size()}, then the object will be added.
	 * If not, the object will be replaced.
	 * 
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @param value The json object to add or replace.
	 * @return This JsonArray, easier for chaining.
	 */
	public JsonArray put(int index, JsonObject value) {
		return put(index, value);
	}
	
	/**
	 * Add or replace the array of {@link Object}s at a given index.
	 * If the index is larger or equal to the {@link #size()}, then the object will be added.
	 * If not, the object will be replaced.
	 * 
	 * @param value The json array to add or replace.
	 * @return This JsonArray, easier for chaining.
	 */
	public JsonArray put(int index, Object[] value) {
		return put(index, new JsonArray(value));
	}
	
	/**
	 * Add or replace the {@link JsonArray} at a given index.
	 * If the index is larger or equal to the {@link #size()}, then the object will be added.
	 * If not, the object will be replaced.
	 * 
	 * @param value The json array to add or replace.
	 * @return This JsonArray, easier for chaining.
	 */
	public JsonArray put(int index, JsonArray value) {
		return put(index, value);
	}
	
	/**
	 * Add or replace the {@link JsonObject} at a given index.
	 * If the index is larger or equal to the {@link #size()}, then the object will be added.
	 * If not, the object will be replaced.
	 * 
	 * @param value The collection to add or replace.
	 * @return This JsonArray, easier for chaining.
	 */
	public JsonArray put(int index, Collection<Object> value) {
		return put(index, new JsonArray(value));
	}
	
	/**
	 * Remove a value at a given index.
	 * 
	 * @param index The index, between {@code 0} and {@link #size()} - 1.
	 * @return The object removed.
	 */
	public Object remove(int index) {
		return array.remove(index);
	}
	
	/**
	 * Remove a value.
	 * 
	 * @param object The object to search for.
	 * @return The object removed.
	 */
	public Object remove(Object object) {
		return array.remove(object);
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
	 * @return This JsonArray, easier for chaining.
	 */
	public JsonArray clear() {
		array.clear();
		return this;
	}
	
	/**
	 * Get this json array as an array of objects.
	 * 
	 * @return An array of objects.
	 */
	public Object[] getAsArray() {
		return array.toArray();
	}
	
	/**
	 * Get this json array as a collection of objects.
	 * 
	 * @return An collection of objects.
	 */
	public Collection<Object> getAsCollection() {
		return array;
	}

	@Override
	public void forEach(Consumer<? super Object> arg0) {
		array.forEach(arg0);
	}

	@Override
	public Iterator<Object> iterator() {
		return array.iterator();
	}

	@Override
	public Spliterator<Object> spliterator() {
		return array.spliterator();
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
		return toString(indent, 1);
	}
	
	String toString(int indent, int layer) {
		final boolean indented = indent != 0;
		/* Opening Bracket */
		final StringBuilder builder = new StringBuilder("[");
		
		if (array.isEmpty()) {
			return builder.append("]").toString();
		}
		
		/* Content */
		int indexC = 0; // Used to count index
		for (Object val : array) {
			if (indented) {
				builder.append("\n");
				for (int i = 0; i < indent * layer; i++) {
					builder.append(" ");
				}
			}
			
			if (val instanceof String) {
				builder.append("\"").append(val).append("\""); 
			} else if (val instanceof JsonObject) {
				builder.append(((JsonObject) val).toString(indent, layer+1));
			} else if (val instanceof JsonArray) {
				builder.append(((JsonArray) val).toString(indent, layer+1));
			} else {
				builder.append(val);
			}
			
			if (indexC != array.size() - 1) { // Ignore last element
				builder.append(",");
			}
			indexC++;
		}
		
		/* Close Parenthesis */
		if (indented) builder.append("\n");
		for (int i = 0; i < indent * (layer - 1); i++) { // layer - 1 because closing parenthesis are one tab before the content indent
			builder.append(" ");
		}
		return builder.append("]").toString();
	}
	
}
