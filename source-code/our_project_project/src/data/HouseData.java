package data;

import java.io.*;
import java.util.*;

public class HouseData
{
	
	public static final String outputDirName = "../our_project_project/output/";
	public static final String inputDataDir = "../our_project_project/data/";
	public static final String houseOutputDirPrefix = "houseInfo"; //TODO set via wer
	
	// Example mapping levels for sensorData() and activityData():
	
	public static final int MAPPING_LEVEL_FEATURE         = 0;
	public static final int MAPPING_LEVEL_METAFEATURE     = 1;
	public static final int MAPPING_LEVEL_METAMETAFEATURE = 2;
	
	// Internal types of data:
	
	private static final int TYPE_DATA_SENSOR   = 0;
	private static final int TYPE_DATA_ACTIVITY = 1;
	
	// All sensors and activities across houses:
	
	private static final HashMap<Integer, NameContainer>[] indexID   = new HashMap[2]; // Index per IDs.
	private static final HashMap<String,  NameContainer>[] indexName = new HashMap[2]; // Index per names.
	
	// House data:
	
	public final String houseName;
					
	private final ArrayList<DataPoint>[]                   dataTime = new ArrayList[2]; // Index per chronological order.
	private final HashMap<Integer, ArrayList<DataPoint>>[] dataID   = new HashMap[2];   // Index per IDs.
	
	// Sensor profile cache:
	
	private final HashMap<Integer, float[][]> sensorProfiles_hist = new HashMap<Integer, float[][]>(); 
	private final HashMap<Integer, NormalDistribution> sensorProfiles_nd = new HashMap<Integer, NormalDistribution>(); 
	private final HashMap<Integer, Integer> firingFrequencies = new HashMap<Integer, Integer> ();
	
	// Dynamic methods:
	
	/**
	 * Loads the data from a given house and creates a HouseData object containing it.
	 * @param houseName Name of the house to load. The data files must be located under the inputDataDir folder, see the 'README.txt' file.
	 */
	public HouseData(String houseName)
	{
		HashMap<Integer, NameContainer>[] mapping = new HashMap[2]; // Private-to-public IDs mapping.
	
		this.houseName = houseName;
		
		loadNames(mapping, TYPE_DATA_SENSOR);
		loadNames(mapping, TYPE_DATA_ACTIVITY);
		
		loadData(mapping, TYPE_DATA_SENSOR);
		loadData(mapping, TYPE_DATA_ACTIVITY);
	}
	
