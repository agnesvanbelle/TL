package art.experiments.wifi.data.processor;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import art.framework.example.generator.PredicateWriter;
import art.framework.example.parser.AbstractPredicateWriter;
import art.framework.example.parser.ValueRange;
import art.framework.utils.Constants;
import art.framework.utils.Utils;

public class WifiAligner {
	
	private static final int PERCENTILE2 = 70;
	private static final int PERCENTILE1 = 30;
	private static final int PERCENTILE = 50;
	private static final boolean USE_CLASS = true;
	private static int INST_ID = 0;
	
	public static void main(String[] args) {
		String baseDir = "houseInfoA";
		String rootDir = baseDir + "/split04-05-07-08-19-28";
		String sensorFile = new File(rootDir,"houseA-ss-train.txt").getAbsolutePath();
		String actionFile = new File(rootDir,"houseA-as-train.txt").getAbsolutePath();
		String sensorMapFile = new File(baseDir,"sensorMapA-ids.txt").getAbsolutePath();
		String actionMapFile = new File(baseDir,"actionMapA.txt").getAbsolutePath();
		
		String outputFile = new File(rootDir, Constants.WIFI_EXAMPLES_FILE + "-train").getAbsolutePath();		
		String outputAbstructFile = new File(rootDir, 
				Constants.WIFI_ABSTRUCT_EXAMPLES_FILE + "-train").getAbsolutePath();
		String outputMapFile = new File(rootDir, Constants.WIFI_CLASS_MAP_FILE + "-train").getAbsolutePath();
		
		Map<String, List<EventInfo>>  consecutiveIntervals = new TreeMap<String, List<EventInfo>>();
		Map<String, Sensor> sensorModels = new HashMap<String, Sensor>();
		getAlignedSensorData(sensorFile, actionFile, sensorMapFile,
				 actionMapFile, consecutiveIntervals, sensorModels);
		
		getPredicates(consecutiveIntervals, sensorModels, outputFile, true);
		saveAbstructRelations(outputFile, outputAbstructFile, outputMapFile, false, null);		
	}


