package art.experiments.wifi.data.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public 	class ActionInfo {
	private String type; 
	private List<Integer> actionDurations; 
	private List<Integer> actionStarts; 
	private List<Integer> actionEnds; 
	private List<Integer> sensorDurations; 
	private List<Integer> sensorStarts; 
	private List<Integer> sensorEnds; 

	private List<Integer> noFirings; 
	
	public ActionInfo(String type) {
		this.type = type; 
		actionDurations = new ArrayList<Integer>();
		actionStarts = new ArrayList<Integer>();
		actionEnds = new ArrayList<Integer>();
		
		sensorDurations = new ArrayList<Integer>();
		sensorStarts = new ArrayList<Integer>();
		sensorEnds = new ArrayList<Integer>();
		noFirings = new ArrayList<Integer>();
	}


	
	public void addActionEnd(int actionEnd) {
		actionEnds.add(actionEnd);
	}

	public void addActionStart(int actionStart) {
		actionStarts.add(actionStart);
	}

	public void addSensorEnds(List<Integer> ends) {
		sensorEnds.addAll(ends);
	}

	public void addSensorStarts(List<Integer> starts) {
		sensorStarts.addAll(starts);
	}

	public List<Integer> getActionDurations() {
		Collections.sort(actionDurations);
		return actionDurations; 
	}

	public List<Integer> getNoFirings() {
		Collections.sort(noFirings);
		return noFirings;
	}

	public List<Integer> getSensorEnds() {
		return sensorEnds;
	}
	
	public List<Integer> getSensorStarts() {
		return sensorStarts;
	}
	
	public List<Integer> getActionEnds() {
		return actionEnds;
	}
	
	public List<Integer> getActionStarts() {
		return actionStarts;
	}
	
	public List<Integer> getSensorDurations() {
		Collections.sort(sensorDurations);
		return sensorDurations;
	}

	public void addNoFirings(int noFirings) {
		this.noFirings.add(noFirings);
	}

	public void addSensorDurations(List<Integer> durations) {
		this.sensorDurations.addAll(durations);
	}

	public void addActionDuration(int actionDuration) {
		this.actionDurations.add(actionDuration);
	}
	
	private int getPrecentile(int percentile, int size) {
		return (int)Math.round((1.0 * percentile /100) * size + 0.5);
	}
	
	public int getInterpolatedPercentile(int percentile, int size, List<Integer> list) {
		Collections.sort(list);
		int[] percentiles = new int[size];
		for (int i = 1; i <= size; i++) {
			int p = (int)Math.round((100.0 / size) * (i-0.5));
			percentiles[i-1] = p;
		}
		if (list.size() == 1) {
			return list.get(0);
		} if (percentile < percentiles[0]) {
			return list.get(0);
		} else if (percentile > percentiles[size-1]) {
			return list.get(size-1);
		} else {
			for (int i = 0; i < size; i++) {
				try {
				if (percentile >= percentiles[i]
				                 && (i+1>=size || percentile < percentiles[i+1])) {
					Integer vk = list.get(i);
					Integer vk1 = (i+1 < size) ? list.get(i+1) : vk+1;
					
					return (int)Math.ceil(vk + 1.0 *size * (1.0*(percentile -
									percentiles[i])/100)*(vk1-vk));
				}
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		}
		
		return -1;
	}
	
	public int getPrecentile(int percentile,List<Integer> list, boolean interpolated) {
		if (interpolated) {
			return getInterpolatedPercentile(percentile, list.size(), list);
		}
		Collections.sort(list);
		return list.get(getPrecentile(percentile, list.size())-1);
	}
	
	public int getSensorDurationPercentile(int percentile, boolean interpolated) {
		return getPrecentile(percentile, sensorDurations, interpolated);
	}
	
	public int getActionDurationPercentile(int percentile, boolean interpolated) {
		return getPrecentile(percentile, actionDurations, interpolated);
	}
	
	public int getNoFiringsPercentile(int percentile, boolean interpolated) {
		return getPrecentile(percentile, noFirings, interpolated);
	}
	
	
	public int getActionEndsPercentile(int percentile, boolean interpolated) {
		return getPrecentile(percentile, actionEnds, interpolated);
	}

	public int getActionStartsPercentile(int percentile, boolean interpolated) {
		return getPrecentile(percentile, actionStarts, interpolated);

	}

	public int getSensorStartsPercentile(int percentile, boolean interpolated) {
		return getPrecentile(percentile, sensorStarts, interpolated);
	}

	public int getSensorEndsPercentile(int percentile, boolean interpolated) {
		return getPrecentile(percentile, sensorEnds, interpolated);
	}


	public int getActionDurationMedian(int percentile, boolean interpolated) {
		return getPrecentile(percentile, actionDurations, interpolated);
	}


	public boolean containsSensorStart(int startTime) {
		return sensorStarts.contains(startTime);
	}



	public boolean containsSensorEnd(int endTime) {
		return sensorEnds.contains(endTime);
	}
	
	public boolean containSensorDuration(int duration) {
		return sensorDurations.contains(duration);
	}

	public boolean containsSensorStarts(List<Integer> startTimes) {
		return sensorStarts.containsAll(startTimes);
	}
	
	public boolean containsSensorEnds(List<Integer> endTimes) {
		return sensorEnds.containsAll(endTimes);
	}
	
	public boolean containsSensorDurations(List<Integer> durations) {
		return sensorDurations.containsAll(durations);
	}

	public boolean containsNoFirings(int noFirings) {
		return this.noFirings.contains(noFirings);
	}

	public String getSensorStartsRange() {
		Collections.sort(sensorStarts);
		return sensorStarts.get(0) + "-" + sensorStarts.get(sensorStarts.size()-1);
	}
	public String getSensorEndsRange() {
		Collections.sort(sensorEnds);
		return sensorEnds.get(0) + "-" + sensorEnds.get(sensorEnds.size()-1);
	}
	public String getSensorDurationsRange() {
		Collections.sort(sensorDurations);
		return sensorDurations.get(0) + "-" + sensorDurations.get(sensorDurations.size()-1);
	}

	public String getSensorNoFiringsRange() {
		Collections.sort(noFirings);
		return noFirings.get(0) + "-" + noFirings.get(noFirings.size()-1);
	}
	public String getActionDurationsRange() {
		Collections.sort(actionDurations);
		return actionDurations.get(0) + "-" + actionDurations.get(actionDurations.size()-1);
	}



	public String getType() {
		return type;
	}



	public void merge(ActionInfo actionInfo) {
		if (!actionInfo.getType().equals(this.getType())) {
			return;
		}
		actionDurations.addAll(actionInfo.getActionDurations());
		actionStarts.addAll(actionInfo.getActionStarts());
		actionEnds.addAll(actionInfo.getActionEnds());
		sensorDurations.addAll(actionInfo.getSensorDurations());
		sensorStarts.addAll(actionInfo.getSensorStarts());
		sensorEnds.addAll(actionInfo.getSensorEnds());
		noFirings.addAll(actionInfo.getNoFirings());
	}

	
}
