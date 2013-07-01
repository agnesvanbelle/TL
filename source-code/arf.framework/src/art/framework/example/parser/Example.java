package art.framework.example.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import art.framework.example.generator.PropertyPredicateGenerate;
import art.framework.utils.AbstractPredicate;
import art.framework.utils.Constants;
import art.framework.utils.Utils;


/**
 * A a data instance consisting of a set of predicates and a class label.
 */
public class Example {
		private int id; 
		private String classLabel;
		private Set<AbstractPredicate> abstructRelations;
		private Map<String, Map<String,Integer>> propertyRelationCounts;
		private Map<String, Predicate> predicateMap;

		private Map<String, Integer> objectRelationCounts;
		private Set<ObjectPredicateParse> objectPredicates;
		private Set<ConnectPredicateParse> connectPredicates;
		private Set<PropertyPredicateParse> propertyPredicates;
		private String predictedLabel;

		
		public Example(int id, String classLabel, TreeSet<AbstractPredicate> relations) {
			this.id = id;
			this.classLabel = classLabel; 
			this.abstructRelations = relations;
		}
		
		public Example(int id) {
			this.id = id;
			abstructRelations = new TreeSet<AbstractPredicate>();
			propertyRelationCounts = new HashMap<String, Map<String,Integer>>();
			objectRelationCounts = new HashMap<String, Integer>();
			objectPredicates = new TreeSet<ObjectPredicateParse>();
			connectPredicates = new TreeSet<ConnectPredicateParse>();
			propertyPredicates = new TreeSet<PropertyPredicateParse>();
			predicateMap = new HashMap<String, Predicate>();

		}

		public int getId() {
			return id;
		}
		
		public String getClassLabel() {
			return classLabel;
		}

		public List<String> getAbstractRelationsStr() {
			List<String> relations = new ArrayList<String>();
			
			for (AbstractPredicate rel: abstructRelations) {
				relations.add(rel.toString());
			}
			return relations;
		}
		
		public List<String> getAllRelationsStr() {
			List<String> relations = new ArrayList<String>();
			
			for (AbstractPredicate rel: abstructRelations) {
				relations.add(rel.toString());
			}
			for (Predicate rel: propertyPredicates) {
				relations.add(rel.toString());
			}
			for (Predicate rel: objectPredicates) {
				relations.add(rel.toString());
			}
			for (Predicate rel: connectPredicates) {
				relations.add(rel.toString());
			}
			return relations;
		}

		public void addAbstructRelation(AbstractPredicate relation) {
			abstructRelations.add(relation);
		}
		
		public void setClassValue(String classLabel) {
			this.classLabel = classLabel;
		}

		public void updateObjectRelationsCounts(String relationName) {
				Integer cnt = objectRelationCounts.get(relationName);
				if (cnt == null) {
					cnt = 0;
				}
				objectRelationCounts.put(relationName, ++cnt);
		}
		
		public Map<String, Integer> getObjectRelationCounts() {
			return objectRelationCounts;
		}
		
		public Map<String, Map<String, Integer>> getPropertyRelationsCounts() {
			return propertyRelationCounts;
		}
		
		public void addHasRelation(Predicate predicate, 
				String relationName, boolean single, DataStats dataStats) {

			Map<String, Integer> relationCntMap = propertyRelationCounts.get(relationName);

			if (relationCntMap == null) {
				return;
			}
			
			for (String relationValue: relationCntMap.keySet()) {
				if (relationValue.equals(Constants.TOTAL)) {
					continue; //skip total value count 
				}
				Integer noOfOccurance = relationCntMap.get(relationValue);
				
				String relationName_ = extractVariableName(relationName);
				if (single && 
					noOfOccurance != null 
					&& noOfOccurance > 0) {
					
					AbstractPredicate hasRelation = new QualifierPredicate(relationValue, relationName_, predicate, "exists");
					abstructRelations.add(hasRelation);
					dataStats.addRelation(hasRelation.toString(), classLabel);
				} else { //specify the number of times a value occurs with a given relation
					for (int i = 1; i < noOfOccurance+1; i++) {
						AbstractPredicate hasRelation = new QualifierPredicate(relationValue, relationName_, i, predicate, "exists");
						abstructRelations.add(hasRelation);
						dataStats.addRelation(hasRelation.toString(), classLabel);
					}
				}
			}
		}
		
