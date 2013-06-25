package meta_feature_mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

import data.HouseData;

public class Meta_feature_mapping{
	
	// Needed to compute sensor profile with histogram
	public int blockSizeStart;
	public int blockNumLength;
	public int maxLengthDuration;
	Sensor_distance distance_metric;
	public float profile_weight; 
	
	/**
	 * Constructor for doing meta feature mapping using sensor profile histograms and relational profile
	 * as the distance mapping 
	 * @param bin_width_start_time : size of bins for discretization of the activation start time dimension
	 * @param nr_bins_duration : amount of bins for duration dimension
	 * @param max_length_duration : durations above this value are placed in the last max_duration bin
	 */
	public Meta_feature_mapping(int bin_width_start_time, int nr_bins_duration, int max_length_duration){
		blockSizeStart = bin_width_start_time;
		blockNumLength = nr_bins_duration;
		maxLengthDuration = max_length_duration;
		distance_metric = Sensor_distance.Profiles_individ;
		profile_weight = 0.5f;
	}
	
	public enum Sensor_distance {Profiles_individ, Profiles_individ_rel};
	
	/**
	 * Maps meta-features of each house to a shared feature space (meta-meta-features). This mapping is stored inside HouseData 
	 * @param data = ArrayList containing the data for each house
	 * @param target_house_index = index in the array of the target house
	 */
	public void map_metafeatures_one_to_one_heuristic(ArrayList<HouseData> data, int target_house_index)
	{	
		
		// Initialize variables
		HouseData target_house = data.get(target_house_index);
		ArrayList<HashMap<String, String>> mappings = new ArrayList<HashMap<String, String>>();
		List<Integer>[] clusters_target_house = target_house.sensorClusters(HouseData.MAPPING_LEVEL_METAFEATURE);
		String[] meta_meta_features_labels = build_labels(clusters_target_house);
		
		// For each house
		for(int house_index = 0; house_index < data.size(); house_index++)
		{ 			
			// If it is a source house
			if(house_index!=target_house_index)
			{
				// Map to target house
				HouseData source_house = data.get(house_index);				
				mappings.add(get_mapping_one_to_one_heuristic(target_house, source_house, clusters_target_house,  meta_meta_features_labels) );				
			}
			else
			{
				// Mapping target house to meta-meta-features
				HashMap<String, String> target_mapping = new HashMap<String, String>();
				for(int i = 0; i<meta_meta_features_labels.length; i++)
				{
					
					List<Integer> target_cluster = clusters_target_house[i];
					String target_cluster_name = HouseData.sensorContainer(target_cluster.get(0)).metacontainer.name;
					target_mapping.put(target_cluster_name, meta_meta_features_labels[i]);
				}								
				mappings.add(target_mapping);
			}
		}
		// Apply mappings in HouseData
		for(int i = 0; i<mappings.size(); i++)
		{
			HashMap <String,String> cluster_mapping = mappings.get(i);
			for(String key: cluster_mapping.keySet())
			{
				HouseData.mapSensors(key, cluster_mapping.get(key));
			}
		}
	}