	/**
	 * Fills in consecutiveIntervals and sensorModels maps
	 * also discretizes/merges the time intervals
	 * 
	 * Aligns times of sensor firings and activities and constructs 
	 * events (objects that aggregate information about time, sensor firings, and activity that took place).  
	 * It stores events in a map, that maps a unique id (constructed from date, action description, and instance id)
	 * to a list of events that happened on this date.
	 * Also constructs a mapping from sensor id to a sensor model - that includes a sensor type 
	 * and firing/action events that happened for this sensor type.
	 * 
	 * @param sensorFile - file with sensor firings information
	 * @param actionFile - file with action information
	 * @param sensorMapFile - mapping of sensor ids and descriptions
	 * @param actionMapFile - mapping of action ids and descriptions
	 * @param consecutiveIntervals - a map of consecutive events 
	 * @param sensorModelMap - a map of sensor types to sensor models
	 */
	public static void getAlignedSensorData(String sensorFile,
			String actionFile, String sensorMapFile, String actionMapFile,  
			Map<String, List<EventInfo>> consecutiveIntervals, 
			Map<String, Sensor> sensorModelMap) {

		Map<String, Map<String, EventInfo>> actionSensorInfoMap = 
							new HashMap<String, Map<String,EventInfo>>();

		List<String> sensorReadings = WifiUtils.getLines(sensorFile);
		List<String> actionReadings = WifiUtils.getLines(actionFile);
		List<String> sensorMapInfo = WifiUtils.getLines(sensorMapFile);
		List<String> actionMapInfo = WifiUtils.getLines(actionMapFile);

		Map<String, String> sensorMap = getReadingsMap(sensorMapInfo, 2);
		Map<String,String> actionMap = getReadingsMap(actionMapInfo, 1);
		
		// sensorEventMap
		Map<String, List<TimeInterval>> sensorEventMap = new TreeMap<String, List<TimeInterval>>();
		List<String> dates = new ArrayList<String>();
		
		// NOTE: getReadings discretizes the time( interval)s
		getReadings(sensorReadings, sensorMap, sensorEventMap, dates, true /*merge consec intervals*/);		
		//saveReadings(timeMap, dates, new File(rootDir,"sensorOut.txt").getAbsolutePath());
		
		// actionEventMap
		Map<String, List<TimeInterval>> actionEventMap = new TreeMap<String, List<TimeInterval>>();
		List<String> actionDates = new ArrayList<String>();
		
		getReadings(actionReadings, actionMap, actionEventMap, actionDates, true/*merge consec intervals*/);
		//saveReadings(timeActionMap, actionDates, new File(rootDir,"actionOut.txt").getAbsolutePath());
		
		for (String date: actionDates) {
			List<TimeInterval> actionTimes = actionEventMap.get(date);
			List<TimeInterval> sensorTimes = sensorEventMap.get(date);
			
			if (sensorTimes == null) {
				System.out.println("No sensors match to activity on date " + date);
			} else {
				int actionId = 0;
				
				
				for (TimeInterval actionI: actionTimes) {
					actionId++;
					String id = date + "-" + actionI.getValue() + "-" + (++INST_ID);
					List<EventInfo> consecIntervals = consecutiveIntervals.get(id);
					if (consecIntervals == null) {
						consecIntervals = new ArrayList<EventInfo>();
					}
					 
					Map<String, EventInfo> sensorInfoMap = 
						actionSensorInfoMap.get(date + "-" + actionId);
					if (sensorInfoMap == null) {
						sensorInfoMap = new HashMap<String, EventInfo>();
					}
					String actionValue = actionI.getValue();

					EventInfo prevEvent = null;
					for (TimeInterval sensorI: sensorTimes) {
						String sensorValue = sensorI.getValue();
					
						if (isWithinRange(actionI, sensorI)) {
							//add to sensor type 
							EventInfo event = sensorInfoMap.get(sensorValue);
							if (event == null) {
								event = new EventInfo(sensorValue, actionValue, 
										actionI.getDuration(), actionI.getStartTime(), 
										actionI.getEndTime());
							} 
							event.addDuration(sensorI.getDuration());
							event.addStart(sensorI.getStartTime());
							event.addEnd(sensorI.getEndTime());
							
							if (prevEvent != null && !prevEvent.getType().equals(event.getType())) {
								consecIntervals.add(prevEvent);
							}
							prevEvent = event;
							
							sensorInfoMap.put(sensorValue, event);
							actionSensorInfoMap.put(date + "-" + actionId, sensorInfoMap);
						} 
					}
					if (prevEvent != null) {
						consecIntervals.add(prevEvent);
					}
					if (!consecIntervals.isEmpty()) {
						consecutiveIntervals.put(id, consecIntervals);
					}
				}
			}
		}
		
		//build sensor model
		for (String id: actionSensorInfoMap.keySet()) {
			Map<String, EventInfo> info = actionSensorInfoMap.get(id);
			for (String sensor: info.keySet()) {
				Sensor s = sensorModelMap.get(sensor);
				if (s == null) {
					s = new Sensor(sensor);
				}
				EventInfo eventInfo = info.get(sensor);
				s.add(eventInfo);
				sensorModelMap.put(sensor, s);
			}
		}
		
//		for (String sensor: sensorModelMap.keySet()) {
//			Sensor s = sensorModelMap.get(sensor);
//			//s.printCountInfo();
//			System.out.println(sensor);
//			s.printActionInfo(true);
//
//		}
		
	}
	
