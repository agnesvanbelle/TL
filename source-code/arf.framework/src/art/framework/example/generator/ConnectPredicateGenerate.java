package art.framework.example.generator;

import art.framework.utils.AbstractPredicate;

/**
 * A predicate connecting two objects.
 */
public class ConnectPredicateGenerate extends AbstractPredicate {

	private String objectId1;
	private String objectId2;
	private String predicateName;

	public ConnectPredicateGenerate(String objId1, String objId2, String predicateName) {
		this.objectId1 = objId1;
		this.objectId2 = objId2;
		this.predicateName = predicateName;
	}

	/**
	 * String representation of the connect predicate.
	 */
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(predicateName).append("(").append(objectId1).append(","
				).append(objectId2).append(")");
		
		return str.toString();
	}
}
