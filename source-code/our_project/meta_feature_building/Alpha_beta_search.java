package meta_feature_building;


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import data.HouseData;

public class Alpha_beta_search {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		ArrayList<HouseData> houses = new ArrayList<HouseData>();
		HouseData houseA = new HouseData("houseA");	
		houses.add(houseA);
		HouseData houseB = new HouseData("houseB");
		houses.add(houseB);
		HouseData houseC = new HouseData("houseC");
		houses.add(houseC);
		HouseData houseD = new HouseData("houseD");
		houses.add(houseD);
		HouseData houseE = new HouseData("houseE");
		houses.add(houseE);	
		
		int[] alphasA = {2,4,6};		
		int[]  betasA = {7,8,9,10,12,14,16,18,20,25,50};
		
		int[] alphasB = {4,5,6,7,8,9,10};
		int[]  betasB = {2, 3,4, 5,6, 7};
		
		int[] alphasC = {4,6,7,8};
		int[]  betasC = {5,6,7,8,9, 10};
		
		int[] alphasD = {4,5,6,7,8};
		int[]  betasD = { 3,4, 5,6, 7,8, 10};
		
		int[] alphasE = {2,3,4,5,6,7};
		int[]  betasE = {7,8,9, 10,15,25, 50};
		
		ArrayList<int[]> alphas = new ArrayList<int[]>();
		ArrayList<int[]> betas = new ArrayList<int[]>();
		alphas.add(alphasA);
		alphas.add(alphasB);
		alphas.add(alphasC);
		alphas.add(alphasD);
		alphas.add(alphasE);
		betas.add(betasA);
		betas.add(betasB);
		betas.add(betasC);
		betas.add(betasD);
		betas.add(betasE);		
		
		int[] alpha = {0};
		int[] beta = {0};
		
		int house_index = 0;
		for(HouseData house: houses){
			System.out.println(house.houseName);
			PrintWriter writer = new PrintWriter(house.houseName+"-alphabeta.csv", "UTF-8"); 
			int[] current_alphas = alphas.get(house_index);
			int[] current_betas = betas.get(house_index);
			
			writer.print(", ");
			for(int j=0; j<current_betas.length;j++)
			{
				writer.print(current_betas[j]);
				if(j!=current_betas.length-1){
					writer.print(",");
				}
			}
			writer.print("\n");	
			for(int i = 0; i<current_alphas.length;i++)
			{
				writer.print(current_alphas[i] + ",");
				for(int j=0; j<current_betas.length;j++)
				{
					// opvragen
					alpha[0] = current_alphas[i];
					beta[0] = current_betas[j];
					ArrayList<HouseData> currentHouse = new ArrayList<HouseData>();
					currentHouse.add(house);
					Meta_feature_building.alpha_beta_clustering(currentHouse, alpha, beta);
					List<Integer>[] clusters = house.sensorClusters(HouseData.MAPPING_LEVEL_METAFEATURE);
					for(List<Integer> clust: clusters)
					{
						writer.print(clust.size()+"-");
					}
					if(j!= current_betas.length-1)
					{
						writer.print(",");
					}
				}
				if(i!= current_alphas.length-1){
					writer.print("\n");
				}
			}
			writer.close();
		}		

	}

}
