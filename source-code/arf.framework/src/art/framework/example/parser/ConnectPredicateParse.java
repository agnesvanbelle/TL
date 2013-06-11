package art.framework.example.parser;

import java.util.ArrayList;
import java.util.List;


/**
 * Predicate connecting two other predicates.
 */
public class ConnectPredicateParse extends Predicate {

	private List<ObjectPredicateParse> objects;
	
	public ConnectPredicateParse(String variableName) {
		super(variableName);
		
		objects = new ArrayList<ObjectPredicateParse>();
	}
	   
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append(variableName).append("(");
			for (Predicate obj: objects) {
				sb.append(obj.toString()).append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append(")");
			return sb.toString(); 
		}

		public void addObjectPredicate(ObjectPredicateParse relation) {
			objects.add(relation);
		}

	public List<String> getPossibleValues(boolean abstruct) {
		List<List<String>> allValues = new ArrayList<List<String>>();
		List<String> predValues = new ArrayList<String>();
		
		int cnt = 0;
		for (ObjectPredicateParse obj : objects) {
			cnt++;
			List<String> objValues = obj.getPossibleValues(abstruct, cnt);
			allValues.add(objValues);
		}
		
		if (allValues.size() < 2) {
			return predValues;
		}

		List<String> values1 = allValues.get(0);
		List<String> values2 = allValues.get(1);
		
		if (values1.isEmpty() || values2.isEmpty()) {
			return predValues;
		}
		
		for (int i = 0; i < values1.size(); i++) {
			StringBuffer predValue = new StringBuffer();
			predValue.append(values1.get(i)).append(",");
			predValue.append(values2.get(i));
			predValues.add(predValue.toString());
		}
		
		return predValues;
	}

	@Override
	public String[] getVars() {
		String[] objectNames = new String[objects.size()];
		int cnt = 0;
		for (ObjectPredicateParse predicate: objects) {
			objectNames[cnt] = predicate.getName()+(cnt+1);
			cnt++;
		}
		return objectNames;
	}

	public List<ObjectPredicateParse> getObjects() {
		return objects;
	}
}
