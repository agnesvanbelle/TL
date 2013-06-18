package meta_feature_building;

import java.util.ArrayList;

import data.DataPoint;
import data.HouseData;
import data.NameContainer;

public class Test
{
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		HouseData houseA = new HouseData("houseD");
		ArrayList<HouseData> data = new ArrayList<HouseData>();
		data.add(houseA);
		int [] alpha = {2};
		int [] beta = {15};
		Meta_feature_building.alpha_beta_clustering(data, alpha, beta);
		NameContainer[] sensors = houseA.sensorList();
		System.out.println("sensor list: ");
		for(int i =0;i<sensors.length;i++)
		{
			System.out.println(i + " " + sensors[i].name);
		}
		
		
		
	}
}