	private void loadNames(HashMap<Integer, NameContainer>[] mapping, int typeData)
	{
		// Index creation when not present already:
		
		if (indexName[typeData] == null)
		{
			indexName[typeData] = new HashMap<String,  NameContainer>();
			indexID[typeData]   = new HashMap<Integer, NameContainer>();
		}
		
		dataTime[typeData] = new ArrayList<DataPoint>();
		dataID[typeData]   = new HashMap<Integer, ArrayList<DataPoint>>();

		mapping[typeData]  = new HashMap<Integer, NameContainer>();
		
		// Name loading:
		
		BufferedReader br = null;
		 
		try
		{
			switch (typeData)
			{
				case TYPE_DATA_SENSOR:
					br = new BufferedReader(new FileReader(inputDataDir + houseName + ".sensor_names"));
				break;
				case TYPE_DATA_ACTIVITY:
					br = new BufferedReader(new FileReader(inputDataDir + houseName + ".activity_names"));
				break;
			}
			
			String line;
 
			while ((line = br.readLine()) != null)
			{
				String[] columns = line.split("\t");
				
				int    ID   = Integer.parseInt(columns[0]);
				String name = columns[1];
				
				if (typeData == TYPE_DATA_SENSOR)
				{
					name += '-' + houseName;
				}
				
				NameContainer entity;

				if (indexName[typeData].containsKey(name))
				{
					entity = indexName[typeData].get(name);
				}
				else
				{
					entity = new NameContainer(name);
					
					indexName[typeData].put(entity.name, entity);
					indexID[typeData].put(entity.ID, entity);
				}
				
				mapping[typeData].put(ID, entity);
			}
		}
		catch (IOException ex)
		{
			// File not found; do nothing...
		}

		try 
		{
			if (br != null)
			{
				br.close();
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void loadData(HashMap<Integer, NameContainer>[] mapping, int typeData)
	{		
		BufferedReader br = null;
		
		try
		{
			switch (typeData)
			{
				case TYPE_DATA_SENSOR:
					br = new BufferedReader(new FileReader(inputDataDir + houseName + ".sensors"));
				break;
				case TYPE_DATA_ACTIVITY:
					br = new BufferedReader(new FileReader(inputDataDir + houseName + ".activities"));
				break;
			}
			
			String line;
 
			/////////////////////DEBUGGING/////////////////////
			int lineNumber = 0;
			/////////////////////DEBUGGING/////////////////////
			while ((line = br.readLine()) != null)
			{
				/////////////////////DEBUGGING/////////////////////
				lineNumber ++;
				/////////////////////DEBUGGING/////////////////////
				String[] columns = line.split("\t");
				
				int start  = Integer.parseInt(columns[0]);
				int length = Integer.parseInt(columns[1]);
				int ID     = Integer.parseInt(columns[2]);
				
				NameContainer entity = null;
				
				if(ID==4 && typeData == TYPE_DATA_SENSOR)
				{
					ID = 7;
				}
				
				if (mapping[typeData].containsKey(ID))
				{
					entity = mapping[typeData].get(ID);
				}
				if(entity != null)
				{
					if(firingFrequencies.containsKey(entity.ID))
					{
						firingFrequencies.put(entity.ID, firingFrequencies.get(entity.ID)+1);
					}
					else
					{
						firingFrequencies.put(entity.ID, 1);
					}
					
					DataPoint data = new DataPoint(start, length, entity.ID);
					
					dataTime[typeData].add(data);
					
					if (!dataID[typeData].containsKey(entity.ID))
					{
						dataID[typeData].put(entity.ID, new ArrayList<DataPoint>());
					}
					
					dataID[typeData].get(entity.ID).add(data);
				}
			} 
		}
		catch (IOException e)
		{
			e.printStackTrace();
		} 

		try 
		{
			if (br != null)
			{
				br.close();
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	// Basic dynamic methods:
	
	/**
	 * Returns an array with the sensor IDs present in the data for this house.
	 * The sensors are returned in the order implicitly assumed throughout the methods of this class.
	 * @return An array with the sensor IDs present in the data for this house.
	 */
	public Integer[] sensorList()
	{
		Integer [] output = dataID[TYPE_DATA_SENSOR].keySet().toArray(new Integer[0]);
		
		Arrays.sort(output);
		
		return output;
	}
	
	/**
	 * Returns an array with the activity IDs present in the data for this house.
	 * The activity are returned in the order implicitly assumed throughout the methods of this class.
	 * @return An array with the activity IDs present in the data for this house.
	 */
	public Integer[] activityList()
	{
		Integer[] output = dataID[TYPE_DATA_ACTIVITY].keySet().toArray(new Integer[0]);
		
		Arrays.sort(output);
		
		return output;
	}
	
	/**
	 * Returns the raw sensor data stored for this house. No sensor IDs will be shared across houses.
	 * @param mappingLevel Number of sensor mapping levels to apply. MAPPING_LEVEL_* constants can be used for convenience.
	 * @return An array of data point objects.
	 */
	public DataPoint[] sensorData(int mappingLevel)
	{
		return returnData(mappingLevel, TYPE_DATA_SENSOR);
	}
	
	/**
	 * Returns the raw activity data stored for this house.
	 * @param mappingLevel Number of activity mapping levels to apply. MAPPING_LEVEL_* constants can be used for convenience.
	 * @return An array of data point objects.
	 */
	public DataPoint[] activityData(int mappingLevel)
	{
		return returnData(mappingLevel, TYPE_DATA_ACTIVITY);
	}
	
	private DataPoint[] returnData(int mappingLevel, int typeData)
	{
		DataPoint[] output = new DataPoint[dataTime[typeData].size()];
		
		for (int i = 0; i < output.length; i++)
		{
			DataPoint data = dataTime[typeData].get(i);
			
			int mappedID = mapApply(data.ID, mappingLevel, typeData);
			
			output[i] = new DataPoint(data.start, data.length, mappedID);
		}
		
		return output;
	}
	
	/**
	 * Returns an array of lists where each list contains the IDs of sensors present in one group after mapping.
	 * @param mappingLevel Number of sensor mapping levels to apply. MAPPING_LEVEL_* constants can be used for convenience.
	 * @return An array of lists where each list contains the IDs of sensors present in one group after mapping.
	 */
	@SuppressWarnings("unchecked")
	public List<Integer>[] sensorClusters(int mappingLevel)
	{
		HashMap<Integer, List<Integer>> output = new HashMap<Integer, List<Integer>>();
		
		for (Integer ID: sensorList())
		{
			int mappedID = mapApply(ID, mappingLevel, TYPE_DATA_SENSOR);
			
			if (!output.containsKey(mappedID))
			{
				output.put(mappedID, new ArrayList<Integer>());
			}
			
			output.get(mappedID).add(ID);
		}
		
		return output.values().toArray(new List[0]);
	}
	
	// Advanced dynamic methods:
	
	/**
	 * Creates the necessary files in the correct format to use the data within the current house as an input for Lena's pipeline.
	 * This method will create a new folder containing all files.
	 * @param mappingLevelSensors Number of sensor mapping levels to apply. MAPPING_LEVEL_* constants can be used for convenience.
	 * @param mappingLevelActivities Number of activity mapping levels to apply. MAPPING_LEVEL_* constants can be used for convenience.
	 */
	public synchronized void formatLena(String outputDirName, int mappingLevelSensors, int mappingLevelActivities)
	{
		String houseLetter = houseName.replaceAll("house", "");
		
//		File outputDir = new File(outputDirName);
//		if (outputDir.exists()) {
//			outputDir.delete();
//		}
//		outputDir.mkdir();
		File houseOutputDir = new File(outputDirName + "/" );
		houseOutputDir.mkdir();
		
		try
		{
			// Sensor data:
			
			PrintWriter writer = new PrintWriter(houseOutputDir + "/" + houseName + "-ss.txt", "UTF-8");
			
			/* edited by Agnes - the *-ss.txt file needs raw feature sensor id as 3rd column 
			 * (same as the first column in sensormap*-ids.txt) 
			 */
			for (DataPoint data: sensorData(MAPPING_LEVEL_FEATURE))
			{
				DataPoint temp = new DataPoint(data.start + data.length, data.length, data.ID); // temp.start = data.end
				
				writer.println(data.startDate() + "\t" + temp.startDate() + "\t" + data.ID);
			}
			
			writer.close();
			
			// Activity data:
			
			writer = new PrintWriter(houseOutputDir + "/" + houseName + "-as.txt", "UTF-8");
			
			for (DataPoint data: activityData(mappingLevelActivities))
			{
				DataPoint temp = new DataPoint(data.start + data.length, data.length, data.ID); // temp.start = data.end
				
				writer.println(data.startDate() + "\t" + temp.startDate() + "\t" + data.ID);
			}
			
			writer.close();
			
			// Sensor labels:
			
			writer = new PrintWriter(houseOutputDir + "/sensorMap" + houseLetter + "-ids.txt", "UTF-8");
			
			for (Integer ID: sensorList())
			{
				int mappedID = mapApply(ID, mappingLevelSensors, TYPE_DATA_SENSOR);
				
				String name  = indexID[TYPE_DATA_SENSOR].get(ID).name.replaceAll(",", "-");
				// String group = indexID[TYPE_DATA_SENSOR].get(mappedID).name.replaceAll(",", "-");
				
				writer.println(ID + "," + name + "," + mappedID);
			}
			
			writer.close();
			
			// Activity labels:
			
			writer = new PrintWriter(houseOutputDir + "/actionMap" + houseLetter + ".txt", "UTF-8");
			
			Set<Integer> printedIDs = new HashSet<Integer>();
			
			for (Integer ID: activityList())
			{
				int mappedID = mapApply(ID, mappingLevelActivities, TYPE_DATA_ACTIVITY);
				
				if (!printedIDs.contains(mappedID))
				{
					String group = indexID[TYPE_DATA_ACTIVITY].get(mappedID).name.replaceAll(",", "-");
					
					writer.println(mappedID + "," + group);
					
					printedIDs.add(mappedID);
				}
			}
			
			writer.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}		
	}
	
	/**
	 * Returns a normalized histogram of sensor activations over start times and firing lengths.
	 * @param ID Sensor ID whose profile will be calculated.
	 * @param blockSizeStart Length of the blocks in which a day will be divided, in seconds.
	 * @param blockNumLength Number of discrete blocks into which firing lengths will be mapped.
	 * @param maxLength Maximum informative firing length. Lengths above this value will simply be regarded as being this value.
	 * @return A matrix of sensor activations over start times (first index) and firing lengths (second index).
	 */
	public float[][] profileSensor(int ID, int blockSizeStart, int blockNumLength, int maxLength)
	{
		if (!sensorProfiles_hist.containsKey(ID))
		{
			float[][] profile = buildProfileSensor(ID, blockSizeStart, blockNumLength, maxLength);
			
			sensorProfiles_hist.put(ID, profile);
		}
		
		return sensorProfiles_hist.get(ID);
	}
	
	public NormalDistribution profileSensor(int ID)
	{
		if (!sensorProfiles_nd.containsKey(ID))
		{
			NormalDistribution profile = buildProfileSensor(ID);
			
			sensorProfiles_nd.put(ID, profile);
		}
		
		return sensorProfiles_nd.get(ID);
	} 
	
	
	private NormalDistribution buildProfileSensor(int ID)
	{
		List<DataPoint> data = dataID[TYPE_DATA_SENSOR].get(ID);
		
		double[][] values = new double[data.size()][2];
			
		for (int i = 0; i < data.size(); i++)
		{
			values[i][0] = (double) data.get(i).startBlock(1) / DataPoint.TIME_BLOCK_SIZE_HOUR;
			values[i][1] = data.get(i).length;
		}		
		
		return new NormalDistribution(values);
	}
	
	private float[][] buildProfileSensor(int ID, int blockSizeStart, int blockNumLength, int maxLength)
	{
		int nr_start_time_bins = (24 * 3600) / blockSizeStart;
		
		float[][] output = new float[nr_start_time_bins][blockNumLength];
		
		double logBase = Math.pow(Math.E, Math.log(maxLength + 1) / blockNumLength);
		
		/* Debug:
		System.out.println(blockNumLength + ", " + maxLength + ", " + logBase);
		
		System.out.println((int) Math.floor((Math.log((maxLength)) / Math.log(logBase))));
		
		for (int i = 0; i < blockNumLength; i++)
		{
			System.out.println(i + ": " + (int) Math.floor(Math.pow(logBase, i)) + " - " + (int) Math.floor((Math.pow(logBase, i + 1) - 1)));
		}*/
		
		long total = 0;
		
		for (DataPoint data: dataID[TYPE_DATA_SENSOR].get(ID))
		{
			// Logarithmic scale for the firing length mapping:
			
			int blockLength = 0;
			
			if (data.length > 0)
			{
				// log(0) is undefined, but blockLength for data.length = 0 should be 0:
				
				blockLength = (int) Math.floor((Math.log(Math.min(data.length, maxLength)) / Math.log(logBase)));
			}
			
			output[data.startBlock(blockSizeStart)][blockLength]++;
			
			total++;
		}
		
		// Normalization:
		
		for (int i = 0; i < nr_start_time_bins; i++)
		{
			for (int j = 0 ; j < blockNumLength; j++)
			{
				output[i][j] /= total;
			}
		}
		
		return output;
	}
	
	/**
	 * Returns the relational profile between two sensors, modeled as a normal distribution of the differences of their firing times.
	 * @param sensorA First sensor ID.
	 * @param sensorB Second sensor ID.
	 * @return The relative profile between two sensors, modeled as a normal distribution of the differences of their firing times.
	 */
	public NormalDistribution profileRelational(int sensorA, int sensorB) throws IllegalArgumentException
	{
		if (sensorA == sensorB)
		{
			throw new IllegalArgumentException();
		}
		
		//System.out.println("sensorA:" + sensorContainer(sensorA).name);
		//System.out.println("sensorB:" + sensorContainer(sensorB).name);
		
		List<DataPoint> dataTarget = dataID[TYPE_DATA_SENSOR].get(sensorA);
		
		
		//System.out.println(dataTarget.size());
		
		double[][] values = new double[dataTarget.size()][1];
		
		for (int i = 0; i < dataTarget.size(); i++)
		{
			DataPoint dataA = dataTarget.get(i);
			
			int minDiff = Integer.MAX_VALUE;
			
			for (DataPoint dataB: dataID[TYPE_DATA_SENSOR].get(sensorB))
			{
				int diff = dataA.start - dataB.start;
				
				if (Math.abs(diff) < Math.abs(minDiff))
				{
					minDiff = diff;
				}
			}
			
			values[i][0] = (double) minDiff;
		}
		
		
		return new NormalDistribution(values);
	}
	
	/**
	 * This method calculates a matrix profile whose elements represent the number of times the second (indexed) sensor fired within beta seconds of the first (indexed) sensor.
	 * The output of this method is suitable for feeding the alpha-beta meta-feature extraction algorithm.
	 * The value -1 is output for those elements which cannot be calculated.
	 * @param beta Beta parameter, expressed in seconds.
	 * @return A matrix of integers. The sensors are indexed in the same order as returned from sensorList().
	 */
	public int[][] profileAlphaBeta(int beta)
	{
		Integer[] sensors = sensorList();
		
		int[][] output = new int[sensors.length][sensors.length];
		
		for (int i = 0; i < sensors.length; i++)
		{
			for (int j = 0; j < sensors.length; j++)
			{
				if (i == j) // Metric not applicable if both sensors are the same one!
				{
					output[i][j] = -1;
				}
				else
				{
					for (DataPoint dataPrev: dataID[TYPE_DATA_SENSOR].get(sensors[i]))
					{
						for (DataPoint dataNext: dataID[TYPE_DATA_SENSOR].get(sensors[j]))
						{
							if (dataNext.start >= dataPrev.start && dataNext.start - dataPrev.start <= beta)
							{
								output[i][j]++;
							}
						}
					}
				}
			}
		}
		
		return output;
	}

	// Static methods:
	
	public static NameContainer sensorContainer(int ID)
	{
		return indexID[TYPE_DATA_SENSOR].get(ID);
	}
	
	public static NameContainer sensorContainer(String name)
	{
		return indexName[TYPE_DATA_SENSOR].get(name);
	}
	
	public static NameContainer activityContainer(int ID)
	{
		return indexID[TYPE_DATA_ACTIVITY].get(ID);
	}
	
	public static NameContainer activityContainer(String name)
	{
		return indexName[TYPE_DATA_ACTIVITY].get(name);
	}
	
	/**
	 * Returns an array with the activity IDs present in the data for all houses.
	 * @param mappingLevel Number of activity mapping levels to apply. MAPPING_LEVEL_* constants can be used for convenience.
	 * @return An array with the activity IDs present in the data for all houses.
	 */
	public static Integer[] activityListAll(int mappingLevel)
	{
		Set<Integer> IDs = new HashSet<Integer>();
		
		for (int ID: indexID[TYPE_DATA_ACTIVITY].keySet())
		{
			int mappedID = mapApply(ID, mappingLevel, TYPE_DATA_ACTIVITY);
			
			if (!IDs.contains(mappedID))
			{
				IDs.add(mappedID);
			}
		}
		
		Integer[] output = IDs.toArray(new Integer[0]);
		
		Arrays.sort(output);
		
		return output;
	}
	
	/**
	 * Maps one sensor into another, so that the source sensor is from that moment on regarded as being the target sensor. The mapping cannot be undone.
	 * @param sensorNameSource Name of the source sensor that will disappear.
	 * @param sensorNameTarget Name of the target sensor that will be augmented. It will be created if it does not exist.
	 */
	public static void mapSensors(String sensorNameSource, String sensorNameTarget)
	{
		mapEntities(sensorNameSource, sensorNameTarget, TYPE_DATA_SENSOR);
	}
	
	/**
	 * Maps one activity into another, so that the source activity is from that moment on regarded as being the target activity. The mapping cannot be undone.
	 * @param activityNameSource Name of the source activity that will disappear.
	 * @param activityNameTarget Name of the target activity that will be augmented. It will be created if it does not exist.
	 */
	public static void mapActivities(String activityNameSource, String activityNameTarget)
	{
		mapEntities(activityNameSource, activityNameTarget, TYPE_DATA_ACTIVITY);
	}
	
	private static void mapEntities(String entityNameSource, String entityNameTarget, int typeData)
	{
		if (indexName[typeData].containsKey(entityNameTarget) )
		{
			indexName[typeData].get(entityNameSource).metacontainer = indexName[typeData].get(entityNameTarget);
		}
		else //if (indexName[typeData].containsKey(entityNameSource))
		{
			NameContainer entity = new NameContainer(entityNameTarget);
			
			indexName[typeData].put(entity.name, entity);
			indexID[typeData].put(entity.ID, entity);
			
			indexName[typeData].get(entityNameSource).metacontainer = entity;
		}
	}
	
	public static ArrayList<String> getAllActivities() {
		Set<String> names = indexName[TYPE_DATA_ACTIVITY].keySet();
		ArrayList<String> namesList = new ArrayList<String>();
		namesList.addAll(names);
		return namesList;
	}
	
	public Integer sensorFiringFrequency(Integer ID)
	{
		return firingFrequencies.get(ID);
	}
	
	private static int mapApply(int ID, int mappingLevel, int typeData)
	{
		NameContainer entity = indexID[typeData].get(ID);
		
		for (int l = 0; l < mappingLevel; l++)
		{
			if (entity.metacontainer != null)
			{
				entity = entity.metacontainer;
			}
		}
		
		return entity.ID;
	}
}
