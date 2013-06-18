package art.experiments;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.apache.mahout.fpm.pfpgrowth.FPGrowthDriver;
import art.experiments.wifi.data.processor.EventInfo;
import art.experiments.wifi.data.processor.Sensor;
import art.experiments.wifi.data.processor.WifiAligner;
import art.experiments.wifi.data.processor.WifiUtils;
import art.framework.example.parser.AbstractPredicateWriter;
import art.framework.utils.Constants;
import art.framework.utils.Utils;

public class WifiExperimentRunner {

	private static final boolean USE_CLASS = true;
	//number of instances 
	private static final int NO_INSTANCES = 1;
	private static final String ROOT_DIR = "../arf.experiments.wifi/housedata/houseInfo";
	
	//number of days in target training data
	private static final int[] noDaysArray = /*{21};*/{2,3,6,11,21}; //{6,11,21};
	
	//if true, ranges will be added for variable values
	private static final boolean withRanges = false;

	private static final String classMapFile = "../arf.experiments.wifi/housedata/classMap.txt";
	
	public static void main(String[] args) {
		AbstractPredicateWriter apw = new AbstractPredicateWriter();

		String[] houses = {"A","B","C"};
		Map<String, List<String>> housesMap = new HashMap<String, List<String>>();
		List<String> h1 = new ArrayList<String>();
		List<String> h2 = new ArrayList<String>();
		List<String> h3 = new ArrayList<String>();
		h1.add("B");
		h1.add("C");
		housesMap.put("A", h1);
		h2.add("A");
		h2.add("C");
		housesMap.put("B", h2);
		h3.add("A");
		h3.add("B");
		housesMap.put("C", h3);
		System.out.println("housesMap:");
		WifiUtils.printMap(housesMap);
		
		for (String house: houses) {
			//this house becomes target 
			
			for (int noDays: noDaysArray) {
			
			System.out.println("House: " + house + " Day: " + noDays);
			
			String rootDir = ROOT_DIR + house;
			
			//file with lines consisting of a date range and a sensor id (that fire during this interval) 
			String sensorFile = new File(rootDir,"house" + house + "-ss.txt").getAbsolutePath();
			
			//file with lines consisting of a date range and an action id (that happens during this interval) 
			String actionFile = new File(rootDir,"house" + house + "-as.txt").getAbsolutePath();
			
			//file with corresponding sensor ids, descriptions, and meta-features
			String sensorMapFile = new File(rootDir,"sensorMap" + house +"-ids.txt").getAbsolutePath();
			
			//file with mapping  action ids to their descriptions
			String actionMapFile = new File(rootDir,"actionMap" + house + ".txt").getAbsolutePath();
			
			//read in lines with dates and activities
			List<String> sensorReadings = WifiUtils.getLines(sensorFile);
			List<String> actionReadings = WifiUtils.getLines(actionFile);					
			
			//WifiUtils.printList(sensorReadings);
			//WifiUtils.printList(actionReadings);
			
			System.out.println("sensorReadings size: " + sensorReadings.size());
			System.out.println("actionReadings size: " + actionReadings.size());
			
			//construct a map of date (day) to activities 
			Map<String,List<String>> actionMap = new HashMap<String, List<String>>();
			saveActionLinesByDate(actionReadings, actionMap);
			
			//System.out.println("actionMap:");
			//WifiUtils.printMap(actionMap);
			System.out.println("actionMap (per day) size: " + actionMap.size());
			
			//construct a map of date to sensor readings 
			Set<String> actionDates = actionMap.keySet();
			Map<String, List<String>> sensorMap = saveSensorLinesByDate(
					sensorReadings, actionDates);
			
			//System.out.println("sensorMap:");
			//WifiUtils.printMap(sensorMap);
			System.out.println("sensorMap (per day) size: " + actionMap.size());
		
			//WifiUtils.stop();
			
			
			
			// create a directory where the results of an experiment (that uses noDays days) will be stored
			// e.g. houseData.houseInfoA.houseA21
			String rootDirHouse = rootDir + "/house" + house + noDays; 
			Utils.deleteDir(rootDirHouse);
			
			new File(rootDirHouse).mkdir();
			
			//System.out.println(rootDirHouse);
			
			
			//save training and testing lines
			List<String> allDates = new ArrayList<String>(actionMap.keySet());
			Random rand = new Random(System.currentTimeMillis());
			Map<String, List<String>> testActionInstances = new HashMap<String, List<String>>();
			Map<String, List<String>> testSensorInstances = new HashMap<String, List<String>>();

			Map<String, List<String>> trainActionInstances = new HashMap<String, List<String>>();
			Map<String, List<String>> trainSensorInstances = new HashMap<String, List<String>>();

			//getTestAndTrainingSets(noDays, actionMap, sensorMap, allDates,
//					rand, testActionInstances, testSensorInstances,
//					trainActionInstances, trainSensorInstances);
			
			//construct test and training data
			getTestAndTrainingSetsLeaveOneOut(noDays, actionMap, sensorMap, allDates,
					rand, testActionInstances, testSensorInstances,
					trainActionInstances, trainSensorInstances);
			
			//WifiUtils.printMapNewlines(testActionInstances);
			System.out.println("nr. train/test sets in testActionInstances: ");			
			System.out.println(testActionInstances.keySet().size());
			
			//WifiUtils.printMapNewlines(trainActionInstances);
			System.out.println("nr. train/test sets in trainActionInstances: ");			
			System.out.println(trainActionInstances.keySet().size());
			
			
			String conf = "0.5"; //confidence cut off used to extract rules with FP growth for non-transfer model (target house)
			String confTr = "0.3"; //confidence cut off used to extract rules with FP growth for transfer model (source houses)
			
			// for each train/test set
			// it comes in dir _rootDir named after the train+test day numbers
			for (String dirName: testActionInstances.keySet()) {
				File rootDir_ = new File(rootDir, "split" + dirName);
				Utils.deleteDir(rootDir_.getAbsolutePath());
				rootDir_.mkdir();
				
				
				//save test data
				String sensorTestFile = new File(rootDir_,"house" + house + "-ss-test.txt").getAbsolutePath();
				String actionTestFile = new File(rootDir_,"house" + house + "-as-test.txt").getAbsolutePath();
				List<String> test_actions = testActionInstances.get(dirName); 
				List<String> test_sensors = testSensorInstances.get(dirName); 
				Utils.saveLines(test_actions, actionTestFile);
				Utils.saveLines(test_sensors, sensorTestFile);

				//save training data
				String sensorTrainFile = new File(rootDir_, "house" + house + "-ss-train.txt").getAbsolutePath();
				String actionTrainFile = new File(rootDir_, "house" + house + "-as-train.txt").getAbsolutePath();
				List<String> actions = trainActionInstances.get(dirName);
				List<String> sensors = trainSensorInstances.get(dirName);
				Utils.saveLines(sensors, sensorTrainFile);
				Utils.saveLines(actions, actionTrainFile);
				
				
				
				
				//======== build no transfer model ======== 
				getFeatureRepresentationOfTrainAndTestDataForNoTransferCase(apw, sensorMapFile, actionMapFile,
						rootDirHouse, dirName, rootDir_, sensorTrainFile,
						actionTrainFile, sensorTestFile, actionTestFile, conf);
				
				//WifiUtils.stop();
				//saveSensorModel(sensorModels, "sensorModelOrig");

				//======== get rules from all domains for transfer ======== 
				
				String outputFileCombined = new File(rootDir_, Constants.WIFI_EXAMPLES_FILE  + "-train-tr").getAbsolutePath();	
				String outputAbstructFileCombined = new File(rootDir_, 
						Constants.WIFI_ABSTRUCT_EXAMPLES_FILE + "-train-tr").getAbsolutePath();
				String outputMapFileCombined = new File(rootDir_, Constants.WIFI_CLASS_MAP_FILE + "-train-tr").getAbsolutePath();

				//get a copy of original sensor model
				Map<String, List<EventInfo>>  consecutiveIntervalsTarget = new TreeMap<String, List<EventInfo>>();
				Map<String, Sensor> sensorModelsComb = new TreeMap<String, Sensor>();
				WifiAligner.getAlignedSensorData(sensorTrainFile, actionTrainFile, sensorMapFile,
						 actionMapFile, consecutiveIntervalsTarget, sensorModelsComb);
				//saveSensorModel(sensorModelsComb, "sensorModelOrig2");
				
				//combine training data from different houses
				Map<String, List<EventInfo>> consecutiveIntervals = new HashMap<String, List<EventInfo>>(); 
				combineTrainingData(housesMap, house, sensorModelsComb, consecutiveIntervals);			
				//saveSensorModel(sensorModelsComb, "sensorModelComb");

				//save basic relations
				WifiAligner.getPredicates(consecutiveIntervals, sensorModelsComb, outputFileCombined, false); 
				//save abstract relations 
				WifiAligner.saveAbstructRelations(outputFileCombined,outputAbstructFileCombined, 
						outputMapFileCombined, withRanges, null);
				//save rules 
				String rulesFileTransfer = new File(rootDir_, "rules.txt").getAbsolutePath();
				getRules(outputMapFileCombined, rulesFileTransfer, confTr);

				//======== represent data in a new domain in terms of these rules  ============	
			
				//get target training data represented in terms of the extended model 
				String outputTargetFile = new File(rootDir_, Constants.WIFI_EXAMPLES_FILE  + "-train-trmodel-target").getAbsolutePath();	
				WifiAligner.getPredicates(consecutiveIntervalsTarget, sensorModelsComb, outputTargetFile, true);
				
				String outputAbstructFileTarget = new File(rootDir_, 
						Constants.WIFI_ABSTRUCT_EXAMPLES_FILE + "-train-trmodel-target").getAbsolutePath();
				String outputMapFileTarget = new File(rootDir_, Constants.WIFI_CLASS_MAP_FILE + "-train-trmodel-target").getAbsolutePath();
				WifiAligner.saveAbstructRelations(outputTargetFile,outputAbstructFileTarget, outputMapFileTarget, withRanges, null);

				//extract new rules from the domain data
				String rulesFileTargetExtModel = new File(rootDir_, "rules-target-extmodel.txt").getAbsolutePath();
				getRules(outputMapFileTarget, rulesFileTargetExtModel, conf);

				//combine the rules extracted from source and target domains
				String targetRulesFileComb = new File(rootDir_, "rules-comb.txt").getAbsolutePath();
				List<String> lines1 = Utils.readLines(rulesFileTargetExtModel);
				List<String> lines2 = Utils.readLines(rulesFileTransfer);
				lines2.addAll(lines1);
				Utils.saveLines(lines2, targetRulesFileComb);
				
				//represent domain training data in terms of new features
				String resultFile2 = new File(rootDirHouse, "/train/wifi-" + dirName +"-train-tr-SVM").getAbsolutePath(); 
				apw.getFeatureRepresentationOfData(
						outputTargetFile,
						resultFile2,
						targetRulesFileComb,
						classMapFile, USE_CLASS);

				//represent domain test data in terms of new features
				String resultFileTest2 = new File(rootDirHouse, "/test/wifi-" + dirName +"-test-tr-SVM").getAbsolutePath(); 
				getFeatureRepresentationOfTestData(rootDir_, sensorTestFile, actionTestFile, 
						 sensorMapFile, actionMapFile, sensorModelsComb, 
						 resultFileTest2,  
						 targetRulesFileComb,
						 "transfer", apw);
				
				//Utils.deleteDir(rootDir_.getAbsolutePath());
			} 			
			//break; //remove if want to execute for different numbers of days
		}
			//break; // remove if want to execute for all houses
		}
		System.out.println("hi");
	}
	
