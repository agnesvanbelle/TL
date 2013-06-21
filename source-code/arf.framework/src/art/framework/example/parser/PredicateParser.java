package art.framework.example.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;


/**
 * Parses text representations of predicates and constructs Predicate objects.
 */
public class PredicateParser {
	
	/**
	 * Parses a complex predicate returns a collection of predicates that constitute it. 
	 * 
	 * @param predicate - a predicate of a form "exists(sensor,and(sensor.act-duration(1-2),sensor.sensor-type(BathroomDoor)))"
	 * @param propertyPairs 
	 * @return a collection of <Predicates 
	 */
	public static Collection<Predicate> parse(String predicate, List<String> propertyPairs) {
		Stack<Index> indexStack = new Stack<Index>();
		Stack<String> relationsStack = new Stack<String>();
		Map<String, Predicate> relationsMap = new TreeMap<String, Predicate>();
		List<String> toDelete = new ArrayList<String>();

		int startIndex = 0;
		for (int i = 0; i < predicate.length(); i++) {
			if (predicate.charAt(i) == '(') {
				if (indexStack.isEmpty()) {
					startIndex = 0;
					String token = predicate.substring(startIndex, i);
					indexStack.push(new Index(true, false, i));
					relationsStack.push(token);
				} else {
					Index prevIndex = indexStack.peek();
					String token = predicate.substring(
							prevIndex.getIndex() + 1, i);

					indexStack.push(new Index(true, false, i));
					relationsStack.push(token);
				}

			} else if (predicate.charAt(i) == ')') {
				Index prevIndex = indexStack.pop();
				while (prevIndex.isCommaIndex()) {
					prevIndex = indexStack.pop();
				}
				String token = predicate.substring(prevIndex.getIndex() + 1, i);

				String topRelation = relationsStack.pop();
				String prevRelation = null;
				if (!relationsStack.isEmpty()) {
					prevRelation = relationsStack.peek();
				}
				addRelation(topRelation, prevRelation, token, relationsMap,
						toDelete, propertyPairs);

				if (predicate.length() > (i + 1)
						&& predicate.charAt(i + 1) == ',') {
					indexStack.push(new Index(false, true, i + 1));
				}
			}
		}
		for (String rel : toDelete) {
			relationsMap.remove(rel);
		}

		return relationsMap.values();
	}
	

	private static void addRelation(String currentRelation,
			String prevRelation, String token,
			Map<String, Predicate> predicateMap, List<String> toDelete, 
			List<String> propertyPairs) {
	
		if (!token.contains("(")) { //handle property relation
			PropertyPredicateParse rel = new PropertyPredicateParse(currentRelation, token);
			predicateMap.put(rel.toString(), rel);
			return;
		}

		List<ObjectPredicateParse> objPreds = new ArrayList<ObjectPredicateParse>();; 
		List<PropertyPredicateParse> propertyPreds = new ArrayList<PropertyPredicateParse>();;

		//get included relations
		for (String predicate : predicateMap.keySet()) {
			Predicate pred = predicateMap.get(predicate);
			if ((token).contains(predicate)) {
				if (pred instanceof ObjectPredicateParse) {
					objPreds.add((ObjectPredicateParse) pred);
				} else if (pred instanceof PropertyPredicateParse) {
					propertyPreds.add((PropertyPredicateParse) pred);
				}
			}
		}
		
		//handle connect relation
		if (!objPreds.isEmpty()) {
			ConnectPredicateParse connectPred = 
				new ConnectPredicateParse(currentRelation);
			
			for (ObjectPredicateParse obj: objPreds) {
				connectPred.addObjectPredicate(obj);
				toDelete.add(obj.toString());
			}			
			predicateMap.put(connectPred.toString(), connectPred);
		} else  { //handle object relation
			ObjectPredicateParse objPred = new ObjectPredicateParse(currentRelation);
			
			for (PropertyPredicateParse pred: propertyPreds) {
				objPred.addPropertyPredicate(pred);
				toDelete.add(pred.toString());
			}
			
			if (propertyPairs != null && propertyPairs.size() % 2 == 0) {
				for (int i = 0; i < propertyPairs.size(); i += 2) {
					PropertyPredicateParse property1 = objPred.getPropertyPredicate(propertyPairs.get(i));
					PropertyPredicateParse property2 = objPred.getPropertyPredicate(propertyPairs.get(i + 1));
					if (property1 != null && property2 != null) {
						property2.setAbstructName(property1.getValue()); 
					}
				}
			}
			
			predicateMap.put(objPred.toString(), objPred);

		}
	}

	static class Index {
		boolean openIndex = false;
		boolean commaIndex = false;
		int index = 0;
		
		public Index(boolean openIndex, 
				boolean commanIndex, int index) 
		{
			this.openIndex = openIndex;
			this.index = index;
			this.commaIndex = commanIndex;
		}

		public boolean isCommaIndex() {
			return commaIndex;
		}
		
		public boolean isCloseIndex() {
			return !openIndex;
		}

		public boolean isOpenIndex() {
			return openIndex;
		}

		public int getIndex() {
			return index;
		}
	}
}
