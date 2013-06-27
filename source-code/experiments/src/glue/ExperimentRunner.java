package glue;

import java.io.File;
import java.util.ArrayList;

import data.HouseData;

import art.framework.utils.*;
import art.experiments.*;
import art.experiments.wifi.data.processor.WifiUtils;

/**
 * Runs the whole stuff: metafeature making + transfer algorithm resp. from
 * our_project_project and from arf.experiments.wifi
 * 
 * 
 */
public class ExperimentRunner {

	private WifiExperimentRunner wer;
	


	public ExperimentRunner() {
		// constructor

	}

	
	public void experiment1MakeFiles(int subsetMin, int subsetMax) {

		Utils.resetDirectory(HouseData.outputDirName);
		Utils.resetDirectory(WifiExperimentRunnerOld.EXP_DIR);
		
		
		for (int i=subsetMin; i < subsetMax; i++) {
			Utils.createDirectory(WifiExperimentRunnerOld.EXP_DIR + HouseData.houseOutputDirPrefix + MetaFeatureMaker.allHouseNames[i]);
			System.out.println("Created dir " + WifiExperimentRunnerOld.EXP_DIR + HouseData.houseOutputDirPrefix + MetaFeatureMaker.allHouseNames[i]);
		}
		
		MetaFeatureMaker.runForSubset(HouseData.outputDirName, subsetMin, subsetMax, 
				WERenums.MF_TYPE.AUTO, WERenums.CLUSTER_TYPE.CT_ABS, WERenums.PROFILE_TYPE.PR_SP, WERenums.TRANSFER_SETTINGS.ONLY_TRANSFER);

		MetaFeatureMaker.runForSubset(HouseData.outputDirName, subsetMin, subsetMax, 
				WERenums.MF_TYPE.AUTO, WERenums.CLUSTER_TYPE.CT_REL, WERenums.PROFILE_TYPE.PR_SP, WERenums.TRANSFER_SETTINGS.ONLY_TRANSFER);

		// copy our created experiment files to input dir
		copyOutputToWifiExperimentRunnerInput(subsetMin, subsetMax, HouseData.outputDirName, WifiExperimentRunnerOld.EXP_DIR);

		// copy (part of) her original features to input dir
		copyOriginalHCToWifiExperimentRunnerInput(subsetMin, subsetMax, WERenums.TRANSFER_SETTINGS.BOTH);
		
		// make classMapFile (maps activity names (for all activities from all processed houses) to a number, is done for SVM classifier in WER)
		MetaFeatureMaker.saveClassMapFile(WifiExperimentRunner.classMapFile);
	}
	
	public void experiment1() {



		int subsetMin = 3;
		int subsetMax = 5;

		
		experiment1MakeFiles(subsetMin, subsetMax);
		
		wer = new WifiExperimentRunner();

		wer.setSubset(subsetMin, subsetMax);
		wer.set_NO_DATA_INSTANCES(5);
		int[] noDaysConsidered = { 2, 3 };
		wer.setNoDaysArray(noDaysConsidered);
		wer.turnLoggingOff();
		wer.setWithRanges(true);

		System.out.println("\n------ WER settings : ------");
		System.out.println(wer);
		System.out.println("----------------------------\n");

		wer.run();

	}

	public void copyOriginalHCToWifiExperimentRunnerInput(int subsetMin, int subsetMax, WERenums.TRANSFER_SETTINGS transferSettings){
		
		ArrayList<String> allHousesDir = Utils.getSubDirectories(WifiExperimentRunnerOld.HC_MMF_DIR);
		for (int i=subsetMin; i < subsetMax; i++) {
			System.out.println(allHousesDir.get(i));
			

			Utils.copyDirectory(new File(WifiExperimentRunnerOld.HC_MMF_DIR + allHousesDir.get(i) + "/"),
					new File(WifiExperimentRunnerOld.EXP_DIR + allHousesDir.get(i) + "/" + 
							WERenums.MMF_TYPE.HF + " " + WERenums.TRANSFER_SETTINGS.BOTH + "/"));

		}
		
	}

	public void copyOutputToWifiExperimentRunnerInput(int subsetMin, int subsetMax, String from, String to) {

		String outputDirName = WifiExperimentRunnerOld.EXP_DIR;

		String inputDirName = MetaFeatureMaker.outputDirName;
		File inputDir = new File(inputDirName);

		ArrayList<String> outputtedHousesDir = Utils.getSubDirectories(inputDirName);

		for (int i=0; i < subsetMax-subsetMin; i++) {
			String houseDir = outputtedHousesDir.get(i);

			Utils.copyDirectory(new File(inputDirName + "/" + houseDir), new File(outputDirName + houseDir));
		}
	}

	public static void main(String[] args) {
		ExperimentRunner t = new ExperimentRunner();

		t.experiment1();
	}

}
