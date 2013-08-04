package glue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import data.HouseData;

import art.framework.utils.*;
import art.experiments.*;
import art.experiments.WERenums.TRANSFER_SETTINGS;
import art.experiments.WERenums.TRANSFER_TYPE;

/**
 * Runs the whole stuff: metafeature making + transfer algorithm resp. from
 * our_project_project and from arf.experiments.wifi
 * 
 * 
 */
public class ExperimentRunner {

	private WifiExperimentRunner wer = null;
	private MetaFeatureMaker mfm = null;


	public ExperimentRunner() {
		// constructor

	}

	
	public void experiment1MakeMappings(int subsetMin, int subsetMax) {

		Utils.resetDirectory(HouseData.outputDirName);
		Utils.resetDirectory(WifiExperimentRunner.EXP_DIR);
		
		
		for (int i=subsetMin; i < subsetMax; i++) {
			Utils.createDirectory(WifiExperimentRunner.EXP_DIR + HouseData.houseOutputDirPrefix + MetaFeatureMaker.allHouseNames[i]);
			System.out.println("Created dir " + WifiExperimentRunner.EXP_DIR + HouseData.houseOutputDirPrefix + MetaFeatureMaker.allHouseNames[i]);
		}
		
		mfm = new MetaFeatureMaker();
		
		System.out.println("\n------ MFM settings : ------");
		System.out.println(MetaFeatureMaker.getString());
		System.out.println("----------------------------\n");
		
//		mfm.runForSubset(HouseData.outputDirName, subsetMin, subsetMax, 
//				WERenums.MF_TYPE.AUTO, WERenums.CLUSTER_TYPE.CT_ABS, WERenums.PROFILE_TYPE.PR_SP, WERenums.DISTANCE_MEASURE.SSE, WERenums.TRANSFER_SETTINGS.ONLY_TRANSFER);

//		mfm.runForSubset(HouseData.outputDirName, subsetMin, subsetMax, 
//				WERenums.MF_TYPE.AUTO, WERenums.CLUSTER_TYPE.CT_ABS, WERenums.PROFILE_TYPE.PR_BOTH, WERenums.DISTANCE_MEASURE.SSE, WERenums.TRANSFER_SETTINGS.ONLY_TRANSFER);
//		
//		mfm.runForSubset(HouseData.outputDirName, subsetMin, subsetMax, 
//				WERenums.MF_TYPE.AUTO, WERenums.CLUSTER_TYPE.CT_REL, WERenums.PROFILE_TYPE.PR_SP, WERenums.DISTANCE_MEASURE.SSE, WERenums.TRANSFER_SETTINGS.ONLY_TRANSFER);
//
//		mfm.runForSubset(HouseData.outputDirName, subsetMin, subsetMax, 
//				WERenums.MF_TYPE.AUTO, WERenums.CLUSTER_TYPE.CT_REL, WERenums.PROFILE_TYPE.PR_BOTH, WERenums.DISTANCE_MEASURE.SSE, WERenums.TRANSFER_SETTINGS.ONLY_TRANSFER);
//		
//		mfm.runForSubset(HouseData.outputDirName, subsetMin, subsetMax, 
//				WERenums.MF_TYPE.AUTO, WERenums.CLUSTER_TYPE.CT_ABS, WERenums.PROFILE_TYPE.PR_SP, WERenums.DISTANCE_MEASURE.KL, WERenums.TRANSFER_SETTINGS.ONLY_TRANSFER);
//
//		mfm.runForSubset(HouseData.outputDirName, subsetMin, subsetMax, 
//				WERenums.MF_TYPE.AUTO, WERenums.CLUSTER_TYPE.CT_ABS, WERenums.PROFILE_TYPE.PR_BOTH, WERenums.DISTANCE_MEASURE.KL, WERenums.TRANSFER_SETTINGS.ONLY_TRANSFER);
//		
//		mfm.runForSubset(HouseData.outputDirName, subsetMin, subsetMax, 
//				WERenums.MF_TYPE.AUTO, WERenums.CLUSTER_TYPE.CT_REL, WERenums.PROFILE_TYPE.PR_SP, WERenums.DISTANCE_MEASURE.KL, WERenums.TRANSFER_SETTINGS.ONLY_TRANSFER);
//
		mfm.runForSubset(HouseData.outputDirName, subsetMin, subsetMax, 
				WERenums.MF_TYPE.AUTO, WERenums.CLUSTER_TYPE.CT_REL, WERenums.PROFILE_TYPE.PR_BOTH, WERenums.DISTANCE_MEASURE.KL, WERenums.TRANSFER_SETTINGS.ONLY_TRANSFER);
		
		
		mfm.runForSubset(HouseData.outputDirName, subsetMin, subsetMax, 
				WERenums.MF_TYPE.HC, WERenums.CLUSTER_TYPE.CT_REL, WERenums.PROFILE_TYPE.PR_BOTH, WERenums.DISTANCE_MEASURE.KL, WERenums.TRANSFER_SETTINGS.ONLY_TRANSFER);
		
		// copy our created experiment files to input dir
		copyOutputToWifiExperimentRunnerInput(subsetMin, subsetMax, HouseData.outputDirName, WifiExperimentRunner.EXP_DIR);

		// copy action id's mapped while making our experiments to lena's original files dir
		// to replace the original actionmap*.txt and *-as.txt files
		// this should only matter for house D and E really
		copyActionListFilesToOriginalHC(subsetMin, subsetMax);
		
		// copy (part of) her original files to input dir
		copyOriginalHCToWifiExperimentRunnerInput(subsetMin, subsetMax, WERenums.TRANSFER_SETTINGS.BOTH);
		
				
		// make classMapFile (maps activity names (for all activities from all processed houses) to a number, is done for SVM classifier in WER)
		mfm.saveClassMapFile(WifiExperimentRunner.classMapFile);
	}
	
