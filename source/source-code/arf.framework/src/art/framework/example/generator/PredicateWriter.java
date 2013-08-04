package art.framework.example.generator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import art.framework.utils.AbstractPredicate;
import art.framework.utils.Constants;

/**
 * Allows conversion of instances to an intermediate format - a set of logic predicates. 
 * Provides API for constructing such instances by incrementally adding 
 * predicates of different kinds, e.g. object and connect. 
 * Object predicates represent an object represented by a name and a set of 
 * properties and connect predicates connect object predicates.  
 */
public class PredicateWriter {
	private int exampleIdCnt = 0;
	
	/**
	 * Maps example to a set of predicates.
	 */
	private Map<Integer, TreeSet<AbstractPredicate>> predicates;
	
	/**
	 * Maps counts of the relations and relations_values in each example. 
	 * This will be used to construct all and has relations. 
	 * The first one indicates that all relations of a type have the same value.
	 * The second relation indicates that a certain value exists at least once in a given example, 
	 * or alternatively, has can indicate the number of times a certain relation value occurs. 
	 */
	private Map<String, Map<String, Integer>> propertyCounts;
	private Map<String, Integer> objectCounts;

	
	/**
	 * Maps a user-given id to an integer internal id.
	 */
	Map<String, Integer> objectIdMap;

	public PredicateWriter() 
	{
		predicates = new HashMap<Integer, TreeSet<AbstractPredicate>>();
		propertyCounts = new HashMap<String, Map<String, Integer>>();
		objectCounts = new HashMap<String, Integer>();
		objectIdMap = new HashMap<String, Integer>();
	}

	/**
	 * This method is invoked prior to addition of any predicates 
	 * and signifies the beginning of an instance. It does all the 
	 * necessary initializations for the new instance.
	 */
	public void startExample() {
		predicates.clear();
		propertyCounts.clear();
		objectCounts.clear();
		objectIdMap.clear();
		exampleIdCnt++;
	}

	/**
	 * Adds a predicate that connects two objects.
	 * 
	 * @param obj1 - String representation of the first object/property
	 * @param obj2 - String representation of the second object/property
	 * @param preicateName - name of the connect predicate.
	 * 
	 * E.g. addConnectPredicate(car1, car2, bumpedInto) will generate bumpedInto(car1, car2)
	 */
	public void addConnectPredicate(String obj1, 
				String obj2, String preicateName) {
		
		String connectPredicateName = preicateName;
		TreeSet<AbstractPredicate> set = getSet(exampleIdCnt);
		AbstractPredicate propertyRelation = 
			new ConnectPredicateGenerate(obj1, obj2, connectPredicateName);
		set.add(propertyRelation);
		predicates.put(exampleIdCnt, set);
	}
	

	/**
	 * Adds an object predicate.
	 * 
	 * @param objName - name of the object predicate.
	 * @param objProperties - a map of object property names and values
	 */
	public void addObjectPredicate(String objName, 
			Map<String, String> objProperties) {
		
		TreeSet<AbstractPredicate> set = getSet(exampleIdCnt);
		AbstractPredicate objectPredicate = 
			new ObjectPredicateGenerate(objName, objProperties);
		set.add(objectPredicate);
		predicates.put(exampleIdCnt, set);
	}
	
	/**
	 * Adds a property predicate.
	 * 
	 * @param objName - name of the object predicate.
	 * @param objProperties - a map of object property names and values
	 */
	public void addExamplePropertyPredicate(String predicateName, String value) {
		TreeSet<AbstractPredicate> set = getSet(exampleIdCnt);

		AbstractPredicate propertyRelation = new PropertyPredicateGenerate(value, predicateName);
		set.add(propertyRelation);
		
		predicates.put(exampleIdCnt, set);
	}
	
	
	/**
	 * Provides a set of all predicates for a given example.
	 * 
	 * @param id - id of the example instance.
	 * @return - a set of predicates associated with the given example.
	 */
	private TreeSet<AbstractPredicate> getSet(int id) {
		
		TreeSet<AbstractPredicate> set =  predicates.get(id);
		if (set == null) {
			return new TreeSet<AbstractPredicate>();
		}
		return set;
	}

	/**
	 * Saves example predicates to a file. 
	 * 
	 * @param classLabel - a label that will be assigned to all examples.
	 * @param file - path to a file to which the output will be saved. 
	 * @param appendToFile - if true an existing file won't be overwritten.
	 */
	public void saveExamples(String classLabel, 
			String file, boolean appendToFile) {
		saveExample(file, predicates, classLabel, appendToFile);		
	}
	
	private void saveExample(String file, Map<Integer, 
			TreeSet<AbstractPredicate>> predicates, 
			String classLabel, boolean appendToFile) {
		
		try {
			BufferedWriter bw = 
				new BufferedWriter(new FileWriter(file,appendToFile));
			if (predicates.isEmpty()) {
				bw.write(classLabel + "\n");
			}
			for (Integer id: predicates.keySet()) {
				StringBuffer predicateStr = new StringBuffer();
				predicateStr.append(classLabel).append(Constants.COLON);
				TreeSet<AbstractPredicate> predicateSet = predicates.get(id);
				
				for (AbstractPredicate predicate: predicateSet) {
					predicateStr.append(predicate.toString()).append(Constants.COLON);
				}
				predicateStr.deleteCharAt(predicateStr.length()-1);
				predicateStr.append("\n");
				bw.write(predicateStr.toString());
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			System.out.println("[ERROR] Failed to save graph to a file: " + file);
			System.out.println(e);
			System.exit(1);
		}
	}
}