	/**
	 * Constructs a mapping of date/time to sensor readings 
	 * 
	 * @param actionReadings - date+sensor readings string
	 * @param actionMap - maps date to sensor readings 
	 */
	private static Map<String, List<String>> saveSensorLinesByDate(
			List<String> sensorReadings, Set<String> actionDates) {
		Map<String,List<String>> sensorMap = new HashMap<String, List<String>>();
		for (String sensorReading: sensorReadings) {
			String[] sensorInfo = sensorReading.split("\\s+");
			String date = sensorInfo[0];
			if (!actionDates.contains(date)) { //only keep annotated readings
				continue;
			}
				
			List<String> lines = sensorMap.get(date);
			if (lines == null) {
				lines = new ArrayList<String>();
			}
			lines.add(sensorReading);
			sensorMap.put(date, lines);
		}
		return sensorMap;
	}

	/**
	 * Constructs a mapping of date/time to activities
	 * 
	 * @param actionReadings - date+action string
	 * @param actionMap - maps date to activities info
	 */
	private static void saveActionLinesByDate(List<String> actionReadings,
			Map<String, List<String>> actionMap) {
		String prevDate = null;
		for (String actionReading: actionReadings) {
			String[] sensorInfo = actionReading.split("\\s+"); // split on whitespace
			
			String date = sensorInfo[0];
				
			if (prevDate != date && prevDate != null) { // only tests for null
				//System.out.println(prevDate + "!=" + date);
				List<String> lines = actionMap.get(prevDate);
				if (lines == null) {
					lines = new ArrayList<String>();
				}
				lines.add(actionReading);
				actionMap.put(prevDate, lines);
			} else {
				//System.out.println(prevDate + "==" + date);
				List<String> lines = actionMap.get(date);
				if (lines == null) {
					lines = new ArrayList<String>();
				}
				lines.add(actionReading);
				actionMap.put(date, lines);
			}
			prevDate = date;
		}
	}

