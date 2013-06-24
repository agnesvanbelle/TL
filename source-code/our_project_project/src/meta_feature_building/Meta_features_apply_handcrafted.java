package meta_feature_building;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import data.HouseData;

public class Meta_features_apply_handcrafted {
	
	public static void apply_hand_crafted_meta_features(ArrayList<HouseData> houses) throws IOException
	{
		BufferedReader br = null;
		String line = "";
		
		for(HouseData house: houses)
		{
			String filename = house.houseName+".meta_features";
			br = new BufferedReader(new FileReader(HouseData.inputDataDir + filename));
			
			while ((line = br.readLine()) != null)
			{
				String[] names = line.split("\t");
				if(names.length == 2)
				{
					String sensorName = names[1] +  '-' + house.houseName;
					HouseData.mapSensors(sensorName, names[0] + "-" + house.houseName);
				}
				else
				{
					System.out.println("Something wrong with handcrafted meta features file of " + house.houseName);
					for(int i =0; i<names.length;i++){
						System.out.println("-" + names[i] + "-" );
					}
				}
			}
		}
	}

}
