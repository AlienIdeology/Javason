package com.github.alienideology.javason;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.github.alienideology.javason.exception.JsonException;

/**
 * A Parser for parsing json strings into {@link JsonObject} and/or {@link JsonArray}.
 * 
 * @author AlienIdeology
 */
public class JsonParser {
	private final String json;
	
	private String seq;
	private char state;
	private int cursor;
	
	/**
	 * Construct a parser by the given json string.
	 * 
	 * @param json The json string.
	 */
	public JsonParser(String json) {
		this.seq = "";
		this.json = json == null || json.isEmpty() ? "{}" : json;
		this.state = json == null || json.isEmpty() ? '{' // Workaround for an empty json object
				: json.charAt(0);
		this.cursor = 0;
	}
	
	/**
	 * Construct a parser by a given reader.
	 * 
	 * @param reader The reader.
	 * @throws IOException
	 * 		If an I/O error occurs.
	 */
	public JsonParser(Reader reader) throws IOException {
		char[] arr = new char[8*1024]; // 8K at a time
  		final StringBuilder buf = new StringBuilder();
  		int numChars;

  		while ((numChars = reader.read(arr, 0, arr.length)) > 0) {
	  		buf.append(arr, 0, numChars);
  		}
  		String json = buf.toString();

  		this.seq = "";
		this.json = json.isEmpty() ? "{}" : json;
		this.state = json.isEmpty() ? '{' // Workaround for an empty json object
				: json.charAt(0);
		this.cursor = 0;
	}
	
	/**
	 * Construct a parser by the given input stream.
	 * 
	 * @param stream The stream.
	 * @throws IOException
	 * 		If an I/O error occurs.
	 */
	public JsonParser(InputStream stream) throws IOException {
		this(new InputStreamReader(stream));
	}
	
	/**
	 * Construct a parser by the given file.
	 * 
	 * @param file The file.
	 * @throws IOException
	 * 		If an I/O error occurs.
	 */
	public JsonParser(File file) throws IOException {
		this(new FileReader(file));
	}
	
	/**
	 * Construct a parser by the given url.
	 * 
	 * @param url The url.
	 * @throws IOException
	 * 		If an I/O error occurs.
	 */
	public JsonParser(URL url) throws IOException {
		this(url.openStream());
	}
	
	/**
	 * Parse an object from the json string.
	 * 
	 * @return The parsed object.
	 * @throws JsonException
	 * 		For any syntax errors.
	 */
	public JsonObject parseObject() throws JsonException {
		if (state != '{') {
			throw new JsonException("A Json object must to starts with \"{\"!");
		}
		
		read(1);
		
		final HashMap<String, Object> pairs = new HashMap<>();
		while (state != '}') {
			final String key = parseKey();
			System.out.println("Key: " + key);
			
			// Json Object, read until ":"
			readUntil(':');
			
			do { // Read one more char, then check white spaces
				reset(); // Reset value since we don't need the spaces
				read(1);
			} while (state == ' ' || state == '\t'); // Ignore white spaces between ":" and the value
			
			final Object value = parseValue(true);
			System.out.println("Value: " + value);
			pairs.put(key, value);
			
			
		}
		
		return new JsonObject(pairs);
	}
	
	/**
	 * Parse an array from the json string.
	 * 
	 * @return The parsed array.
	 * @throws JsonException
	 * 		For any syntax errors.
	 */
	public JsonArray parseArray() {
		if (state != '[') {
			throw new JsonException("A Json array must to starts with \"[\"!");
		}
		
		reset(); // Reset "["
		read(1);
		
		final List<Object> array = new ArrayList<>();
		while (state != ']') {
			while (state == ' ' || state == '\t' || state == ',') { // Ignore white spaces between "," or "[" and the value
				reset(); // Reset value since we don't need the spaces
				read(1);
			}
			
			final Object value = parseValue(false);
			System.out.println("Value: " + value);
			array.add(value);
		}
		return new JsonArray(array);
	}
	
	private String parseKey() {
		if (state != '\"')
			readUntil('\"');
		reset();
		readUntil('\"');
		String key = seq.substring(0, seq.length() - 1);
		reset();
		return key;
	}
	
	// Parse general value for json objects and arrays
	// At the end, the state should either be "}", "]", or ","
	private Object parseValue(boolean isObj) {
		Object result;
		switch (state) {
			case '{': {
				result = parseObject(); 
				break;
			}
			case '[': {
				result = parseArray(); 
				break;
			}
				
			case '\"': {
				result = parseString();
				break;
			}
				
			case '0': 
			case '1': 
			case '2': 
			case '3': 
			case '4': 
			case '5': 
			case '6': 
			case '7': 
			case '8':
			case '9':
			case '.': {
				result = parseNum();
				break;
			}
				
			case 't': {
				read(3); // t + rue
				if (seq.equalsIgnoreCase("true")) {
					result = Boolean.TRUE;
					break;
				} else {
					read(-3);
				}
			} 
			case 'f': {
				read(4);
				if (seq.equalsIgnoreCase("false")) {
					result = Boolean.FALSE;
					break;
				} else {
					read(-3);
				}
			}
			
			case 'n': {
				read(3);
				if (seq.equalsIgnoreCase("null")) {
					result = null;
					break;
				} else {
					read(-4);
				}
			} 
			case ' ': {
				read(1);
				result = parseValue(isObj);
				break;
			}
			case '}':
			case ']': {
				throw new JsonException("Reached end of the json string without a value!");
			}
			default: {
				throw new JsonException("Unknown character \"" + state + "\" at index: " + cursor);
			}
		}
		
		// Make it so that the state will be "}", "]", or "," after parsing value.
		if (isObj) {
			readUntil(',', '}');
		} else {
			readUntil(',', ']');
		}
		
		return result;
	}
	
	private String parseString() {
		reset();
		readUntil('\"');
		return seq.substring(0, seq.length() - 1); // Seq contains the last ", so substring to ignore it
	}
	
	private Number parseNum() {
		do {
			read(1);
		} while (Character.isDigit(state) || state == '.');
		read(-1); // Above loop will read 1 more char than expected
		try {
			return Javason.getNumberFromString(seq);
		} catch (NumberFormatException nfe) {
			throw new JsonException("Encountered an invalid number: " + seq);
		}
	}
	
	private void read(int amount) {
		if (cursor + amount > json.length()) {
			throw new JsonException("Reach end of the json string while parsing process is still going!");
		}
		
		if (amount > 0) {
			seq += json.substring(cursor + 1, // Substring to not include the cursor
					cursor + amount + 1); // Plus one, since substring's upper bound is not included
		} else if (amount < 0) {
			seq = seq.substring(0, seq.length() + amount);
		}
		state = seq.charAt(seq.length()-1);
		cursor += amount;
	}
	
	private void readUntil(char... chars) {
		final int cursor = this.cursor;
		
		checker:
			while (true) {
				if (!hasNext()) {
					throw new JsonException("Expecting char(s) " + Arrays.toString(chars) + " to appear after index: " + cursor + ", but it is not found!");
				}
				read(1);
				
				// Check for multiple alternative chars
				for (char ch : chars) {
					if (state == ch) break checker;
				}
			}
	}
	
	private void reset() {
		seq = "";
	}
	
	private boolean hasNext() {
		return cursor + 1
				< json.length();
	}

}