	/**
	 * First models sensors to convert the original data to abstract logic form, then extracts
	 * rules from this data representation. Finally, represents both training and test data
	 * in terms of these rules. The new, vector representation will be then used to train a classifier, 
	 * e.g. SVN.
	 * 
	 * @param apw - AbstractPredicateWriter converts the data to abstract logic form
	 * @param sensorMapFile - file with corresponding sensor ids, descriptions, and meta-features a map of sensor readings
	 * @param actionMapFile - file with lines consisting of a date range and an action id (that happens during this interval) 
	 * @param rootDirHouse - a sub-directory with data associated with a particular house
	 * @param dirName - date identifier
	 * @param rootDir_ - root directory
	 * @param sensorTrainFile - training file with sensor firings information 
	 * @param actionTrainFile - training file with activity information
	 * @param sensorTestFile - test file with sensor firings information 
	 * @param actionTestFile - test file with activity information
	 * @param conf - minimum confidence threshold
	 */
	private static void getFeatureRepresentationOfTrainAndTestDataForNoTransferCase(
			AbstractPredicateWriter apw, String sensorMapFile,
			String actionMapFile, String rootDirHouse, String dirName,
			File rootDir_, String sensorTrainFile, String actionTrainFile,
			String sensorTestFile, String actionTestFile, String conf) 
	{
		
		Map<String, List<EventInfo>> consecutiveIntervals = new TreeMap<String, List<EventInfo>>();
		Map<String, Sensor> sensorModels = new TreeMap<String, Sensor>();

		// fills in consecutiveIntervals and sensorModels maps
		WifiAligner.getAlignedSensorData(sensorTrainFile, actionTrainFile, sensorMapFile,
				 actionMapFile, consecutiveIntervals, sensorModels);
		
		
		System.out.println("consecutiveIntervals: ");
		WifiUtils.printEventInfoMap(consecutiveIntervals);
		/* e.g.
		   8-2-2008-use-toilet-154 --> 
			type = BathroomDoor actionType = use-toilet sensorDurations = 1 1  sensorStarts = 673 675  sensorEnds = 672 675  actionDuration = 3 actionStart = 672 actionEnd = 675
			type = Toilet actionType = use-toilet sensorDurations = 1  sensorStarts = 676  sensorEnds = 675  actionDuration = 3 actionStart = 672 actionEnd = 675
			type = BathroomDoor actionType = use-toilet sensorDurations = 1 1  sensorStarts = 673 675  sensorEnds = 672 675  actionDuration = 3 actionStart = 672 actionEnd = 675
		 */
		System.out.println("sensorModels: ");
		WifiUtils.printSensorTreeMap(sensorModels);
		/* e.g.
		   KitchHeat --> 
			|||action name leave-house sensor durations: [1] s.d. median: 1 no firings: [1] no firings median: 1 action durations: [385] a.d. median: 385 ,  sensor starts: [1035] s.d. starts median: 1035 sensor ends: [1034] s.d. ends median: 1035 action starts: [649] s.d. starts median: 649 action ends: [1034] s.d. ends median: 1034|||
			|||action name prep-dinner sensor durations: [1, 1, 1, 10071] s.d. median: 1 no firings: [1, 3] no firings median: 2 action durations: [25, 45] a.d. median: 35 ,  sensor starts: [1096, 1107, 1120, 1136] s.d. starts median: 1114 sensor ends: [1096, 1107, 1119, 1145] s.d. ends median: 1114 action starts: [1095, 1134] s.d. starts median: 1115 action ends: [1120, 1179] s.d. ends median: 1150|||
			|||action name prep-breakfast sensor durations: [1, 1, 1, 1] s.d. median: 1 no firings: [1, 1, 1, 1] no firings median: 1 action durations: [3, 3, 6, 6] a.d. median: 5 ,  sensor starts: [580, 554, 549, 503] s.d. starts median: 552 sensor ends: [579, 553, 549, 502] s.d. ends median: 552 action starts: [578, 552, 544, 501] s.d. starts median: 548 action ends: [584, 555, 550, 504] s.d. ends median: 553|||
		 */
		
		//using consecutiveIntervals and sensorModels maps give data logic representation
		String outputTargetFile = new File(rootDir_, Constants.WIFI_EXAMPLES_FILE  + "-train-notr").getAbsolutePath();	
		WifiAligner.getPredicates(consecutiveIntervals, sensorModels, outputTargetFile, true);
		
		//given a simple logic data representation obtained above, create abstract data representation
		//by making generalizations over predicates
		// make two files:
		// WIFI_ABSTRUCT_EXAMPLES_FILE + "-train-notr"
		// WIFI_CLASS_MAP_FILE + "-train-notr"
		// NOTE: this doesn't seem to add much..
		// NOTE: full path+filename is appended at the end of the files
		String outputAbstructTargetFile = new File(rootDir_, 
				Constants.WIFI_ABSTRUCT_EXAMPLES_FILE + "-train-notr").getAbsolutePath();
		String outputMapTargetFile = new File(rootDir_, Constants.WIFI_CLASS_MAP_FILE + "-train-notr").getAbsolutePath();
		WifiAligner.saveAbstructRelations(outputTargetFile,outputAbstructTargetFile, outputMapTargetFile, withRanges, null);
		
		
		
		//extract rules/features from the abstract data
		// NOTE: uses RFE algorithm ( with FPgrowth)
		// uses train data
		String targetRulesFile = new File(rootDir_, "rules-target.txt").getAbsolutePath();
		getRules(outputMapTargetFile, targetRulesFile, conf);
		
		//WifiUtils.stop();
		
		//represent training data in terms of extracted rules
		// NOTE: for SVM
		String svmFileTrain = new File(rootDirHouse, "/train/wifi-" + dirName +"-train-notr-SVM").getAbsolutePath(); 
		apw.getFeatureRepresentationOfData(outputTargetFile, svmFileTrain, targetRulesFile, classMapFile, USE_CLASS); 

		// also do for test data (but use extracted rules of train data?)
		String svmFileTest = new File(rootDirHouse, "/test/wifi-" + dirName + "-test-notr-SVM").getAbsolutePath(); 
		getFeatureRepresentationOfTestData(rootDir_, sensorTestFile, actionTestFile, 
				 sensorMapFile, actionMapFile, sensorModels, svmFileTest, 
				 targetRulesFile, "notransfer", apw);
	}
	

