package art.framework.example.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import art.framework.utils.Constants;

/**
 * Aggregates possible values of a certain type that occur in data, e.g. 
 * for certain object predicates and class values.
 * Allows then to get statistical properties of data, e.g. min, max values.
 */
public class ValueRange {
		Set<Value> values;
		Map<Integer,List<String>> intervalMap;
		
		boolean hasIntegerType = false;
		boolean hasRealType = false;
		boolean hasStringType = false;
		boolean uniform = true;
		Integer minValue = null;
		Integer maxValue = null;
		
		boolean hasNegatives = false;
		private boolean hasRangeType;
		private boolean hasTimeType = false;
		
		String time;
		
		public ValueRange() {
			values = new HashSet<Value>();
			intervalMap = new HashMap<Integer, List<String>>();
		}

		public String getTime() {
			return time;
		}

		
	public void add(Value valueObj) {
		values.add(valueObj);

		Constants.Type type = valueObj.getType();
		String value = valueObj.getValue();

		if (type.equals(Constants.Type.INT)) {
			hasIntegerType = true;
			int intValue = Integer.parseInt(value);
			if (intValue < 0) { // negative
				hasNegatives = true;
			}
			if (minValue == null || intValue < minValue) {
				minValue = intValue;
			}
			if (maxValue == null || intValue > maxValue) {
				maxValue = intValue;
			}

		} else if (type.equals(Constants.Type.REAL)) {
			hasRealType = true;
			double doubleValue = Double.parseDouble(value);
			if (doubleValue < 0) { // negative
				hasNegatives = true;
			}
		} else if (type.equals(Constants.Type.TIME)) { 
			time = getRangeCluster(value);
			hasTimeType = true;	
		} else if (type.equals(Constants.Type.STR)) {
			hasStringType = true;
		}

		if (hasTimeType || hasIntegerType && hasRealType || hasIntegerType && hasStringType
				|| hasRealType && hasStringType) {
			uniform = false;
		}
	}
	
	private String getRangeCluster(String value) {
		int intValue = Integer.parseInt(value);
		return Integer.toString((int)Math.floor(intValue * 1.0 / 72));
	}

		public int getIntMin() {
			return minValue;
		}
		
		public int getIntMax() {
			return maxValue;
		}
		
		public boolean hasNegatives() {
			return hasNegatives;
		}
		
		public boolean isInteger() {
			return hasIntegerType && uniform;
		}
		
		public boolean isString() {
			return hasStringType || !uniform;
		}
		
		public boolean isReal() {
			return hasRealType && uniform;
		}

		public boolean isRange() {
			return hasRangeType;
		}
		

		public boolean isTime() {
			return hasTimeType;
		}

	public void printIntervalValues() {
		for (int duration : intervalMap.keySet()) {
			List<String> intervals = intervalMap.get(duration);
			System.out.println("duration: " + duration + " " + intervals);
		}
	}

	
}