		public boolean addAllRelation(String relationName, Predicate predicate, DataStats dataStats) {
			Map<String, Integer> relationCntMap = propertyRelationCounts.get(relationName);
			if (relationCntMap == null) {
				return false;
			}
			
			boolean addedRelation = false;
			int totalValueCount = relationCntMap.get(Constants.TOTAL);
			for (String relationValue: relationCntMap.keySet()) {
				if (relationValue.equals(Constants.TOTAL)) {
					continue;
				}
				Integer noOfOccurance = relationCntMap.get(relationValue);
				if (noOfOccurance != null && noOfOccurance == totalValueCount
						&& totalValueCount > 1) {
					//all relations of a given type have the same value
					String relationName_ = extractVariableName(relationName);
					AbstractPredicate allRelation = new QualifierPredicate(relationValue, relationName_, predicate, "for_all");
					abstructRelations.add(allRelation);
					dataStats.addRelation(allRelation.toString(), classLabel);
					addedRelation = true;
				} 
			}
			return addedRelation;
		}

		private String extractVariableName(String relationName) {
			String relationName_ = relationName;
			if (relationName_.contains("_")) {
				relationName_ = relationName_.substring(0, relationName_.indexOf("_"));
			} else {
				relationName_ = relationName_.substring(0, relationName.length()-1);
			}
			return relationName_;
		}
		
		
		 //TODO: refine this with more added relations 
		public void addAbstructValues(
				Map<String, ValueRange> examplePropertyRelValueMap) 
		{
			for (ObjectPredicateParse pred: objectPredicates) {
				for (PropertyPredicateParse property: pred.getPropertyPredicates()) {
					abstructAwayIntegerValues(examplePropertyRelValueMap, property);
				}
			}
			for (PropertyPredicateParse property: propertyPredicates) {					
					abstructAwayIntegerValues(examplePropertyRelValueMap, property);
			}
			
			for (ConnectPredicateParse pred: connectPredicates) {
				List<ObjectPredicateParse> objects = pred.getObjects();
				for (ObjectPredicateParse objPred: objects) {
					for (PropertyPredicateParse property: objPred.getPropertyPredicates()) {
						abstructAwayIntegerValues(examplePropertyRelValueMap, property);
					}
				}
			}
		}

		private void abstructAwayIntegerValues(
				Map<String, ValueRange> examplePropertyRelValueMap,
				PropertyPredicateParse property) {
			
			String relationName = property.getAbstructName();
			ValueRange valueRange = examplePropertyRelValueMap.get(relationName); //+classLabel);
			
			if (valueRange == null) {
				return;
			}

			if (valueRange.isTime()) {
				property.addAbstructValue(valueRange.getTime());				
			} else if (valueRange.isInteger()) {
				Integer value = Integer.parseInt(property.getValue());
				if (valueRange.hasNegatives()) {
					if (value > 0) {
						property.addAbstructValue("gt(0)");
					} else if (value < 0) {
						property.addAbstructValue("lt(0)");
					} 
				}
				int max = valueRange.getIntMax();
				int min = valueRange.getIntMin();
				
				if (min < max) { //if values differ
					property.addAbstructValue(min+"-"+max);
//					if (value >= min) {
//						property.addAbstructValue("gte(" + Integer.toString(min) + ")");
//					} else if (value <= max) {
//						property.addAbstructValue("lte(" + Integer.toString((value+1))+ ")");
//					}					
				}
			}
		}

		
		
	public void addQuantifiedRelation(String relationName, DataStats dataStats) {
		Predicate predicate = predicateMap.get(relationName);
		if (predicate.getVars() == null
				&& predicate instanceof PropertyPredicateParse) {
			AbstractPredicate propertyRelation = new PropertyPredicateGenerate(
					((PropertyPredicateParse) predicate).getValue(),
					predicate.getName());
			abstructRelations.add(propertyRelation);
			dataStats.addRelation(propertyRelation.toString(), classLabel);

			String abstructValue = ((PropertyPredicateParse) predicate).getAbstructValue();
			if (abstructValue != null) {
				AbstractPredicate propertyRelation_abstruct = new PropertyPredicateGenerate(
						abstructValue, predicate.getName());
				abstructRelations.add(propertyRelation_abstruct);
				dataStats.addRelation(propertyRelation_abstruct.toString(),
						classLabel);
			}

			return;
		}
		if (!addAllRelation(relationName, predicate, dataStats)) {
			// has relation is only relevant if all relation wasn't added
			addHasRelation(predicate, relationName, true, dataStats);
		}
	}

