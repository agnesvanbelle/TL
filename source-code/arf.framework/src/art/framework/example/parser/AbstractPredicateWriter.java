package art.framework.example.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import art.framework.utils.Constants;
import art.framework.utils.Utils;

public class AbstractPredicateWriter {

	
	/**
	 * Stores examples to a file for each class in turn.
	 * 
	 * @param outputFile - file to which abstract relations will be saved for each example.
	 * @param mapOutputFile - a file to which class distribution and outputFile path will be stored 
	 *  			(this file is used in the future for rule extraction).
	 * @param all  - if true both abstract and non-abstract relations will be stored together.	 
	 * @param saveToOneFile - if true all examples will be appended to a file.
	 * @param classExamplesMap - a map from class labels to corresponding examples, this map is populated by the method.
	 * @param irrelevantRelations - a set of unimportant relations that shouldn't be saved.
	 */
	private void saveExamplesByClass(String outputFile, 
			String mapOutputFile, Map<String, List<Example>> classExamplesMap,
			boolean appendToFile, boolean all, boolean saveToOneFile) {
		
		Utils.deleteFile(mapOutputFile);
		Utils.deleteFile(outputFile);

		try {
			String newOutputFile = outputFile;
			BufferedWriter mapBW = new BufferedWriter(new FileWriter(
					mapOutputFile, true));

			StringBuffer sbClasses = new StringBuffer();
			StringBuffer sb = new StringBuffer();
			
			for (String classLabel : classExamplesMap.keySet()) {
				int size = 0;
				
				String newOutputFile_ = outputFile;
				if (!saveToOneFile) {
					newOutputFile_ = newOutputFile + classLabel;
						//+ outputFileExtension;
					Utils.deleteFile(newOutputFile_);
				}
				
				List<Example> examples = classExamplesMap.get(classLabel);
				size += examples.size();
				
				saveRelations(examples, all, classLabel, newOutputFile_, appendToFile);
				
				sbClasses.append(classLabel + ":" + size + ",");
				if (!saveToOneFile) {
					sb.append(classLabel).append(":").append(examples.size()
							).append(",").append(newOutputFile_).append("\n");
				} 
			}
			
			if (!sbClasses.toString().isEmpty()) {
				sbClasses.deleteCharAt(sbClasses.length()-1);
			}
			
			if (saveToOneFile) { // NOTE here this path+filename is appended
				mapBW.write(sbClasses.toString() + "," + outputFile + "\n");
			} else {
				mapBW.write(sb.toString());
			}
			mapBW.flush();
			mapBW.close();
		} catch (IOException e) {
			System.out.println("[ERROR] Failed to write to a file: "
					+ mapOutputFile);
			System.out.println(e);
			System.exit(1);
		}
	}