	public void experiment1RunWER(int subsetMin, int subsetMax) {
		wer = new WifiExperimentRunner();

		wer.setSubset(subsetMin, subsetMax);
		wer.set_NO_DATA_INSTANCES(100);
		int[] noDaysConsidered = { 2, 3, 6,11,21};
		wer.setNoDaysArray(noDaysConsidered);
		wer.turnLoggingOff();
		wer.setWithRanges(true);

		System.out.println("\n------ WER settings : ------");
		System.out.println(wer);
		System.out.println("----------------------------\n");

			
		wer.run();
	}
	
	public void experiment1() {


		// Note that subsetMax - subsetMin should be > 1  for transfer case
		int subsetMin = 0;
		int subsetMax = 3;

	
		//experiment1MakeMappings(subsetMin, subsetMax);
		
		experiment1RunWER(subsetMin, subsetMax);
		
		
		settingsToFile();

	}
	

	public void settingsToFile() {
		BufferedWriter bw = null;
		try {
			File file = new File(WifiExperimentRunner.ROOT_DIR + "settings.txt");
			bw = new BufferedWriter(new FileWriter(file));


			if (mfm != null) {
				bw.write("\n------ MFM settings : ------\n");
				bw.write(MetaFeatureMaker.getString());
				bw.write("\n----------------------------\n");
				bw.write("\n");
			}
			
			if (wer != null) {
				bw.write("\n------ WER settings : ------\n");
				bw.write(wer.toString());
				bw.write("\n----------------------------\n");
				bw.write("\n");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (bw != null) {
				try {
					bw.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void copyOriginalHCToWifiExperimentRunnerInput(int subsetMin, int subsetMax, WERenums.TRANSFER_SETTINGS transferSettings){
		
		ArrayList<String> allHousesDir = Utils.getSubDirectories(WifiExperimentRunner.HC_MMF_DIR);
		ArrayList<String> outputtedHousesDir = Utils.getSubDirectories(MetaFeatureMaker.outputDirName);
		
		Collections.sort(allHousesDir);
		Collections.sort(outputtedHousesDir);
		
		for (int i=subsetMin; i < subsetMax; i++) {
			System.out.println(allHousesDir.get(i));
			System.out.println(outputtedHousesDir.get(i));
			
			if (allHousesDir.get(i).compareTo(outputtedHousesDir.get(i))==0) {
				
				
	
				Utils.copyDirectory(new File(WifiExperimentRunner.HC_MMF_DIR + allHousesDir.get(i) + "/"),
						new File(WifiExperimentRunner.EXP_DIR + allHousesDir.get(i) + "/" + 
								WERenums.MMF_TYPE.HC_MMF + " " + transferSettings + "/"));
			}
		}
		
		
		
	}
	
	public void copyActionListFilesToOriginalHC(int subsetMin, int subsetMax){

		String inputDirName = MetaFeatureMaker.outputDirName;

		ArrayList<String> outputtedHousesDir = Utils.getSubDirectories(inputDirName);		
		ArrayList<String> allHousesDirHC = Utils.getSubDirectories(WifiExperimentRunner.HC_MMF_DIR);
		
		Collections.sort(allHousesDirHC);
		Collections.sort(outputtedHousesDir);
		
		for (int i=0; i < subsetMax-subsetMin; i++) {
			
			String houseDirActionFiles = outputtedHousesDir.get(i) ;
			
			System.out.println("houseDirActionFiles: " + houseDirActionFiles);
			ArrayList<String> expDirs = Utils.getSubDirectories(HouseData.outputDirName + houseDirActionFiles + "/");
			String expDir = expDirs.get(0);
			
			String houseName = houseDirActionFiles.substring(houseDirActionFiles.length()-1);
			
			houseDirActionFiles = HouseData.outputDirName + houseDirActionFiles + "/" + expDir + "/";
			
			String asFile = "house" + houseName + "-as.txt";
			
			System.out.println("Copying " + houseDirActionFiles + asFile + " to " + 
								WifiExperimentRunner.HC_MMF_DIR + allHousesDirHC.get(i) + "/" + asFile);
			
			String actionMapFile = "actionMap" + houseName + ".txt";
			
			System.out.println("Copying " + houseDirActionFiles + actionMapFile + " to " + 
								WifiExperimentRunner.HC_MMF_DIR + allHousesDirHC.get(i) + "/" + actionMapFile);
			
			Utils.copyFile(new File(houseDirActionFiles + asFile),
							new File(WifiExperimentRunner.HC_MMF_DIR + allHousesDirHC.get(i) + "/" + asFile));
			
			Utils.copyFile(new File(houseDirActionFiles + actionMapFile),
					new File(WifiExperimentRunner.HC_MMF_DIR + allHousesDirHC.get(i) + "/" + actionMapFile));
		}
		
	}

	public void copyOutputToWifiExperimentRunnerInput(int subsetMin, int subsetMax, String from, String to) {

		String outputDirName = WifiExperimentRunner.EXP_DIR;

		String inputDirName = MetaFeatureMaker.outputDirName;

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
