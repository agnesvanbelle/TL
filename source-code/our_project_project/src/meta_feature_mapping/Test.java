package meta_feature_mapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import meta_feature_building.Meta_feature_building;
import meta_feature_building.Meta_features_apply_handcrafted;
import meta_feature_mapping.Meta_feature_mapping.Sensor_distance;
import data.HouseData;
import data.NameContainer;

public class Test{
	
//	public static final String[] houseNames = {"A", "B", "C", "D", "E"};
	
	public static final String[] houseNames = {"A", "B", "C"};
	public static final int nrHouses = houseNames.length;
	public static final String houseNamePrefix = "house";
	public static final int [] alpha = {5, 5,5,5,5};
	public static int [] beta = {14, 7,7,14,14};
	
	public static void main(String[] args) 
	{
		// Replicate mapping results as described by Koehn et al (comment all stuff below first)
//		replicate_mapping_Bonenkamp_et_al();
		
		ArrayList<HouseData> housesData = new ArrayList<HouseData>();
		
		for (String houseName : houseNames) {
			HouseData h = new HouseData(houseNamePrefix + houseName);
			housesData.add(h);
		}
		int target_house = 1;
		
		//Test procedure with automatic clustering
		automatic_clustering(housesData, target_house, Sensor_distance.Profiles_individ_KL_rel_KL);		
		
		// Test procedure of mapping with handmade clusters
//		boolean diffent_meta_features = true;
//		hand_made_clusters(housesData, target_house, diffent_meta_features, Meta_feature_mapping.Sensor_distance.Profiles_individ_SSE);
		
		HashMap<String, String> meta_Features_check = new  HashMap<String, String>();
		get_extended_names(meta_Features_check, housesData.get(target_house));
		
		for (HouseData houseData : housesData) {
			print_metaFeatures(houseData, meta_Features_check);
		}
		
	}
	
	
	/**
	 * Run mapping using hand made clusters (meta-features)
	 * @param housesData
	 * @param target_house_index
	 * @param diffent_meta_features using the meta-features designed to be the same or designed to be somewhat different
	 * @param distance_type the type of profiles and appropriate distance metric you would like to use for mapping
	 */
	private static void hand_made_clusters(ArrayList<HouseData> housesData, int target_house_index, boolean diffent_meta_features, Meta_feature_mapping.Sensor_distance distance_type)  {
		Meta_features_apply_handcrafted mf_hc = new Meta_features_apply_handcrafted();
		mf_hc.apply_hand_crafted_meta_features(housesData, diffent_meta_features);
		int two_hours = 60*60*2;
		Meta_feature_mapping map = new Meta_feature_mapping(two_hours, 5, 200,distance_type);
		map.map_metafeatures_one_to_one_heuristic(housesData, target_house_index); //2nd param. is index houseData 
	}

	/**
	 * Run mapping using automatic clustering 
	 * @param housesData 
	 * @param target_house_index
	 * @param distance_type the type of profiles and appropriate distance metric you would like to use for mapping
	 */
	private static void automatic_clustering(ArrayList<HouseData> housesData, int target_house_index, Meta_feature_mapping.Sensor_distance distance_type)
	{
		Meta_feature_building cluster_building = new Meta_feature_building(0.3f, beta);
		cluster_building.alpha_beta_clustering(housesData);
		
//		int one_hour = 60*60;
		int two_hours = 60*60*2;
		Meta_feature_mapping map = new Meta_feature_mapping(two_hours, 5, 200, distance_type);
		map.map_metafeatures_one_to_one_heuristic(housesData, target_house_index); //2nd param. is index houseData 
	}

	/**
	 * Prints the meta features that are found by mapping
	 * @param houseData
	 * @param meta_Features_check
	 */
	private static void print_metaFeatures(HouseData houseData, HashMap<String, String> meta_Features_check) {
		System.out.println("\n\n"+houseData.houseName + "\n");
		HashMap<String, String> mapping = new  HashMap<String, String>();
		String cluster_name = null;
		String meta_meta_name = null;
		for(Integer sensor_id: houseData.sensorList())
		{
			NameContainer sensor = HouseData.sensorContainer(sensor_id);
			cluster_name = sensor.metacontainer.name;
			if(!mapping.containsKey(cluster_name))
			{
				meta_meta_name =  meta_Features_check.get(sensor.metacontainer.metacontainer.name);
				mapping.put(cluster_name, meta_meta_name);
			}				
		}
		for(String cluster: mapping.keySet()){
			System.out.println(cluster + "\t\t-->" + mapping.get(cluster));
		}
		System.out.println("\n");
		
	}
	
	private static void get_extended_names(
		HashMap<String, String> meta_Features_check, HouseData houseData) {
		String cluster_name = null;
		String meta_meta_name = null;
		for(Integer sensor_id: houseData.sensorList())
		{
			NameContainer sensor = HouseData.sensorContainer(sensor_id);
			meta_meta_name =  sensor.metacontainer.metacontainer.name;
			if(!meta_Features_check.containsKey(meta_meta_name))
			{
				cluster_name = sensor.metacontainer.name;
				meta_Features_check.put(meta_meta_name, meta_meta_name+ "----" +cluster_name);
			}				
		}
		
	}
	
	/**
	 * Replicates the mapping described by Bonenkamp et al
	 */
	private static void replicate_mapping_Bonenkamp_et_al()
	{
		// To replicate the results by Bonenkamp at all the accuracy needs to 
		// be interpreted relaxed. E.g. bathroomdoor mapped to bathroom is considered
		// correct. Secondly the mapping of A to B by our algorithm is interpreted as 
		// the mapping of B to A in their implementation, because they map all target clusters
		// the source clusters. We do this the other way around because later the clusters that
		// map to the same clusters in another house are merged.
		
		ArrayList<HouseData> housesData = new ArrayList<HouseData>();
				
		housesData.add(new HouseData(houseNamePrefix + houseNames[0]));
		housesData.add(new HouseData(houseNamePrefix + houseNames[1]));
		
		boolean diffent_meta_features = false;
		
		// Map B to A
		int target_house = 0;	
		hand_made_clusters(housesData, target_house, diffent_meta_features, Meta_feature_mapping.Sensor_distance.Profiles_individ_KL_rel_KL);
		
		HashMap<String, String> meta_Features_check = new  HashMap<String, String>();
		get_extended_names(meta_Features_check, housesData.get(target_house));
		
		for (HouseData houseData : housesData) {
			print_metaFeatures(houseData, meta_Features_check);
		}
		
		// Map A to B
		target_house = 1;			
		hand_made_clusters(housesData, target_house, diffent_meta_features, Meta_feature_mapping.Sensor_distance.Profiles_individ_KL_rel_KL);
		
		meta_Features_check = new  HashMap<String, String>();
		get_extended_names(meta_Features_check, housesData.get(target_house));
		
		for (HouseData houseData : housesData) {
			print_metaFeatures(houseData, meta_Features_check);
		}
	}
}