	/**
	 * Saves examples of a given class to the same file 
	 * 
	 * @param examples - a list of examples
	 * @param all - if true abstract and non-abstract example predicates will be saved.
	 * @param classLabel - class for which examples should be saved.
	 * @param outputFile - output file.
	 * @param appendToFile - if true the examples will be saved to one file.
	 */
	private void saveRelations(List<Example> examples, boolean all, String classLabel,
			String outputFile, boolean appendToFile)
	{
			
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, appendToFile));

			for (Example example : examples) {
				StringBuffer relStr = new StringBuffer();
				relStr.append(classLabel).append(Constants.COLON);

				List<String> relations;
				if (all) {
					relations = example.getAllRelationsStr();
				} else {
					relations = example.getAbstractRelationsStr();
				}
				for (String rel : relations) {
					relStr.append(rel).append(Constants.COLON);
				}

				relStr.deleteCharAt(relStr.length() - 1);
				relStr.append("\n");
				bw.write(relStr.toString());
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			System.out.println("[ERROR] Failed to write to a file: " + outputFile);
			System.out.println(e);
			System.exit(1);
		}
		
	}
	
	static class Rule {
		private String classLabel; 
		private List<String> predicatesStr;
		Map<String,Integer> distribution;
		private double confidence;
		
		public Rule(String classLabel, List<String> predicates, 
				Map<String, Integer> distribution) {
			this.classLabel = classLabel;
			this.predicatesStr = predicates;
			this.distribution = distribution;
		}
		
		public Rule(String classLabel, List<String> predicates, double confidence) {
			this.classLabel = classLabel;
			this.predicatesStr = predicates;
			this.confidence = confidence;
		}

		public String getClassLabel() {
			return classLabel;
		}
		
		public List<String> getPredicates() {
			return predicatesStr;
		}
		
		public Map<String, Integer> getDistribution() {
			return distribution;
		}
		
		public double getConfidence() {
			return confidence;
		}
	}
	
	/**
	 * Constructs a list of Rule objects given a file with 
	 * a set of rules consisting of delimiter separated predicates and 
	 * including a confidence score of the rule.
	 * 
	 * @param file - file with rules.
	 * @param delimiter - delimiter separating predicates within each rule.
	 * @return a list of Rule objects, containing information about each rule.
	 */
	public static List<Rule> readRules(String file, String delimiter) {
		List<Rule> rules = new ArrayList<Rule>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				String classLabel = line.substring(0,line.indexOf(" ["));
				
				Double confidence = 1.0;
				if (line.length() > line.indexOf(']')+2) {
					String confStr = line.substring(line.indexOf(']')+2);
					confidence = Double.parseDouble(confStr); //add confidence
				} 
				line = line.substring(line.indexOf('[')+1, line.indexOf(']'));
				String[] rulesInfo = line.split(delimiter);
				List<String> patterns = Arrays.asList(rulesInfo);
				
				rules.add(new Rule(classLabel, patterns, confidence));
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("[ERROR] Failed to read from file: " + file);
			System.out.println(e);
			System.exit(1);
		} catch (IOException e) {
			System.out.println("[ERROR] Unexpected exception while reading from file: " + file);
			System.out.println(e);
			System.exit(1);
		}
		return rules;
	}


	/**
	 * Represents data in terms of features.
	 * 
	 * @param intputFile - data in logic form
	 * @param outputFile - output file for the new data representation
	 * @param rulesFile - a file with features/logic rules
	 * @param classMapFile - a file containing a mapping from class labels to 
	 * 			numerical classes, used with SVN classifier.
	 * @param useClass - specifies whether a class label should be considered when aggregating 
	 * values for predicate types, that will later be used to infer value ranges 
	 * (associated either just with predicate type or with both predicate type and class). 
	 * 
	 */
	public void getFeatureRepresentationOfData(String intputFile, 
			String outputFile, String rulesFile, String classMapFile, boolean useClass) {
		 getFeatureRepresentationOfData(intputFile, outputFile,  
				 rulesFile, null, null, classMapFile, useClass);
	}
	
	/**
	 * @param intputFile - data in logic form
	 * @param outputFile - output file for the new data representation
	 * @param rulesFile - a file with features/logic rules
	 * @param classMapFile - a file containing a mapping from class labels to 
	 * 			numerical classes, used with SVN classifier. 
	 * 
	 * @param propertyValueRangeMap - a mapping of a predicate names to ValueRange objects that 
	 * aggregate all possible values associated with this name (or property pairs if propertyPairs isn't null) 
	 * and class values if useClass is true. These values are later used to infer ranges, e.g. min-max that
	 * are a generalization over the original values. 
	 * 
	 * @param propertyPairs - a list of property names that stores pairs of predicate
	 * names, e.g. sensor-type, sensor-start-time, such that when the values of the second predicate
	 * are aggregated, they are associated also with the first predicate name, e.g. all possible start
	 * times for a sensor-type Kitchen.
	 * 
	 * @param useClass - specifies whether a class label should be considered when aggregating 
	 * values for predicate types, that will later be used to infer value ranges 
	 * (associated either just with predicate type or with both predicate type and class). 

	 */
	public void getFeatureRepresentationOfData(String intputFile, String outputFile, 
			String rulesFile, List<String> propertyPairs  /* == null */, 
			Map<String, ValueRange> propertyValueRangeMap /* == null */, String classMapFile, boolean useClass) 
	{
		List<Rule> rules = readRules(rulesFile, ", ");
		
		DataStats dataStats = new DataStats();
		Map<String, List<Example>> classExamplesMap = 
						new HashMap<String,List<Example>>();
		
		if (propertyValueRangeMap == null) {
			propertyValueRangeMap = new HashMap<String, ValueRange>();
		}
		
		getAbstructRelationalRepresentation(intputFile, 
				dataStats, classExamplesMap, propertyPairs, propertyValueRangeMap, useClass);

		//save examples represented in terms of rules
		StringBuffer convertedExamplesRules = new StringBuffer();

		HashMap<String, String> classMap = getClassMap(classMapFile);
		
		List<String> classExamples = new ArrayList<String>(classExamplesMap.keySet());
		Collections.sort(classExamples);
		
		for (String classLabel : classExamples) {
			List<Example> examples = classExamplesMap.get(classLabel);
			
			for (Example example : examples) {
				String svmLabel = classMap.get(classLabel); 
				
				convertedExamplesRules.append(svmLabel).append(Constants.SPACE);
				List<String> exRelationsStr = example.getAllRelationsStr();

				for (int i = 0; i < rules.size(); i++) {
					Rule rule = rules.get(i);
					List<String> predicates = rule.getPredicates();
					if (exRelationsStr.containsAll(predicates)) {						
						convertedExamplesRules.append((i+1) + Constants.COLON + 1.0).append(Constants.SPACE);
					} else {
						double value = getPartialContainment(exRelationsStr, predicates);
						if (value > 0) {
							convertedExamplesRules.append((i+1) + Constants.COLON + value).append(Constants.SPACE);
						}
					}
				}
				convertedExamplesRules.append(Constants.NEW_LINE);
			}	
		}
		Utils.saveContent(outputFile, convertedExamplesRules.toString());
		
	}

	/**
	 * Reads a file that maps class descriptive names to 
	 * numeric values. 
	 * 
	 * @param classMapFile - file specifying the mapping
	 * @return - a map of class descriptions to numeric values
	 */
	private HashMap<String, String> getClassMap(String classMapFile) {
		HashMap<String, String> classMap = new HashMap<String, String>();
		List<String> classLines = Utils.readLines(classMapFile);
		for (String cl: classLines) {
			String[] clInfo = cl.split(",");
			classMap.put(clInfo[1], clInfo[0]);
		}
		return classMap;
	}
	
	/**
	 * Converts non-abstract predicates to SVM form - in terms of features.
	 * 
	 * @param intputFile - file with examples
	 * @param outputFile - SVM format file
	 * @param rulesFile - file with rules
	 * @param classMapFile - a mapping from class labels to SVM labels
	 */
	public void getFeatureRepresentationOfNonAbstractData(String intputFile, String outputFile, 
			String rulesFile, String classMapFile) 
	{
		List<Rule> rules = readRules(rulesFile, ", ");

		Map<String,List<List<String>>> classExamplesMap = new HashMap<String, List<List<String>>>();
		List<String> lines = Utils.readLines(intputFile);
		for (String line: lines) {
			List<String> relations = new ArrayList<String>(Arrays.asList(line.split(":")));
			String classLabel = relations.remove(0);
			List<List<String>> ex = classExamplesMap.get(classLabel);
			if (ex == null) {
				ex = new ArrayList<List<String>>();
			}
			ex.add(relations);
			classExamplesMap.put(classLabel, ex);
		}
		
		//save examples represented in terms of rules
		StringBuffer convertedExamplesRules = new StringBuffer();
		
		List<String> classExamples = new ArrayList<String>(classExamplesMap.keySet());
		Collections.sort(classExamples);
		
		HashMap<String, String> classMap = getClassMap(classMapFile);		
		for (String classLabel : classExamples) {
			List<List<String>> examples = classExamplesMap.get(classLabel);
			
			for (List<String> example : examples) {
				String svmLabel = classMap.get(classLabel);
				convertedExamplesRules.append(svmLabel).append(Constants.SPACE);
							
				for (int i = 0; i < rules.size(); i++) {
					Rule rule = rules.get(i);
					List<String> predicates = rule.getPredicates();
					if (example.containsAll(predicates)) {						
						convertedExamplesRules.append((i+1) + 
								Constants.COLON + 1.0).append(Constants.SPACE);
					} 
				}
				
				convertedExamplesRules.append(Constants.NEW_LINE);
			}	
		}
		
		Utils.saveContent(outputFile, convertedExamplesRules.toString());
		
	}
	
	

	private double getPartialContainment(List<String> exRelationsStr,
			List<String> rule) {
		
		int ruleLength = rule.size();
		double predicateCount = 0;
		
		for (String predicate: rule) {
			if (exRelationsStr.contains(predicate)) {
				predicateCount++;
			}
		}
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(1.0*predicateCount / ruleLength));
	}


	/**
	 * Saves abstract relations to a file.
	 * 
	 * @param inputFile - the file with non-abstract first order predicates.
	 * @param outputFile - file to which abstract relations will be saved for each example.
	 * @param mapOutputFile - a file to which class distribution and outputFile path will be stored 
	 *  (this file is used in the future for rule extraction).
	 * @param all  - if true both abstract and non-abstract relations will be stored together.
	 * @param saveToOneFile - if true all examples will be appended to a file.
	 * @param useClass - specifies whether a class label should be considered when aggregating 
	 * values for predicate types, that will later be used to infer value ranges 
	 * (associated either just with predicate type or with both predicate type and class). 
     *
	 */
	public void saveAbstructRelations(String intputFile, 
			String outputFile, String mapOutputFile, boolean all, boolean useClass) {
		
		saveAbstructRelations(intputFile, outputFile, 
				mapOutputFile, all, false, null, null, useClass);
	}
	
	
	/**
	 * 
	 * @param inputFile - the file with non-abstract first order predicates.
	 * @param outputFile - file to which abstract relations will be saved for each example.
	 * @param mapOutputFile - a file to which class distribution and outputFile path will be stored 
	 *  (this file is used in the future for rule extraction).
	 * @param all  - if true both abstract and non-abstract relations will be stored together.
	 * @param saveToOneFile - if true all examples will be appended to a file.
	 * 
	 * @param useClass - specifies whether a class label should be considered when aggregating 
	 * values for predicate types, that will later be used to infer value ranges 
	 * (associated either just with predicate type or with both predicate type and class). 
	 */
	public void saveAbstructRelations(String intputFile, 
			String outputFile, String mapOutputFile, boolean all, 
			boolean saveToOneFile, List<String> propertyPairs, 
			Map<String, ValueRange> propertyValueRangeMap, boolean useClass) 
	{
		DataStats dataStats = new DataStats();
		Map<String, List<Example>> classExamplesMap = 
			new HashMap<String,List<Example>>();
		
		if (propertyValueRangeMap == null) {
			propertyValueRangeMap = new HashMap<String, ValueRange>();
		}
		getAbstructRelationalRepresentation(
				intputFile, dataStats, classExamplesMap, 
				propertyPairs, propertyValueRangeMap, useClass);
		
		saveExamplesByClass(outputFile, mapOutputFile, classExamplesMap, 
				saveToOneFile, all, saveToOneFile);
	}
	
	

	/**
	 * Represents data in terms of abstract predicates derived from the original
	 * first order representation. 
	 * 
	 * @param intputFile - file with first order predicates.
	 * @param dataStats - statistical information about predicates and classes used to derive new abstract relations. 
	 * @param classExamplesMap - a map from class labels to corresponding examples, this map is populated by the method.
     *
	 * @param propertyValueRangeMap - a mapping of a predicate names to ValueRange objects that 
	 * aggregate all possible values associated with this name (or property pairs if propertyPairs isn't null) 
	 * and class values if useClass is true. These values are later used to infer ranges, e.g. min-max that
	 * are a generalization over the original values. This data structure is populated by the method.
	 * 
	 * @param propertyPairs - a list of property names that stores pairs of predicate
	 * names, e.g. sensor-type, sensor-start-time, such that when the values of the second predicate
	 * are aggregated, they are associated also with the first predicate name, e.g. all possible start
	 * times for a sensor-type Kitchen.
	 * 
	 * @param useClass - specifies whether a class label should be considered when aggregating 
	 * values for predicate types, that will later be used to infer value ranges 
	 * (associated either just with predicate type or with both predicate type and class). 
	 * 
	 * @return total number of examples
	 */
	private int getAbstructRelationalRepresentation(String intputFile, 
			DataStats dataStats, Map<String, List<Example>> classExamplesMap, 
			List<String> propertyPairs, Map<String, ValueRange> propertyValueRangeMap, boolean useClass) 
	{
		Pattern pattern = Pattern.compile("\\n");	
		Scanner scan = null;
		try {
			scan = new Scanner(new File(intputFile));	
			scan.useDelimiter(pattern);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//maps property relation name associated with example 
		//onto a set of possible values 
		
		int numExamples = 0;
		while (scan.hasNext()){
			numExamples++;									
			String text = scan.next();								
			
			String itemDelimiter = ":";
			String[] relations = text.split(itemDelimiter);
			String classValue = relations[0];
			
			Example example = new Example(numExamples);
			example.setClassValue(classValue);
			dataStats.addClassLabel(classValue);
			
			for (String relation: relations) {
				Collection<Predicate> predicates = PredicateParser.parse(relation, propertyPairs);
				
				for (Predicate predicate: predicates) {
					//background knowledge should go here 
					updateValueRange(predicate, classValue, propertyValueRangeMap, useClass);
					example.addPredicate(predicate);
				}
			}
			//use set to save examples, and avoid redundancy
			String classLabel = example.getClassLabel();
			List<Example> examples = classExamplesMap.get(classLabel);
			if (examples == null) {
				examples = new ArrayList<Example>();
			}
			examples.add(example);
			classExamplesMap.put(classLabel,examples);			
		}	
		
		generateAbstructRelations(classExamplesMap, propertyValueRangeMap, dataStats);
		
		return numExamples;
	}
	
	
	/**
	 * Update a map of predicate -> possible values. This 
	 * map will aggregate all possible values for different predicate types.
	 * 
	 * @param predicate - simple property predicate object, with name and value
	 * @param classLabel - class label value
	 * @param propertyValueRangeMap - map of predicate names to their value collection objects (ValueRange)
	 * @param useClassLabel - specifies whether aggregated values for a predicate type should be also class dependent.
	 */
	private void updateValueRange(Predicate predicate, 
			String classLabel,
			Map<String, ValueRange> propertyValueRangeMap, boolean useClassLabel)
	{
		if (predicate instanceof ObjectPredicateParse) {

			for (Predicate pred : ((ObjectPredicateParse) 
					predicate).getPropertyPredicates()) {
				PropertyPredicateParse property = (PropertyPredicateParse)pred;
				updatePropertyValueRange(classLabel, 
						propertyValueRangeMap, property, useClassLabel);
			}
		} else if (predicate instanceof PropertyPredicateParse) {
			updatePropertyValueRange(classLabel, 
					propertyValueRangeMap,
					(PropertyPredicateParse) predicate, useClassLabel);
		}
	}

	/**
	 * Constructs a map of ValueRange objects for a given predicate.
	 * A ValueRange object accumulates all the possible values associated
	 * with a given predicate and later can be used to infer a range, e.g. min-max 
	 * for this predicate.
	 * 
	 * @param classLabel - class label in which this predicate occurred, thus the value ranges
	 * are not only per predicate, but also per class.
	 * 
	 * @param propertyValueRangeMap - the mapping from predicate name to a ValueRange object
	 * @param predicateObj - simple grounded property predicate, with a name and a value.
	 */
	private void updatePropertyValueRange(String classLabel,
			Map<String, ValueRange> propertyValueRangeMap, 
			PropertyPredicateParse predicateObj, boolean useClass) 
	{
		Value valueObj = new Value(predicateObj.getValue()); 
		
		String relationName = predicateObj.getAbstructName(); //TODO: this is important sometimes + classLabel;
		if (useClass) {
			relationName += "_" + classLabel;
		}
		ValueRange valueRange = propertyValueRangeMap.get(relationName);
		if (valueRange == null) {
			valueRange = new ValueRange();
		}
		valueRange.add(valueObj);
		propertyValueRangeMap.put(relationName, valueRange);
	}

	/**
	 * Adds new, abstract relations to the examples in classExamplesMap.
	 * 
	 * @param classExamplesMap - a map from class labels to corresponding examples. 
	 * @param propertyValueRangeMap - a mapping of a predicate names to ValueRange objects that 
	 * aggregate all possible values associated with this name (or property pairs if propertyPairs isn't null) 
	 * and class values if useClass is true. These values are later used to infer ranges, e.g. min-max that
	 * are a generalization over the original values. 

	 * @param dataStats - statistical properties of the data, e.g. number of total occurrences of relations, or
	 * number of occurrences of relations per class.
	 */
	public void generateAbstructRelations(
			Map<String, List<Example>> classExamplesMap,
			Map<String, ValueRange> propertyValueRangeMap, DataStats dataStats) {

		// add object count relations
		for (String classLabel: classExamplesMap.keySet()) {
			List<Example> examples = classExamplesMap.get(classLabel);

			for (Example example : examples) {
				example.addAbstructValues(propertyValueRangeMap);
				example.runStatistics();
				example.addAbstructPredicates(dataStats);
			}
		}
	}
	
	public static void main(String[] args) {
		String rootDir = "data";
		AbstractPredicateWriter brw = new AbstractPredicateWriter();	
		
		String inputChessFile = new File(rootDir,"chessExamples").getAbsolutePath();
		String outputChessFile = new File(rootDir,"chessSVM.txt").getAbsolutePath();
		String rulesChessFile = new File(rootDir,"chess-rules").getAbsolutePath();
		String chessMapFile = new File(rootDir, "classChessMap.txt").getAbsolutePath();
		brw.getFeatureRepresentationOfData(inputChessFile, outputChessFile,rulesChessFile, chessMapFile, false);
		//brw.saveAbstructRelations("chessExamplesShort", Constants.CHESS_ABSTRUCT_EXAMPLES_FILE+2, 
		//	/* will save only abstruct relations*/ false);
		
//		//wifi data
//		String intputFile = new File(rootDir,"wifiExamples-test-notransfer").getAbsolutePath();
//		String outputFile = new File(rootDir,"wifiExamples-test-notr-SVM").getAbsolutePath();
//		String rulesFile = new File(rootDir,"rules-target.txt").getAbsolutePath();
//		String wifiMapFile = new File(rootDir, "classWifiMap.txt").getAbsolutePath();
//		brw.convertSimpleRelationsFile(intputFile, outputFile, rulesFile, wifiMapFile);
	}

}