	/**
	 * Constructs a training and a test set, such that noDays are selected
	 * for training and one instance out of this set is used for testing in turn. 
	 * Thus, noDays test and training sets are created in total, where 
	 * each training set contains noDays-1 instances, and a test set contains a single instance,
	 * different for each of the sets.
	 * 
	 * 
	 * @param noDays - number of days/instances to be used for training/testing
	 * @param actionMap - date to activity map
	 * @param sensorMap - date to sensor reading map
	 * @param allDates - a list of all dates present in the data set
	 * @param rand - random number generator
	 * @param testActionInstances - test set maps with action instances
	 * @param testSensorInstances - test set maps with sensor instances
	 * @param trainActionInstances - training set maps with action instances
	 * @param trainSensorInstances - training set maps with sensor instances
	 */
	private static void getTestAndTrainingSetsLeaveOneOut(int noDays,
			Map<String, List<String>> actionMap,
			Map<String, List<String>> sensorMap, List<String> allDates,
			Random rand, Map<String, List<String>> testActionInstances,
			Map<String, List<String>> testSensorInstances,
			Map<String, List<String>> trainActionInstances,
			Map<String, List<String>> trainSensorInstances) {
		
		for (int k = 0; k < NO_INSTANCES; k++) {
			List<String> instanceDates = new ArrayList<String>(); 
		
			int cnt = 0;
			int stop = 0;
			while (cnt < noDays && stop < 100) {
				int index = rand.nextInt(allDates.size()-1);
				String date = allDates.get(index);
				List<String> actList = actionMap.get(date);
				
				if (actList.size() > 4 && 
						!instanceDates.contains(date)) {
					instanceDates.add(date);
					cnt++;
				}
				stop++;
			}
			//System.out.println("instanceDates:");
			//WifiUtils.printList(instanceDates);
			
			for (int i = 0; i < instanceDates.size(); i++) {
				List<String> idSet = new ArrayList<String>();

				String testDate = instanceDates.get(i);
				//System.out.println("testDate: " + testDate + ", index: " + i);
				
				String[] dateInfotest = testDate.split("-");
				//String id = dateInfotest[0] + "-";
				
				// NOTE: if below line is commented,
				// noDays train/test combi's will be added, one
				// for each day in instanceDates (the test day)
				//idSet.add(dateInfotest[0]); 
				
				//System.out.println("idSet:");
				//WifiUtils.printList(idSet);
				
				
				//create test and training sets
				List<String> actionTests = new ArrayList<String>();
				List<String> sensorTests = new ArrayList<String>();
				actionTests.addAll(actionMap.get(testDate));
				sensorTests.addAll(sensorMap.get(testDate));
			
				
				List<String> actionTrainSet = new ArrayList<String>();
				List<String> sensorTrainSet = new ArrayList<String>();
				List<String> trainDates = new ArrayList<String>(instanceDates);
				trainDates.remove(i);
				
				//System.out.println("trainDates:");
				//WifiUtils.printList(trainDates);
				
				for (String trainDate: trainDates) {
					String[] dateInfo = trainDate.split("-");
					//id += dateInfo[0] + "-";
					idSet.add(dateInfo[0]);

					actionTrainSet.addAll(actionMap.get(trainDate));
					sensorTrainSet.addAll(sensorMap.get(trainDate));
				}
				Collections.sort(idSet);
				String id = "";
				for (String id_: idSet) {
					id += id_ + "-";
				}
				id = id.substring(0, id.length()-1);
				
				//System.out.println("id: " + id);
				
				if (trainActionInstances.containsKey(id)) {
					continue;
				}
				
			
				trainActionInstances.put(id, actionTrainSet);
				trainSensorInstances.put(id, sensorTrainSet);
				testActionInstances.put(id, actionTests);
				testSensorInstances.put(id, sensorTests);

			}
		}	
	}
	
