package meta_feature_building;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import data.HouseData;
import data.NameContainer;
import data.NameContainer;

public class Meta_feature_building{
	
	/**
	 * 
	 * @param data
	 * @param alphas
	 * @param betas in seconds
	 */
	public static void alpha_beta_clustering(ArrayList<HouseData> data, int[] alphas, int[] betas)
	{
		int index = 0;
		for(HouseData house: data){
			
			// Get sensornamers, alpha and beta for this house
			int alpha = alphas[index];
			int beta = betas[index];
			NameContainer[] sensors = house.sensorList();
			index++;
			// Get frequencies for a time difference <beta
			int[][] frequencies = house.profileAlphaBeta(beta);
			
			ConcurrentSkipListSet<Integer> sensorsSet = new ConcurrentSkipListSet<Integer>();
			ArrayList<ConcurrentSkipListSet<Integer>> groups = new ArrayList<ConcurrentSkipListSet<Integer>>();
			ConcurrentSkipListSet<Integer> sensorsList = new ConcurrentSkipListSet<Integer> ();
			// For each sensor combination
			for(int i =0;i<sensors.length;i++)
			{
				for(int j=i;j<sensors.length;j++)
				{
					// If the frequency is larger than alpha
					if(frequencies[i][j]>alpha)
					{
						int index_i = -1;
						int index_j = -1;					
						// See if the sensors are already part of a cluster
						for(int group_index =0; group_index < groups.size();group_index++)
						{
							if(groups.get(group_index).contains(i))
							{
								index_i = group_index;
							}
							if(groups.get(group_index).contains(j)){
								index_j = group_index;
							}
						}
						// Check if the sensor(s) can be added to a cluster or if two clusters need to be merged 
						if(index_i == -1 && index_j==-1){
							groups.add(new ConcurrentSkipListSet<Integer>());
							groups.get(groups.size()-1).add(index_i);
							groups.get(groups.size()-1).add(index_j);
							sensorsSet.add(index_i);
							sensorsSet.add(index_j);
						}
						else if(index_i == -1){
							groups.get(index_j).add(index_i);
							sensorsSet.add(index_i);
						}
						else if(index_j == -1){
							groups.get(index_i).add(index_j);
							sensorsSet.add(index_j);
						}
						else if(index_j != index_i){
							groups.get(index_i).addAll(groups.get(index_j));
							groups.remove(index_j);
						}
					}
					
				}
				
			}
			
			//Check if all sensor ID's are in a group and if not add singletons as a group
			if(sensorsSet.size()<sensors.length)
			{
				for(int i =0;i<sensors.length;i++){
					if(!sensorsSet.contains(i))
					{
						groups.add(new ConcurrentSkipListSet<Integer>());
						groups.get(groups.size()-1).add(i);
					}
				}
			}
			
			
			//Map sensors with meta feature ID's in houseData
			for(int group_index = 0; group_index<groups.size();group_index++)
			{
				String meta_feature_name = "metaFeature_" + house.houseName + "_" + group_index;
				for(int sensor_index: groups.get(group_index))
				{
					HouseData.mapSensors(sensors[sensor_index].name, meta_feature_name);
				}
			}
		}
	}
	
	
}