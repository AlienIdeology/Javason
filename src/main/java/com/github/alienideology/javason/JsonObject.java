package com.github.alienideology.javason;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.github.alienideology.javason.exception.InvalidTypeException;
import com.github.alienideology.javason.exception.JsonException;

/**
 * An object for the json file format.
 * The object is constructed base on map(key and value pairs).
 * 
 * @author AlienIdeology
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class JsonObject {
	
	private final HashMap<String, Object> pairs;
	
	/**
	 * Creates an empty JsonObject.	
	 */
	public JsonObject() {
		pairs = new HashMap<>();
	}
	
	/**
	 * Create a JsonObject from a json source.
	 * 
	 * @param json The json source.
	 */
	public JsonObject(String json) {
		pairs = new JsonParser(json).parseObject().getAsMap();
	}
	
	/**
	 * Create a JsonObject from a map.
	 * 
	 * @param pairs The map, with keys(String) and values(Object).
	 */
	public JsonObject(Map<String, Object> pairs) {
		this.pairs = new HashMap<>(pairs);
	}
	
	/**
	 * Serialize a {@link JsonObject} to a new java object.
	 * 
	 * @param clazz The new java object's class.
	 * @return A new java object, with fields set by serialization.
	 * @throws IllegalAccessException
     * 		If the class's constructor is not accessible.
     * @throws InstantiationException
     * 		If an error occurred while constructing a new instance of the class.
	 */
	public <T> T toNewObject(Class<T> clazz) throws InstantiationException, IllegalAccessException {
		return new JsonSerializer<T>().toNewObject(clazz, this);
	}
	
	/**
	 * Serialize a {@link JsonObject} to a given java object.
	 * 
	 * @param t The java object.
	 * @return The same java object, with fields set by serialization.
	 */
	public <T> T toObject(T t) {
		return new JsonSerializer<T>().toObject(t, this);
	}
	
	/**
	 * Convert this json object to an {@link JsonArray}.
	 * 
	 * @param convertValues If true, then the json array will have all values provided by this json object, 
	 * 			and the keys will be discarded. If false, then this object will be added to a new empty array.
	 * @return The converted json array.
	 */
	public JsonArray toJsonArray(boolean convertValues) {
		if (convertValues) {
			return new JsonArray().add(this);
		} else {
			JsonArray array = new JsonArray();
			for (Object obj : pairs.values()) {
				array.add(obj);
			}
			return array;
		}
	}
	
	/**
	 * Get an object from a given key.
	 * 
	 * @param key The string key.
	 * @return The object, or null if there is no such key existed in the json.
	 */
	// Nullable
	public Object get(String key) {
		return pairs.get(key);
	}
	
	/**
	 * Get the integer value from a given key.
	 * Possible conversions:
	 * <ul>
	 * 		<li>If the value is a {@link Long}, then it will be converted to integer using {@link Math#toIntExact(long)}.</li>
	 * 		<li>If the value is a {@link Number}, then it will be converted using {@link Number#intValue()}.</li>
	 * 		<li>If the value is a {@link String}, then it will be parsed using {@link Integer#parseInt(String)}.</li>
	 * </ul>
	 * 
	 * @param key The string key.
	 * @return The integer converted.
	 * @throws JsonException
	 * 		If there is no such key existed in the json.
	 * @throws InvalidTypeException 
	 * 		If the value is not listed above, or if the {@link String} cannot be converted to an {@link Integer}. See {@link NumberFormatException}.
	 */
	public int getInt(String key) throws JsonException, InvalidTypeException {
		Object val = pairs.get(key);
		// TODO: Contents below should be in Javason with static access
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
			throw new JsonException("There is no integer value for the key \"" + key + "\".");
		} else {
			throw new InvalidTypeException(Integer.class, val.getClass());
		}
	}
	
	/**
	 * Get the integer value from a given key by invoking {@link #getInt(String)}.
	 * This returns null if a {@link JsonException} is thrown.
	 * 
	 * @param key The string key.
	 * @return The integer converted.
	 * @throws InvalidTypeException 
	 * 		See {@link #getInt(String)}.
	 */
	// Nullable
	public Integer getIntOpt(String key) throws InvalidTypeException {
		try {
			return getInt(key);
		} catch (JsonException ignored) {} 
		return null;
	}
	
	/**
	 * Get the double value from a given key.
	 * Possible conversions:
	 * <ul>
	 * 		<li>If the value is a {@link Number}, then it will be converted using {@link Number#doubleValue()}.</li>
	 * 		<li>If the value is a {@link String}, then it will be parsed using {@link Double#parseDouble(String)}.</li>
	 * </ul>
	 * 
	 * @param key The string key.
	 * @return The double converted.
	 * @throws JsonException 
	 * 		If there is no such key existed in the json.
	 * @throws InvalidTypeException 
	 * 		If the value is not listed above, or if the {@link String} cannot be converted to an {@link Double}. See {@link NumberFormatException}.
	 */
	public double getDouble(String key) throws JsonException, InvalidTypeException {
		Object val = pairs.get(key);
		if (val instanceof Number) {
			return ((Number) val).doubleValue();
		} else if (val instanceof String) {
			try {
				return Double.parseDouble((String) val);
			} catch (NumberFormatException nfe) {
				throw new InvalidTypeException(Double.class, String.class);
			}
		} else if (val == null) {
			throw new JsonException("There is no double value for the key \"" + key + "\".");		
		} else {
			throw new InvalidTypeException(Double.class, val.getClass());
		}
	}
	
	/**
	 * Get the double value from a given key by invoking {@link #getDouble(String)}.
	 * This returns null if a {@link JsonException} is thrown.
	 * 
	 * @param key The string key.
	 * @return The double converted.
	 * @throws InvalidTypeException 
	 * 		See {@link #getDouble(String)}.
	 */
	// Nullable
	public Double getDoubleOpt(String key) throws InvalidTypeException {
		try {
			return getDouble(key);
		} catch (JsonException ignored) {} 
		return null;
	}
	
	/**
	 * Get the float value from a given key.
	 * Possible conversions:
	 * <ul>
	 * 		<li>If the value is a {@link Number}, then it will be converted using {@link Number#floatValue()}.</li>
	 * 		<li>If the value is a {@link String}, then it will be parsed using {@link Float#parseFloat(String)}.</li>
	 * </ul>
	 * 
	 * @param key The string key.
	 * @return The float converted.
	 * @throws JsonException 
	 * 		If there is no such key existed in the json.
	 * @throws InvalidTypeException 
	 * 		If the value is not listed above, or if the {@link String} cannot be converted to an {@link Float}. See {@link NumberFormatException}.
	 */
	public float getFloat(String key) throws JsonException, InvalidTypeException {
		Object val = pairs.get(key);
		if (val instanceof Number) {
			return ((Number) val).floatValue();
		} else if (val instanceof String) {
			try {
				return Float.parseFloat((String) val);
			} catch (NumberFormatException nfe) {
				throw new InvalidTypeException(Float.class, String.class);
			}
		} else if (val == null) {
			throw new JsonException("There is no float value for the key \"" + key + "\".");		
		} else {
			throw new InvalidTypeException(Float.class, val.getClass());
		}
	}
	
	/**
	 * Get the float value from a given key by invoking {@link #getFloat(String)}.
	 * This returns null if a {@link JsonException} is thrown.
	 * 
	 * @param key The string key.
	 * @return The float converted.
	 * @throws InvalidTypeException 
	 * 		See {@link #getFloat(String)}.
	 */
	// Nullable
	public Float getFloatOpt(String key) throws InvalidTypeException {
		try {
			return getFloat(key);
		} catch (JsonException ignored) {} 
		return null;
	}
	
	/**
	 * Get the long value from a given key.
	 * Possible conversions:
	 * <ul>
	 * 		<li>If the value is a {@link Number}, then it will be converted using {@link Number#longValue()}.</li>
	 * 		<li>If the value is a {@link String}, then it will be parsed using {@link Long#parseLong(String)}.</li>
	 * </ul>
	 * 
	 * @param key The string key.
	 * @return The long converted.
	 * @throws JsonException 
	 * 		If there is no such key existed in the json.
	 * @throws InvalidTypeException 
	 * 		If the value is not listed above, or if the {@link String} cannot be converted to an {@link Long}. See {@link NumberFormatException}.
	 */
	public long getLong(String key) throws JsonException, InvalidTypeException {
		Object val = pairs.get(key);
		if (val instanceof Number) {
			return ((Number) val).longValue();
		} else if (val instanceof String) {
			try {
				return Long.parseLong((String) val);
			} catch (NumberFormatException nfe) {
				throw new InvalidTypeException(Long.class, String.class);
			}
		} else if (val == null) {
			throw new JsonException("There is no long value for the key \"" + key + "\".");		
		} else {
			throw new InvalidTypeException(Long.class, val.getClass());
		}
	}
	
	/**
	 * Get the long value from a given key by invoking {@link #getLong(String)}.
	 * This returns null if a {@link JsonException} is thrown.
	 * 
	 * @param key The string key.
	 * @return The long converted.
	 * @throws InvalidTypeException 
	 * 		See {@link #getLong(String)}.
	 */
	// Nullable
	public Long getLongOpt(String key) throws InvalidTypeException {
		try {
			return getLong(key);
		} catch (JsonException ignored) {} 
		return null;
	}
	
	/**
	 * Get a number from a given key.
	 * Possible conversions:
	 * <ul>
	 * 		<li>If the value is a {@link Number}, return.</li>
	 * 		<li>If the value is a {@link String}, then it will be parsed using {@link Javason#getNumberFromString(String)}.</li>
	 * </ul>
	 * 
	 * @param key The string key.
	 * @return The number converted, or null if no number is found.
	 * @throws NumberFormatException
	 * 		If the {@link String} cannot be converted to a {@link Number}.
	 * @throws InvalidTypeException
	 * 		If the value is not a listed above.
	 */
	// Nullable
	public Number getNum(String key) throws NumberFormatException,  InvalidTypeException {
		Object val = pairs.get(key);
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
	 * Get the big integer value from a given key.
	 * Possible conversions:
	 * <ul>
	 * 		<li>If the value is a {@link BigInteger}, return.</li>
	 * 		<li>If the value if a {@link BigDecimal}, then it will be converted using {@link BigDecimal#toBigIntegerExact()}.</li>
	 * 		<li>If the value is a {@link Number}, then it will be converted using the {@link BigInteger(String)} constructor and {@link Number#toString()}.</li>
	 * 		<li>If the value is a {@link String}, then it will be parsed using the {@link BigInteger(String)} constructor.</li>
	 * </ul>
	 * 
	 * @param key The string key.
	 * @return The big integer converted, or {@code null} if no such key existed.
	 * @throws InvalidTypeException 
	 * 		If the value is not listed above, or if the {@link String} cannot be converted to an {@link BigInteger}. See {@link NumberFormatException}.
	 */
	// Nullable
	public BigInteger getBigInteger(String key) throws InvalidTypeException {
		Object val = pairs.get(key);
		if (val instanceof BigInteger) {
			return (BigInteger) val;
		} else if (val instanceof BigDecimal) {
			return ((BigDecimal) val).toBigIntegerExact();
		} else if (val instanceof Number) {
			return new BigInteger(val.toString());
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
	 * Get the big decimal value from a given key.
	 * Possible conversions:
	 * <ul>
	 * 		<li>If the value is a {@link BigDecimal}, return.</li>
	 * 		<li>If the value if a {@link BigInteger}, then it will be converted using {@link BigDecimal#valueOf(double)} and {@link BigInteger#doubleValue()}.</li>
	 * 		<li>If the value is a {@link Number}, then it will be converted using the {@link BigDecimal(String)} constructor and {@link Number#toString()}.</li>
	 * 		<li>If the value is a {@link String}, then it will be parsed using the {@link BigDecimal(String)} constructor.</li>
	 * </ul>
	 * 
	 * @param key The string key.
	 * @return The big decimal converted, or {@code null} if no such key existed.
	 * @throws InvalidTypeException 
	 * 		If the value is not listed above, or if the {@link String} cannot be converted to an {@link BigDecimal}. See {@link NumberFormatException}.
	 */
	// Nullable
	public BigDecimal getBigDecimal(String key) throws InvalidTypeException {
		Object val = pairs.get(key);
		if (val instanceof BigDecimal) {
			return (BigDecimal) val;
		} else if (val instanceof BigInteger) {
			return BigDecimal.valueOf(((BigInteger) val).doubleValue());
		} else if (val instanceof Number) {
			return new BigDecimal(val.toString());
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
	 * Get the boolean value from a given key.
	 * Possible conversions:
	 * <ul>
	 * 		<li>If the value is a {@link Boolean}, return.</li>
	 * 		<li>If the value is a {@link String}, then it will be parsed using {@link Boolean#parseBoolean(String)}.</li>
	 * </ul>
	 * 
	 * @param key The string key.
	 * @return The boolean converted.
	 * @throws JsonException 
	 * 		If there is no such key existed in the json.
	 * @throws InvalidTypeException 
	 * 		If the value is not listed above.
	 */
	public boolean getBoolean(String key) throws JsonException, InvalidTypeException {
		Object val = pairs.get(key);
		if (val instanceof Boolean) {
			return (boolean) val;
		} else if (val instanceof String) {
			try {
				return Boolean.parseBoolean((String) val);
			} catch (NumberFormatException nfe) {
				throw new InvalidTypeException(Boolean.class, String.class);
			}
		} else if (val == null) {
			throw new JsonException("There is no boolean value for the key \"" + key + "\".");		
		} else {
			throw new InvalidTypeException(Boolean.class, val.getClass());
		}
	}
	
	/**
	 * Get the boolean value from a given key by invoking {@link #getBoolean(String)}.
	 * This returns null if a {@link JsonException} is thrown.
	 * 
	 * @param key The string key.
	 * @return The boolean converted.
	 * @throws InvalidTypeException 
	 * 		See {@link #getBoolean(String)}.
	 */
	public Boolean getBooleanOpt(String key) throws InvalidTypeException {
		try {
			return getBoolean(key);
		} catch (JsonException ignored) {} 
		return null;
	}
	
	/**
	 * Get the string value from a given key.
	 * This uses {@link String#valueOf(Object)} to convert an object.
	 * 
	 * @param key The string key.
	 * @return The string value of any object.
	 * @throws JsonException 
	 * 		If there is no such key existed in the json.
	 */
	public String getString(String key) throws JsonException {
		Object val = pairs.get(key);
		if (val == null) {
			throw new JsonException("There is no string value for the key \"" + key + "\".");
		}
		return String.valueOf(val);
	}
	
	/**
	 * Get the JsonObject from a given key.
	 * 
	 * @param key The string key.
	 * @return The json object, or {@code null} if no such key exist.
	 * @throws InvalidTypeException 
	 * 		If the value is not a {@link JsonObject}.
	 */
	// Nullable
	public JsonObject getObject(String key) throws InvalidTypeException {
		Object val = pairs.get(key);
		
		if (val instanceof JsonObject) {
			return (JsonObject) val;
		} else if (val == null) {
			return null;
		} else {
			throw new InvalidTypeException(JsonObject.class, val.getClass());
		}
	}
	
	/**
	 * Get the JsonArray from a given key.
	 * 
	 * @param key The string key.
	 * @return The json array, or {@code null} if no such key exist.
	 * @throws InvalidTypeException 
	 * 		If the value is not a {@link JsonArray}.
	 */
	// Nullable
	public JsonArray getArray(String key) throws InvalidTypeException {
		Object val = pairs.get(key);
		
		if (val instanceof JsonArray) {
			return (JsonArray) val;
		} else if (val == null) {
			return null;
		} else {
			throw new InvalidTypeException(JsonArray.class, val.getClass());
		}
	}
 	
	/**
	 * Check if this json object contains a given key.
	 * 
	 * @param key The string key.
	 * @return True if the json object contains the key.
	 */
	public boolean contains(String key) {
		return pairs.containsKey(key);
	}
	
	/**
	 * Check if the json object contains the given key, and if the value matched to the key is not {@code null}.
	 * 
	 * @param key The string key.
	 * @return True if the json object contains the key, and the value is not {@code null}.
	 */
	public boolean notNull(String key) {
		return contains(key) && pairs.get(key) != null;
	}
	
	/**
	 * Put or replace a value with the given key.
	 * 
	 * @param key The string key.
	 * @param value The matching value.
	 * @return This JsonObject, easier for chaining.
	 * @throws IllegalArgumentException
	 * 		If the object is a {@link Double} or {@link Float}, and the number is not valid. See {@link Javason#isNumberValid(Number)}.
	 */
	public JsonObject put(String key, Object value) throws IllegalArgumentException {
		if (value instanceof Number) {
			Javason.validateNumber((Number) value);
		}
		
		pairs.put(key, value);
		return this;
	}
	
	/**
	 * Put or replace an {@link Integer} with the given key.
	 * 
	 * @param key The string key.
	 * @param value The matching integer.
	 * @return This JsonObject, easier for chaining.
	 */
	public JsonObject put(String key, int value) {
		return put(key, new Integer(value));
	}
	
	/**
	 * Put or replace a {@link Double} with the given key.
	 * 
	 * @param key The string key.
	 * @param value The matching double.
	 * @return This JsonObject, easier for chaining.
	 * @throws IllegalArgumentException
	 * 		If the {@link Double} is not valid. See {@link Javason#isNumberValid(Number)}.
	 */
	public JsonObject put(String key, double value) throws IllegalArgumentException {
		return put(key, new Double(value));
	}
	
	/**
	 * Put or replace a {@link Float} with the given key.
	 * 
	 * @param key The string key.
	 * @param value The matching float.
	 * @return This JsonObject, easier for chaining.
	 * @throws IllegalArgumentException
	 * 		If the {@link Float} is not valid. See {@link Javason#isNumberValid(Number)}.
	 */
	public JsonObject put(String key, float value) throws IllegalArgumentException {
		return put(key, new Float(value));
	}
	
	/**
	 * Put or replace a {@link Long} with the given key.
	 * 
	 * @param key The string key.
	 * @param value The matching long.
	 * @return This JsonObject, easier for chaining.
	 */
	public JsonObject put(String key, long value) {
		return put(key, new Long(value));
	}
	
	/**
	 * Put or replace a {@link Boolean} with the given key.
	 * 
	 * @param key The string key.
	 * @param value The matching boolean.
	 * @return This JsonObject, easier for chaining.
	 */
	public JsonObject put(String key, boolean value) {
		return put(key, Boolean.valueOf(value));
	}
	
	/**
	 * Put or replace a {@link String} with the given key.
	 * 
	 * @param key The string key.
	 * @param value The matching string.
	 * @return This JsonObject, easier for chaining.
	 */
	public JsonObject put(String key, String value) {
		return put(key, (Object) value);
	}
	
	/**
	 * Put or replace a {@link JsonObject} with the given key.
	 * 
	 * @param key The string key.
	 * @param value The matching json object.
	 * @return This JsonObject, easier for chaining.
	 */
	public JsonObject put(String key, JsonObject value) {
		return put(key, (Object) value);
	}
	
	/**
	 * Put or replace an array of {@link Object}s with the given key.
	 * 
	 * @param key The string key.
	 * @param value The matching array of objects.
	 * @return This JsonObject, easier for chaining.
	 */
	public JsonObject put(String key, Object[] value) {
		return put(key, new JsonArray(value));
	}
	
	/**
	 * Put or replace a {@link JsonArray} with the given key.
	 * 
	 * @param key The string key.
	 * @param value The matching json array.
	 * @return This JsonObject, easier for chaining.
	 */
	public JsonObject put(String key, JsonArray value) {
		return put(key, (Object) value);
	}
	
	/**
	 * Put or replace a {@link Collection} of {@link Object}s with the given key.
	 * 
	 * @param key The string key.
	 * @param value The matching collection of objects.
	 * @return This JsonObject, easier for chaining.
	 */
	public JsonObject put(String key, Collection<Object> value) {
		return put(key, new JsonArray(value));
	}
	
	/**
	 * Remove a value by the given key.
	 * 
	 * @param key The string key.
	 * @return The value removed.
	 */
	public Object remove(String key) {
		return pairs.remove(key);
	}
	
	/**
	 * Remove a entry by the given value.
	 * 
	 * @param value The object value.
	 * @return The entry removed, or {@code null} is no entry is found by the value.
	 */
	// Nullable
	public Entry<String, Object> removeByValue(Object value) {
        Iterator<Entry<String, Object>> iterator = pairs.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> key = iterator.next();
            if (key.getValue().equals(value)) {
                pairs.remove(key.getKey());
                return key;
            }
        }
        return null;
	}
	
	/**
	 * Get the pairs' size of this json object.
	 * 
	 * @return The size.
	 */
	public int size() {
		return pairs.size();
	}
	
	/**
	 * Check if the json object is an empty object.
	 * 
	 * @return True if the json object is empty.
	 */
	public boolean isEmpty() {
		return pairs.isEmpty();
	}
	
	/**
	 * Clear the json object.
	 * 
	 * @return This JsonObject, easier for chaining.
	 */
	public JsonObject clear() {
		pairs.clear();
		return this;
	}
	
	/**
	 * Get the map of pairs in this json object. 
	 * 
	 * @return The map.
	 */
	public HashMap<String, Object> getAsMap() {
		return pairs;
	}

	/**
	 * Construct a json string from this json object with no indentation.
	 * 
	 * @return The json string.
	 */
	@Override
	public String toString() {
		return toString(0);
	}
	
	/**
	 * Construct a json string from this json object with the given indentation.
	 * 
	 * @param indent The amount of white spaces to indent.
	 * @return The json string.
	 */
	public String toString(int indent) {
		return toString(indent, 1);
	}
	
	// layer: starting from 1
	String toString(int indent, int layer) {
		final boolean indented = indent != 0;
		/* Opening Parenthesis */
		final StringBuilder builder = new StringBuilder("{");
		
		if (pairs.isEmpty()) {
			return builder.append("}").toString();
		}
		
		/* Content */
		int indexC = 0; // Used to count index
		for (String key : pairs.keySet()) {
			if (indented) {
				builder.append("\n");
				for (int i = 0; i < indent * layer; i++) {
					builder.append(" ");
				}
			}
			
			builder.append("\"").append(key).append("\"");
			
			if (indented) {
				builder.append(" : ");
			} else {
				builder.append(":");
			}
			
			final Object val = pairs.get(key);
			if (val instanceof String) {
				builder.append("\"").append(val).append("\""); 
			} else if (val instanceof JsonObject) {
				builder.append(((JsonObject) val).toString(indent, layer+1));
			} else if (val instanceof JsonArray) {
				builder.append(((JsonArray) val).toString(indent, layer+1));
			} else {
				builder.append(val);
			}
			
			if (indexC != pairs.size() - 1) { // Ignore last element
				builder.append(",");
			}
			indexC++;
		}
		
		/* Close Parenthesis */
		if (indented) builder.append("\n");
		for (int i = 0; i < indent * (layer - 1); i++) { // layer - 1 because closing parenthesis are one tab before the content indent
			builder.append(" ");
		}
		return builder.append("}").toString();
	}
	
}
