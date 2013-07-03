package data;

import java.text.*;
import java.util.*;

/*
 * Objects of this class represent a point within the data (either a sensor firing or an activity occurrence).
 * This class also contains useful methods for manipulating the information within such data points.
 */
public class DataPoint
{
	// Example time block sizes:
	
	public static final int TIME_BLOCK_SIZE_MINUTE = 60;
	public static final int TIME_BLOCK_SIZE_HOUR   = 3600;
	
	// Data fields:
	
	public final int start, length, ID;
	
	// Format for the human-readable version of the "start" field:
	
	private static final SimpleDateFormat startDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
		
	private final Date startDate;
	
	public DataPoint(int start, int length, int ID)
	{
		this.start  = start;
		this.length = length;
		this.ID     = ID;
		
		startDate = new Date(start * 1000L);
		
		startDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	/**
	 * Returns the starting time of the event in a human-readable format.
	 * @return The starting time of the event in a human-readable format.
	 */
	public  String startDate()
	{
		return startDateFormat.format(startDate);
	}
	
	/**
	 * Returns the starting time block of the event within the day. The first time block within a day has the number zero.
	 * @param blockSize Size of the time blocks, in seconds.
	 * @return The starting time block of the event within the day.
	 */
	@SuppressWarnings("deprecation")
	public int startBlock(int blockSize)
	{
		int mappedHour = (startDate.getHours() + 24 + startDate.getTimezoneOffset() / 60) % 24;
		
		int secondsElapsedDay = mappedHour * 3600 + startDate.getMinutes() * 60 + startDate.getSeconds();
		
		return secondsElapsedDay / blockSize;
	}
	
	public String toString()
	{
		StringBuilder s = new StringBuilder();
		
		s.append("Datapoint ID:" + ID);
		s.append(", start: " + start);
		s.append(", length: " + length);
		s.append(", start date: " + startDate);
		
		return s.toString();
	}
}
