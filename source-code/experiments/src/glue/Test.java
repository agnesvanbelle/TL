package glue;


import java.io.File;
import java.util.ArrayList;

import art.experiments.WifiExperimentRunner;
import art.framework.utils.*;



import experiment.MetaFeatureMaker;


/**
 * Runs the whole stuff: metafeature making + transfer algorithm
 * resp. from our_project_project and from arf.experiments.wifi
 * 
 *
 */
public class Test {
	
	private WifiExperimentRunner wer;
	
	
	public Test() {		
		// constructor
		
	}
	
	public void run() {
		
		Utils.resetDirectory(MetaFeatureMaker.outputDirName);		
		
		MetaFeatureMaker.runForABC();
		
		copyOutputtedMFtoWifiExperimentRunnerInput() ;
		
		wer = new WifiExperimentRunner();
		
		//wer.run();
		
	}
	
	public void copyOutputtedMFtoWifiExperimentRunnerInput() {
		
		String outputDirName = WifiExperimentRunner.ROOT_DIR + "input/" + WifiExperimentRunner.FEATURE_TYPE.OF + "/";
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
		Test t = new Test();
		
		
		t.run();
	}

}
