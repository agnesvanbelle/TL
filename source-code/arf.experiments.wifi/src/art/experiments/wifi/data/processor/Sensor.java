package art.experiments.wifi.data.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Sensor {
	//sensor type
	private String type;
	//action => fire pattern
	private List<EventInfo> info;
	private Map<String, ActionInfo> actionInfoMap;
	
	public Sensor(String type) {
		this.type = type;
		info = new ArrayList<EventInfo>();
		actionInfoMap = new HashMap<String, ActionInfo>();
	}
	

	public void merge(Sensor ss) {
		if (!type.equals(ss.getType())) {
			return;
		}
		this.info.addAll(ss.getInfo());
		Map<String, ActionInfo> actionInfoMapSS = ss.getActions();
		for (String action: actionInfoMapSS.keySet()) {
			ActionInfo aST = actionInfoMap.get(action);
			ActionInfo aSS = actionInfoMapSS.get(action);
			if (aST == null) {
				actionInfoMap.put(action, aSS);
			} else {
				aST.merge(aSS);
			}
		}
	}
	
	public String getType() {
		return type;
	}

	public List<EventInfo> getInfo() {
		return info;
	}


	public void add(EventInfo countInfo) {
		info.add(countInfo);
		String action = countInfo.getActionType();
		ActionInfo actionInfo = actionInfoMap.get(action);
		if (actionInfo == null) {
			actionInfo = new ActionInfo(action);
		}
		actionInfo.addSensorDurations(countInfo.getDurations());
		actionInfo.addNoFirings(countInfo.getDurations().size());
		actionInfo.addSensorStarts(countInfo.getStarts());
		actionInfo.addSensorEnds(countInfo.getEnds());

		actionInfo.addActionDuration(countInfo.getActionDuration());
		actionInfo.addActionStart(countInfo.getActionStart());
		actionInfo.addActionEnd(countInfo.getActionEnd());
		
		
		actionInfoMap.put(action, actionInfo);
	}
	
	public void printActionInfo(boolean interpolated) {
		for (String actionName: actionInfoMap.keySet()) {
			System.out.println("action name " + actionName);
			ActionInfo actionInfo = actionInfoMap.get(actionName);
			System.out.println("sensor durations: " + actionInfo.getSensorDurations());
			System.out.println("s.d. median: " + actionInfo.getSensorDurationPercentile(50, interpolated));
			System.out.println("no firings: " + actionInfo.getNoFirings());
			System.out.println("no firings median: " + actionInfo.getNoFiringsPercentile(50, interpolated));
			System.out.println("action durations: " + actionInfo.getActionDurations());
			System.out.println("a.d. median: " + actionInfo.getActionDurationPercentile(50, interpolated));
			System.out.println();
			System.out.println("sensor starts: " + actionInfo.getSensorStarts()); 
			System.out.println("s.d. starts median: " + actionInfo.getSensorStartsPercentile(50, interpolated)); 
			System.out.println("sensor ends: " + actionInfo.getSensorEnds()); 
			System.out.println("s.d. ends median: " + actionInfo.getSensorStartsPercentile(50, interpolated)); 
			System.out.println("action starts: " + actionInfo.getActionStarts()); 
			System.out.println("s.d. starts median: " + actionInfo.getActionStartsPercentile(50, interpolated)); 
			System.out.println("action ends: " + actionInfo.getActionEnds()); 
			System.out.println("s.d. ends median: " + actionInfo.getActionEndsPercentile(50, interpolated)); 
			System.out.println();


		}
	}
	
	public void printEventInfo() {
		System.out.println("sensor " + type);
		for (EventInfo ci: info) {
			System.out.println(ci.getDurations());
			System.out.println(ci.getActionType() + " act duration: " + 
				ci.getActionDuration() + " sens duration: " + ci.getDurationPercentile(50));
			
		}

	}

	public ActionInfo getAction(String action) {
		return actionInfoMap.get(action);
	}

	public Map<String, ActionInfo> getActions() {
		return actionInfoMap;
	}
}