	public static void saveAbstructRelations(String outputFile,
			String outputAbstructFile, String outputMapFile, 
			boolean withRanges, Map<String, ValueRange> propertyValueRangeMap) {
		
		Utils.deleteFile(outputAbstructFile);
		Utils.deleteFile(outputMapFile);
		
		AbstractPredicateWriter apw = new AbstractPredicateWriter();	
		List<String> propertyPairs = new ArrayList<String>();
		if (withRanges) {
			propertyPairs.add(Constants.SENSOR_TYPE);
			propertyPairs.add(Constants.SENSOR_DURATION);

			propertyPairs.add(Constants.SENSOR_TYPE);
			propertyPairs.add(Constants.SENSOR_NO_FIRINGS);

			propertyPairs.add(Constants.SENSOR_TYPE);
			propertyPairs.add(Constants.SENSOR_START);

			propertyPairs.add(Constants.SENSOR_TYPE);
			propertyPairs.add(Constants.SENSOR_END);
		}
		
	    apw.saveAbstructRelations(outputFile, outputAbstructFile,
				outputMapFile, false, true, propertyPairs, propertyValueRangeMap, USE_CLASS);
	}

	
	/**
	 * Constructs examples made of basic predicates constructed from sensor/action information
	 * 
	 * @param consecutiveIntervals - 
	 * @param sensorModels - 
	 * @param outputFile - file to which new instances are stored
	 * @param deletePrevFile - if true, removes previous output file
	 */
	public static void getPredicates(
			Map<String, List<EventInfo>> consecutiveIntervals,
			Map<String, Sensor> sensorModels, String outputFile, 
			boolean deletePrevFile) {
		
		if (deletePrevFile) {		
			Utils.deleteFile(outputFile);
		}
		
		PredicateWriter mew = new PredicateWriter();
		
		for (String instanceId: consecutiveIntervals.keySet()) {	
			List<EventInfo> sensors = consecutiveIntervals.get(instanceId);
			mew.startExample();
			String actionLabel = sensors.get(0).getActionType();
			
			for (int i = 0; i < sensors.size(); i++) {
				EventInfo sensorI = sensors.get(i);
				
				List<EventInfo> sensorCandidates;
				sensorCandidates = getSensorCandidates(sensorI, sensorModels);
				
				for (EventInfo sensor: sensorCandidates) {	
					String startMedians = sensor.getSensorStartsRange();
					String endMedians = sensor.getSensorEndsRange();
					String durationMedians = sensor.getSensorDurationsRange();
					String firingsMedians = sensor.getSensorNoFiringsRange();
					String actionDurations = sensor.getActionDurationsRange();
					
					TreeMap<String, String> sens_relations = new TreeMap<String, String>();
					sens_relations.put(Constants.SENSOR_START,startMedians);
					sens_relations.put(Constants.SENSOR_END,endMedians);
					sens_relations.put(Constants.SENSOR_DURATION,durationMedians);
					sens_relations.put(Constants.SENSOR_NO_FIRINGS,firingsMedians);
					sens_relations.put(Constants.ACT_DURATION, actionDurations);
					sens_relations.put(Constants.SENSOR_TYPE, sensor.getType());
					
					mew.addObjectPredicate(Constants.SENSOR, sens_relations);
				}
		}
			mew.saveExamples(actionLabel, outputFile, true);

		}
	}

	
	private static List<EventInfo> getSensorCandidates(EventInfo sensorI, 
			Map<String, Sensor> sensorModels) {
		List<EventInfo> candidates = new ArrayList<EventInfo>();

		List<Integer> startTimes =  sensorI.getStarts();
		List<Integer> endTimes = sensorI.getEnds();
		List<Integer> durations = sensorI.getDurations();
		List<Integer> noFirings = new ArrayList<Integer>();
		noFirings.add(sensorI.getNoFirings());

		Sensor sensor = sensorModels.get(sensorI.getType());
		if (sensor == null) {
			return candidates;
		}
		Map<String, ActionInfo> actions = sensor.getActions();

		Map<ActionInfo, Double> weightMap = new HashMap<ActionInfo, Double>();
		Map<ActionInfo, Double> freqMap = new HashMap<ActionInfo, Double>();

		double maxWeight = 0;
		
		double avgFrequency = 0.0;
		for (ActionInfo action : actions.values()) {
			if (action == null) {
				continue;
			}
			
			List<Integer> sensorStarts = action.getSensorStarts();
			double frequency = sensorStarts.size();
			avgFrequency += frequency;
			freqMap.put(action, frequency);
			
			double startWeight = findRange(sensorStarts, startTimes, PERCENTILE); 
			List<Integer> sensorEnds = action.getSensorEnds();
			double endWeight = findRange(sensorEnds, endTimes, PERCENTILE);

			List<Integer> sensorDurations = action.getSensorDurations();
			double durationsWeight = findRange(sensorDurations, durations, PERCENTILE);

			List<Integer> sensorFirings = action.getNoFirings();
			double firingsWeight = findRange(sensorFirings, noFirings, PERCENTILE);

			double avgWeight = (startWeight + endWeight + durationsWeight + firingsWeight) / 4;
			weightMap.put(action, avgWeight);
			if (maxWeight < avgWeight) {
				maxWeight = avgWeight;
			}
		}
		avgFrequency = avgFrequency / actions.size();
		
		HashMap<ActionInfo, Double> actionCandidates = new HashMap<ActionInfo, Double>();
		double maxSavedFreq = 0;
		//select candidates
		for (ActionInfo ac: freqMap.keySet()) {
			double freq = freqMap.get(ac);
			double weight = weightMap.get(ac);
			
			if (weight == maxWeight) { 
				actionCandidates.put(ac, freq);
				if (maxSavedFreq < freq) {
					maxSavedFreq = freq;
				}
			}
		}
			for (ActionInfo a: actionCandidates.keySet()) {
				double freqa = actionCandidates.get(a);
				
				if (freqa >= avgFrequency || freqa == maxSavedFreq) {
				
					EventInfo sensorCopy = new EventInfo(sensorI.getType(), sensorI.getActionType(), 
						sensorI.getActionDuration(),
						sensorI.getActionStart(), 
						sensorI.getActionEnd());
				
					sensorCopy.setStartTimeRange(a.getSensorStartsPercentile(PERCENTILE1, true) 
								+ "-" + a.getSensorStartsPercentile(PERCENTILE2, true));
					sensorCopy.setEndTimeRange(a.getSensorEndsPercentile(PERCENTILE1, true) 
								+ "-" +a.getSensorEndsPercentile(PERCENTILE2, true));
					sensorCopy.setDurationsRange(a.getSensorDurationPercentile(PERCENTILE1, true)
								+ "-" + a.getSensorDurationPercentile(PERCENTILE2, true));
					sensorCopy.setNoFiringsRange(a.getNoFiringsPercentile(PERCENTILE1, true) 
								+ "-" +a.getNoFiringsPercentile(PERCENTILE2, true));
					sensorCopy.setActionDurationRange(a.getActionDurationMedian(PERCENTILE1, true) 
								+ "-" +a.getActionDurationMedian(PERCENTILE2, true));
					
					candidates.add(sensorCopy);
			}
		}
	
		return candidates;
	}
	
	
	private static double findRange(List<Integer> list1, 
			List<Integer> list2, int percentile) {
		// noCandidates that fall within 25th and 70th percentiles
		Collections.sort(list1);
		
		double averageWeight = 0;
		for (int value : list2) {
			int below = 0;
			int equal = 0;
			for (int v : list1) {
				if (v < value) {
					below++;
				} else if (v == value) {
					equal++;
				}
			}
			double valuePercentile = ((1.0 * below + 0.5 * equal) / list1.size()) * 100;
			double weight = 1 - 1.0 * Math.abs(percentile - valuePercentile) / percentile;
			averageWeight += weight;
		}
		return (1.0* averageWeight / list2.size());
	}