	/**
	 * @param rootDir_ - root directory
	 * @param sensorTestFile - test file with sensor firings information
	 * @param actionTestFile - test file with activities information
	 * @param sensorMapFile - file with mapping  action ids to their descriptions
	 * @param actionMapFile - file with lines consisting of a date range and an action id (that happens during this interval)
	 * @param sensorModels - sensor models built form training data
	 * @param svmFileTest - file to which data represented in SVM format - in terms of rules/features is stored
	 * @param rulesFile - file with rules/features
	 * @param transferType - string that will be used in all output file names, e.g. "transfer" or "notransfer".
	 * @param apw - AbstractPredicateWriter to convert test data to a new feature form.
	 */
	private static void getFeatureRepresentationOfTestData(File rootDir_, 
			String sensorTestFile, String actionTestFile, 
			String sensorMapFile, String actionMapFile, Map<String, Sensor> sensorModels,
			String svmFileTest, String rulesFile,
			String transferType,  AbstractPredicateWriter apw) {
		
		String outputFileTest = new File(rootDir_, Constants.WIFI_EXAMPLES_FILE + "-test-" + transferType).getAbsolutePath();	
		String outputAbstructFileTest = new File(rootDir_, Constants.WIFI_ABSTRUCT_EXAMPLES_FILE + "-test-" + transferType).getAbsolutePath();
		String outputMapFileTest = new File(rootDir_, Constants.WIFI_CLASS_MAP_FILE + "-test-" + transferType).getAbsolutePath();

		Map<String, List<EventInfo>> consecutiveIntervalsTest = new TreeMap<String, List<EventInfo>>();
		Map<String, Sensor> sensorModelsTest = new HashMap<String, Sensor>();
		// use non-transfer model
		WifiAligner.getAlignedSensorData(sensorTestFile, actionTestFile,
				sensorMapFile, actionMapFile, consecutiveIntervalsTest,
				sensorModelsTest); // NOTE: TODO: this is where models don't overlap
		
		WifiAligner.getPredicates(consecutiveIntervalsTest, sensorModels /*old  model*/, outputFileTest, true);
		
		WifiAligner.saveAbstructRelations(outputFileTest, outputAbstructFileTest, outputMapFileTest, withRanges, null);
		apw.getFeatureRepresentationOfData(outputFileTest, svmFileTest, rulesFile, classMapFile, USE_CLASS); 
	}

