package data;

import java.io.*;
import java.util.*;

public class HouseData
{
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
	
	private final HashMap<Integer, float[][]> sensorProfiles = new HashMap<Integer, float[][]>(); 
	
	// Dynamic methods:
	
	/**
	 * Loads the data from a given house and creates a HouseData object containing it.
	 * @param houseName Name of the house to load. The data files must be located under the 'data/' folder, see the 'README.txt' file.
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
					br = new BufferedReader(new FileReader("data/" + houseName + ".sensor_names"));
				break;
				case TYPE_DATA_ACTIVITY:
					br = new BufferedReader(new FileReader("data/" + houseName + ".activity_names"));
				break;
			}
			
			String line;
 
			while ((line = br.readLine()) != null)
			{
				String[] columns = line.split("\t");
				
				int    ID   = Integer.parseInt(columns[0]);
				String name = columns[1];
				
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
					br = new BufferedReader(new FileReader("data/" + houseName + ".sensors"));
				break;
				case TYPE_DATA_ACTIVITY:
					br = new BufferedReader(new FileReader("data/" + houseName + ".activities"));
				break;
			}
			
			String line;
 
			while ((line = br.readLine()) != null)
			{
				String[] columns = line.split("\t");
				
				int start  = Integer.parseInt(columns[0]);
				int length = Integer.parseInt(columns[1]);
				int ID     = Integer.parseInt(columns[2]);
				
				NameContainer entity;
				
				if (mapping[typeData].containsKey(ID))
				{
					entity = mapping[typeData].get(ID);
				}
				else
				{
					entity = new NameContainer();
					
					indexName[typeData].put(entity.name, entity);
					indexID[typeData].put(entity.ID, entity);
					
					mapping[typeData].put(ID, entity);
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
		return dataID[TYPE_DATA_SENSOR].keySet().toArray(new Integer[0]);
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
	
	private int mapApply(int ID, int mappingLevel, int typeData)
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
	 * Returns a normalized histogram of sensor activations over start times and firing lengths.
	 * @param ID Sensor ID whose profile will be calculated.
	 * @param blockSizeStart Length of the blocks in which a day will be divided, in seconds.
	 * @param blockNumLength Number of discrete blocks into which firing lengths will be mapped.
	 * @param maxLength Maximum informative firing length. Lengths above this value will simply be regarded as being this value.
	 * @return A matrix of sensor activations over start times (first index) and firing lengths (second index).
	 */
	public float[][] profileSensor(int ID, int blockSizeStart, int blockNumLength, int maxLength)
	{
		if (!sensorProfiles.containsKey(ID))
		{
			float[][] profile = buildProfileSensor(ID, blockSizeStart, blockNumLength, maxLength);
			
			sensorProfiles.put(ID, profile);
		}
		
		return sensorProfiles.get(ID);
	}
	
	private float[][] buildProfileSensor(int ID, int blockSizeStart, int blockNumLength, int maxLength)
	{
		int nr_start_time_bins = (24 * 3600) / blockSizeStart;
		
		float[][] output = new float[nr_start_time_bins][blockNumLength];
		
		long total = 0;
		
		for (DataPoint data: dataID[TYPE_DATA_SENSOR].get(ID))
		{
			// Linear scale for the firing length mapping:
			
			int blockLength = (int) (((float) Math.min(data.length, maxLength) / maxLength) * (blockNumLength - 1));
			
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
	 * Returns the relative profile between two sensors, modeled as a normal distribution of the differences of their firing times.
	 * @param sensorA First sensor ID.
	 * @param sensorB Second sensor ID.
	 * @return The relative profile between two sensors, modeled as a normal distribution of the differences of their firing times.
	 */
	public NormalDistribution profileRelative(int sensorA, int sensorB)
	{
		ArrayList<Integer> values = new ArrayList<Integer>();
		
		for (DataPoint dataA: dataID[TYPE_DATA_SENSOR].get(sensorA))
		{
			int minDiff = Integer.MAX_VALUE;
			
			for (DataPoint dataB: dataID[TYPE_DATA_SENSOR].get(sensorB))
			{
				int diff = dataA.start - dataB.start;
				
				if (Math.abs(diff) < Math.abs(minDiff))
				{
					minDiff = diff;
				}
			}
			
			values.add(minDiff);
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
	
	public void sensorDataFormatLena(int mappingLevel, String filename)
	{
		DataPoint[] output = sensorData(mappingLevel);
		
		// TODO
	}
	
	public void activityDataFormatLena(int mappingLevel, String filename)
	{
		DataPoint[] output = activityData(mappingLevel);
		
		// TODO
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
		if (indexName[typeData].containsKey(entityNameTarget))
		{
			indexName[typeData].get(entityNameSource).metacontainer = indexName[typeData].get(entityNameTarget);
		}
		else
		{
			NameContainer entity = new NameContainer(entityNameTarget);
			
			indexName[typeData].put(entity.name, entity);
			indexID[typeData].put(entity.ID, entity);
			
			indexName[typeData].get(entityNameSource).metacontainer = entity;
		}
	}
}
