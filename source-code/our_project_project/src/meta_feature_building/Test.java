package meta_feature_building;

import java.util.ArrayList;
import java.util.List;

import data.HouseData;


public class Test
{
	
	private static void printClusters(HouseData house, String prefix)
	{
		System.out.println("\n\n"+prefix);
		List<Integer>[] clusters = house.sensorClusters(HouseData.MAPPING_LEVEL_METAFEATURE);
		System.out.println("sensor list: ");
		for(int i =0;i<clusters.length;i++)
		{
			System.out.println("------------\ncluster: " + i);
			for(int j = 0; j<clusters[i].size();j++)
			{
				System.out.println(HouseData.sensorContainer(clusters[i].get(j)).name);
			}			
			System.out.println("");
		}
	}
	
	
	public static void main(String[] args)
	{
		HouseData house = new HouseData("houseA");
		ArrayList<HouseData> data = new ArrayList<HouseData>();
		data.add(house);
		int [] alpha = {5};
		int [] beta = {6};
		Meta_feature_building builder = new Meta_feature_building(alpha, beta);
		builder.alpha_beta_clustering(data);
		printClusters(house, "absolute alpha");	
		builder.set_relative_alpha(0.2f);
		builder.alpha_beta_clustering(data);
		printClusters(house, "relative alpha");
		
	}
}
