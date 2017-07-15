package com.github.alienideology.javason;

/**
 * The naming policy for serializing and deserializing fields.
 * This converts between field and parameter names and json keys.
 * 
 * @author AlienIdeology
 */
public class SerializeNamingPolicy {
	
	public final static SerializeNamingPolicy DEFAULT_NAMING_POLICY = new SerializeNamingPolicy() {
		
		/**
		 * Example: {@code naming_policy} will be turned into {@code namingPolicy}.
		 */
		@Override
		public String fromKey(String jsonKey) {
			StringBuilder builder = new StringBuilder();
			boolean found = false;
			
			for (int i = 0; i < jsonKey.length(); i++) {
				String ch = jsonKey.substring(i, i+1);
				if (i == 0) {
					ch = ch.toLowerCase();
				}
				if (found) {
					builder.append(ch.toUpperCase());
					found = false;
				} else if (ch.equals("_") || ch.equals("-")) {
					found = true;
				} else {
					builder.append(ch);
				}
			}
			return builder.toString();
		}

		/**
		 * Example: {@code namingPolicy} will be turned into {@code naming_policy}.
		 */
		@Override
		public String toKey(String name) {
			StringBuilder builder = new StringBuilder();
			
			for (int i = 0; i < name.length(); i++) {
				char ch = name.charAt(i);
				if (Character.isUpperCase(ch)) {
					builder.append("_").append(Character.toLowerCase(ch));
				} else {
					builder.append(ch);
				}
			}
			return builder.toString();
		}
		
	};
	
	/**
	 * Convert a json key to a field or parameter name.
	 * 
	 * @param jsonKey The json key.
	 * @return The field or parameter name.
	 */
	public String fromKey(String jsonKey) {
		return jsonKey; 
		// Default (Does not change anything), requires override
	}
	
	/**
	 * Convert a json key to a field name.
	 * 
	 * @param jsonKey The json key.
	 * @return The field name.
	 */
	public String fromKeyToField(String jsonKey) {
		return fromKey(jsonKey);
	}
	
	/**
	 * Convert a json key to a parameter name.
	 * 
	 * @param jsonKey The json key.
	 * @return The parameter name.
	 */
	public String fromKeyToParameter(String jsonKey) {
		return fromKey(jsonKey);
	}
	
	/**
	 * Convert a field or parameter name to a json key.
	 * 
	 * @param name The field or parameter name.
	 * @return The json key.
	 */
	public String toKey(String name) {
		return name;
		// Default (Does not change anything), requires override
	}
	
	/**
	 * Convert a field name to a json key.
	 * 
	 * @param fieldName The field name.
	 * @return The json key.
	 */
	public String fromFieldToKey(String fieldName) {
		return toKey(fieldName);
	}
	
	/**
	 * Convert a parameter name to a json key.
	 * 
	 * @param paramName The parameter name.
	 * @return The json key.
	 */
	public String fromParameterToKey(String paramName) {
		return toKey(paramName);
	}

}
