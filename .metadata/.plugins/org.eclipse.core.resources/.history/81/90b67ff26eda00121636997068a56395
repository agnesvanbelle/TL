package meta_feature_mapping;

import java.util.ArrayList;

import data.HouseData;
import data.NameContainer;

public class Meta_feature_mapping{
	
	public static void map_metafeatures_one_to_one_heuristic(ArrayList<HouseData> data, int target_house_index)
	{
		HouseData target_house = data.get(target_house_index);
		ArrayList<String[][]> mappings = new ArrayList<>();
		for(int house_index = 0; house_index < data.size(); house_index++)
		{ 			
			if(house_index!=target_house_index)
			{
				HouseData source_house = data.get(house_index);				
				mappings.add(get_mapping_houses(target_house, source_house) );				
			}
			else
			{
				mappings.add(null);
			}
		}
	}
	
	public static String[][] get_mapping_houses(HouseData house_1, HouseData house_2) {
		NameContainer[] sensors_house_1 = house_1.sensorList();
		NameContainer[] sensors_house_2 = house_2.sensorList();
		return null;
	}

	/**
	 * Calculates cluster distance as defined by Koen et al.
	 * @param cluster_1 List of ID's of sensors belonging to cluster 1
	 * @param cluster_2 List of ID's of sensors belonging to cluster 1
	 * @return cluster distance
	 */
	public static double cluster_distance(int[] cluster_1, int[] cluster_2, HouseData house_1, HouseData house_2)
	{
		int blockSizeStart = 6;
		int blockNumLength = 6;
		
		int[] small_cluster, large_cluster;
		HouseData house_small, house_large;
		double avg_min_divergence = 0.0;
		
		
		if(cluster_1.length>cluster_2.length)
		{
			small_cluster 	= cluster_2;
			house_small 	= house_2;
			large_cluster 	= cluster_1;
			house_large 	= house_1;
		}
		else
		{
			small_cluster 	= cluster_1;
			house_small 	= house_1;
			large_cluster 	= cluster_2;
			house_large 	= house_2;
		}	
		
		
		
		for(int i = 0; i<small_cluster.length; i++)
		{
			int sensor_id_s = small_cluster[i];
			double min_div = 1000000.0;
			for(int j = 0; j<large_cluster.length; j++)
			{
				int sensor_id_l = large_cluster[j];
				double current_min_div = -1.0;
				
				double[][] hist_s = house_small.getProfileSensor(sensor_id_s, blockSizeStart ,blockNumLength);
				double[][] hist_l = house_small.getProfileSensor(sensor_id_l, blockSizeStart ,blockNumLength);
				current_min_div = bhattacharyya_dist(hist_s, hist_l);
				
				if(current_min_div < min_div)
				{
					min_div = current_min_div;
				}
			}
			avg_min_divergence += min_div;
			
		}
		return avg_min_divergence / (double)small_cluster.length;
		
	}
	
	public static double bhattacharyya_dist(double[][] hist_1, double[][] hist_2)
	{
		double bcoeff = 0.0;
		int nr_bins_x = hist_1.length;
		int nr_bins_y = hist_1[0].length;
				
		for(int i = 0; i<nr_bins_x; i++){
			for(int j = 0; j< nr_bins_y; j++)
			{
				bcoeff += Math.sqrt(hist_1[i][j]*hist_2[i][j]);
			}
		}
		return Math.sqrt(1-bcoeff);
	}
	
	
}