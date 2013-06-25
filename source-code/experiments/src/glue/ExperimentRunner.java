package glue;


import java.io.File;
import java.util.ArrayList;

import art.framework.utils.*;
import art.experiments.*;


/**
 * Runs the whole stuff: metafeature making + transfer algorithm
 * resp. from our_project_project and from arf.experiments.wifi
 * 
 *
 */
public class ExperimentRunner {
	
	private WifiExperimentRunner wer;
	
	
	public ExperimentRunner() {		
		// constructor
		
	}
	
	public void run() {
		
		

		Directory experimentStructure = new Directory("output");
		experimentStructure.addChild(new Directory(WERenums.FEATURE_TYPE.OF.toString()));
		experimentStructure.addChild(new Directory(WERenums.FEATURE_TYPE.HF.toString()));
		
		System.out.println(experimentStructure);
		
		System.out.println("----------");
		
		experimentStructure.get(WERenums.FEATURE_TYPE.HF.toString())
			.addChild(new Directory(WERenums.TRANSFER_TYPE.TRANSFER.toString()));
		experimentStructure.get(WERenums.FEATURE_TYPE.HF.toString())
			.addChild(new Directory(WERenums.TRANSFER_TYPE.NOTRANSFER.toString()));
		
		experimentStructure.get(WERenums.FEATURE_TYPE.OF.toString())
			.addChild(new Directory(WERenums.TRANSFER_TYPE.TRANSFER.toString()));
	
		
		//experimentStructure.get(WERenums.FEATURE_TYPE.OF.toString()).addChildToAllLeafs(WERenums.PROFILE_TYPE.valuesStr());

		//experimentStructure.get(WERenums.FEATURE_TYPE.OF.toString()).addChildToAllLeafs(WERenums.CLUSTER_TYPE.valuesStr());
		
		
			
//		experimentStructure.get(WERenums.FEATURE_TYPE.OF.toString()).
//			get(WERenums.TRANSFER_TYPE.TRANSFER.toString()).
//				get("CT_REL").
//					addChildToAllLeafs("bla");
		
		System.out.println(experimentStructure);
		
		System.out.println("nr. comparisons: " + experimentStructure.leafCount());
		
		System.out.println(experimentStructure.getDirName("../our_project_project/"));
		
		
		//Utils.resetDirectory(MetaFeatureMaker.outputDirName);		
		
		
		//MetaFeatureMaker.runForSubsetNormal(0,5);
		
		copyOutputtedMFtoWifiExperimentRunnerInput() ;
		
				
		
		wer = new WifiExperimentRunner();
		
		wer.setNumberHouses(3);
		wer.set_NO_DATA_INSTANCES(100);
		int[] noDaysConsidered = {2,3,6,11,21};
		wer.setNoDaysArray(noDaysConsidered);
		wer.turnLoggingOff();
		wer.setWithRanges(true);
		
		System.out.println("\n------ WER settings : ------");		
		System.out.println(wer);
		System.out.println("----------------------------\n");
		
		
		//wer.run();
		
	}
	
	public void copyOutputtedMFtoWifiExperimentRunnerInput() {
		
		String outputDirName = WifiExperimentRunner.ROOT_DIR + "input/" + WERenums.FEATURE_TYPE.OF + "/";
		Utils.resetDirectory(outputDirName);
		String inputDirName = MetaFeatureMaker.outputDirName;
		File inputDir = new File(inputDirName);
				
		
		 ArrayList<String> dirList = Utils.getSubDirectories(inputDirName);
		 System.out.println("Listing of " + inputDir.getAbsolutePath());
		 for (String dir : dirList) {
			 System.out.println("copying " + inputDirName + "/" + dir + " to " +  outputDirName + dir );
			 Utils.copyDirectory(new File(inputDirName + "/" + dir), new File(outputDirName + dir));
		 }
	}
	
	
	
	
	public static void main(String[] args) {
		ExperimentRunner t = new ExperimentRunner();
		
		
		t.run();
	}

}
