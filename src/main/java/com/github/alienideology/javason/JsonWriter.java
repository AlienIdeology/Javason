package com.github.alienideology.javason;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.alienideology.javason.exception.JsonException;

/**
 * A faster writer for constructing json strings.
 * 
 * @author AlienIdeology
 */
public class JsonWriter {
	
	private Appendable json;
	
	private Mode mode;
	private List<ModeWrapper> memory;
	
	/**
	 * Creates an empty json writer, with the writing destination to be a internal {@link StringBuilder}.
	 * Requires to invoke additional {@link JsonWriter#writeTo(Appendable)} to write to an object.
	 */
	public JsonWriter() {
		this(new StringBuilder());
	}
	
	/**
	 * Creates an json writer to append to the appendable destination.
	 * @param appendable The destination to write to.
	 */
	public JsonWriter(Appendable appendable) {
		json = appendable;
		memory = new ArrayList<>();
	}
	
	/**
	 * Open this writer as an object, only used to write {@link JsonObject}.
	 * 
	 * @return This JsonWriter, easier for chaining.
	 * @throws JsonException
	 * 		<ul>
	 * 			<li>If the writer is already opened.</li>
	 * 			<li>If there are {@link IOException} thrown when opening a object.</li>
	 * 		</ul>	
	 */
	public JsonWriter openObject() throws JsonException {
		if (mode != null) {
			throw new JsonException("Opening a JsonObject while the writer has been opened!");
		}
		
		try {
			json.append("{");
		} catch (IOException e) {
			throw new JsonException(e);
		}
		mode = Mode.OBJ;
		return this;
	}
	
	/**
	 * End this writer.
	 * 
	 * @return This JsonWriter, easier for chaining.
	 * @throws JsonException
	 * 		<ul>
	 * 			<li>If the writer is not opened.</li>
	 * 			<li>If the writer is for writing to a {@link JsonArray}.</li>
	 * 			<li>If the writer is already closed.</li>
	 * 			<li>If there are {@link IOException} thrown when ending the object.</li>
	 * 		</ul>
	 */
	public JsonWriter endObject() throws JsonException {
		if (mode != Mode.OBJ && mode != Mode.VALUE) {
			throw new JsonException("Cannot properly end a JsonObject!");
		}
		
		try {
			json.append("}");
		} catch (IOException e) {
			throw new JsonException(e);
		}
		mode = Mode.FINISH;
		return this;
	}
	
	/**
	 * Append a key to the {@link JsonObject}. 
	 * 
	 * @param key The string key.
	 * @return This JsonWriter, easier for chaining.
	 * @throws JsonException
	 * 		<ul>
	 * 			<li>If the writer is not opened.</li>
	 * 			<li>If the writer is for writing to a {@link JsonArray}.</li>
	 * 			<li>If the writer is already closed.</li>
	 * 			<li>If there are {@link IOException} thrown when appending the key.</li>
	 * 		</ul>
	 */
	public JsonWriter key(String key) throws JsonException {
		if (mode != Mode.OBJ && mode != Mode.VALUE) {
			throw new JsonException("Expected to append a key to an object or after a value!");
		}
		
		try {
			if (!memory.isEmpty()) json.append(",");
			json.append("\"" + key + "\":");
			memory.add(new ModeWrapper(Mode.KEY, key));
		} catch (IOException e) {
			throw new JsonException(e);
		}
		mode = Mode.KEY;
		return this;
	}
	
	/**
	 * Return the last key appended.
	 * If no key had appended, then this returns null.
	 * 
	 * @return The last key.
	 */
	// Nullable
	public String lastKey() {
		for (ModeWrapper w : memory) {
			if (w.mode == Mode.KEY) 
				return (String) w.value;
		}
		return null;
	}
	

	/**
	 * Append a value to the {@link JsonObject}. 
	 * 
	 * @param value The value.
	 * @return This JsonWriter, easier for chaining.
	 * @throws JsonException
	 * 		<ul>
	 * 			<li>If the writer is not opened.</li>
	 * 			<li>If the writer is for writing to a {@link JsonArray}.</li>
	 * 			<li>If the writer is already closed.</li>
	 * 			<li>If there are {@link IOException} thrown when appending the value.</li>
	 * 		</ul>
	 */
	public JsonWriter value(Object value) {
		if (mode != Mode.KEY) {
			throw new JsonException("Expected to append a value after a key!");
		}
		
		try {
			if (value instanceof String) {
				value = "\"" + (String) value + "\"";
			}
			json.append(String.valueOf(value));
			memory.add(new ModeWrapper(Mode.VALUE, value));
		} catch (IOException e) {
			throw new JsonException(e);
		}
		mode = Mode.VALUE;
		return this;
	}
	
	/**
	 * Return the last value appended.
	 * If no value had appended, then this returns null.
	 * 
	 * @return The last value.
	 */
	// Nullable
	public Object lastValue() {
		for (ModeWrapper w : memory) {
			if (w.mode == Mode.VALUE)
				return w.value;
		}
		return null;
	}
	
	/**
	 * Open this writer as an object, only used to write {@link JsonArray}.
	 * 
	 * @return This JsonWriter, easier for chaining.
	 * @throws JsonException
	 * 		<ul>
	 * 			<li>If the writer is already opened.</li>
	 * 			<li>If there are {@link IOException} thrown when opening a array.</li>
	 * 		</ul>	
	 */
	public JsonWriter openArray() throws JsonException {
		if (mode != null) {
			throw new JsonException("Opening a JsonArray while the writer has been opened!");
		}
		
		try {
			json.append("[");
		} catch (IOException e) {
			throw new JsonException(e);
		}
		mode = Mode.ARRAY;
		return this;
	}
	
