package art.framework.example.parser;

import art.framework.utils.Constants;

/**
 * Object representing a value in which a predicate can be grounded.
 * The value can belong to any of the fixed types: time, range, string, integer, real number.
 * It could further be extended to handle other types.
 */
public class Value implements Comparable<Value> {
	private String value;
	private Constants.Type type;  
	
	public Value(String value) {
		if (value.contains("t@")) {
			this.type = Constants.Type.TIME;
			this.value = value.replaceAll("t@", "");
			return;
		} else if (value.contains("-")) {
			String[] values = value.split("-");
			if (values.length == 3) {
				this.type = Constants.Type.RANGE;
			} else {
				this.type = Constants.Type.STR;
			}
		} else if (isInteger(value)) { // integer
			this.type = Constants.Type.INT;
		} else if (isReal(value)) { // real
			this.type = Constants.Type.REAL;
		} else { // string
			this.type = Constants.Type.STR;
		}
		this.value = value;
	}

	private boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isReal(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean equals(Object obj) {
		Value val = (Value)obj;
		return (this.type.equals(val.type) 
				&& this.value.equals(val.getValue()));
	}
	
	String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}
	
	@Override
	public int compareTo(Value val) {
		return this.toString().compareTo(val.toString());
	}

	public Constants.Type getType() {
		return type;
	}
	
}
