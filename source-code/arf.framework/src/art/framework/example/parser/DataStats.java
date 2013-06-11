package art.framework.example.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Contains statistical information about relations and classes.
 */
public class DataStats {
	/**
	 * A set of all existing class labels.
	 */
	Set<String> classLabels;
	
	/**
	 * Counts for different relations occurring in a data set.
	 */
	Map<String, Integer> relationsTotalCounts;
	
	/**
	 * Counts of the same relations within each class.
	 */
	Map<String, Map<String, Integer>> relationsClassCounts;
	
	public DataStats() {
		classLabels = new HashSet<String>();
		relationsTotalCounts = new HashMap<String, Integer>();
		relationsClassCounts = new HashMap<String, Map<String,Integer>>();
	}
	
	public void addClassLabel(String relation) {
		classLabels.add(relation);
	}

	public void addRelation(String relName, String classLabel) {
		Integer count = relationsTotalCounts.get(relName);
		if (count == null) {
			count = 0;
		}
		relationsTotalCounts.put(relName, ++count);
		
		Map<String, Integer> relationCounts = relationsClassCounts.get(classLabel);
		if (relationCounts == null) {
			relationCounts = new HashMap<String, Integer>();
		}
		count = relationCounts.get(relName);
		if (count == null) {
			count = 0;
		}
		relationCounts.put(relName, ++count);
		relationsClassCounts.put(classLabel, relationCounts);
	}
	
	/**
	 * @param irrelevantRelations - 
	 * collections that will be filled with
	 *  relations appearing in all examples and thus deemed not very informative.
	 * @param dataSize 
	 */
	public void getIrrelevantRelations(Set<String> irrelevantRelations, int dataSize) {		
		
		//delete relations with low probabilities within a given class
//		for (String classLabel: relationsClassCounts.keySet()) {
//			Map<String, Integer> classCntMap = relationsClassCounts.get(classLabel);
//			
//			for (String relName: classCntMap.keySet()) {
//				Integer relPerClassCnt = classCntMap.get(relName);
//				Integer totalCnt = relationsTotalCounts.get(relName);
//				
//				double probability = ((double)relPerClassCnt / (double)totalCnt);
//				
//				if (probability <= 0.5) {
//					relationsToDelete.add(relName);
//					System.out.println("To delete: " + relName);
//				}
//			}
//		}
		
		//if the same relation is present in all examples, then it's not very informative
		for (String relName: relationsTotalCounts.keySet()) {
			Integer relTotalCnt = relationsTotalCounts.get(relName);
			
			int totalOccurance = 0;
			boolean presentInAllClasses = true;
			for (String classValue: relationsClassCounts.keySet()) {
				Integer relCounts = relationsClassCounts.get(classValue).get(relName);
				if (relCounts != null) {
					totalOccurance += relCounts;
				} else {
					presentInAllClasses = false;
				}
			}
			
			double total = (double)totalOccurance;
			double datatotal = (double)dataSize;
			
			double percentage = (total / datatotal) *100.00;
			
			if (relTotalCnt == totalOccurance 
					&& presentInAllClasses && percentage > 90) {
				irrelevantRelations.add(relName);
			}
		}
	}

}