	/**
	 * End this writer.
	 * 
	 * @return This JsonWriter, easier for chaining.
	 * @throws JsonException
	 * 		<ul>
	 * 			<li>If the writer is not opened.</li>
	 * 			<li>If the writer is for writing to a {@link JsonObject}.</li>
	 * 			<li>If the writer is already closed.</li>
	 * 			<li>If there are {@link IOException} thrown when ending the array.</li>
	 * 		</ul>
	 */
	public JsonWriter endArray() throws JsonException {
		if (mode != Mode.ARRAY && mode != Mode.ELEMENT) {
			throw new JsonException("Cannot properly end a JsonArray!");
		}
		
		try {
			json.append("]");
		} catch (IOException e) {
			throw new JsonException(e);
		}
		mode = Mode.FINISH;
		return this;
	}
	
	/**
	 * Append an element to the {@link JsonArray}. 
	 * 
	 * @param element The element.
	 * @return This JsonWriter, easier for chaining.
	 * @throws JsonException
	 * 		<ul>
	 * 			<li>If the writer is not opened.</li>
	 * 			<li>If the writer is for writing to a {@link JsonObject}.</li>
	 * 			<li>If the writer is already closed.</li>
	 * 			<li>If there are {@link IOException} thrown when appending the element.</li>
	 * 		</ul>
	 */
	public JsonWriter element(Object element) throws JsonException {
		if (mode != Mode.ARRAY && mode != Mode.ELEMENT) {
			throw new JsonException("Expected to append an element to an array or after another element!");
		}
		
		try {
			if (!memory.isEmpty()) json.append(",");
			json.append(String.valueOf(element));
			memory.add(new ModeWrapper(Mode.ELEMENT, element));
		} catch (IOException e) {
			throw new JsonException(e);
		}
		mode = Mode.ELEMENT;
		return this;
	}
	
	/**
	 * Return the last element appended.
	 * If no element had appended, then this returns null.
	 * 
	 * @return The last element.
	 */
	// Nullable
	public Object lastElement() {
		for (ModeWrapper w : memory) {
			if (w.mode == Mode.ELEMENT)
				return w.value;
		}
		return null;
	}
	
	/**
	 * Write to a custom destination.
	 * @see JsonWriter(Appendable)
	 * 
	 * @param appendable The destination.
	 * @throws IOException For any exceptions when appending the value.
	 */
	public void writeTo(Appendable appendable) throws IOException {
		appendable.append(json.toString());
	}
	
	/**
	 * Convert this JsonWriter into a {@link JsonObject}.
	 * 
	 * @return The json object converted.
	 * @throws JsonException
	 * 		<ul>
	 * 			<li>If the writer is not opened.</li>
	 * 			<li>If the writer is for writing to a {@link JsonArray}.</li>
	 * 			<li>If there are {@link IOException} thrown when converting to a json object.</li>
	 * 		</ul>
	 */
	public JsonObject toJsonObject() throws JsonException {
		if (mode == Mode.OBJ) {
			throw new JsonException("The writer is not opened!");
		}
		
		if (mode == Mode.ARRAY || mode == Mode.ELEMENT || mode == Mode.KEY) {
			throw new JsonException("Cannot convert a writer to a JsonObject due to unmatched type or value!");
		}
		
		if (mode != Mode.FINISH) {
			try {
				json.append("}");
			} catch (IOException e) {
				throw new JsonException(e);
			}
		}
		
		return new JsonObject(json.toString());
	}
	
	/**
	 * Convert this JsonWriter into a {@link JsonArray}.
	 * 
	 * @return The json object converted.
	 * @throws JsonException
	 * 		<ul>
	 * 			<li>If the writer is not opened.</li>
	 * 			<li>If the writer is for writing to a {@link JsonObject}.</li>
	 * 			<li>If there are {@link IOException} thrown when converting to a json array.</li>
	 * 		</ul>
	 */
	public JsonArray toJsonArray() throws JsonException {
		if (mode == Mode.ARRAY) {
			throw new JsonException("The writer is not opened!");
		}
		
		
		if (mode == Mode.OBJ || mode == Mode.KEY || mode == Mode.VALUE) {
			throw new JsonException("Cannot convert a writer to a JsonArray due to unmatched type or value!");
		}
		
		if (mode != Mode.FINISH) {
			try {
				json.append("]");
			} catch (IOException e) {
				throw new JsonException(e);
			}
		}
		
		return new JsonArray(json.toString());
	}
	
	/**
	 * @return The json string constructed, without indentation.
	 */
	@Override
	public String toString() {
		return json.toString();
	}

	/**
	 * Writer modes
	 * 
	 * @author AlienIdeology
	 */
	public enum Mode {
		OBJ, // Start
		ARRAY, // Start
		KEY, 
		VALUE,
		ELEMENT,
		FINISH
	}
	
	private class ModeWrapper {
		public Mode mode;
		public Object value;
		
		public ModeWrapper(Mode mode, Object value) {
			super();
			this.mode = mode;
			this.value = value;
		}
	}

}