	private static boolean isWithinRange(TimeInterval action,
			TimeInterval sensor) {
		return sensor.getStartTime() == action.getStartTime()
				|| (sensor.getStartTime() > action.getStartTime()
						&& sensor.getStartTime() <= action.getEndTime());
	}

	private static TimeInterval getInterval(String sensorReading, 
			Map<String, String> sensorMap) 
	{
		String[] sensorInfo = sensorReading.split("\\s+");	
		Calendar startDate = getDate(sensorInfo[0]);
		Calendar endDate = getDate(sensorInfo[2]);
		
		int startTime = getTimeInMins(sensorInfo[1]);
		int endTime = getTimeInMins(sensorInfo[3]);
		String sensorId = sensorMap.get(sensorInfo[4]);
		if (sensorId == null) {
			return null;
		}
		
		return new TimeInterval(startTime, 
				endTime, startDate, endDate, sensorId);
	}


	private static void sortTimeIntervalsByDate(
			Map<String, List<TimeInterval>> timeMap,
			List<TimeInterval> timeIntervals) {
		//sort time intervals by date
		for (TimeInterval interval: timeIntervals) {
			String date = interval.getDate();
			List<TimeInterval> intervals = timeMap.get(date);
			if (intervals == null) {
				intervals = new ArrayList<TimeInterval>();
			}
			if (!intervals.contains(interval)) {
				intervals.add(interval);
			}
			timeMap.put(date, intervals);
		}
	}

	
	/**
	 * For each string in a mapInfo list, splits on comma
	 * and creates a map from left element to the right.
	 * 
	 * @param list - a list of strings, containing comma separated mappings
	 * @param index - a string can contain 2 or more elements separated by commas, 
	 * the mapping is done from the first element to the element specified by the index.
	 *  
	 * @return a map of strings
	 */
	private static Map<String, String> getReadingsMap(List<String> list, int index) {
		Map<String,String> sensorMap = new HashMap<String, String>();
		
		for (String reading: list) {
			String[] info = reading.split(",");
			sensorMap.put(info[0].trim(), info[index].trim());
		}
		
		return sensorMap;
	}


