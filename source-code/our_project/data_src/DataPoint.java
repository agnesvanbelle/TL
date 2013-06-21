package data;

import java.text.*;
import java.util.*;

public class DataPoint
{
	// Example time block sizes:
	
	public static final int TIME_BLOCK_SIZE_MINUTE = 60;
	public static final int TIME_BLOCK_SIZE_HOUR   = 3600;
	
	// Format for the human-readable version of the "start" field:
	
	private static final SimpleDateFormat startDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
	
	// Private data fields:
	
	public final int start, length, ID;
	
	private final Date startDate;
	
	DataPoint(int start, int length, int ID)
	{
		this.start  = start;
		this.length = length;
		this.ID     = ID;
		
		startDate = new Date(start * 1000L);
	}
	
	/**
	 * Returns the starting time of the event in a human-readable format.
	 * @return The starting time of the event in a human-readable format.
	 */
	public String startDate()
	{
		startDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		
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
}
