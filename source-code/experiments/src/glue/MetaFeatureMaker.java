package glue;

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

/**
 * !!! static class !!!
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
	public static final int[] alphaAllHouses = { 3, 3, 3, 5, 5 }; 		// TODO: read/set from something
	public static double relativeAlpha = 0.3; 							// TODO: check with relative alpha
	public static int[] betaAllHouses = { 14, 10, 10, 10, 12 }; 		// TODO: read/set from something
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

		HouseData.mapActivities("Toileting", "use-toilet");

		HouseData.mapActivities("Bathing", "take-shower");

		HouseData.mapActivities("Grooming", "brush-teeth");

		HouseData.mapActivities("Sleeping", "go-to-bed");
		HouseData.mapActivities("Resting", "go-to-bed");

		HouseData.mapActivities("Preparing-breakfast", "prep-breakfast");

		HouseData.mapActivities("Preparing-lunch", "prep-dinner");
		HouseData.mapActivities("Preparing-dinner", "prep-dinner");
		HouseData.mapActivities("Preparing-a-snack", "prep-dinner");

		HouseData.mapActivities("Preparing-a-beverage", "get-drink");

	}

	public static void fetchSpecs(Directory d) {

	}

	public static void runForSubsetClusterMapping(int min_nr, int max_nr, 
			WERenums.PROFILE_TYPE profileType, WERenums.CLUSTER_TYPE clusterType , WERenums.MF_TYPE mfType) { // first max_nr houses
		if (max_nr > nrAllHouses) {
			System.err.println("There are only " + nrAllHouses + " houses you called runForSubset with " + max_nr);
		}

		String[] houseNames = Arrays.copyOfRange(allHouseNames, min_nr, max_nr);
		int[] alphas = Arrays.copyOfRange(alphaAllHouses, min_nr, max_nr);
		int[] betas = Arrays.copyOfRange(betaAllHouses, min_nr, max_nr);
		//double alpha = // TODO: check with relative alpha

		
		
		System.out.println("Making metafeatures for houses: " + Arrays.toString(houseNames));

		ArrayList<HouseData> housesData = getHousesData(houseNames);
		makeMappingForHouseDandE();
		
		// build metafeatures
		Meta_feature_building mfb = new Meta_feature_building();
		
		
		Meta_feature_mapping.Sensor_distance sd;
		
	
		if (mfType == WERenums.MF_TYPE.AUTO) { 
			switch (clusterType) {
				case CT_ABS:
					mfb.set_relative_alpha(relativeAlpha);
					break;
				case CT_REL:
					mfb.set_alphas(alphas);
					mfb.set_betas(betas);
					break;
				default:
					System.err.println("No clusterType provided going with " + WERenums.CLUSTER_TYPE.CT_ABS);
					mfb.set_alphas(alphas);
					mfb.set_betas(betas);
					break;		
			}
			
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
			
			mfb.alpha_beta_clustering(housesData);
		}
		
		else if  (mfType == WERenums.MF_TYPE.HC) {
			Meta_features_apply_handcrafted.apply_hand_crafted_meta_features(housesData, diffent_meta_features);
		}
		
	
		

	
		for (int targetHouseIndex = 0; targetHouseIndex < max_nr-min_nr; targetHouseIndex++) {

			
			//createMetaFeatures(housesData, targetHouseIndex, bin_width_start_time, nr_bins_duration, max_length_duration);
			
	
			Meta_feature_mapping map = new Meta_feature_mapping(bin_width_start_time, bin_width_start_time, max_length_duration, sd);
			map.map_metafeatures_one_to_one_heuristic(housesData, targetHouseIndex);

			HouseData targetHouse = housesData.get(targetHouseIndex);
			
			
			targetHouse.formatLena(HouseData.MAPPING_LEVEL_METAMETAFEATURE, HouseData.MAPPING_LEVEL_METAMETAFEATURE);

			System.out.println("Created metafeatures for house " + houseNames[targetHouseIndex]);
		}
		
	}



	public static void main(String[] args) {
		System.out.println("Doing nothing.");
		System.out.println("Better to invoke \"our_project_project.experiment.MetaFeatureMaker.java\" from package \"experiments.glue\".");
	}

}
