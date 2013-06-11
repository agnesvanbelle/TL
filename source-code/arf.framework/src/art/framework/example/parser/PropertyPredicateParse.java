package art.framework.example.parser;


/**
 * Property predicate object.
 */
public class PropertyPredicateParse extends Predicate {

	private String value;
	private String abstructValue;
	private String longName;
	private String objectName;
	private String abstructName;
	
	public PropertyPredicateParse(String variableName, String value) {
		super(variableName);
		this.value = value;
	}

	@Override
	public String toString() {
			 return variableName + "(" + value + ")";
	}

	public String toAbstructString() {
		if (abstructValue != null) {
			 return variableName + "(" + abstructValue + ")";
		}
		return null;
	}

	public String toLongAbstructString() {
		if (longName != null && abstructValue != null) {
			 return longName + "(" + abstructValue + ")";
		}
		return null;
	}
	
	public String toLongAbstructString(int cnt) {
		if (longName != null && abstructValue != null) {
			 return objectName +cnt+ "." + variableName  + "(" + abstructValue + ")";
		}
		return null;
	}

	public String toLongString() {
		if (longName != null) {
		 return longName + "(" + value + ")";
		}
		return null;
	}

	public String toLongString(int cnt) {
		if (longName != null) {
		 return objectName +cnt+ "." + variableName + "(" + value + ")";
		}
		return null;
	}
	
	public String getValue() {
		return value;
	}

	public void addAbstructValue(String abstructValue) {
		this.abstructValue = abstructValue;
	}

	public String getAbstructValue() {
		return abstructValue;
	}
	
	public String getLongName() {
		if (longName != null 
				&& !longName.isEmpty()) {
			return longName;
		}
		return variableName;
	}

	public void addObjectName(String objectName) {
		this.objectName = objectName;
		this.longName = objectName + "." + variableName;
	}

	@Override
	public String[] getVars() {
		if (objectName == null) {
			return null;
		}
		return new String[]{objectName};
	}

	public void setAbstructName(String abstructName) {
		this.abstructName = abstructName + "-" + variableName;
	}
	
	public String getAbstructName() {
		if (abstructName == null) {
			return variableName;
		}
		return abstructName;
	}
	
}