	/**
	 * Returns the mapping from sensors CHECK to meta-meta-features
	 * @param target_house = data of the target house
	 * @param source_house = data of the source house
	 * @param clusters_target_house = clusters in the target house
	 * @param meta_meta_features_labels = labels of the meta-meta-features
	 * @return the mapping from sensors CHECK to meta-meta-features in a HashMap from sensor label to meta-meta-feature label
	 */
	private HashMap<String, String> get_mapping_one_to_one_heuristic(HouseData target_house, HouseData source_house, 
			List<Integer>[] clusters_target_house, String[]  meta_meta_features_labels) {
		
		//Initialize variables
		HashMap<String, String> mapping = new HashMap<String,String>();
		
//		Integer[] source_sensors = source_house.sensorList();
//		Integer[] target_sensors = target_house.sensorList();
		
		List<Integer>[] clusters_source_house = source_house.sensorClusters(HouseData.MAPPING_LEVEL_METAFEATURE);
		
		ConcurrentSkipListSet<Integer> cluster_indexes_target_house = new ConcurrentSkipListSet<Integer>();
		ConcurrentSkipListSet<Integer> cluster_indexes_source_house = new ConcurrentSkipListSet<Integer>();
		
		int nr_clusters_target = clusters_target_house.length;
		int nr_clusters_source = clusters_source_house.length;
				
		float[][] diff = new float[nr_clusters_target][nr_clusters_source];		
		for(int i =0; i<nr_clusters_target; i++)
		{
			for(int j = 0; j<nr_clusters_source; j++)
			{
				diff[i][j] = -1.0f;
			}
		}
		
		for(int i = 0; i<nr_clusters_source; i++)
		{
			cluster_indexes_source_house.add(i);
		}
		for(int i = 0; i<nr_clusters_target; i++)
		{
			cluster_indexes_target_house.add(i);
		}
		
		int match = 0;
		System.out.println("\n\nhouse: " + source_house.houseName);
		
		// While there are unmapped source sensors
		while(cluster_indexes_source_house.size()!=0) 
		{
			
			// If there are still target clusters to which no source cluster is mapped
			if(cluster_indexes_target_house.size()!=0)
			{
				match++;
				float[] best_candidate_diff = new float [cluster_indexes_target_house.size()];
				int[] best_candidate = new int [cluster_indexes_target_house.size()];
				int[] cluster_index = new int [cluster_indexes_target_house.size()];				
				int array_index = 0;
				
				// Find best candidate for each target house cluster
				for(int index_target:cluster_indexes_target_house)
				{
					float current_diff = -1.0f;
					float smallest_diff = 100000.0f;
					int candidate = -1;
//					System.out.println("\tCandidates:");
					for(int index_source: cluster_indexes_source_house)
					{
//						System.out.println("\t" + HouseData.sensorContainer(clusters_source_house[index_source].get(0)).metacontainer.name + " " +
//										HouseData.sensorContainer(clusters_target_house[index_target].get(0)).metacontainer.name );
						if(diff[index_target][index_source] == -1.0)
						{
							
								diff[index_target][index_source] = cluster_distance(clusters_target_house[index_target], clusters_source_house[index_source], target_house, source_house);
														
						}
						current_diff = diff[index_target][index_source];
						if(current_diff < smallest_diff)
						{
							smallest_diff = current_diff;
							candidate = index_source;
						}
						
					}
//					System.out.println("best match: " );
//					System.out.println("\t" + HouseData.sensorContainer(clusters_source_house[candidate].get(0)).metacontainer.name + " " +
//							HouseData.sensorContainer(clusters_target_house[index_target].get(0)).metacontainer.name );
					best_candidate_diff[array_index] = smallest_diff;
					best_candidate[array_index] = candidate;
					cluster_index[array_index] = index_target;
					array_index++;
				}
				// Find match with smallest distance
				float smallest_diff = 100000.0f;
				int source_cluster = -1;
				int target_cluster = -1;
				for(int i =0; i<best_candidate.length; i++)
				{
					if(best_candidate_diff[i]<smallest_diff)
					{
						smallest_diff = best_candidate_diff[i];
						source_cluster = best_candidate[i];
						target_cluster = cluster_index[i];
					}
				}
				// Put the mapping in the hash map
				String source_cluster_name = HouseData.sensorContainer(clusters_source_house[source_cluster].get(0)).metacontainer.name;
				String meta_meta_label = meta_meta_features_labels[target_cluster];
				System.out.println("Match: " + match + " " + source_cluster_name + "\t\t" + meta_meta_label);
				////////////////////DEBUGGING/////////////////////////////////
//				String target_cluster_name = HouseData.sensorContainer(clusters_target_house[target_cluster].get(0)).metacontainer.name;
//				System.out.println(source_cluster_name + "\t" + target_cluster_name +"\t\t" + meta_meta_label);
				////////////////////DEBUGGING/////////////////////////////////
				
				// Save mapping
				mapping.put(source_cluster_name, meta_meta_label);

				// Remove source and target clusters from sets
				cluster_indexes_target_house.remove(target_cluster);
				cluster_indexes_source_house.remove(source_cluster);	
				
				////////////////////DEBUGGING/////////////////////////////////
//				System.out.println("Removed clusters " + target_cluster + " " + source_cluster);
//				System.out.println("nr unmapped source clusters: " + cluster_indexes_source_house.size());
//				System.out.println("nr unmapped target clusters: " + cluster_indexes_target_house.size());
				////////////////////DEBUGGING/////////////////////////////////
				
			}
			else
			{
//				System.out.println("Map remaining source clusters");
				// Map remaining source clusters to target cluster with smallest distance
				for(int id_source: cluster_indexes_source_house)
				{
					float current_diff = -1.0f;
					float smallest_diff = 100000.0f;
					int target_cluster = -1;
					for(int index_target=0; index_target<clusters_target_house.length;index_target++)
					{
						if(diff[index_target][id_source] == -1.0)
						{							
							diff[index_target][id_source] = cluster_distance(clusters_target_house[index_target], clusters_source_house[id_source], target_house, source_house);							
						}
						current_diff = diff[index_target][id_source];
						if(current_diff < smallest_diff)
						{
							smallest_diff = current_diff;
							target_cluster = index_target;
						}
						
					}
					String source_cluster_name = HouseData.sensorContainer(clusters_source_house[id_source].get(0)).metacontainer.name;					
					String meta_meta_label = meta_meta_features_labels[target_cluster];
					// Save mapping
					mapping.put(source_cluster_name, meta_meta_label);
					cluster_indexes_source_house.remove(id_source);
//					System.out.println("Removed cluster " + id_source);
				}
			}
		}
		
		
		return mapping;
	}

