package glue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import meta_feature_building.Meta_feature_building;
import meta_feature_building.Meta_features_apply_handcrafted;
import meta_feature_mapping.Meta_feature_mapping;
import data.HouseData;
import art.framework.utils.*;
import art.experiments.*;
import art.experiments.WERenums.CLUSTER_TYPE;
import art.experiments.WERenums.PROFILE_TYPE;
import art.experiments.wifi.data.processor.WifiUtils;

/**
 * 
 * Creates the metafeatures Does the whole procedure Place were parameters need
 * * to be given though
 * 
 * NOTE invoke the methods in this class from project experiment, package glue
 * 
 */
public class MetaFeatureMaker {

	public static final String[] allHouseNames = { "A", "B", "C", "D", "E" }; // TODO: read from folder contents
	public static final int nrAllHouses = allHouseNames.length; // TODO: read from folder contents
	public static final String houseNamePrefix = "house";

	public static String outputDirName = HouseData.outputDirName;
	public static int one_hour = 60 * 60;

	/* set the following to something resonable! */
	public static final int[] alphaAllHouses = { 3, 3, 3, 5, 5 }; // TODO: read/set from something
	public static double relativeAlpha = 0.3; // TODO: check with relative alpha
	public static int[] betaAllHouses = { 14, 10, 10, 10, 12 }; // TODO: read/set from something
	/**/
	public static int bin_width_start_time = one_hour * 2;
	public static int nr_bins_duration = 5;
	public static int max_length_duration = 200;

	public static boolean diffent_meta_features = true; // for when handcrafted mf

	private static ArrayList<HouseData> getHousesData(String[] houseNames) {
		ArrayList<HouseData> housesData = new ArrayList<HouseData>();

		for (String houseName : houseNames) {
			HouseData h = new HouseData(houseNamePrefix + houseName);
			housesData.add(h);
		}
		return housesData;
	}

	/**
	 * @param housesData : @see getHousesData(String[] houseNames)
	 * @param targetHouseIndex : target house
	 * @param bin_width_start_time : size of bins for discretization of the
	 *            activation start time dimension
	 * @param nr_bins_duration : amount of bins for duration dimension
	 * @param max_length_duration : durations above this value are placed in the
	 *            last max_duration bin
	 */
	public static void createMetaFeatures(ArrayList<HouseData> housesData, int targetHouseIndex, int bin_width_start_time, int nr_bins_duration, int max_length_duration) {

		Meta_feature_mapping.Sensor_distance sd = Meta_feature_mapping.Sensor_distance.Profiles_individ_SSE;
		Meta_feature_mapping map = new Meta_feature_mapping(bin_width_start_time, bin_width_start_time, max_length_duration, sd);
		map.map_metafeatures_one_to_one_heuristic(housesData, targetHouseIndex);

	}

	public static void makeMappingForHouseDandE() {
		HouseData.mapActivities("Going-out-to-work", "leave-house");
		HouseData.mapActivities("Taking-out-the-trash", "leave-house");
		HouseData.mapActivities("Lawnwork", "leave-house");
		HouseData.mapActivities("Going-out-to-school", "leave-house");
		HouseData.mapActivities("Going-out-for-entertainment", "leave-house");
		HouseData.mapActivities("Going-out-for-shopping", "leave-house");
		HouseData.mapActivities("Going-out-to-exercise", "leave-house");
		
		HouseData.mapActivities("Toileting", "use-toilet");

		HouseData.mapActivities("Bathing", "take-shower");

		HouseData.mapActivities("Grooming", "brush-teeth"); // ?

		HouseData.mapActivities("Sleeping", "go-to-bed");
		HouseData.mapActivities("Resting", "go-to-bed");

		HouseData.mapActivities("Preparing-breakfast", "prep-breakfast");

		HouseData.mapActivities("Preparing-lunch", "prep-dinner");
		HouseData.mapActivities("Preparing-dinner", "prep-dinner");
		HouseData.mapActivities("Preparing-a-snack", "prep-dinner");

		HouseData.mapActivities("Preparing-a-beverage", "get-drink");

	}

