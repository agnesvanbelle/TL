package art.framework.example.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 * Object predicate represents an object characterized by one or more properties.
 *
 */
public class ObjectPredicateParse extends Predicate {
	
	protected Set<PropertyPredicateParse> propertyPredicates;


	public ObjectPredicateParse(String variableName) {
		super(variableName);
		this.propertyPredicates = new TreeSet<PropertyPredicateParse>();
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(variableName).append("(");
		for (Predicate var: propertyPredicates) {
			sb.append(var.toString()).append(",");
		}
		sb.deleteCharAt(sb.length()-1);	
		sb.append(")");
		return sb.toString(); 
	}

	
	public String getValue() {
		StringBuffer sb = new StringBuffer();
		if (propertyPredicates.size() == 1) {
			sb.append(propertyPredicates.iterator().next().toLongString());
		} else {
			sb.append("and(");
			for (PropertyPredicateParse var : propertyPredicates) {
				sb.append(var.toLongString()).append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
		}
		return sb.toString();
	}
	
	/**
	 * @return null if none of the property values are abstract, and 
	 * a string representation of the object with abstract values, included 
	 * where possible otherwise.
	 */
	public String getAbstructValue() {
		StringBuffer sb = new StringBuffer();
		if (propertyPredicates.size() == 1) {
			sb.append(propertyPredicates.iterator().next().toLongString());
		} else {
			sb.append("and(");
			int cnt = 0;
			for (PropertyPredicateParse var : propertyPredicates) {
				String abstructValue = var.toAbstructString();
				if (abstructValue != null) {
					sb.append(variableName).append(".").append(abstructValue)
							.append(",");
					cnt++;
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			if (sb.toString().contains(",")) {
				sb.append(")");
			} else {
				sb.replace(0, sb.indexOf("(")+1, "");
			}

			if (cnt == 0) {
				return null;
			}
		}

		return sb.toString();
	}

	public void addPropertyPredicate(PropertyPredicateParse pred) {
		propertyPredicates.add(pred);
		pred.addObjectName(variableName);
	}

	
	public Set<PropertyPredicateParse> getPropertyPredicates() {
		return propertyPredicates;
	}

	public PropertyPredicateParse getPropertyPredicate(String predName) {
		for (PropertyPredicateParse pred: propertyPredicates) {
			if (pred.getName().equals(predName)) {
				return pred;
			}
		}
		return null;
	}

	public List<String> getPossibleValues(boolean abstruct, int objCount) {
		List<String> values = new ArrayList<String>();
		
		List<PropertyPredicateParse> valuesLst = new ArrayList<PropertyPredicateParse>();
		
		for (PropertyPredicateParse property: propertyPredicates) {
			valuesLst.add(property);
		}
		
		int cnt = 0;
		for (int i = 0; i < valuesLst.size(); i++) {
			PropertyPredicateParse propertyPredicate1 = valuesLst.get(i);
			
			String p1;
			if (abstruct) {
				p1 =  propertyPredicate1.toLongAbstructString(objCount);
				if (p1 == null) {
					p1 =  propertyPredicate1.toLongString(objCount);
				} else {
					cnt++;
				}
			} else {
				p1 = propertyPredicate1.toLongString(objCount);
			}
			
			StringBuffer sb = new StringBuffer();
			sb.append(p1).append(",");
			addValue(values,p1);

			for (int j = i+1; j < valuesLst.size(); j++) {
				PropertyPredicateParse propertyPredicate2 = valuesLst.get(j);
				
				String p2;
				if (abstruct) {
					p2 =  propertyPredicate2.toLongAbstructString(objCount);
					if (p2 == null) {
						p2 = propertyPredicate2.toLongString(objCount);
					} else {
						cnt++;
					}
				} else {
					p2 =  propertyPredicate2.toLongString(objCount);
				}
			
				addValue(values,p2);
				addValue(values,"and(" + p1 + "," + p2 +")");
				
				sb.append(p2);
				String fullValue = sb.toString();
				addValue(values, "and(" + fullValue + ")");
				sb.append(",");
			}
		}
		
		if (abstruct && cnt == 0) {
			values.clear();
			return values;
		}
		return values;
	}

	private void addValue(List<String> values, String value) {
		if (!values.contains(value)) {
			values.add(value);
		}
	}

	@Override
	public String[] getVars() {
		return new String[]{variableName};
	}

}
