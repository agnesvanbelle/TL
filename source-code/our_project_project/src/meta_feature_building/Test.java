package meta_feature_building;

import java.util.ArrayList;

import data.HouseData;

public class Test
{
	
	public static void main(String[] args)
	{
		HouseData houseA = new HouseData("houseTest");
		ArrayList<HouseData> data = new ArrayList<HouseData>();
		data.add(houseA);
		int [] alpha = {5};
		int [] beta = {1400};
		Meta_feature_building.alpha_beta_clustering(data, alpha, beta);
		Integer[] sensors = houseA.sensorList();
		System.out.println("sensor list: ");
		for(int i =0;i<sensors.length;i++)
		{
			System.out.println(i + " " + HouseData.sensorContainer(sensors[i]).name);
		}
		
		
		
	}
}
