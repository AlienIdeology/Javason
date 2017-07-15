import com.github.alienideology.javason.JsonArray;
import com.github.alienideology.javason.JsonObject;
import com.github.alienideology.javason.JsonParser;

public class Test {

	public static void main(String[] args) {
//		testObject();
		testArray();
	}
	
	public static void testObject() {
		JsonObject obj = new JsonParser(
				"{"
				+ "\"key1\":\"value\","
				+ "\"key2\" :true,"
				+ "\"key3\" : {"
						+ "\"number\":1000E+78,"
						+ "\"empty\":\"\","
						+ "\"null\":null"
				+ "},"
				+ "\"array\" : [0.1,0,0,0]"
				+ "}").parseObject();
		System.out.println(obj.toString(4));
	}
	
	public static void testArray() {
		JsonArray obj = new JsonParser(
				"["
				+ "0,"
				+ "{\"sub-obj\" : 0},"
				+ "[\"sub-array\", 0]"
				+ "]").parseArray();
		System.out.println(obj.toString(4));
	}
	
	static class Example {
		private String key;
		public boolean key2;
		protected boolean key4;

		public Example(boolean key2, boolean key4) {
			super();
			this.key2 = key2;
			this.key4 = key4;
		}
		
		public void print() {
			System.out.println(key + " " + key2);
		}
	}
	
}
