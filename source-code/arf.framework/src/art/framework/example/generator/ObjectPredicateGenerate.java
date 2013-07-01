package art.framework.example.generator;


import java.util.Map;

import art.framework.utils.AbstractPredicate;

/**
 * Object predicate represented by a nesting of a set of 
 * property predicates. 
 */
public class ObjectPredicateGenerate extends AbstractPredicate {
	int exampleId;
	String relationName;
	private Map<String, String> relations;

	public ObjectPredicateGenerate(String relationName,
			Map<String, String> objRelations) {
		this.relationName = relationName;
		this.relations = objRelations;
	}

	/**
	 * String representation of the object predicate.
	 */
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(relationName).append("(");
		for (String relation : relations.keySet()) {
			if (relation.startsWith("@")) {
				continue;
			}
			String relValue = relations.get(relation);
			expandRelation(str,relation, relValue, relations);
		}
		str.deleteCharAt(str.length()-1);
		str.append(")");
		return str.toString();
	}

	private void expandRelation(StringBuffer str, String relName, String relValue,
			Map<String, String> relations) {
		
		if (!relValue.startsWith("@")) {
			str.append(relName).append("(");
			str.append(relValue);
			str.append(")").append(",");
		} else {
			str.append(relName).append("(");
			String[] subRelations = relValue.split(",");
			for (String subRelation : subRelations) {
				relValue = relations.get(subRelation);
				subRelation = subRelation.replace("@", "");
				expandRelation(str,subRelation,  relValue, relations);
			}
			str.deleteCharAt(str.length()-1);
			str.append(")").append(",");
		}
		return;
	}

}
