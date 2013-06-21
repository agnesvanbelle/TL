package art.experiments.wifi.data.processor;


public class Interval {

	private int start;
	private int end;
	private int value;
	private int duration;
	private String range;
	private int dayStart;
	private int dayEnd;
	private String dayRange;

	public Interval(int value, int start, int end, int dayStart, int dayEnd) throws Exception {
		this.start = start;
		this.end = end;
		this.value = value;
		this.duration = end - start;
		this.range = start + "-" + end;
		
		this.dayStart = dayStart;
		this.dayEnd = dayEnd;
		
		int dayDuration = (int) Math.ceil(duration/1440);
		
		if (dayEnd == dayStart && dayStart == 0) {
			dayRange = "n/a";
			throw new Exception("Invalid range");
		} else {
			if (dayDuration >= 1) {
				if (duration == 1440) {
					dayRange = dayStart + "-1-" + dayEnd;
				} else {
					dayRange = //"1=" + 
						dayStart + "-";// + "1440-";
					dayRange += dayDuration+1 + "-" + //1 + "=1-" + 
						dayEnd;
				}
			} else if (dayEnd - dayStart < 0) {
				dayRange = //"1=" + 
					dayStart + "-"; // + "1440-";
				dayRange +=  "2-" + //"2=1-" + 
					dayEnd;
			} else {
				dayRange = dayStart + "-1-" + dayEnd;
			}
		}
	}
	
	public int getDayStart() {
		return dayStart;
	}
	public int getDayEnd() {
		return dayEnd;
	}
	
	public String getDayRange() {
		return dayRange;
	}
	
	public String getRange() {
		return range;
	}

	public int getDuration() {
		return duration;
	}

	public int getValue() {
		return value;
	}

	/**
	 * This method check if the beginning this interval 
	 * falls within another interval and visa versa. 
	 * For example, the activity can either start before the sensor begins firing, but 
	 * sensor begins firing before the activity is over, or
	 * the activity can start after the sensor began firing, but it does 
	 * start before the firing is over. 
	 * This is done to account for the error in either the sensor readings or 
	 * inaccuracy in the activity recording. 
	 * 
	 * @param interval - interval to compare against
	 * @return true if the start of either of the two intervals falls
	 * within the range of the other
	 */
	public boolean withinRange(Interval interval) {			
		int start2= interval.getStart();
		int end2 = interval.getEnd();
		
//		if (this.start  > start2 && this.start < end2
//				|| (start2 > this.start && start2 < this.end)) {
//			return true;
//		}
		
		if (//this.start == start2 
			//containment
//			(this.start <= start2
//					&& this.end <= end2)) {
//					//&& (start-start_) <= threshold_
//					//&& (end-end_) <= threshold_)) {
//			
//			} else if (this.start < start2 
//					&& end2 <= this.end //start cover
//					){
//					//&& (start-start_) <= threshold_) {
//				
//			} else if (this.end < end2 //end cover
//					&& start2 <= this.end) {
//					//&& (end-end_) <= threshold_) {
//				
//			} else if ((start2 <= this.start
//					&& end2 <= this.end)) {
//					//&& (start-start_) <= threshold_
//					//&& (end-end_) <= threshold_)) {
//			}
				start >= start2 && start <= end2 && end <= end2 && end >= start2 //contained
					//|| start <= start2 && end >= end2 //contains
//					|| start <= start2 && end <= end2 && end >= start2 //partial overlap left
//					|| start >= start2 && start <= end2 && end >= end2 //partial overlap right
					) {
			return true;
		}
	
		return false;
	}
	
	//4-7
	
	//5-6 - contained
	
	//2-7 - contains
	//2-4 - partial overlap
	//5-8 - partial overlap
	
	//2-3 - no overlap
	

	public int getEnd() {
		return end;
	}

	public int getStart() {
		return start;
	}

	@Override
	public String toString() {
		//return start + "-" + end;
		return value + ": " + dayRange; //start + "-" + end;
	}
	
	public String toStringWithValue() {
		return value + ": " + start + "-" + end;
	}
	
}
