package com.github.alienideology.javason;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collection;

import com.github.alienideology.javason.reflect.Exclude;
import com.github.alienideology.javason.reflect.Serialization;

/**
 * A serializer for serializing and deserializing json objects to java objects.
 * 
 * @author AlienIdeology
 *
 * @param <T> The generic type of object to serialize to, or deserialize from.
 */
public class JsonSerializer<T> {
	
	private SerializeHandler<T> handler;
	private SerializeNamingPolicy namingPolicy;
	
	public JsonSerializer() {
		this.namingPolicy = SerializeNamingPolicy.DEFAULT_NAMING_POLICY;
	}
	
	/**
	 * Set the handler for serializing and deserializing.
	 * 
	 * @param handler The handler.
	 * @return This JsonSerializer, easier for chaining.
	 */
	public JsonSerializer<T> setHandler(SerializeHandler<T> handler) {
		this.handler = handler;
		return this;
	}
	
	/**
	 * Set the naming policy for changing names when serializing and deserializing.
	 * The default naming policy is {@link SerializeNamingPolicy#DEFAULT_NAMING_POLICY}.
	 * 
	 * @param policy The policy.
	 * @return This JsonSerializer, easier for chaining.
	 */
	public JsonSerializer<T> setNamingPolicy(SerializeNamingPolicy policy) {
		this.namingPolicy = policy;
		return this;
	}
	
	/**
	 * Deserialize a java object to a new {@link JsonObject}.
	 * This will automatically excludes fields with the {@link Exclude} annotation or {@code final} modifier. 
	 * If the field have a {@link Serialization} annotation, then the key of the entry will be {@link Serialization#key()} instead of {@link Field#getName()}.
	 * 
	 * This method invokes {@link SerializeHandler#onDeserialize(Object)} before deserializing the object (If the handler is set).
	 * 
	 * @param obj The object to deserialize.
	 * @return The new json object.
	 */
	public JsonObject fromObject(T obj) {
		if (handler != null) handler.onDeserialize(obj);
		JsonObject json = new JsonObject();
		
		for (Field field : obj.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(Exclude.class)){
				if (field.getAnnotation(Exclude.class).excludeOnDeserialize())
					continue;
			}
			
			field.setAccessible(true);
			String key = namingPolicy.fromFieldToKey(field.getName());
			if (field.isAnnotationPresent(Serialization.class)) {
				Serialization sl = field.getAnnotation(Serialization.class);
				key = sl.key().isEmpty() ? key : sl.key();
			}
			
			try {
				json.put(key, field.get(obj));
			} catch (IllegalAccessException ignored) {} // Field was set to accessible
		}
		
		return json;
	}
	
	/**
	 * Serialize a {@link JsonObject} to a new java object.
	 * This will automatically choose the default constructor. 
	 * If no default constructor is present, then returns null.
	 * This method invokes {@link SerializeHandler#onSerialize(JsonObject)} before serializing the object (If the handler is set).
	 * 
	 * @param clazz The new java object's class.
	 * @param object The json object.
	 * @return A new java object, with fields set by serialization.
	 * @throws IllegalAccessException
	 * 		If the class's constructor is not accessible.
	 * @throws InstantiationException 
	 * 		If an error occurred while constructing a new instance of the class.
	 */
	@SuppressWarnings("unchecked")
	public T toNewObject(Class<T> clazz, JsonObject object) throws InstantiationException, IllegalAccessException  {
		if (handler != null) handler.onSerialize(object);
		if (clazz.getConstructors().length != 0) { // Has constructor
			for (Constructor<?> con : clazz.getConstructors()) {
				if (con.getParameterCount() == 0) { // Default Constructor
					try {
						return toObject((T) con.newInstance(new Object[]{}), object);
					} catch (IllegalArgumentException | IllegalAccessException | InstantiationException
							| InvocationTargetException e) {
						throw new InstantiationException("An error occurred while constructing a new instance!");
					}
				}
			}
			return null;
		} else {
			return toObject(clazz.newInstance(), object);
		}
	}
	
	/**
	 * Serialize a {@link JsonObject} to a given java object.
	 * 
	 * This method invokes {@link SerializeHandler#onSerialize(JsonObject)} before serializing the object (If the handler is set).
	 * 
	 * @param t The java object.
	 * @param object The json object.
	 * @return The same java object, with fields set by serialization.
	 */
	public T toObject(T t, JsonObject object) {
		if (handler != null) handler.onSerialize(object);
		for (Field field : t.getClass().getDeclaredFields()) {
			/* Exclude */
			if (field.isAnnotationPresent(Exclude.class)) {
				if (field.getAnnotation(Exclude.class).excludeOnSerialize())
					continue;
			}
			if (Modifier.isFinal(field.getModifiers())) continue; // Final Variables
			
			/* Initialize */
			field.setAccessible(true);
			Object value = object.get(namingPolicy.fromFieldToKey(field.getName()));
			if (field.isAnnotationPresent(Serialization.class)) {
				Serialization sl = field.getAnnotation(Serialization.class);
				if (object.contains(sl.key())) {
					value = object.get(sl.key());
				}
			}
			
			// Json does contains the value
			if (value != null) {
				// Array
				if (field.getDeclaringClass().isArray()) {
					// TODO: Json Array Serialization
				}
				// Collection
				if (Collection.class.isAssignableFrom(field.getDeclaringClass())) {
					// TODO: Json Array Serialization
				}
				
				try {
					field.set(t, value);
				} catch (IllegalAccessException ignored) {} // Final fields was excluded, and field was set to accessible
			}
		}
		
		return t;
	}

}
