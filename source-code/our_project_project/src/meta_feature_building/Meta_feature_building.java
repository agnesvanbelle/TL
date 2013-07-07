package meta_feature_building;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListSet;



import data.HouseData;

public class Meta_feature_building{
	
	
	private int[] alphas;
	private int[] betas;
	private double single_alpha;
	private Alpha_beta_type ab_type;
	
	
	public Meta_feature_building () {
		
	}
	
	public Meta_feature_building (int[] alphas, int[] betas)
	{
		this.alphas = alphas;
		this.betas = betas;
		ab_type = Alpha_beta_type.Absolute;
	}
	
	public Meta_feature_building (float alpha, int[] betas)
	{
		single_alpha = alpha;
		this.betas = betas;		
		ab_type = Alpha_beta_type.Relative;
	}
	
	
	public static enum Alpha_beta_type {Relative, Absolute};
	/**
	 * 
	 * @param data
	 * @param alphas (In a percentage in integers in case of a relative alpha)
	 * @param betas in seconds
	 */
	public void alpha_beta_clustering(ArrayList<HouseData> data)
	{
		int index = 0;
		
		for(HouseData house: data){
//			System.out.println("house: " + house.houseName);
			// Get sensor names, alpha and beta for this house
			 
			int beta = betas[index];
			Integer[] sensors = house.sensorList();			
			// Get frequencies for a time difference <beta
			int[][] frequencies = house.profileAlphaBeta(beta);
			
			////////////DEBUGGING/////////////////////
			//print_frequencies(frequencies);
			Integer[] sensssors = house.sensorList();
			int index2 =0;
			for(Integer i: sensssors){
				//System.out.println(index2+" " + HouseData.sensorContainer(i).name);
				index2++;
			}
			////////////DEBUGGING/////////////////////
			
			ConcurrentSkipListSet<Integer> sensorsSet = new ConcurrentSkipListSet<Integer>();
			ArrayList<ConcurrentSkipListSet<Integer>> groups = new ArrayList<ConcurrentSkipListSet<Integer>>();
			
			int alpha = -1;
			
			// For each sensor combination
			for(int i =0;i<sensors.length;i++)
			{
				if(ab_type == Alpha_beta_type.Relative)
				{
					// Get frequencies for sensor firings by the sensor.
					Integer freq = house.sensorFiringFrequency(sensors[i]);					
					alpha = (int) ((float)freq*single_alpha) +1;
				}
				else if(ab_type == Alpha_beta_type.Absolute)
				{
					alpha = alphas[index];
//					System.out.println("relative alpha: " + alpha);
				}
				for(int j=i;j<sensors.length;j++)
				{
					// If the frequency is larger than alpha
					if(frequencies[i][j]+frequencies[j][i]>alpha)
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
							groups.get(groups.size()-1).add(i);
							groups.get(groups.size()-1).add(j);
							sensorsSet.add(i);
							sensorsSet.add(j);
						}
						else if(index_i == -1){
							groups.get(index_j).add(i);
							sensorsSet.add(i);
						}
						else if(index_j == -1){
							groups.get(index_i).add(j);
							sensorsSet.add(j);
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
//					System.out.print("sensor" + HouseData.sensorContainer(sensors[sensor_index]).name + "\t\t");
					HouseData.mapSensors(HouseData.sensorContainer(sensors[sensor_index]).name, meta_feature_name);
//					System.out.println(HouseData.sensorContainer(sensors[sensor_index]).metacontainer.name);
				}
//				System.out.println(" ");
			}
			index++;
		}		
	}
	
	public void set_relative_alpha(double alpha)
	{
		single_alpha = alpha;
		ab_type = Alpha_beta_type.Relative;
	}
	
	public void set_alphas(int[] alphas)
	{
		this.alphas = alphas;
		ab_type = Alpha_beta_type.Absolute;
	}
	
	public void set_betas(int[] betas)
	{
		this.betas = betas;
	}
	
	public void print_frequencies(int[][] freq)
	{
		for(int i = 0;i<freq.length;i++)
		{
			for(int j =0; j<freq[i].length;j++)
			{
				System.out.print(freq[i][j] + "\t");				
			}
			System.out.print("\n");
		}
	}
	
	
}ngth;i++)
		{
			for(int j =0; j<freq[i].length;j++)
			{
				System.out.print(freq[i][j] + "\t");				
			}
			System.out.print("\n");
		}
	}
	
	
}