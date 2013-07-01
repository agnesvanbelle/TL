package art.framework.example.generator;

import art.framework.utils.AbstractPredicate;


public class PropertyPredicateGenerate extends AbstractPredicate {

	private String objectId;
	private String value;
	private String relationName;

	public PropertyPredicateGenerate(String objectId, String value, String relationName) {
		this.objectId = objectId;
		this.value = value;
		this.relationName = relationName;
	}

	public PropertyPredicateGenerate(String value, String relationName) {
		this.objectId = null;
		this.value = value;
		this.relationName = relationName;
	}

	public String toString() {
		StringBuffer str = new StringBuffer();
		
		if (objectId == null) {
			str.append(relationName).append("(").append(value).append(")");
		} else {
			str.append(relationName).append("(").append(objectId).append(",")
				.append(value).append(")");
		}
		return str.toString();
	}

}
