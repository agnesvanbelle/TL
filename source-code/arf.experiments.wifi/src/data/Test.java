package data;

public class Test
{
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		HouseData houseA = new HouseData("houseA");		
		HouseData houseB = new HouseData("houseB");
		HouseData houseC = new HouseData("houseC");
		HouseData houseD = new HouseData("houseD");
		HouseData houseE = new HouseData("houseE");
		
		DataPoint[] data = houseA.sensorData(HouseData.MAPPING_LEVEL_FEATURE);
		
		// Example 1:
		
		for (DataPoint p: data)
		{
			System.out.println(p.startDate() + "\t" + p.length + "\t" + p.ID);
		}
		
		// Example 2:
		
		for (DataPoint p: data)
		{
			System.out.println(p.startBlock(DataPoint.TIME_BLOCK_SIZE_MINUTE) + "\t" + p.length + "\t" + p.ID);
		}
	}
}