		public void addPredicate(Predicate predicate) {	
			//example.updatePropertyRelationsCounts(relName, relContent);
			if (predicate instanceof ObjectPredicateParse) {
				objectPredicates.add((ObjectPredicateParse) predicate);
			} else if (predicate instanceof PropertyPredicateParse) {
				propertyPredicates.add((PropertyPredicateParse) predicate);
			} else if (predicate instanceof ConnectPredicateParse) {
				connectPredicates.add((ConnectPredicateParse) predicate);
			}
		}

		public Set<ObjectPredicateParse> getObjectPredicates() {
			return objectPredicates;
		}
		
		public Set<PropertyPredicateParse> getPropertyPredicates() {
			return propertyPredicates;
		}
		
		public Set<ConnectPredicateParse> getConnectPredicates() {
			return connectPredicates;
		}
		
		public void updatePropertyRelationsCounts(String relationName, 
				String value, Predicate predicate) 
		{
			Map<String, Integer> relationCntMap = propertyRelationCounts.get(relationName);
			if (relationCntMap == null) {
				relationCntMap = new HashMap<String, Integer>();
			}
			Integer cnt1 = relationCntMap.get(Constants.TOTAL);
			if (cnt1 == null) {
				cnt1 = 0;
			}
			relationCntMap.put(Constants.TOTAL, ++cnt1);
			
			//increment relation/value count
			Integer cnt2 = relationCntMap.get(value);
			if (cnt2 == null) {
				cnt2 = 0;
			}
			relationCntMap.put(value, ++cnt2);
			propertyRelationCounts.put(relationName, relationCntMap);
			predicateMap.put(relationName, predicate);
		}

	public void runStatistics() {
		for (ObjectPredicateParse predicate : objectPredicates) {
			String objectPredicateName = predicate.getName();
			
			String relationName = objectPredicateName + "_" + classLabel;
			updatePropertyRelationsCounts(relationName, predicate.getValue(), predicate);
			
			String abstructValue = predicate.getAbstructValue();
			if (abstructValue != null) {
				String relationName2 = objectPredicateName + "_a_" + classLabel;
				updatePropertyRelationsCounts(relationName2, abstructValue, predicate);
			}
			
			for (PropertyPredicateParse property : predicate.getPropertyPredicates()) {	
				String relationName2 = property.getLongName() + "_" +classLabel;
				updatePropertyRelationsCounts(relationName2, property.getValue(), property);
				abstructValue = property.getAbstructValue();
				if (abstructValue != null) {
					String relationName3 = property.getLongName() + "_a_" + classLabel;
					updatePropertyRelationsCounts(relationName3, abstructValue, property);
				}
			}
		}

		for (PropertyPredicateParse property : propertyPredicates) {
			updatePropertyRelationsCounts(property.getName() + "_" + 
					classLabel, property.getValue(), property);
			String abstructValue = property.getAbstructValue();
			if (abstructValue != null) {
				String relationName = property.getName() + "_a_" + classLabel;
				updatePropertyRelationsCounts(relationName, abstructValue, property);
			}
		}

		for (ConnectPredicateParse predicate : connectPredicates) {
			List<String> values = predicate.getPossibleValues(false);
			String relationName = predicate.getName();
			for (String value : values) {
				String valueSignature = Utils.getValueSignature(value); 
				updatePropertyRelationsCounts(relationName + "_" + 
						valueSignature  + "_" + classLabel, value, predicate);
			}
			values = predicate.getPossibleValues(true);
			for (String value : values) {
				String valueSignature = Utils.getValueSignature(value); 
				updatePropertyRelationsCounts(relationName + "_" + 
						valueSignature  + "_" + classLabel, value, predicate);
			}
		}
	}

		public void addAbstructPredicates(DataStats dataStats) {
			for (String relation: propertyRelationCounts.keySet()) {
				addQuantifiedRelation(relation, dataStats);
			}
		}

		public void setPredictedLabel(String predictedLabel) {
			this.predictedLabel = predictedLabel;
		}

		public String getPredictedLabel() {
			return predictedLabel;
		}

	}
	

