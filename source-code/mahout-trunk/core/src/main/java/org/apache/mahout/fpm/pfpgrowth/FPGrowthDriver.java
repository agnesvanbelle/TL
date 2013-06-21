/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.mahout.fpm.pfpgrowth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.common.CommandLineUtil;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.Parameters;
import org.apache.mahout.common.commandline.DefaultOptionCreator;
import org.apache.mahout.common.iterator.FileLineIterable;
import org.apache.mahout.common.iterator.StringRecordIterator;
import org.apache.mahout.fpm.pfpgrowth.convertors.ContextStatusUpdater;
import org.apache.mahout.fpm.pfpgrowth.convertors.SequenceFileOutputCollector;
import org.apache.mahout.fpm.pfpgrowth.convertors.string.StringOutputConverter;
import org.apache.mahout.fpm.pfpgrowth.convertors.string.TopKStringPatterns;
import org.apache.mahout.fpm.pfpgrowth.fpgrowth.FPGrowth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FPGrowthDriver {

	private static final Logger log = LoggerFactory.getLogger(FPGrowthDriver.class);
	private static BufferedReader br;

	public FPGrowthDriver() {
	}

	/**
	 * Run TopK FPGrowth given the input file,
	 */
	public static void main(String[] args) throws Exception {
		runFPGrowthDriver(args);
	}

	public static void runFPGrowthDriver(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		DefaultOptionBuilder obuilder = new DefaultOptionBuilder();
		ArgumentBuilder abuilder = new ArgumentBuilder();
		GroupBuilder gbuilder = new GroupBuilder();

		Option inputDirOpt = obuilder.withLongName("input").withRequired(true).withArgument(abuilder.withName("input").withMinimum(1).withMaximum(1).create())
				.withDescription("The Directory on HDFS containing the transaction files").withShortName("i").create();

		Option outputOpt = DefaultOptionCreator.outputOption().create();

		Option helpOpt = DefaultOptionCreator.helpOption();

		// minSupport(3), maxHeapSize(50), numGroups(1000)
		Option minSupportOpt = obuilder.withLongName("minSupport").withArgument(abuilder.withName("minSupport").withMinimum(1).withMaximum(1).create())
				.withDescription("(Optional) Minimum Support. Default Value: 3").withShortName("s").create();

		Option maxHeapSizeOpt = obuilder.withLongName("maxHeapSize").withArgument(abuilder.withName("maxHeapSize").withMinimum(1).withMaximum(1).create())
				.withDescription("(Optional) Maximum Heap Size k, to denote the requirement to mine top K items. Default value: 50").withShortName("k").create();

		Option numGroupsOpt = obuilder.withLongName("numGroups").withArgument(abuilder.withName("numGroups").withMinimum(1).withMaximum(1).create())
				.withDescription("(Optional) Number of groups the features should be divided in the map-reduce version." + " Doesn't work in sequential version Default Value:1000").withShortName("g")
				.create();

		Option recordSplitterOpt = obuilder
				.withLongName("splitterPattern")
				.withArgument(abuilder.withName("splitterPattern").withMinimum(1).withMaximum(1).create())
				.withDescription(
						"Regular Expression pattern used to split given string transaction into itemsets." + " Default value splits comma separated itemsets.  Default Value:"
								+ " \"[ ,\\t]*[,|\\t][ ,\\t]*\" ").withShortName("regex").create();

		Option treeCacheOpt = obuilder
				.withLongName("numTreeCacheEntries")
				.withArgument(abuilder.withName("numTreeCacheEntries").withMinimum(1).withMaximum(1).create())
				.withDescription(
						"(Optional) Number of entries in the tree cache to prevent duplicate tree building. " + "(Warning) a first level conditional FP-Tree might consume a lot of memory, "
								+ "so keep this value small, but big enough to prevent duplicate tree building. " + "Default Value:5 Recommended Values: [5-10]").withShortName("tc").create();

		Option methodOpt = obuilder.withLongName("method").withRequired(true).withArgument(abuilder.withName("method").withMinimum(1).withMaximum(1).create())
				.withDescription("Method of processing: sequential|mapreduce").withShortName("method").create();
		Option encodingOpt = obuilder.withLongName("encoding").withArgument(abuilder.withName("encoding").withMinimum(1).withMaximum(1).create())
				.withDescription("(Optional) The file encoding.  Default value: UTF-8").withShortName("e").create();

		Group group = gbuilder.withName("Options").withOption(minSupportOpt).withOption(inputDirOpt).withOption(outputOpt).withOption(maxHeapSizeOpt).withOption(numGroupsOpt).withOption(methodOpt)
				.withOption(encodingOpt).withOption(helpOpt).withOption(treeCacheOpt).withOption(recordSplitterOpt).create();
		try {
			Parser parser = new Parser();
			parser.setGroup(group);
			CommandLine cmdLine = parser.parse(args);

			if (cmdLine.hasOption(helpOpt)) {
				CommandLineUtil.printHelp(group);
				return;
			}

			Parameters params = new Parameters();

			if (cmdLine.hasOption(minSupportOpt)) {
				String minSupportString = (String) cmdLine.getValue(minSupportOpt);
				params.set("minSupport", minSupportString);
			}
			if (cmdLine.hasOption(maxHeapSizeOpt)) {
				String maxHeapSizeString = (String) cmdLine.getValue(maxHeapSizeOpt);
				params.set("maxHeapSize", maxHeapSizeString);
			}
			if (cmdLine.hasOption(numGroupsOpt)) {
				String numGroupsString = (String) cmdLine.getValue(numGroupsOpt);
				params.set("numGroups", numGroupsString);
			}

			if (cmdLine.hasOption(treeCacheOpt)) {
				String numTreeCacheString = (String) cmdLine.getValue(treeCacheOpt);
				params.set("treeCacheSize", numTreeCacheString);
			}

			if (cmdLine.hasOption(recordSplitterOpt)) {
				String patternString = (String) cmdLine.getValue(recordSplitterOpt);
				params.set("splitPattern", patternString);
			}

			String encoding = "UTF-8";
			if (cmdLine.hasOption(encodingOpt)) {
				encoding = (String) cmdLine.getValue(encodingOpt);
			}
			params.set("encoding", encoding);
			Path inputDir = new Path(cmdLine.getValue(inputDirOpt).toString());
			Path outputDir = new Path(cmdLine.getValue(outputOpt).toString());

			params.set("input", inputDir.toString());
			params.set("output", outputDir.toString());

			String classificationMethod = (String) cmdLine.getValue(methodOpt);
			if ("sequential".equalsIgnoreCase(classificationMethod)) {
				// runFPGrowth(params);
				getFeatures(params);
			}
			else if ("mapreduce".equalsIgnoreCase(classificationMethod)) {
				Configuration conf = new Configuration();
				HadoopUtil.delete(conf, outputDir);
				PFPGrowth.runPFPGrowth(params);
			}
		}
		catch (OptionException e) {
			CommandLineUtil.printHelp(group);
		}
	}

	private static void runFPGrowthOrig(Parameters params) throws IOException {
		log.info("Starting Sequential FPGrowth");
		int maxHeapSize = Integer.valueOf(params.get("maxHeapSize", "50"));
		int minSupport = Integer.valueOf(params.get("minSupport", "1000"));
		// "3"));

		String output = params.get("output", "output.txt");

		Path path = new Path(output);
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);

		Charset encoding = Charset.forName(params.get("encoding"));
		String input = params.get("input");

		String pattern = params.get("splitPattern", PFPGrowth.SPLITTER.toString());

		SequenceFile.Writer writer = new SequenceFile.Writer(fs, conf, path, Text.class, TopKStringPatterns.class);

		FPGrowth<String> fp = new FPGrowth<String>();
		Collection<String> features = new HashSet<String>();

		fp.generateTopKFrequentPatterns(new StringRecordIterator(new FileLineIterable(new File(input), encoding, false), pattern), fp.generateFList(new StringRecordIterator(new FileLineIterable(
				new File(input), encoding, false), pattern), minSupport), minSupport, maxHeapSize, features, new StringOutputConverter(
				new SequenceFileOutputCollector<Text, TopKStringPatterns>(writer)), new ContextStatusUpdater(null));
		writer.close();

		List<Pair<String, TopKStringPatterns>> frequentPatterns = FPGrowth.readFrequentPattern(conf, path);

		for (Pair<String, TopKStringPatterns> entry : frequentPatterns) {
			log.info("Dumping Patterns for Feature: {} \n{}", entry.getFirst(), entry.getSecond());
			System.out.println("Dumping Patterns for Feature: " + entry.getFirst() + " \n" + entry.getSecond());

		}
	}

	private static void getFeatures(Parameters params) throws IOException {

		String rulesOutput =
		//"/Users/dagga/Desktop/ClassWork/Thesis/MAHOUT/MAHOUT_WS/game.tree.analysis/houseInfoA/split08-19-15-27-18/rules.txt"; 
		params.get("output");
		String outputFile = "../arf.experiments.wifi/test-output/output.txt";
		params.set("output", outputFile);
		params.set("method", "sequential");
		params.set("splitterPattern", ":");

		double conf = Double.parseDouble(params.get("minSupport"));

		//String rootDir = "/Users/dagga/Desktop/ClassWork/Thesis/MAHOUT/MAHOUT_WS/trunk/core/testdata/" + "test-wifi";
		//"test-data2/";
		String classMapFile = params.get("input");
		//"/Users/dagga/Desktop/ClassWork/Thesis/MAHOUT/MAHOUT_WS/game.tree.analysis/houseInfoA/split08-19-15-27-18/wifiClassMap-train-tr";
		//"/Users/dagga/Desktop/ClassWork/Thesis/MAHOUT/MAHOUT_WS/game.tree.analysis/houseInfoA/split03-18-05-08/wifiClassMap-train";
		//"/Users/dagga/Desktop/ClassWork/Thesis/MAHOUT/MAHOUT_WS/game.tree.analysis/houseInfoA/wifiClassMap";
		//new File(rootDir,"wifiClassMap").getAbsolutePath();

		Map<Example, Float> selectedRules = new HashMap<FPGrowthDriver.Example, Float>();
		Set<Example> selectedPatterns = new HashSet<FPGrowthDriver.Example>();
		List<String> classValues = null;
		List<String> classLabels = new ArrayList<String>();

		Map<Example, Float> selectedPatternsMap = new HashMap<FPGrowthDriver.Example, Float>();

		try {
			br = new BufferedReader(new FileReader(classMapFile));
			String line = null;

			String dataFile = null;
			while ((line = br.readLine()) != null) {
				String[] classInfo = line.split(",");
				classValues = new ArrayList<String>(Arrays.asList(classInfo));
				dataFile = classValues.remove(classValues.size() - 1);
			}

			Map<String, List<Example>> exampleMap = new HashMap<String, List<Example>>();
			for (String classValue : classValues) {
				String[] classInfo = classValue.split(":");
				String classLabel = classInfo[0];
				int dataSize = Integer.parseInt(classInfo[1]);

				if (classLabel.contains("@")) {
					String[] inf = classLabel.split("@");
					classLabel = inf[1];
					int weight = Integer.parseInt(inf[0]);
					dataSize = dataSize * weight;
					if (weight > 1) {
						conf = 0.5;
					}
				}
				classLabels.add(classLabel);

				Set<Example> selectedPatterns_ = getSelectedRules(conf, dataSize, params, dataFile, classLabel, exampleMap, selectedPatternsMap, selectedRules);

				//				for (Example rule : selectedPatterns_) {
				//					List<String> patternsList = rule.getPatterns();
				//					
				//					if (patternsList.size() <= 1) {
				//						double newconf = conf - 0.2; //reduce the value
				//						if (newconf >= 0.1) {
				//							selectedPatterns_ = getSelectedRules(newconf, dataSize, 
				//									 params, dataFile, classLabel, exampleMap, selectedPatternsMap, selectedRules);
				//						}	
				//						break;
				//					}
				//				}

				selectedPatterns.addAll(selectedPatterns_);
				//selectedPatterns.addAll(selectedRules_.keySet());
			}

			//			System.out.println("Best rules: ");
			//			for (Example ex : selectedRules.keySet()) {
			//				System.out.println(ex.getPatterns() + " " + ex.getScore());
			//			}
			//			System.out.println("Filtered rules: ");
			//			for (Example ex : selectedPatterns) {
			//				System.out.println(ex.getPatterns());
			//			}

			//attachClassDistribution(exampleMap, selectedPatterns);

			saveSelectedFeatures( //selectedRules.keySet(),
					selectedPatterns, rulesOutput, classLabels);
			// "combinedBasicRules-nofilter.txt");//"combinedRules-new.txt");

		}
		catch (FileNotFoundException e) {
			System.out.println("[ERROR] Failed to read from file: " + classMapFile);
			System.out.println(e);
			System.exit(1);
		}
		catch (IOException e) {
			System.out.println("[ERROR] Unexpected exception while reading from file: " + classMapFile);
			System.out.println(e);
			System.exit(1);
		}
		catch (Exception e) {
			System.out.println("[ERROR] Unexpected exception with " + classMapFile);
			System.out.println(e);
			System.exit(1);
		}

		//	    
		//	    //get frequent patterns for positvie examples
		//	    String inputFile = "/Users/dagga/Desktop/ClassWork/Thesis/MAHOUT/MAHOUT_WS/trunk/core/testdata/test-data2/" +
		//	    	//"tttAbstructExamplesP.txt";
		//	       	"chessAbstructExamplesP.txt";
		//			//"combinedAbstructExamplesP.txt";
		//			//"nmmAbstructExamplesP.txt";
		//	    	//"combinedP";
		//	    	//"tttExamplesP";
		//	    	//"nmmExamplesP";
		//	    	//"chessExamplesP";
		//	    
		//	    params.set("input", inputFile);
		//		List<Example> posPatterns = runFPGrowth(params, examples,"50","100");
		//		
		//		//get frequent patterns for negative examples
		//		inputFile = "/Users/dagga/Desktop/ClassWork/Thesis/MAHOUT/MAHOUT_WS/trunk/core/testdata/test-data2/" +
		//				//"tttAbstructExamplesN.txt";
		//				"chessAbstructExamplesN.txt";
		//				//"combinedAbstructExamplesN.txt";
		//				//"nmmAbstructExamplesN.txt";
		//				//"combinedN";
		//				//"nmmExamplesN";
		//				//"tttExamplesN";
		//				//"chessExamplesN";
		//		
		//		params.set("input", inputFile);
		//	    List<Example> negPatterns = runFPGrowth(params, examples, "50", "1000");
		//	    
		////	    deleteContainedPatternsWithTheSameSupport(posPatterns);
		////	    deleteContainedPatternsWithTheSameSupport(negPatterns);
		////	    deletePatternsContainedInOppositeClass(posPatterns, negPatterns);
		//
		//	    
		//	    Map<Example, Float> selectedRulesP = getBestRules(posPatterns, examples, 100 /* times each pattern can be covered */, 
		//	    		6 /* number of best patterns to return*/); 
		//	    Map<Example, Float> selectedRulesN = getBestRules(negPatterns, examples, 100 /* times each pattern can be covered */, 
		//	    		6 /* number of best patterns to return*/); 
		//
		//	    Map<Example, Float> selectedRules = new HashMap<FPGrowthDriver.Example, Float>();
		//	    selectedRules.putAll(selectedRulesN);
		//	    selectedRules.putAll(selectedRulesP);
		//		
		//	    System.out.println("Best rules:");
		//	    for (Example ex: selectedRules.keySet()) {
		//	    	System.out.println(ex.getPatterns());
		//	    }
		//	    
		//	    Set<Example> selectedPatterns = new HashSet<FPGrowthDriver.Example>();
		//	    Set<Example> selectedPatternsN = filterIrrelevantFeatures(examples, selectedRulesN);
		//	    Set<Example> selectedPatternsP = filterIrrelevantFeatures(examples, selectedRulesP);
		//	    selectedPatterns.addAll(selectedPatternsN);
		//	    selectedPatterns.addAll(selectedPatternsP);
		//	 
		//	    System.out.println("Filtered rules:");
		//	    for (Example ex: selectedPatterns) {
		//	    	System.out.println(ex.getPatterns());
		//	    }
		//	    	
		//	    saveSelectedFeatures(//selectedRules.keySet(), 
		//	    		selectedPatterns, "chessRules2-abstruct2.txt");
		//	    		//"combinedBasicRules-nofilter.txt");//"combinedRules-new.txt"); 
	}

	private static Set<Example> getSelectedRules(double conf, int dataSize, Parameters params, String dataFile, String classLabel, Map<String, List<Example>> exampleMap,
			Map<Example, Float> selectedPatternsMap, Map<Example, Float> selectedRules) throws IOException {

		List<Example> examples = new ArrayList<Example>();

		int support = (int) Math.ceil(conf * dataSize); //0.7 * dataSize);

		params.set("minSupport", Integer.toString(support));

		params.set("input", dataFile);
		//new File(rootDir, dataFile).getAbsolutePath());
		List<Example> posPatterns = runFPGrowth(params, examples, "50", Integer.toString(support), classLabel);// "100");

		//		int noBestRules = 100;
		//		if (conf != 0.5) { //TODO: this assumes that source rules are here
		//			noBestRules = 6; 
		//		}
		exampleMap.put(classLabel, examples); //save examples
		Map<Example, Float> selectedRules_ = getBestRules(posPatterns, examples, 100 /*
																					 * times
																					 * each
																					 * pattern
																					 * can
																					 * be
																					 * covered
																					 */, 100, //15, //100, //6 /* number of best patterns to return */, 
				classLabel);

		selectedPatternsMap.putAll(selectedRules_);

		selectedRules.putAll(selectedRules_);
		Set<Example> selectedPatterns_ = filterIrrelevantFeatures(examples, selectedRules_);
		return selectedPatterns_;
		//		return new HashSet<FPGrowthDriver.Example>(posPatterns);
		//	return selectedRules_.keySet();
	}

	private static void attachClassDistribution(Map<String, List<Example>> exampleMap, Set<Example> selectedRules) {
		for (Example rule : selectedRules) {//for each rule get class distribution
			List<String> rulePredicates = rule.getPatterns();

			for (String classLabel : exampleMap.keySet()) { // for every instance
				if (!rulePredicates.contains(classLabel)) {
					continue;
				}
				List<Example> examples = exampleMap.get(classLabel);

				for (Example example : examples) {
					List<String> examplePredicates = example.getPatterns();

					//if example is covered by a rule increment class count
					if (examplePredicates.containsAll(rulePredicates)) {
						rule.incrementClassCount(classLabel);
					}
				}
			}
		}

		//		for (Example rule : selectedRules) {
		//			System.out.println(rule.getPatterns());
		//			Map<String, Integer> distribution = rule.getClassDistribution();
		//			for (String classLabel: distribution.keySet()) {
		//				System.out.println(classLabel + " = " + distribution.get(classLabel));
		//			}
		//		}
	}

	/**
	 * This method detects irrelevant rules. An irrelevant rule is a rule for
	 * which there exists another one or more rules that cover the same subset
	 * of examples, have higher accuracy, have length the same as or longer than
	 * the irrelevant rule. The last requirement is to avoid overfitting to
	 * shorter, less specific rules.
	 * 
	 * @param examples - a list of example instances
	 * @param selectedRules - rules selected thus far
	 * @return - a set of rules that are relevant
	 */
	private static Set<Example> filterIrrelevantFeatures(List<Example> examples, Map<Example, Float> selectedRules) {
		//map each rule onto a map of each pattern to a sorted set of examples containing it
		Map<Example, Map<String, Set<Integer>>> rulePatternsExmpleMap = new HashMap<Example, Map<String, Set<Integer>>>();

		Map<Example, Set<Integer>> ruleExampleMap = new HashMap<Example, Set<Integer>>();

		for (Example rule : selectedRules.keySet()) {
			for (Example example : examples) { // for every instance
				List<String> examplePatterns = example.getPatterns();
				// if every example contains rule X see if it also contains rule Y
				// prefer more general rule over specific one
				for (String p : rule.getPatterns()) {
					if (examplePatterns.contains(p)) {
						Set<Integer> ruleExamples = ruleExampleMap.get(rule);
						if (ruleExamples == null) {
							ruleExamples = new TreeSet<Integer>();
						}
						ruleExamples.add(example.getId());
						ruleExampleMap.put(rule, ruleExamples);

						Map<String, Set<Integer>> patternsExmpleMap = rulePatternsExmpleMap.get(rule);
						if (patternsExmpleMap == null) {
							patternsExmpleMap = new HashMap<String, Set<Integer>>();
						}
						Set<Integer> exampleIdSet = patternsExmpleMap.get(p);
						if (exampleIdSet == null) {
							exampleIdSet = new TreeSet<Integer>();
						}
						exampleIdSet.add(example.getId());
						patternsExmpleMap.put(p, exampleIdSet);
						rulePatternsExmpleMap.put(rule, patternsExmpleMap);
					}
				}

			}
		}
		for (Example rule1 : ruleExampleMap.keySet()) {
			Set<Integer> ruleExamples1 = ruleExampleMap.get(rule1);
			float accuracy1 = selectedRules.get(rule1);

			for (Example rule2 : ruleExampleMap.keySet()) {
				Set<Integer> ruleExamples2 = ruleExampleMap.get(rule2);
				float accuracy2 = selectedRules.get(rule2);

				if (rule1 == rule2) {
					continue;
				}
				if (ruleExamples1.containsAll(ruleExamples2)
				//&& rule1.getPatterns().size() >= rule2.getPatterns().size() 
				//to punish shorter rules
						&& accuracy1 > accuracy2) {
					//delete the second rule
					//System.out.println("Delete rule " + rule2.getPatterns());

					rulePatternsExmpleMap.remove(rule2);
				}
				//				} else if (ruleExamples2.containsAll(ruleExamples1)
				//						&& rule2.getPatterns().size() >= rule1.getPatterns().size() 
				//						//to punish shorter rules
				//						&& accuracy2 > accuracy1) {
				//					//delete the second rule
				//					System.out.println("Delete rule " + rule1.getPatterns());
				//					rulePatternsExmpleMap.remove(rule1);
				//				}
			}

		}

		//		for (Example r: rulePatternsExmpleMap.keySet()) {
		//			System.out.println(r.getPatterns());
		//		}

		return rulePatternsExmpleMap.keySet();
	}

	private static List<Example> runFPGrowth(Parameters params, List<Example> examples, String confidence, String support, String classLabel) throws IOException {
		int maxHeapSize = Integer.valueOf(params.get("maxHeapSize", confidence));
		int minSupport = Integer.valueOf(params.get("minSupport", support));

		String output = params.get("output", "output.txt");

		Path path = new Path(output);
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);

		Charset encoding = Charset.forName(params.get("encoding"));
		String input = params.get("input");

		String pattern = params.get("splitPattern", PFPGrowth.SPLITTER.toString());

		SequenceFile.Writer writer = new SequenceFile.Writer(fs, conf, path, Text.class, TopKStringPatterns.class);

		//TODO: add another parameter that specifies a list of features
		FPGrowth<String> fp = new FPGrowth<String>();
		Collection<String> features = new HashSet<String>();
		features.add(classLabel);

		StringRecordIterator transactionIterator = new StringRecordIterator(new FileLineIterable(new File(input), encoding, false), pattern);

		// transactionIterator.
		while (transactionIterator.hasNext()) { //for every instance

			Pair<List<String>, Long> transaction = transactionIterator.next();
			List<String> instance = transaction.getFirst();

			Float weight = new Float(transaction.getSecond());
			Example example = new FPGrowthDriver.Example(instance, weight, 0);
			examples.add(example);
		}

		transactionIterator = new StringRecordIterator(new FileLineIterable(new File(input), encoding, false), pattern);

		fp.generateTopKFrequentPatterns(transactionIterator, fp.generateFList(new StringRecordIterator(new FileLineIterable(new File(input), encoding, false), pattern), minSupport), minSupport,
				maxHeapSize, features, new StringOutputConverter(new SequenceFileOutputCollector<Text, TopKStringPatterns>(writer)), new ContextStatusUpdater(null));
		writer.close();

		List<Pair<String, TopKStringPatterns>> frequentPatterns = FPGrowth.readFrequentPattern(conf, path);
		List<Example> allPatterns = new ArrayList<Example>();

		for (Pair<String, TopKStringPatterns> entry : frequentPatterns) {
			//	      log.info("Dumping Patterns for Feature: {} \n{}", entry.getFirst(), entry.getSecond());
			//	      System.out.println("Dumping Patterns for Feature: " + entry.getFirst() + " \n" + entry.getSecond());

			TopKStringPatterns feature = entry.getSecond();
			List<Pair<List<String>, Long>> patterns = feature.getPatterns();

			for (Pair p : patterns) {
				List<String> subFeatures = (List<String>) p.getFirst();
				Long second = (Long) p.getSecond();
				Example example = new Example(subFeatures, (float) 1.0, 0, second);
				//System.out.println("conf: " + second + " " + example.getPatterns());
				allPatterns.add(example);
			}
		}

		return allPatterns;
	}

	private static void runFPGrowth(Parameters params) throws IOException {
		log.info("Starting Sequential FPGrowth");
		int maxHeapSize = Integer.valueOf(params.get("maxHeapSize", "50"));
		int minSupport = Integer.valueOf(params.get("minSupport", "100")); //"50"));
		//"3"));

		String output = params.get("output", "output.txt");

		Path path = new Path(output);
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);

		Charset encoding = Charset.forName(params.get("encoding"));
		String input = params.get("input");

		String pattern = params.get("splitPattern", PFPGrowth.SPLITTER.toString());

		SequenceFile.Writer writer = new SequenceFile.Writer(fs, conf, path, Text.class, TopKStringPatterns.class);

		FPGrowth<String> fp = new FPGrowth<String>();
		Collection<String> features = new HashSet<String>();
		String posClass = "P";
		String negClass = "N";

		features.add(posClass);
		features.add(negClass);

		StringRecordIterator transactionIterator = new StringRecordIterator(new FileLineIterable(new File(input), encoding, false), pattern);

		fp.generateTopKFrequentPatterns(transactionIterator, fp.generateFList(new StringRecordIterator(new FileLineIterable(new File(input), encoding, false), pattern), minSupport), minSupport,
				maxHeapSize, features, new StringOutputConverter(new SequenceFileOutputCollector<Text, TopKStringPatterns>(writer)), new ContextStatusUpdater(null));
		writer.close();

		StringBuffer sb = new StringBuffer();
		List<Pair<String, TopKStringPatterns>> frequentPatterns = FPGrowth.readFrequentPattern(conf, path);

		List<Example> posPatterns = new ArrayList<Example>();
		List<Example> negPatterns = new ArrayList<Example>();

		List<Example> allPatterns = new ArrayList<Example>();

		for (Pair<String, TopKStringPatterns> entry : frequentPatterns) {
			log.info("Dumping Patterns for Feature: {} \n{}", entry.getFirst(), entry.getSecond());
			System.out.println("Dumping Patterns for Feature: " + entry.getFirst() + " \n" + entry.getSecond());

			TopKStringPatterns feature = entry.getSecond();
			List<Pair<List<String>, Long>> patterns = feature.getPatterns();

			for (Pair p : patterns) {
				List<String> subFeatures = (List<String>) p.getFirst();
				if (entry.getFirst().equals(posClass)) {
					posPatterns.add(new Example(subFeatures, (float) 1.0, 0, (Long) p.getSecond()));
				}
				else {
					negPatterns.add(new Example(subFeatures, (float) 1.0, 0, (Long) p.getSecond()));
				}
			}
		}

		List<Example> examples = new ArrayList<Example>();

		transactionIterator = new StringRecordIterator(new FileLineIterable(new File(input), encoding, false), pattern);

		while (transactionIterator.hasNext()) { //for every instance

			Pair<List<String>, Long> transaction = transactionIterator.next();
			List<String> instance = transaction.getFirst();

			Float weight = new Float(transaction.getSecond());
			Example example = new FPGrowthDriver.Example(instance, weight, 0);
			examples.add(example);
		}

		deleteContainedPatternsWithTheSameSupport(posPatterns);
		deleteContainedPatternsWithTheSameSupport(negPatterns);
		deletePatternsContainedInOppositeClass(posPatterns, negPatterns);

		allPatterns.addAll(posPatterns);
		allPatterns.addAll(negPatterns);

		Map<Example, Float> selectedRulesP = getBestRules(posPatterns, examples, 100 /*
																					 * times
																					 * each
																					 * pattern
																					 * can
																					 * be
																					 * covered
																					 */, 6 /*
																							 * number
																							 * of
																							 * best
																							 * patterns
																							 * to
																							 * return
																							 */, "P");
		Map<Example, Float> selectedRulesN = getBestRules(negPatterns, examples, 100 /*
																					 * times
																					 * each
																					 * pattern
																					 * can
																					 * be
																					 * covered
																					 */, 6 /*
																							 * number
																							 * of
																							 * best
																							 * patterns
																							 * to
																							 * return
																							 */, "N");

		Map<Example, Float> selectedRules = new HashMap<FPGrowthDriver.Example, Float>();
		selectedRules.putAll(selectedRulesN);
		selectedRules.putAll(selectedRulesP);

		filterIrrelevantFeatures(examples, selectedRules);

	}

	private static Map<Example, Float> getBestRules(List<Example> rules, List<Example> examples, int k, int b, String classLabel) {

		Map<Example, Float> bestRules = new HashMap<Example, Float>();

		//iterate over all examples 
		int iteration = 0;

		while ((iteration == 0 || !rules.isEmpty()) || examples.isEmpty()) {

			Map<Example, Float> ruleScores = new HashMap<Example, Float>();

			Set<Integer> indexesOfHighCoverageExamples = new TreeSet<Integer>();

			float maxRuleScore = -10;

			//sort rules by support 
			Collections.sort(rules);

			for (Example rule : rules) {
				// see if the instance is covered by any of the rules

				float nX = 0; //sum of the weights of all covered examples
				float nXY = 0; //sum of the weights of all correctly covered examples
				float nY = 0; // number of instances of class Y

				float Nw = 0; //sum of the weights of all example		
				float N = 0; //total number of examples 

				if (examples.isEmpty()) {
					break;
				}

				for (Example example : examples) { //for every instance
					Float weight = example.getWeight();
					Nw += weight;
					N++;

					List<String> examplePatterns = example.getPatterns();
					List<String> ruleNoClass = new ArrayList<String>(rule.getPatterns());
					ruleNoClass.remove(classLabel);

					if (examplePatterns.containsAll(ruleNoClass)) { // covered
						nX += weight;

						//decrease weight
						Float newWeight = ((float) (1.0 / (iteration + 1.0)));
						example.setWeight(newWeight);
						example.incrementCoverage();

						if (example.getCoverage() > k) {
							indexesOfHighCoverageExamples.add(examples.indexOf(example));
						}

						if (examplePatterns.contains(classLabel)) { // correctly covered
							nXY += weight;
						}
					}

					if (examplePatterns.contains(classLabel)) { // correctly covered
						nY++; // total number of patterns of a given class
					}
				}
				//delete examples with coverage > k
				//deleteListMembersAtIndex(examples, indexesOfHighCoverageExamples);
				indexesOfHighCoverageExamples.clear();

				//access rule quality
				Float wWRAcc = (nX / Nw) * ((nXY / nX) - (nY / N));

				rule.setScore(wWRAcc);
				ruleScores.put(rule, wWRAcc);

				if (maxRuleScore < wWRAcc) {
					maxRuleScore = wWRAcc;
				}
			}

			//select the best rule
			for (Example rule : ruleScores.keySet()) {
				Float ruleScore = ruleScores.get(rule);
				if (bestRules.size() >= b) {
					break;
				}
				if (ruleScore.equals(Float.NaN)) {
					rules.remove(rule);
				}
				else if (ruleScore == maxRuleScore) { // && ruleScore > 0) {
					bestRules.put(rule, ruleScore);
				}

			}

			//delete best rules
			for (Example rule : bestRules.keySet()) {
				rules.remove(rule);
			}

			if (bestRules.size() >= b) {
				break;
			}
			iteration++;
		}

		//	for (Example rule: bestRules.keySet()) {
		//		System.out.println("final best rule " + 
		//				bestRules.get(rule) + ": " +
		//			rule.getPatterns());
		//	}

		return bestRules;
	}

	private static void saveSelectedFeaturesMap(Map<Example, Float> selectedRules, String file, List<String> classLabels) {
		StringBuffer sb = new StringBuffer();
		for (Example rule : selectedRules.keySet()) {
			float ruleScore = selectedRules.get(rule);
			if (ruleScore == 0) {
				continue;
			}

			List<String> patternsList = rule.getPatterns();
			String classLabel_ = null;
			for (String classLabel : classLabels) {
				if (patternsList.contains(classLabel)) {
					patternsList.remove(classLabel);
					classLabel_ = classLabel;
				}
			}
			String distribution = rule.getClassDistributionStr();

			if (classLabel_ == null) {
				System.out.println("[ERROR]: Unexpected exception - pattern with no known class label");
				System.out.println();
			}
			if (!patternsList.isEmpty()) {
				sb.append(classLabel_).append(" ").append(patternsList).append(distribution).append("@").append(ruleScore).append("\n");
			}
			else {
				//System.out.println("NO RULES for " + classLabel_);
				//System.exit(1);
			}

		}

		saveText(file, sb.toString(), false);

	}

	private static void saveSelectedFeatures(Set<Example> selectedRules, String file, List<String> classLabels) {
		StringBuffer sb = new StringBuffer();
		for (Example rule : selectedRules) {
			List<String> patternsList = rule.getPatterns();
			String classLabel_ = null;
			for (String classLabel : classLabels) {
				if (patternsList.contains(classLabel)) {
					patternsList.remove(classLabel);
					classLabel_ = classLabel;
				}
			}
			//String distribution = rule.getClassDistributionStr();

			if (classLabel_ == null) {
				System.out.println("[ERROR]: Unexpected exception - pattern with no known class label");
				System.out.println();
			}
			if (!patternsList.isEmpty()) {
				//sb.append(classLabel_).append(" ").append(patternsList).append(distribution).append("\n");
				sb.append(classLabel_).append(" ").append(patternsList).append("\n");
			}
			else {
				System.out.println("NO RULES for " + classLabel_);
				//	System.exit(1);
			}

		}

		saveText(file, sb.toString(), false);

	}

	//private static void deleteSubPatternsContainedInOppositeClass(
	//		Set<Example> posPatterns, Set<Example> negPatterns) {
	//	
	//	Set<String> posPatternsTD = new TreeSet<String>();
	//    Set<String> negPatternsTD = new TreeSet<String>();
	//     
	//      
	//     for (Example posExample: posPatterns) {
	//    	  List<String> posPs = posExample.getPatterns();
	//   		  for (Example negExample: negPatterns) {
	//   	    	 List<String> negPs = negExample.getPatterns();
	//   	    	 
	//   	    	 
	//   		  } 	 
	//     }
	//     
	//	deleteListMembersAtIndex(posPatterns, posIndex);
	//	deleteListMembersAtIndex(negPatterns, negIndex);
	//}

	/**
	 * Use Modi�ed WRAcc Heuristic with Example Weights, as used in RSD
	 * algorithm, to obtain k best rules.
	 * 
	 * @param posPatterns
	 * @param negPatterns
	 */
	private static void deletePatternsContainedInOppositeClass(List<Example> posPatterns, List<Example> negPatterns) {
		Set<Integer> posIndex = new TreeSet<Integer>();
		Set<Integer> negIndex = new TreeSet<Integer>();

		for (Example posPs : posPatterns) {

			for (Example negPs : negPatterns) {
				//Collections.sort(posPs);
				//Collections.sort(negPs);

				List<String> posPList = new ArrayList<String>(posPs.getPatterns());
				List<String> negPList = new ArrayList<String>(negPs.getPatterns());

				posPList.remove(0); //delete class
				negPList.remove(0); //delete class

				//TODO: maybe revise this and only delete pattern from one class if it occurs more often in another one

				if (posPList.containsAll(negPList)) {
					negIndex.add(negPatterns.indexOf(negPs));
					//   				System.out.println("to delete neg: " + negPs.getPatterns()
					//   						+ "\n positive pattern: " + posPList);
				}

				if (negPList.containsAll(posPList)) {
					posIndex.add(posPatterns.indexOf(posPs));
					//System.out.println("to delete pos: " + posPs);
				}
			}
		}

		deleteListMembersAtIndex(posPatterns, posIndex);
		deleteListMembersAtIndex(negPatterns, negIndex);
	}

	private static void deleteContainedPatternsWithTheSameSupport(List<Example> patterns) {

		Set<Integer> indexes = new TreeSet<Integer>();

		int cnt = 0;
		for (Example ex : patterns) {
			cnt++;
			System.out.println("ex " + cnt + ": " + ex.getPatterns() + " " + ex.getSupport());
		}

		for (int i = 0; i < patterns.size(); i++) {
			for (int j = 0; j < patterns.size(); j++) {

				if (i == j) {
					continue; //avoid self-comparison
				}
				Example ex1 = patterns.get(i);
				List<String> p1 = ex1.getPatterns();
				Example ex2 = patterns.get(j);
				List<String> p2 = ex2.getPatterns();

				if (p2.containsAll(p1)) {
					if (ex1.getSupport().longValue() == ex2.getSupport().longValue()) {
						//    			 System.out.println("Pattern " + p1 + " is in " + p2);
						indexes.add(i);
					}
					else if (isSamePlusInst(p2, p1)) {
						indexes.add(j);
					}
				}
			}
		}

		//     System.out.println(indexes);
		deleteListMembersAtIndex(patterns, indexes);

		System.out.println("After deletion: ");
		cnt = 0;
		for (Example ex : patterns) {
			cnt++;
			System.out.println("ex " + cnt + ": " + ex.getPatterns() + " " + ex.getSupport());
		}
	}

	/**
	 * Checks whether p2 parameter patterns contain all of the patterns in p1
	 * plus some instance patterns, in other words it checks if p1 is a
	 * generalization of p2.
	 * 
	 * @param p2 - list of patterns that is checked for containment of all p1
	 *            patterns
	 * @param p1 - list of patterns that are checked for containment in p2
	 * 
	 * @return true if p1 is a generalization of p2, false otherwise
	 */
	private static boolean isSamePlusInst(List<String> p2, List<String> p1) {
		List<String> p2Copy = new ArrayList<String>(p2);
		for (String pattern : p2) {
			if (pattern.contains("inst")) {
				p2Copy.remove(pattern);
			}
		}

		if (p2Copy.size() == p1.size() && p2Copy.containsAll(p1)) {
			return true;
		}
		return false;
	}

	private static void deleteListMembersAtIndex(List<Example> posPatterns, Set<Integer> indexes) {
		int cnt = 0;
		for (int index : indexes) {
			Example p = posPatterns.remove(index - cnt);
			//System.out.println("deleted: " + p.getPatterns());
			cnt++;
		}
	}

	static class Example implements Comparable<Example> {
		private static int commonId = 0;
		private int id;
		private List<String> patterns;
		private Float weight;
		private int coverage;
		private Long support;
		private Float wWRAcc;
		private Map<String, Integer> classDistribution;

		public Example(List<String> patterns, Float weight, int coverage) {
			this.id = commonId++;
			this.patterns = patterns;
			this.weight = weight;
			this.coverage = coverage;
			classDistribution = new HashMap<String, Integer>();
		}

		public String getClassDistributionStr() {
			StringBuffer sb = new StringBuffer();
			for (String classLabel : classDistribution.keySet()) {
				int count = classDistribution.get(classLabel);
				sb.append(classLabel).append(":").append(count).append(",");
			}
			sb.deleteCharAt(sb.length() - 1);

			return sb.toString();
		}

		public void incrementClassCount(String classLabel) {
			Integer count = classDistribution.get(classLabel);
			if (count == null) {
				count = 0;
			}
			count++;
			classDistribution.put(classLabel, count);
		}

		public Map<String, Integer> getClassDistribution() {
			return classDistribution;
		}

		public void setScore(Float wWRAcc) {
			this.wWRAcc = wWRAcc;
		}

		public float getScore() {
			return wWRAcc;
		}

		public Example(List<String> patterns, Float weight, int coverage, Long support) {
			this(patterns, weight, coverage);
			this.support = support;
		}

		public int getId() {
			return id;
		}

		public Long getSupport() {
			return support;
		}

		public void incrementCoverage() {
			coverage++;
		}

		public void setWeight(Float newWeight) {
			this.weight = newWeight;
		}

		public List<String> getPatterns() {
			return patterns;
		}

		public Float getWeight() {
			return weight;
		}

		public int getCoverage() {
			return coverage;
		}

		@Override
		public int compareTo(Example example) {
			Example tmp = (Example) example;
			if (this.support < tmp.support) {
				return 1;
			}
			else if (this.support > tmp.support) {
				return -1;
			}
			return 0;
		}
	}

	private static void saveText(String file, String text, boolean appendToFile) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, appendToFile));
			bw.write(text);
			bw.flush();
			bw.close();
		}
		catch (IOException e) {
			System.out.println("[ERROR] Failed to save graph to a file: " + file);
			System.out.println(e);
			System.exit(1);
		}
	}
}