	/**
	 * Runs an association algorithm FPGrowthDriver to extract frequent rules informative of class.
	 * 
	 * @param outputMapFile - a file with the following information: 
	 * class1:num_values_in_class1, class2:num_vlaues_in_class2, ... : a full path to abstract relational data representation file
	 * 
	 * @param rulesFile - a file where rules will be stored to
	 * @param conf - minimum confidence 
	 */
	private static void getRules(String outputMapFile, String rulesFile, String conf) {
		try {
			String[] params = {"--input", outputMapFile, "--output", rulesFile, 
					"--method", "sequential", "--encoding","US-ASCII", "--splitterPattern",":", "--minSupport", conf};
				
			FPGrowthDriver fpGrowthDriver = new FPGrowthDriver();
			fpGrowthDriver.runFPGrowthDriver(params);
			
		} catch (Exception e) {
			System.out.println("[ERROR]: Unexpected exception.");
			e.printStackTrace();
			System.exit(1);
		}
	}

	
	/**
	 * Fills sensorModelsTarget
	 * 
	 * @param housesMap - maps each house id, e.g. A,B,C to the remaining houses, e.g. A-> B,C
	 * @param house - current house id
	 * @param sensorModelsTarget - a sensor model obtained from data coming from all houses
	 * @param consecutiveIntervals - aligned sensor and activity information
	 */
	private static void combineTrainingData(
			Map<String, List<String>> housesMap, String house, Map<String, Sensor> sensorModelsTarget,
			Map<String, List<EventInfo>> consecutiveIntervals) 
	{
		List<String> sourceDomains = housesMap.get(house);
		
		List<Map<String, Sensor>> sensorModelsAll = new ArrayList<Map<String,Sensor>>();
		
		for (String sourceHouse: sourceDomains) {
			String rootDir_ = ROOT_DIR + sourceHouse;
			String sensorFile_ = new File(rootDir_, "house" + sourceHouse + "-ss.txt").getAbsolutePath();
			String actionFile_ = new File(rootDir_, "house" + sourceHouse + "-as.txt").getAbsolutePath();
			String sensorMapFile_ = new File(rootDir_, "sensorMap" + sourceHouse +"-ids.txt").getAbsolutePath();
			String actionMapFile_ = new File(rootDir_, "actionMap" + sourceHouse + ".txt").getAbsolutePath();
			
			Map<String, Sensor> sensorModels = new HashMap<String, Sensor>();
			WifiAligner.getAlignedSensorData(sensorFile_, actionFile_,
					sensorMapFile_, actionMapFile_, consecutiveIntervals,
					sensorModels);
			sensorModelsAll.add(sensorModels);
		}
		// NOTE: here sensor models are merged
		mergeSensorModels(sensorModelsTarget, sensorModelsAll);
	}

