package data;

public class Test
{
	/**
	 * Runs these tests to check that everything is working fine.
	 * They also serve as examples of how this package can be used.
	 * @param args Not used.
	 */
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
		
		System.out.println("Example 1:");
		
		for (DataPoint p: data)
		{
			System.out.println(p.startDate() + "\t" + p.length + "\t" + p.ID);
		}
		
		// Example 2:
		
		System.out.println("Example 2:");
		
		HouseData.mapSensors("Hall-Bedroom door",  "New metafeature");
		HouseData.mapSensors("Hall-Toilet door",   "New metafeature");
		HouseData.mapSensors("Hall-Bathroom door", "New metafeature");
		
		data = houseA.sensorData(HouseData.MAPPING_LEVEL_METAFEATURE);
		
		for (DataPoint p: data)
		{
			System.out.println(p.startBlock(DataPoint.TIME_BLOCK_SIZE_MINUTE) + "\t" + p.length + "\t" + p.ID);
		}
		
		// Example 3:
		
		System.out.println("Example 3:");
		
		int[][] alphaBetaMatrix = houseA.profileAlphaBeta(15); // <= 15s difference between sensor activations.
		
		for (int i = 0; i < alphaBetaMatrix.length; i++)
		{
			for (int j = 0; j < alphaBetaMatrix.length; j++)
			{
				System.out.print(alphaBetaMatrix[i][j] + " ");
			}
			
			System.out.println();
		}
		
		// Example 4:
		
		System.out.println("Example 4:");
		
		for (int ID: houseA.sensorList())
		{
			System.out.println(ID + " --> " + HouseData.sensorContainer(ID).name);
		}
		
		// Example 5:
		
		System.out.println("Example 4:");
		
		NormalDistribution r1 = houseA.profileRelative(1, 2); // Hall-Toilet door vs Hall-Bathroom door
		NormalDistribution r2 = houseA.profileRelative(1, 8); // Hall-Toilet door vs Toilet Flush
		
		System.out.println(r1.mu + ", " + r1.sigma);
		System.out.println(r2.mu + ", " + r2.sigma);
		System.out.println(r1.KLDivergence(r2));
		System.out.println(r2.KLDivergence(r1));
	}
}
