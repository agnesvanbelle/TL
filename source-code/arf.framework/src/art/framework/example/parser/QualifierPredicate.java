package art.framework.example.parser;

import art.framework.utils.AbstractPredicate;

/**
 * Handles qualifier predicates like exists and for-all.
 */
public class QualifierPredicate extends AbstractPredicate {
	/**
	 * Type of the quantifier relation, e.g. exists or for_all.
	 */
	String quantifierType;
	/**
	 * Value of the original relation/predicate.
	 */
	String relationValue; 
	/**
	 * Name of the original relation/predicate.
	 */
	String relationName;
	/**
	 * Number of times this relation occurs in data.
	 */
	Integer noOfOccurance = null; 
	/**
	 * Predicate object containing information about a given relation.
	 */
	Predicate predicate;
	
	/**
	 * Constructs a quantifier relation object. 
	 * 
	 * @param relationValue - the value of the original relation/predicate
	 * @param relationName - the name of the original relation/predicate
	 * @param predicate - predicate object containing information about a given relation
	 * @param quantifierType - type of the quantifier relation, e.g. exists or for_all
	 */
	public QualifierPredicate(String relationValue, String relationName, Predicate predicate, String quantifierType) {
		this.relationName = relationName;
		this.relationValue = relationValue; 
		this.quantifierType = quantifierType;
		this.predicate = predicate;
	}
	
	public QualifierPredicate(String value, String relationName,
			Integer noOfOccurance, Predicate predicate, String type) {
		this(value, relationName, predicate, type);
		this.noOfOccurance = noOfOccurance;
	}

	public String toString() {
		StringBuffer str = new StringBuffer();
		String[] vars = predicate.getVars();
		boolean addPredicateName = true;
		if (predicate instanceof ObjectPredicateParse) {
				//|| predicate instanceof PropertyPredicate) {
			addPredicateName = false;
		}
		
		if (noOfOccurance == null) {
			str.append(quantifierType).append("(");
			if (vars  != null) {
				for (String var : vars) {
					str.append(var).append(",");
				}
			}
			if (addPredicateName) {
				str.append(relationName).append("(").append(relationValue).append(")").append(")");
			} else {
				str.append(relationValue).append(")");
			}
		} else {
			str.append(quantifierType).append("(");
			if (vars != null) {
				for (String var : vars) {
					str.append(var).append(",");
				}
			}
			if (addPredicateName) {
				str.append(relationName).append("(").append(relationValue).append(")").append(",").append(noOfOccurance).append(")");
			} else {
				str.append(relationValue).append(")").append(",").append(noOfOccurance);

			}
			
		}
		return str.toString();

	}

}
