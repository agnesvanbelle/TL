package art.experiments.wifi.data.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Describes an event - time interval at which certain sensor fired or certain actions occured.
 */
public class EventInfo {
	private String type;
	private String actionType;
	private List<Integer> sensorDurations;
	private int noFirings = 0;
	private double avgDuration = 0;
	private int actionDuration = 0;
	private int actionStart;
	private int actionEnd;
	private List<Integer> sensorStarts;
	private List<Integer> sensorEnds;
	private List<Integer> sensorStartsMedians;
	private List<Integer> sensorDurationsMedians;
	private List<Integer> sensorEndsMedians;
	private List<Integer> noFiringsMedians;
	private List<Integer> actionDurationMedians;
	private String actionDurationsRange;
	private String sensorNoFiringsRange;
	private String sensorDurationsRange;
	private String sensorEndsRange;
	private String sensorStartsRange;
	
	public EventInfo(String type, String actionType, 
			int actionDuration, int actionStart, int actionEnd) 
	{
		this.type = type;
		this.actionType = actionType;
		this.sensorDurations = new ArrayList<Integer>();
		this.sensorStarts = new ArrayList<Integer>();
		this.sensorEnds = new ArrayList<Integer>();
		this.actionDuration = actionDuration;
		this.actionStart = actionStart;
		this.actionEnd = actionEnd; 
		
		sensorStartsMedians = new ArrayList<Integer>();
		sensorDurationsMedians = new ArrayList<Integer>();
		sensorEndsMedians = new ArrayList<Integer>();
		noFiringsMedians = new ArrayList<Integer>();
		actionDurationMedians = new ArrayList<Integer>();
	}

	public void addEnd(int endTime) {
		sensorStarts.add(endTime);
	}

	public void addStart(int startTime) {
		sensorEnds.add(startTime);
	}

	public void addDuration(int sensorDuration) {
		sensorDurations.add(sensorDuration);
		noFirings++;
		avgDuration += sensorDuration; 
		
	}
	
	public int getActionEnd() {
		return actionEnd;
	}
	public int getActionStart() {
		return actionStart;
	}
	
	public List<Integer> getStarts() {
		return sensorStarts;
	}
	
	public List<Integer> getEnds() {
		return sensorEnds;
	}
	
	public List<Integer> getDurations() {
		return sensorDurations;
	}
	
	public int getDurationPercentile(int percentile) {
		Collections.sort(sensorDurations);
		int range = (int)Math.round((1.0 * percentile /100) * 
				sensorDurations.size() + 0.5);
		return sensorDurations.get(range-1);
	}
	
	public String getType() {
		return type;
	}
	
	public String getActionType() {
		return actionType;
	}
	
	public double getAvgDuration() {
		return (1.0*avgDuration / 1.0*noFirings);
	}
	
	public int getActionDuration() {
		return actionDuration;
	}

	public void setFollowedBy(EventInfo prevCi) {
		
	}

	public void setStartTimeMedian(int sensorStartsPercentile) {
		this.sensorStartsMedians.add(sensorStartsPercentile);
	}
	public void setEndTimeMedian(int sensorEndsPercentile) {
		this.sensorEndsMedians.add(sensorEndsPercentile);
	}
	public void setDurationsMedian(int sensorDurationsPercentile) {
		this.sensorDurationsMedians.add(sensorDurationsPercentile);
	}
	public void setNoFiringsMedian(int noFiringsPercentile) {
		this.noFiringsMedians.add(noFiringsPercentile);
	}

	public void setActionDurationMedian(int actionDurationMedian) {
		this.actionDurationMedians.add(actionDurationMedian);
	}
	
	public List<Integer> getSensorStartsMedians() {
		return sensorStartsMedians;
	}
	
	public List<Integer> getSensorEndsMedians() {
		return sensorEndsMedians;
	}
	
	public List<Integer> getSensorDurationsMedians() {
		return sensorDurationsMedians;
	}

	public List<Integer> getNoFiringsMedians() {
		return noFiringsMedians;
	}

	public List<Integer> getActionDurationMedians() {
		return sensorDurationsMedians;
	}

	public int getNoFirings() {
		return noFirings;
	}

	public void setStartTimeRange(String sensorStartsRange) {
		this.sensorStartsRange = sensorStartsRange;
	}
	
	public void setEndTimeRange(String sensorEndsRange) {
		this.sensorEndsRange = sensorEndsRange;
	}

	public void setDurationsRange(String sensorDurationsRange) {
		this.sensorDurationsRange = sensorDurationsRange;
	}
	
	public void setNoFiringsRange(String sensorNoFiringsRange) {
		this.sensorNoFiringsRange = sensorNoFiringsRange;
	}
	public void setActionDurationRange(String actionDurationsRange) {
		this.actionDurationsRange = actionDurationsRange;
	}

	public String getSensorDurationsRange() {
		return sensorDurationsRange;
	}
	public String getSensorNoFiringsRange() {
		return sensorNoFiringsRange;
	}
	
	public String getSensorEndsRange() {
		return sensorEndsRange;
	}
	public String getActionDurationsRange() {
		return actionDurationsRange;
	}
	
	public String getSensorStartsRange() {
		return sensorStartsRange;
	}
}

