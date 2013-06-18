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
						}
						else if(index_i == -1){
							groups.get(index_j).add(index_j);
						}
						else if(index_j == -1){
							groups.get(index_i).add(index_j);
						}
						else if(index_j != index_i){
							groups.get(index_i).addAll(groups.get(index_j));
							groups.remove(index_j);
						}
					}
					
				}
				
			}
			
			//Check of alle sensor ID's erin zitten, eenlingen toevoegen indien nodig
			
			//Meta feature ID's meegeven aan HouseData
		}
	}
	
	
}