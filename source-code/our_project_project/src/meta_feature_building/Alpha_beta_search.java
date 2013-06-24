package meta_feature_building;


import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import data.HouseData;

public class Alpha_beta_search {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		Meta_feature_building.Alpha_beta_type alpha_style = Meta_feature_building.Alpha_beta_type.Relative;
			
		int[] alphasA = {2,4,6,8,10};		
		int[]  betasA = {3,6,9,12,15};
		
		int[] alphasB = {2,4,6,8,10};		
		int[]  betasB = {3,6,9,12,15};
		
		int[] alphasC = {2,4,6,8,10};		
		int[]  betasC = {3,6,9,12,15};
		
		int[] alphasD = {2,4,6,8,10};		
		int[]  betasD = {3,6,9,12,15};
		
		int[] alphasE = {2,4,6,8,10};		
		int[]  betasE = {3,6,9,12,15};
		
		float[] single_alphas = {0.01f, 0.05f, 0.1f, 0.15f, 0.2f, 0.25f, 0.3f};
		
		String[] houseNames = {"A", "B","C","D","E"};
		ArrayList<HouseData> houses = new ArrayList<HouseData>();
		for(String houseName: houseNames){
			houses.add(new HouseData("house"+houseName));
		}
		
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
		
		String suffix = "-Absolute";
		if(alpha_style == Meta_feature_building.Alpha_beta_type.Relative)
			suffix = "-Relative";
		
		
		
		int house_index = 0;
		for(HouseData house: houses){
			System.out.println(house.houseName);			
			 
			int[] current_alphas = alphas.get(house_index);
			int[] current_betas = betas.get(house_index);
						
			int[][][] stats_freq;
			float[][] stats_avg;
						
			PrintWriter writer = new PrintWriter(house.houseName+"-alphabeta"+suffix+".csv", "UTF-8");
			writer.print(", ");
			for(int j=0; j<current_betas.length;j++)
			{
				writer.print(current_betas[j]);
				if(j!=current_betas.length-1){
					writer.print(",");
				}
			}
			writer.print("\n");	
			
			if(alpha_style == Meta_feature_building.Alpha_beta_type.Absolute){
				System.out.println("Absolute calculations");
				stats_freq = new int [current_alphas.length][current_betas.length][2];
				stats_avg = new float [current_alphas.length][current_betas.length];
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
						
						Meta_feature_building builder = new Meta_feature_building(alpha, beta);				
						
						builder.alpha_beta_clustering(currentHouse);
						List<Integer>[] clusters = house.sensorClusters(HouseData.MAPPING_LEVEL_METAFEATURE);
												
						stats_freq[i][j][0] = clusters.length;
						stats_freq[i][j][1] = 0;
						stats_avg[i][j] = 0.0f;
						
						for(List<Integer> clust: clusters)
						{
							writer.print(clust.size()+"-");
							if(clust.size()>1){
								stats_freq[i][j][1]++;
								stats_avg[i][j] += (float)clust.size();
							}
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
			}
			else
			{			
				System.out.println("Relative calculations");
				stats_freq = new int [single_alphas.length][current_betas.length][2];
				stats_avg = new float [single_alphas.length][current_betas.length];
				for(int i = 0; i<single_alphas.length;i++)
				{
					writer.print(single_alphas[i] + ",");
					for(int j=0; j<current_betas.length;j++)
					{
						// opvragen
						float single_alpha = single_alphas[i];						
						ArrayList<HouseData> currentHouse = new ArrayList<HouseData>();
						currentHouse.add(house);
						
						Meta_feature_building builder = new Meta_feature_building(single_alpha, beta);				
						
						builder.alpha_beta_clustering(currentHouse);
						List<Integer>[] clusters = house.sensorClusters(HouseData.MAPPING_LEVEL_METAFEATURE);
												
						stats_freq[i][j][0] = clusters.length;
						stats_freq[i][j][1] = 0;
						stats_avg[i][j] = 0.0f;
						
						for(List<Integer> clust: clusters)
						{
							writer.print(clust.size()+"-");
							if(clust.size()>1){
								stats_freq[i][j][1]++;
								stats_avg[i][j] += (float)clust.size();
							}
						}
						if(j!= current_betas.length-1)
						{
							writer.print(",");
						}
					}
					if(i!= single_alphas.length-1){
						writer.print("\n");
					}
				}
			}
			
			// Write stats to file
			FileWriter stats_writer = new FileWriter("Stats-"+house.houseName+suffix+".txt");
			stats_to_file(stats_avg, stats_freq, stats_writer, current_alphas, current_betas, single_alphas, alpha_style);
			
			writer.close();
			stats_writer.close();
		}		
	}

	private static void stats_to_file(float[][] stats_avg, int[][][] stats_freq, FileWriter stats_writer,int[] alphas,int[] betas, float[] single_alfas, Meta_feature_building.Alpha_beta_type alpha_style) throws IOException {
		int nr_stats = 3;
		
		String tableFormat = "{r|l|";
		for(int i =0; i<stats_avg.length;i++)
		{
			tableFormat+="c";
		}
		tableFormat+="|}";
		
		String sep = " & ";
		String bs = "\\";
		String nl = " "+bs+bs+"\n"; 
		String hline = "\\hline \n";
		
		stats_writer.write("\\begin{table} \n \\centering \n \\begin{tabular}"+tableFormat+"\n");
		stats_writer.write("Beta" + sep + "Statistic" + sep + "\\multicolumn{"+ stats_avg.length  +"}{c}{Alpha}" + nl);
		stats_writer.write(sep + sep);
		for(int i =0; i<stats_avg.length;i++){
			if(alpha_style == Meta_feature_building.Alpha_beta_type.Absolute)
			{
				stats_writer.write(Integer.toString(alphas[i]));
			}
			else if(alpha_style == Meta_feature_building.Alpha_beta_type.Relative)
			{
				stats_writer.write(Float.toString(single_alfas[i]));
			}
			if(i<stats_avg.length-1){
				stats_writer.write(sep);
			}			
		}
		stats_writer.write(nl+hline);
		
		for(int j = 0; j<stats_avg[0].length;j++)
		{
			stats_writer.write(Integer.toString(betas[j]));			
			for(int k=0;k<nr_stats;k++)
			{
				switch(k)
				{
				case 0: stats_writer.write("& Nr clusters"); break;
				case 1: stats_writer.write("& Nr nu-clusters"); break; 
				case 2: stats_writer.write("& Avg size nu-clusters"); break;
				}
				for(int i =0; i<stats_freq[0].length;i++)
				{
					stats_writer.write(sep);
					switch(k)
					{
					case 2: 
						if(stats_freq[i][j][1]==0)
						{
							stats_writer.write("-");
						}
						else
						{
							stats_writer.write(Double.toString(roundTwoDecimals((double)stats_avg[i][j] / ((double)stats_freq[i][j][1])) ) ); 
						}						
						break;
					default: stats_writer.write(Integer.toString(stats_freq[i][j][k])); break; 					
					}					
				}
				stats_writer.write(nl);
			}
			stats_writer.write(hline);
		}
		
		
		stats_writer.write("\\end{tabular} \n \\caption{}\n \\label{} \n \\end{table}");
	}
	
	private static double roundTwoDecimals(double d) {
		  DecimalFormat twoDForm = new DecimalFormat("#.##");
		  return Double.valueOf(twoDForm.format(d));
		}

}
