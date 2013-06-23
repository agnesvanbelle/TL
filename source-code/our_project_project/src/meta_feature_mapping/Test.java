package meta_feature_mapping;

import java.util.ArrayList;

import meta_feature_building.Meta_feature_building;
import data.HouseData;

public class Test{
	
//	public static final String[] houseNames = {"A", "B", "C", "D", "E"};
//	public static final int nrHouses = houseNames.length;
//	public static final String houseNamePrefix = "house";
//	public static final int [] alpha = {5, 5,5,5,5};
//	public static int [] beta = {14, 7,7,10,12};
	
	public static final String[] houseNames = {"A", "B", "C", "D", "E"};
	public static final int nrHouses = houseNames.length;
	public static final String houseNamePrefix = "house";
	public static final int [] alpha = {5, 5,5,5,5};
	public static int [] beta = {14, 7,7,14,14};
	
	public static void main(String[] args)
	{
		
		ArrayList<HouseData> housesData = new ArrayList<HouseData>();
		
		for (String houseName : houseNames) {
			HouseData h = new HouseData(houseNamePrefix + houseName);
			housesData.add(h);
		}
		
		
		Meta_feature_building cluster_building = new Meta_feature_building(0.3f, beta);
		cluster_building.alpha_beta_clustering(housesData);
		
//		int one_hour = 60*60;
		int two_hours = 60*60*2;
		Meta_feature_mapping map = new Meta_feature_mapping(two_hours, 5, 200);
		map.map_metafeatures_one_to_one_heuristic(housesData, 0); //2nd param. is index houseData 
		
		
		for (HouseData houseData : housesData) {
			System.out.println("---------------------");
			System.out.println("House: "+ houseData.houseName);
			System.out.println("---------------------");
			
			for(Integer sensor: houseData.sensorList())
			{
				System.out.print("sensor name: " + HouseData.sensorContainer(sensor).name + ", sensor id: " + HouseData.sensorContainer(sensor).ID + "\n" );
				System.out.print("metacontainer name: " + HouseData.sensorContainer(sensor).metacontainer.name + ", ");
					System.out.print("metacontainer ID: " + HouseData.sensorContainer(sensor).metacontainer.ID + "\n");
				System.out.print("metacontainer.metacontainer name: " + HouseData.sensorContainer(sensor).metacontainer.metacontainer.name + ", ");		
					System.out.print("metacontainer.metacontainer ID: " + HouseData.sensorContainer(sensor).metacontainer.metacontainer.ID + "\n\n");			
			}
		}
	
		for (HouseData houseData : housesData) {
			houseData.formatLena(HouseData.MAPPING_LEVEL_METAMETAFEATURE, HouseData.MAPPING_LEVEL_METAMETAFEATURE);
		}
	}
}