	/**
	 * Calculates cluster distance as defined by Koen et al.
	 * @param cluster_1 List of ID's of sensors belonging to cluster 1
	 * @param cluster_2 List of ID's of sensors belonging to cluster 1
	 * @return cluster distance
	 */
	private float cluster_distance(List<Integer> cluster_1, List<Integer> cluster_2, HouseData house_1, HouseData house_2)
	{
		//Initialize variables	
		
		List<Integer> small_cluster, large_cluster;
		HouseData house_small, house_large;
		float avg_min_divergence = 0.0f;
		
		
		if(cluster_1.size()>cluster_2.size())
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
		
		
		// For each sensor in the small cluster
		for(int i = 0; i<small_cluster.size(); i++)
		{
			int sensor_id_s = small_cluster.get(i);
			float min_div = 1000000.0f;
			// For each sensor in the large cluster
			for(int j = 0; j<large_cluster.size(); j++)
			{				
				int sensor_id_l = large_cluster.get(j);
				float current_min_div = -1.0f;
				// Get profile sensors
				float[][] hist_s = house_small.profileSensor(sensor_id_s, blockSizeStart ,blockNumLength, maxLengthDuration);
				float[][] hist_l = house_large.profileSensor(sensor_id_l, blockSizeStart ,blockNumLength, maxLengthDuration);
				// Calculate difference between them
				if(distance_metric == Sensor_distance.Profiles_individ)
				{
					current_min_div = sse_dist(hist_s, hist_l);
				}
				else if (distance_metric == Sensor_distance.Profiles_individ_rel)
				{
					float sensor_profile_distance = sse_dist(hist_s, hist_l);
					Integer[] sensors_b = house_small.sensorList();
					Integer[] sensors_beta = house_large.sensorList();
					float relative_dist = 2.0f;
					for(Integer b:sensors_b)
					{
						for(Integer beta: sensors_beta)
						{
							data.NormalDistribution a_b = house_small.profileRelative(sensor_id_s, b);
							data.NormalDistribution alpha_beta = house_large.profileRelative(sensor_id_l, beta);
							float overlap = a_b.overlapLevel(alpha_beta);
							if(overlap < relative_dist)
							{
								relative_dist = overlap; 
							}
						}
					}
					current_min_div = (profile_weight*sensor_profile_distance)+( (1-profile_weight)*relative_dist);
					
				}
				// If it is the smallest difference so far
				if(current_min_div < min_div)
				{
					min_div = current_min_div;
				}
			}
			// Append smallest difference to average difference
			avg_min_divergence += min_div;
			
		}
		
		
		// Return average smallest difference
		return avg_min_divergence / (float)small_cluster.size();
		
	}
	
	/**
	 * Return the sum of squared error as distance between two histograms
	 * @param hist_1 = histogram one
	 * @param hist_2 = histogram two
	 * @return sum of squared error between two histograms
	 */
	public float sse_dist(float[][] hist_1, float[][] hist_2)
	{
//		System.out.println("Histogram 1");
//		for(int i =0; i<hist_1.length;i++){
//			for(int j = 0; j<hist_1[i].length;j++){
//				System.out.print(hist_1[i][j]+"\t");
//			}
//			System.out.println("\n");
//		}
//		System.out.println("\n + Histogram 2");
//		for(int i =0; i<hist_2.length;i++){
//			for(int j = 0; j<hist_2[i].length;j++){
//				System.out.print(hist_2[i][j]+"\t");
//			}
//			System.out.println("\n");
//		}
		
		
		float bcoeff = 0.0f;
		int nr_bins_x = hist_1.length;
		int nr_bins_y = hist_1[0].length;
				
		for(int i = 0; i<nr_bins_x; i++){
			for(int j = 0; j< nr_bins_y; j++)
			{
				bcoeff += Math.pow(hist_1[i][j]-hist_2[i][j],2);
			}
		}
		return (float) Math.sqrt(bcoeff);
	}
	
	private String[] build_labels(List<Integer>[] clusters_target_house) {
		String base_name = "meta-meta-feature-";
		String[] labels = new String [clusters_target_house.length];
		for(int i =0; i<clusters_target_house.length; i++)
		{
			labels[i] = base_name+i;
		}
		return labels;
	}
	
	
}