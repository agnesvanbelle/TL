package meta_feature_building;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import data.DataPoint;
import data.HouseData;
import data.NameContainer;


public class Test
{
	public static void main(String[] args) throws IOException
	{
		String[] house_letters = {"A", "B", "C", "D","E"};
		
//		check_sensors_activities();
		
//		hand_crafted_clusters (house_letters);
		
		generate_clusters("houseB", "Absolute", 3, 10);
		
	}
	
	private static void printClusters(HouseData house, String prefix)
	{
		System.out.println("\n\n"+prefix);
		List<Integer>[] clusters = house.sensorClusters(HouseData.MAPPING_LEVEL_METAFEATURE);
		for(int i =0;i<clusters.length;i++)
		{
//			System.out.println(HouseData.sensorContainer(clusters[i].get(0)).name);
			System.out.println("------------\ncluster: " + i + " " + HouseData.sensorContainer(clusters[i].get(0)).metacontainer.name );
			for(int j = 0; j<clusters[i].size();j++)
			{
				System.out.println("\t"+HouseData.sensorContainer(clusters[i].get(j)).name);
			}			
			System.out.println("");
		}
	}
	
	public static void generate_clusters(String houseName, String alphaType, int alfa_abs, int beta_abs)
	{
		HouseData house = new HouseData(houseName);
		ArrayList<HouseData> data = new ArrayList<HouseData>();
		data.add(house);
		int [] alpha = {alfa_abs};
		int [] beta = {beta_abs};
		float relative = 0.5f;
		Meta_feature_building builder = new Meta_feature_building(alpha, beta);
		
		if(alphaType == "Absolute" || alphaType == "Both")
		{
			builder.alpha_beta_clustering(data);
			printClusters(house, "absolute alpha");
		}
		
		if(alphaType == "Relative" || alphaType == "Both")
		{
			builder.set_relative_alpha(relative);
			builder.alpha_beta_clustering(data);
			printClusters(house, "relative alpha");
		}
	}
	
	public static void hand_crafted_clusters (String[] houseLetters) throws IOException
	{

		ArrayList<HouseData> data = new ArrayList<HouseData>();
		
		for(String houseLetter: houseLetters)
		{			
			HouseData house = new HouseData("house" + houseLetter);
			data.add(house);
		}		
		boolean diffent_meta_features = false;
		Meta_features_apply_handcrafted.apply_hand_crafted_meta_features(data, diffent_meta_features);
		for(HouseData house: data){
			printClusters(house, "Hand crafted ");
		}
	}
	
	
	
	
}
