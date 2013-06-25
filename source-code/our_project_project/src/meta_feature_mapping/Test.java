package meta_feature_mapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import meta_feature_building.Meta_feature_building;
import meta_feature_building.Meta_features_apply_handcrafted;
import data.HouseData;
import data.NameContainer;

public class Test{
	
//	public static final String[] houseNames = {"A", "B", "C", "D", "E"};
//	public static final int nrHouses = houseNames.length;
//	public static final String houseNamePrefix = "house";
//	public static final int [] alpha = {5, 5,5,5,5};
//	public static int [] beta = {14, 7,7,10,12};
	
	public static final String[] houseNames = { "D", "E"};
	public static final int nrHouses = houseNames.length;
	public static final String houseNamePrefix = "house";
	public static final int [] alpha = {5, 5,5,5,5};
	public static int [] beta = {14, 7,7,14,14};
	
	public static void main(String[] args) throws IOException
	{
		
		ArrayList<HouseData> housesData = new ArrayList<HouseData>();
		
		for (String houseName : houseNames) {
			HouseData h = new HouseData(houseNamePrefix + houseName);
			housesData.add(h);
		}
		int target_house = 1;
		
//		automatic_clustering(housesData, target_house);		
		
		boolean diffent_meta_features = true;
		hand_made_clusters(housesData, target_house, diffent_meta_features, Meta_feature_mapping.Sensor_distance.Profiles_individ_SSE);
		
		HashMap<String, String> meta_Features_check = new  HashMap<String, String>();
		get_extended_names(meta_Features_check, housesData.get(target_house));
		
		for (HouseData houseData : housesData) {
			print_metaFeatures(houseData, meta_Features_check);
		}
	
		for (HouseData houseData : housesData) {
			houseData.formatLena(HouseData.MAPPING_LEVEL_METAMETAFEATURE, HouseData.MAPPING_LEVEL_METAMETAFEATURE);
		}
	}
	
	

	private static void hand_made_clusters(ArrayList<HouseData> housesData, int target_house_index, boolean diffent_meta_features, Meta_feature_mapping.Sensor_distance distance_type) throws IOException {
		Meta_features_apply_handcrafted.apply_hand_crafted_meta_features(housesData, diffent_meta_features);
		int two_hours = 60*60*2;
		Meta_feature_mapping map = new Meta_feature_mapping(two_hours, 5, 200,distance_type);
		map.map_metafeatures_one_to_one_heuristic(housesData, target_house_index); //2nd param. is index houseData 
	}

	private static void automatic_clustering(ArrayList<HouseData> housesData, int target_house_index, Meta_feature_mapping.Sensor_distance distance_type)
	{
		Meta_feature_building cluster_building = new Meta_feature_building(0.3f, beta);
		cluster_building.alpha_beta_clustering(housesData);
		
//		int one_hour = 60*60;
		int two_hours = 60*60*2;
		Meta_feature_mapping map = new Meta_feature_mapping(two_hours, 5, 200, distance_type);
		map.map_metafeatures_one_to_one_heuristic(housesData, target_house_index); //2nd param. is index houseData 
	}

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
}