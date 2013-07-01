package art.experiments.wifi.data.processor;

import java.util.Calendar;

/**
 * Contains time interval information of a sensor firing, e.g. start/end times of an event.
 */
class TimeInterval implements Comparable<Interval> {
	private int startTime;
	private int endTime;
	private Calendar startDate;
	private Calendar endDate;
	private String sensor;
	private int noDays;
	private String type;
	private int sensorDurationMedian;
	private int sensorEndsMedian;
	private int sensorStartsMedian;
	private int noFiringsMedian; 
	private int noFirings;
	
	public TimeInterval(int startTime, int endTime, 
			Calendar startDate, Calendar endDate, String sensor) 
	{
		this.startTime = startTime;
		this.endTime = endTime;
		this.startDate = startDate;
		this.endDate = endDate;
		this.sensor = sensor;
		this.noDays = Math.round((endDate.getTime().getTime() - 
				startDate.getTime().getTime()) / (1000 * 60 * 60 * 24));
		this.noFirings = 1;
	}
	
	public void incrementFirings() {
		noFirings++;
	}

	public int getNoFirings() {
		return noFirings;
	}
	
	public void setNoFiringsMedian(int noFiringsPercentile) {
		this.noFiringsMedian = noFiringsPercentile;		
	}

	public void setDurationMedian(int sensorDurationPercentile) {
		this.sensorDurationMedian = sensorDurationPercentile;
	}

	public void setEndTimeMedian(int sensorEndsPercentile) {
		this.sensorEndsMedian = sensorEndsPercentile;
	}

	public void setStartTimeMedian(int sensorStartsPercentile) {
		this.sensorStartsMedian = sensorStartsPercentile;
	}
	
	public int getSensorEndsMedian() {
		return sensorEndsMedian;
	}
	
	public int getSensorDurationMedian() {
		return sensorDurationMedian;
	}
	
	public int getSensorStartsMedian() {
		return sensorStartsMedian;
	}
	
	public int getNoFiringsMedian() {
		return noFiringsMedian;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}

	public int getDuration() {
		int duration =  endTime - startTime;
		if (noDays > 0) {
			int m = (noDays-1)*1440; //duration in between
			int s = 1440 - startTime; //first day duration
			int e = endTime; //last day duration
			duration = e + s + m;
		}
		if (duration == 0) {
			duration = 1;
		}
		return duration;
	}

	public String getRange() {
		//if (noDays > 0) {
			return startTime + "-" + (noDays+1) + "-" + endTime;
//		}
//		return startTime + "-" + endTime;

	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	@Override
	public int compareTo(Interval interval) {
		return this.toString().compareTo(interval.toString());
	}
	
	@Override
	public String toString() {
		return getDate() + ": " + startTime + "-" + endTime + " => " + sensor;
	}
	
	public int getStartTime() {
		return startTime;
	}
	public String getValue() {
		return sensor;
	}
	
	public int getEndTime() {
		return endTime;
	}
	
	public String getDate() {
//		if (startDate.compareTo(endDate) == 0) {
			String date = startDate.get(Calendar.DATE) + "-" + 
				startDate.get(Calendar.MONTH) + "-" + startDate.get(Calendar.YEAR);
			return date;
//		}
//		return startDate.get(Calendar.DATE) + "-" + 
//				startDate.get(Calendar.MONTH) + "-" + startDate.get(Calendar.YEAR) + 
//				"-" + endDate.get(Calendar.DATE) + "-" + 
//				endDate.get(Calendar.MONTH) + "-" + endDate.get(Calendar.YEAR);
	}
	public Calendar getStartDate() {
		return startDate;
	}
	public Calendar getEndDate() {
		return endDate;
	}
}