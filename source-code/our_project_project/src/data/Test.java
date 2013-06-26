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
		
//		System.out.println("Example 1:");
//		
//		for (DataPoint p: data)
//		{
//			System.out.println(p.startDate() + "\t" + p.length + "\t" + p.ID);
//		}
		
		// Example 2:
//		
//		System.out.println("Example 2:");
//		
//		HouseData.mapSensors("Hall-Bedroom door-houseA",  "New metafeature");
//		HouseData.mapSensors("Hall-Toilet door-houseA",   "New metafeature");
//		HouseData.mapSensors("Hall-Bathroom door-houseA", "New metafeature");
//		
//		data = houseA.sensorData(HouseData.MAPPING_LEVEL_METAFEATURE);
//		
//		for (DataPoint p: data)
//		{
//			System.out.println(p.startBlock(DataPoint.TIME_BLOCK_SIZE_MINUTE) + "\t" + p.length + "\t" + p.ID);
//		}
		
		// Example 3:
//		
//		System.out.println("Example 3:");
//		
//		// frequency table
//		// only needs beta
//		int[][] alphaBetaMatrix = houseA.profileAlphaBeta(15); // <= 15s difference between sensor activations.
//		
//		for (int i = 0; i < alphaBetaMatrix.length; i++)
//		{
//			for (int j = 0; j < alphaBetaMatrix.length; j++)
//			{
//				System.out.print(alphaBetaMatrix[i][j] + " ");
//			}
//			
//			System.out.println();
//		}
		
		// Example 4:
//		
//		System.out.println("Example 4:");
//		
//		for (int ID: houseE.sensorList())
//		{
//			System.out.println(ID + " --> " + HouseData.sensorContainer(ID).name);
//		}
//		
		// Example 5:
		
		System.out.println("Example 5:");
		
		NormalDistribution r1 = houseA.profileRelational(1, 2); // Hall-Toilet door vs Hall-Bathroom door
		NormalDistribution r2 = houseA.profileRelational(1, 8); // Hall-Toilet door vs Toilet Flush
		
		System.out.println(r1.mu + ", " + r1.sigma);
		System.out.println(r2.mu + ", " + r2.sigma);
		System.out.println(r1.KLDivergence(r2));
		System.out.println(r2.KLDivergence(r1));
		
		// Example 6:
		
		System.out.println("\nExample 6:");
		
		NormalDistribution a = new NormalDistribution(new float[]{10f}, new float[]{5f});
		NormalDistribution b = new NormalDistribution(new float[]{8f},  new float[]{7f});
		
		System.out.println(a.overlapLevel(b));
		System.out.println(b.overlapLevel(a));
		System.out.println(a.overlapLevel(a));
		
		// Example 7:
		
		System.out.println("\nExample 7:");
		
		float[][] profile = houseA.profileSensor_hist(houseA.sensorList()[1], 3600, 4, 3600);
		
		for (int i = 0; i < profile.length; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				System.out.print(profile[i][j] + "\t");
				
				if (profile[i][j] == 0.0)
				{
					System.out.print("\t");
				}
			}
			System.out.println();
		}
		
		// Example 8:
		
		HouseData.mapActivities("Going-out-to-work",           	"leave-house");
		HouseData.mapActivities("Taking-out-the-trash",        	"leave-house");
		HouseData.mapActivities("Lawnwork",                    	"leave-house");
		HouseData.mapActivities("Going-out-to-school",         	"leave-house");
		HouseData.mapActivities("Going-out-for-entertainment", 	"leave-house");
		HouseData.mapActivities("Going-out-for-shopping",      	"leave-house");

		HouseData.mapActivities("Toileting",                   	"use-toilet");

		HouseData.mapActivities("Bathing",                     	"take-shower");
		
		HouseData.mapActivities("Grooming",                    	"brush-teeth");
		
		HouseData.mapActivities("Sleeping",                    	"go-to-bed");
		HouseData.mapActivities("Resting",                     	"go-to-bed");

		HouseData.mapActivities("Preparing-breakfast",	       	"prep-breakfast");

		HouseData.mapActivities("Preparing-lunch",	       		"prep-dinner");
		HouseData.mapActivities("Preparing-dinner",	       		"prep-dinner");
		HouseData.mapActivities("Preparing-a-snack",	      	"prep-dinner");

		HouseData.mapActivities("Preparing-a-beverage",        	"get-drink");
		
		//houseE.formatLena(HouseData.MAPPING_LEVEL_METAFEATURE, HouseData.MAPPING_LEVEL_METAFEATURE);
	}
}
