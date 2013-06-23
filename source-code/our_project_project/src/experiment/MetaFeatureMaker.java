package experiment;

import java.util.ArrayList;
import java.util.Arrays;

import meta_feature_building.Meta_feature_building;
import meta_feature_mapping.Meta_feature_mapping;
import data.HouseData;

/* static class */
/**
 * Creates the metafeatures
 * Does the whole procedure
 * Place were parameters need to be given though 
 *
 */
public class MetaFeatureMaker {
	
	
	public static final String[] allHouseNames = {"A", "B", "C", "D", "E"}; // TODO: read from folder contents
	public static final int nrAllHouses = allHouseNames.length; // TODO: read from folder contents
	public static final String houseNamePrefix = "house";
	public static final int [] alphaAllHouses = {5, 5,5,5,5}; // TODO: read/set from something
	public static int [] betaAllHouses = {14, 7,7,10,12}; // TODO: read/set from something
	
	public static String outputDirName = HouseData.outputDirName;
	
	public static	int one_hour = 60*60;
	
	private static ArrayList<HouseData> getHousesData(String[] houseNames) {
		ArrayList<HouseData> housesData = new ArrayList<HouseData>();
		
		for (String houseName : houseNames) {
			HouseData h = new HouseData( houseNamePrefix + houseName);
			housesData.add(h);
		}
		
		return housesData;
	}
	
	/**
	 * @param housesData : @see getHousesData(String[] houseNames) 
	 * @param targetHouseIndex : target house
	 * @param bin_width_start_time : size of bins for discretization of the activation start time dimension
	 * @param nr_bins_duration : amount of bins for duration dimension
	 * @param max_length_duration : durations above this value are placed in the last max_duration bin
	 */
	public static void createMetaFeatures(ArrayList<HouseData> housesData, int targetHouseIndex, int bin_width_start_time, int nr_bins_duration, int max_length_duration) {
		
		Meta_feature_mapping map = new Meta_feature_mapping(bin_width_start_time, bin_width_start_time, max_length_duration);
		map.map_metafeatures_one_to_one_heuristic(housesData, targetHouseIndex); 
		
	}
	
	
	public static void runForABC() { // first 3 houses
		System.out.println("Making metafeatures for house A, B and C.");
		
		int nrHouses = 3;
		String[] houseNames = Arrays.copyOfRange(allHouseNames, 0,nrHouses);
		int[] alpha = Arrays.copyOfRange(alphaAllHouses, 0, nrHouses);
		int[] beta = Arrays.copyOfRange(betaAllHouses, 0, nrHouses);
		
		
		ArrayList<HouseData> housesData = getHousesData(houseNames);
		
		
		// build metafeatures
		Meta_feature_building.alpha_beta_clustering(housesData, alpha, beta);
		


		
		int bin_width_start_time = one_hour*2;
		int nr_bins_duration = 5;
		int max_length_duration = 200;
		
		for (int targetHouseIndex=0; targetHouseIndex < nrHouses; targetHouseIndex++) {
		
			createMetaFeatures(housesData, targetHouseIndex, bin_width_start_time, nr_bins_duration, max_length_duration);
	
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