	public static void saveClassMapFile(String fileName) {
		Integer[] activityIDList = HouseData.activityListAll(HouseData.MAPPING_LEVEL_METAMETAFEATURE);

		BufferedWriter bf = null;

		
		try {
			bf = new BufferedWriter(new FileWriter(fileName, false));

			for (int activityIndex=0; activityIndex < activityIDList.length; activityIndex++) {
				Integer id = activityIDList[activityIndex ];
				//System.out.println("activity ID: " + id);
				String activityName = HouseData.activityContainer(id).name;
				//System.out.println("activity name: " + activityName);
				//String activityName = HouseData.indexID[HouseData.TYPE_DATA_ACTIVITY].get(id).name;
				bf.write(activityIndex + "," + activityName + "\n");
				
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (bf != null) {
				try {
					bf.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Make and save metafeatures for subset of houses with specified settings
	 * 
	 * @param min_nr
	 * @param max_nr
	 * @param profileType
	 * @param clusterType
	 * @param mfType
	 */
	public static void runForSubset(String rootOutputDir, int min_nr, int max_nr, WERenums.MF_TYPE mfType, WERenums.CLUSTER_TYPE clusterType, WERenums.PROFILE_TYPE profileType,
			WERenums.TRANSFER_SETTINGS trSetting) {


		if (max_nr > nrAllHouses) {
			System.err.println("There are only " + nrAllHouses + " houses you called runForSubset with " + max_nr);
			return;
		}

		// get settings for specified subset  of houses
		String[] houseNames = Arrays.copyOfRange(allHouseNames, min_nr, max_nr);
		int[] alphas = Arrays.copyOfRange(alphaAllHouses, min_nr, max_nr);
		int[] betas = Arrays.copyOfRange(betaAllHouses, min_nr, max_nr);
		ArrayList<HouseData> housesData = getHousesData(houseNames);

		System.out.println("Making metafeatures for houses: " + Arrays.toString(houseNames) + " (Experiment settings: " + mfType + "," + clusterType + ", " + profileType + ", " + trSetting + ")");

		// map house D and E sensors (AFTER housesData has been made!)
		makeMappingForHouseDandE();

		//////// build metafeatures ////////
		// TODO: these should inherit the same Interface or class...
		Meta_feature_building mfb = new Meta_feature_building();
		Meta_features_apply_handcrafted mfb_hc = new Meta_features_apply_handcrafted();

		// automatic metafeatures (not handcrafted)
		if (mfType == WERenums.MF_TYPE.AUTO) {
			// clustering: abs or relative alpha 
			switch (clusterType) {
			case CT_REL:
				mfb.set_relative_alpha(relativeAlpha);
				mfb.set_betas(betas);
				break;
			case CT_ABS:
				mfb.set_alphas(alphas);
				mfb.set_betas(betas);
				break;
			default:
				System.err.println("No clusterType provided going with " + WERenums.CLUSTER_TYPE.CT_ABS);
				mfb.set_alphas(alphas);
				mfb.set_betas(betas);
				break;
			}

			mfb.alpha_beta_clustering(housesData);

		}
		// handcrafted metafeatures
		else if (mfType == WERenums.MF_TYPE.HC) {
			mfb_hc.apply_hand_crafted_meta_features(housesData, diffent_meta_features);
		}

		//////// map metafeatures ////////
		Meta_feature_mapping.Sensor_distance sd;
		// sensor distances (for mapping):  only sensor profile, or both sensor profile and relational profile
		switch (profileType) {
		case PR_SP:
			sd = Meta_feature_mapping.Sensor_distance.Profiles_individ_SSE;
			break;
		case PR_BOTH:
			sd = Meta_feature_mapping.Sensor_distance.Profiles_individ_SSE_rel_OL;
			break;
		default:
			System.err.println("No Meta_feature_mapping provided going with " + Meta_feature_mapping.Sensor_distance.Profiles_individ_SSE);
			sd = Meta_feature_mapping.Sensor_distance.Profiles_individ_SSE;
			break;
		}

		// for all specified houses
		for (int targetHouseIndex = 0; targetHouseIndex < max_nr - min_nr; targetHouseIndex++) {

			Meta_feature_mapping map = new Meta_feature_mapping(bin_width_start_time, bin_width_start_time, max_length_duration, sd);
			map.map_metafeatures_one_to_one_heuristic(housesData, targetHouseIndex);

			//WifiUtils.stop();
			String outputSubDir = new String(rootOutputDir + HouseData.houseOutputDirPrefix + allHouseNames[targetHouseIndex + min_nr] + "/" + mfType + " " + clusterType + " " + profileType + " "
					+ trSetting + "/");
			Utils.createDirectory(outputSubDir);
			//System.out.println("outputSUbDir " + outputSubDir);

			HouseData targetHouse = housesData.get(targetHouseIndex);
			targetHouse.formatLena(outputSubDir, HouseData.MAPPING_LEVEL_METAMETAFEATURE, HouseData.MAPPING_LEVEL_METAMETAFEATURE);

			System.out.println("Created metafeatures for house " + houseNames[targetHouseIndex]);
		}

	}

	public static void main(String[] args) {
		System.out.println("Doing nothing.");
		System.out.println("Better to invoke \"our_project_project.experiment.MetaFeatureMaker.java\" from package \"experiments.glue\".");
	}

}