	private static void mergeSensorModels(
			Map<String, Sensor> sensorModelsTarget, 
			List<Map<String, Sensor>> sensorModelsAll) {
		
			for (Map<String, Sensor> sensorModels: sensorModelsAll) {
				for (String sensor: sensorModels.keySet()) {
					Sensor ss = sensorModels.get(sensor);
					if (ss == null) { //can not happen
						continue;
					}
					Sensor ts = sensorModelsTarget.get(sensor);
					if (ts == null) {                                                                                                                                                                
						sensorModelsTarget.put(sensor, ss);
					} else {
						ts.merge(ss);
						sensorModelsTarget.put(sensor, ts);
					}
			}
		}
			
			for (String sensor: sensorModelsTarget.keySet()) {
				Sensor s = sensorModelsTarget.get(sensor);
				//s.printCountInfo();
				System.out.println(sensor);
				s.printActionInfo(true);

			}
	}

	
	/**
	 * Constructs a training and a test set by randomly selecting noDays 
	 * for training and using the rest for testing. 
	 * 
	 * @param noDays - number of days to use for training
	 * @param actionMap - date to activity map
	 * @param sensorMap - date to sensor reading map
	 * @param allDates - a list of all dates present in the data set
	 * @param rand - random number generator
	 * @param testActionInstances - test set with action instances
	 * @param testSensorInstances - test set with sensor instances
	 * @param trainActionInstances - training set with action instances
	 * @param trainSensorInstances - training set with sensor instances
	 */
/*	private static void getTestAndTrainingSets(int noDays,
			Map<String, List<String>> actionMap,
			Map<String, List<String>> sensorMap, List<String> allDates,
			Random rand, Map<String, List<String>> testActionInstances,
			Map<String, List<String>> testSensorInstances,
			Map<String, List<String>> trainActionInstances,
			Map<String, List<String>> trainSensorInstances) {
		
		if (noDays == 0) {
			List<String> instanceDates = new ArrayList<String>(allDates);
			List<String> actions2 = new ArrayList<String>();
			List<String> sensors2 = new ArrayList<String>();
			for (String date: instanceDates) {
				actions2.addAll(actionMap.get(date));
				sensors2.addAll(sensorMap.get(date));
			}
			testActionInstances.put("0", actions2);
			testSensorInstances.put("0", sensors2);
			return;
		}
		
		for (int i = 0; i < NO_INSTANCES; i++) {
			List<String> instanceDates = new ArrayList<String>(allDates);
			
			String id = "";
			List<String> actions = new ArrayList<String>();
			List<String> sensors = new ArrayList<String>();
			
			//save training instances
			for (int j = 0; j < noDays; j++) {
				int index = rand.nextInt(instanceDates.size()-1);
				String date = instanceDates.remove(index);
				String[] dateInfo = date.split("-");
				id += dateInfo[0] + "-";
				
				actions.addAll(actionMap.get(date));
				sensors.addAll(sensorMap.get(date));
			}
			id = id.substring(0, id.length()-1);
			if (trainActionInstances.containsKey(id)) {
				continue;
			}
			trainActionInstances.put(id, actions);
			trainSensorInstances.put(id, sensors);
		
			//save test instances
			List<String> actions2 = new ArrayList<String>();
			List<String> sensors2 = new ArrayList<String>();
			for (String date: instanceDates) {
				actions2.addAll(actionMap.get(date));
				sensors2.addAll(sensorMap.get(date));
			}
			testActionInstances.put(id, actions2);
			testSensorInstances.put(id, sensors2);
		}
	}
	
		private static void saveSensorModel(Map<String, Sensor> sensorModels, String file) {
		StringBuffer sb = new StringBuffer();
		for (String id: sensorModels.keySet()) {
			Sensor s = sensorModels.get(id);
			sb.append("\n" + s.getType() + "\n");
			Map<String, ActionInfo> actions = s.getActions();
			for (String action: actions.keySet()) {
				sb.append(action + ":\n");
				ActionInfo actionInfo = actions.get(action);
				List<Integer> sensorDurations = actionInfo.getSensorDurations();
				Collections.sort(sensorDurations);
				sb.append("durations: " + sensorDurations + "\n");
				List<Integer> noFirings = actionInfo.getNoFirings();
				Collections.sort(noFirings);
				sb.append("firings: " + noFirings+ "\n");
				List<Integer> sensorEnds = actionInfo.getSensorEnds();
				Collections.sort(sensorEnds);
				sb.append("ends: " + sensorEnds+ "\n");			
				List<Integer> sensorStarts = actionInfo.getSensorStarts();
				Collections.sort(sensorStarts);
				sb.append("starts: " + sensorStarts+ "\n");
			}
		}
		Utils.saveContent(file, sb.toString());
	}
	*/


}
