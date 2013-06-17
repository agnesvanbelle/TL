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
	
	private final ArrayList<DataPoint>[]                   dataTime = new ArrayList[2]; // Index per chronological order.
	private final HashMap<Integer, ArrayList<DataPoint>>[] dataID   = new HashMap[2];   // Index per IDs.
	
	// Private-to-public IDs mapping:
	
	private final HashMap<Integer, NameContainer>[] mapping = new HashMap[2];
	
	// Dynamic methods:
	
	/**
	 * Loads the data from a given house and creates a HouseData object containing it.
	 * @param houseName Name of the house to load. The data files must be located under the 'data/' folder, see the 'README.txt' file.
	 */
	public HouseData(String houseName)
	{
		loadNames(houseName, TYPE_DATA_SENSOR);
		loadNames(houseName, TYPE_DATA_ACTIVITY);
		
		loadData(houseName, TYPE_DATA_SENSOR);
		loadData(houseName, TYPE_DATA_ACTIVITY);
	}
	
	private void loadNames(String houseName, int typeData)
	{
		indexName[typeData] = new HashMap<String,  NameContainer>();
		indexID[typeData]   = new HashMap<Integer, NameContainer>();
		mapping[typeData]   = new HashMap<Integer, NameContainer>();
		
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
				
				NameContainer entity;

				if (indexName[typeData].containsKey(columns[1]))
				{
					entity = indexName[typeData].get(columns[1]);
				}
				else
				{
					entity = new NameContainer(columns[1]);
					
					indexName[typeData].put(entity.name, entity);
					indexID[typeData].put(entity.ID, entity);
				}
				
				mapping[typeData].put(Integer.parseInt(columns[0]), entity);
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
	
	private void loadData(String houseName, int typeData)
	{
		dataTime[typeData] = new ArrayList<DataPoint>();
		dataID[typeData]   = new HashMap<Integer, ArrayList<DataPoint>>();
		
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
				
				DataPoint data = new DataPoint(start, length, ID);
				
				dataTime[typeData].add(data);
				
				if (!dataID[typeData].containsKey(ID))
				{
					dataID[typeData].put(ID, new ArrayList<DataPoint>());
				}
				
				dataID[typeData].get(ID).add(data);
				
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
	 * Returns an array with information about the sensors present in the data for this house.
	 * The sensors are returned in the order implicitly assumed throughout the methods of this class.
	 * @return An array with information about the sensors present in the data for this house.
	 */
	public NameContainer[] sensorList()
	{
		return dataID[TYPE_DATA_SENSOR].keySet().toArray(new NameContainer[0]);
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
		NameContainer entity = mapping[typeData].get(ID);
		
		for (int l = 0; l < mappingLevel; l++)
		{
			if (entity.metacontainer != null)
			{
				entity = entity.metacontainer;
			}
		}
		
		return entity.ID;
	}
	
	// Advanced dynamic methods:
	
	/**
	 * Suitable for feeding the alpha-beta meta-feature extraction algorithm.
	 * @param beta Beta parameter, expressed in seconds.
	 * @return
	 */
	public int[][] profileAlphaBeta(int beta)
	{
		NameContainer[] sensors = sensorList();
		
		int[][] output = new int[sensors.length][sensors.length];
		
		for (int i = 0; i < sensors.length; i++)
		{
			int IDPrev = sensors[i].ID;
			
			for (int j = 0; j < sensors.length; j++)
			{
				int IDNext = sensors[j].ID;
				
				for (DataPoint dataPrev: dataID[TYPE_DATA_SENSOR].get(IDPrev))
				{
					for (DataPoint dataNext: dataID[TYPE_DATA_SENSOR].get(IDNext))
					{
						if (dataNext.start - dataPrev.start >= beta)
						{
							output[i][j]++;
						}
					}
				}
			}
		}
		
		return output;
	}
	
	// Static methods:
	
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
			
			indexName[typeData].put(entityNameTarget, entity);
			indexID[typeData].put(entity.ID, entity);
			
			indexName[typeData].get(entityNameSource).metacontainer = entity;
		}
	}
}