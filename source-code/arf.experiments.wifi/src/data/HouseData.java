package data;

import java.io.*;
import java.util.*;

public class HouseData
{
	// List of all activities across houses (if mappings are added, some activities might be repeated):
	
	private static final ArrayList<Activity> activities = new ArrayList<Activity>();
	
	// Mapping from activity IDs within the current house to activity IDs across houses:
	
	private final HashMap<Integer, Integer> activityMapping = new HashMap<Integer, Integer>();
	
	// House data:
	
	private final ArrayList<DataPoint> sensorData = new ArrayList<DataPoint>();
	private final ArrayList<DataPoint> labelData  = new ArrayList<DataPoint>();
	
	// Dynamic methods:
	
	/**
	 * Loads the data from a given house and creates a HouseData object containing it.
	 * @param houseName Name of the house to load. The data files must be located under the 'data/' folder, and the files 'houseName.activities', 'houseName.labels' and 'houseName.sensors' must be present.
	 */
	public HouseData(String houseName)
	{
		BufferedReader br = null;
		 
		try
		{
			// Loading of activities:
			
			String line;
 
			br = new BufferedReader(new FileReader("data/" + houseName + ".activities"));
 
			while ((line = br.readLine()) != null)
			{
				String[] columns = line.split("\t");
				
				Activity activity = new Activity(columns[1]);
				
				int index = activities.indexOf(activity);
				
				if (index >= 0)
				{
					activityMapping.put(Integer.parseInt(columns[0]), index);
				}
				else
				{
					activityMapping.put(Integer.parseInt(columns[0]), activities.size());
					activities.add(activity);
				}
			}
			
			// Loading of sensor and label data:
			
			loadData(sensorData, "data/" + houseName + ".sensors");
			loadData(labelData,  "data/" + houseName + ".labels");
 
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
	
	private void loadData(ArrayList<DataPoint> dataObject, String dataFile)
	{
		BufferedReader br = null;
		 
		try
		{
			String line;
 
			br = new BufferedReader(new FileReader(dataFile));
 
			while ((line = br.readLine()) != null)
			{
				String[] columns = line.split("\t");
				
				dataObject.add(new DataPoint(Integer.parseInt(columns[0]), Integer.parseInt(columns[1]), Integer.parseInt(columns[2])));
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
	
	/**
	 * Returns the number of entries of the sensor data for this house.
	 * @return The number of entries of the sensor data for this house.
	 */
	public int sensorDataSize()
	{
		return sensorData.size();
	}
	
	/**
	 * Returns the number of entries of the label data for this house.
	 * @return The number of entries of the label data for this house.
	 */
	public int labelDataSize()
	{
		return labelData.size();
	}
	
	/**
	 * Returns the sensor data stored for this house.
	 * @return An array of data point objects.
	 */
	public DataPoint[] sensorData() // TODO
	{
		return null;
	}
	
	/**
	 * Returns the label data stored for this house, applying the mappings between activities specified so far.
	 * @return An array of data point objects.
	 */
	public DataPoint[] labelData()
	{
		DataPoint[] output = new DataPoint[labelData.size()];
		
		// Final mapping of label IDs:
		
		int index = 0;
		
		for (DataPoint data: labelData)
		{
			Activity activity = activities.get(activityMapping.get(data.ID));
			
			int mappedID = activities.indexOf(activity);
			
			output[index++] = new DataPoint(data.start, data.length, mappedID);
		}
		
		return output;
	}
	
	// Static methods:
	
	/**
	 * Maps one activity into another, so that the source activity is from that moment on regarded as being the target activity. The mapping cannot be undone.
	 * @param activityNameSource Name of the source activity that will disappear.
	 * @param activityNameTarget Name of the target activity that will be augmented.
	 */
	public static void activitiesMap(String activityNameSource, String activityNameTarget)
	{
		Activity activitySource = new Activity(activityNameSource);
		Activity activityTarget = new Activity(activityNameTarget);
		
		int indexSource = activities.indexOf(activitySource);
		int indexTarget = activities.indexOf(activityTarget);
		
		if (indexSource >= 0 && indexTarget >= 0)
		{
			activities.set(indexSource, activities.get(indexTarget));
		}
		else
		{
			System.out.println("WARNING: Incorrect activity name. No mapping was done. Check that both activity names are correctly spelled within the mapping.");
		}
	}
}