	public static Calendar getDate(String date_) {
		try {
			DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			Date date = (Date) formatter.parse(date_);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			//System.out.println("the date is " + cal.getTime());
			return cal;
		} catch (ParseException e) {
			System.out.println("[ERROR]: Failed to parse date " + date_);
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	/**
	 * discretizes (merges) the raw time data
	 * 
	 * @param sensorReadings
	 * @param sensorMap
	 * @param timeMap
	 * @param dates
	 * @param doMerge
	 */
	private static void getReadings(List<String> sensorReadings,
			Map<String, String> sensorMap, Map<String, List<TimeInterval>> timeMap,
			List<String> dates, boolean doMerge) {
		
		List<TimeInterval> timeIntervals = new ArrayList<TimeInterval>();

		for (String sensorReading: sensorReadings) {
			TimeInterval interval = getInterval(sensorReading, sensorMap);
			if (interval == null) {
				continue;
			}
			String date = interval.getDate();
			if (!dates.contains(date)) {
				dates.add(date);
			}
			
			boolean updated = false;

			if (doMerge) {
				for (int i = 0; i < timeIntervals.size(); i++) {
					TimeInterval nextInterval = timeIntervals.get(i);

					if (interval.getValue().equals(nextInterval.getValue())) {
						if (isMeargableEnd(interval, nextInterval)) {
							nextInterval.setEndDate(interval.getEndDate());
							nextInterval.setEndTime(interval.getEndTime());
							nextInterval.incrementFirings();
							updated = true;
							break;
						} else if (isMeargeableStart(interval, nextInterval)) {
							nextInterval.setStartDate(interval.getStartDate());
							nextInterval.setStartTime(interval.getStartTime());
							nextInterval.incrementFirings();
							updated = true;
							break;
						}
					}
				}
			}
			
			if (!updated && !timeIntervals.contains(interval)) {
				timeIntervals.add(interval);
			}
		}
		sortTimeIntervalsByDate(timeMap, timeIntervals);
	}


	private static boolean isMeargeableStart(TimeInterval interval,
			TimeInterval nextInterval) {
		return (interval.getEndTime() == nextInterval.getStartTime() 
				|| interval.getEndTime() + 1 == nextInterval.getStartTime())
				&& interval.getEndDate().compareTo(nextInterval.getStartDate()) == 0
				&& interval.getStartDate().compareTo(nextInterval.getStartDate()) == 0
				&& interval.getEndDate().compareTo(nextInterval.getEndDate()) == 0
				&& interval.getStartTime() <= nextInterval.getStartTime();
	}


	private static boolean isMeargableEnd(TimeInterval interval,
			TimeInterval nextInterval) {
		return (nextInterval.getEndTime() == interval.getStartTime() 
				|| nextInterval.getEndTime() + 1 == interval.getStartTime())
				&& nextInterval.getEndDate().compareTo(interval.getStartDate()) == 0 
						&& (nextInterval.getStartDate().compareTo(interval.getStartDate()) == 0
						&& nextInterval.getEndDate().compareTo(interval.getEndDate()) == 0 
						&& nextInterval.getStartTime() <= interval.getStartTime());
	}
	


	private static int getTimeInMins(String time) {
		String[] timeInfo = time.split(":");
		int hours = Integer.parseInt(timeInfo[0]);
		int mins = Integer.parseInt(timeInfo[1]);
		int secs = Integer.parseInt(timeInfo[2]);
		
		int secsTime = hours * 3600 + mins * 60 + secs;
		int minsTime = (int) Math.round(1.0*secsTime/60);
		
		return minsTime;
	}
	

	
	public static void getRelationsNoModel(String sensorFile,
			String actionFile, String sensorMapFile, String actionMapFile, 
			String outputFile, boolean deletePrevFile, String outputMapTargetFile) {

		if (deletePrevFile) {		
			Utils.deleteFile(outputFile);
		}
		
		PredicateWriter mew = new PredicateWriter();
		
		List<String> sensorReadings = WifiUtils.getLines(sensorFile);
		List<String> actionReadings = WifiUtils.getLines(actionFile);
		List<String> sensorMapInfo = WifiUtils.getLines(sensorMapFile);
		List<String> actionMapInfo = WifiUtils.getLines(actionMapFile);

		Map<String, String> sensorMap = getReadingsMap(sensorMapInfo, 2);
		Map<String,String> actionMap = getReadingsMap(actionMapInfo, 1);
			
		Map<String, List<TimeInterval>> timeMap = new TreeMap<String, List<TimeInterval>>();
		List<String> dates = new ArrayList<String>();
		getReadings(sensorReadings, sensorMap, timeMap, dates, true /*merge consec intervals*/);
		
		Map<String, List<TimeInterval>> timeActionMap = new TreeMap<String, List<TimeInterval>>();
		List<String> actionDates = new ArrayList<String>();
		getReadings(actionReadings, actionMap, timeActionMap, actionDates, true/*merge consec intervals*/);

		Map<String, Integer> classCounts = new HashMap<String, Integer>();
		for (String date: actionDates) {
			List<TimeInterval> actionD = timeActionMap.get(date);
			List<TimeInterval> sensorD = timeMap.get(date);
			if (sensorD == null) {
				System.out.println("No sensors match to activity on date " + date);
			} else {
				
				for (TimeInterval actionI: actionD) {
					String actionValue = actionI.getValue();
					Integer cnt = classCounts.get(actionValue);
					if (cnt == null) {
						cnt = 0;
					}
					cnt++;
					classCounts.put(actionValue, cnt);
					for (TimeInterval sensorI: sensorD) {
					
						if (isWithinRange(actionI, sensorI)) {
							
							//add to sensor type 
							TreeMap<String, String> sens_relations = new TreeMap<String, String>();
							sens_relations.put(Constants.SENSOR_START,""+sensorI.getStartTime());
							sens_relations.put(Constants.SENSOR_END,""+sensorI.getEndTime());
							sens_relations.put(Constants.SENSOR_DURATION,""+sensorI.getDuration());
							sens_relations.put(Constants.SENSOR_NO_FIRINGS,""+sensorI.getNoFirings());
							sens_relations.put(Constants.ACT_DURATION, ""+actionI.getDuration());
							sens_relations.put(Constants.SENSOR_TYPE, sensorI.getValue());
							
							mew.addObjectPredicate(Constants.SENSOR, sens_relations);
						} 
					}
					mew.saveExamples(actionValue, outputFile, true);
				}
			}
		}
		StringBuffer sb = new StringBuffer();
		for (String classValue: classCounts.keySet()) {
			Integer cnt = classCounts.get(classValue);
			sb.append(classValue).append(":").append(cnt).append(",");
		}
		sb.append(outputFile);
		Utils.saveContent(outputMapTargetFile, sb.toString());
	}

	/*
	private static Map<TimeInterval, List<TimeInterval>> getFollowedBySensors(
			Map<String, List<TimeInterval>> timeMap) {
		Map<TimeInterval, List<TimeInterval>> sensorsMap = 
			new HashMap<TimeInterval, List<TimeInterval>>();
		
		for (String date: timeMap.keySet()) {
			List<TimeInterval> sensors = timeMap.get(date);
			for (int i = 0; i < sensors.size(); i++) {
				TimeInterval sensor = sensors.get(i);
				List<TimeInterval> followedBySensors = sensorsMap.get(sensor);
				if (followedBySensors == null) {
					followedBySensors = new ArrayList<TimeInterval>();
				}
				for (int j = i+1; j < sensors.size(); j++) {
					TimeInterval followingSensor = sensors.get(j);
					
					if (followingSensor.getValue().equals(sensor.getValue())) {
						break;
					}
					if ((followedBySensors.isEmpty() && 
							followingSensor.getStartTime() > sensor.getStartTime())
							|| (!followedBySensors.isEmpty() 
									&& followingSensor.getStartTime() == 
								followedBySensors.get(0).getStartTime())) {
						followedBySensors.add(followingSensor);
					} 
					//System.out.println(sensor.getStartTime());
				}
				
				sensorsMap.put(sensor, followedBySensors);
			}
		}
		
//		for (TimeInterval sensor: sensorsMap.keySet()) {
//			List<TimeInterval> followingSensor = sensorsMap.get(sensor);
//			System.out.print(sensor.getStartTime() + ": ");
//			for (TimeInterval fs: followingSensor) {
//				System.out.print(fs.getStartTime() + ", ");
//			}
//			System.out.println();
//		}
		return sensorsMap;
	}
*/
}






