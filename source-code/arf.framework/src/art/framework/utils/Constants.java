package art.framework.utils;


public class Constants {
	public static enum Type {
		INT, REAL, STR, RANGE, TIME;
	};

	public static final int COLOR_BLACK = 0;
	public static final int COLOR_RED = 1;
	public static final int COLOR_GREEN = 2;

	public final String mainRelation = "fork";	
	public static final String MOVE_VALUE = "val";
	public static final String MOVE_POSITION = "pos";

	public static final String B_MOVE_VALUE = "bmove-val";
	public static final String B_MOVE_POSITION = "bmove-pos";
	public static final String A_MOVE = "amove";
	public static final String B_MOVE = "bmove";
	
	public static final String B_WINS = "num-of-bwins";
	public static final String A_WINS = "num-of-awins";

	public static final String HAS = "has";
	public static final String FOLLOWED_BY = "followedBy";
	public static final String FOLLOWED_BY_VALUE = "followedByVal";
	public static final String FOLLOWED_BY_POSITION = "followedByPos";
	public static final String PROPERTY = "property_";
	public static final String OBJECT = "object_";
	public static final String CONNECT = "connect_";
	public static final String TOTAL = "total";
	public static final String CHESS_EXAMPLES_FILE = "chessExamples";
	public static final String CHESS_ABSTRUCT_EXAMPLES_FILE = "chessAbstructExamples";
	public static final String POSITIVE = "P";
	public static final String NEGATIVE = "N";
	public static final String AND = "and";
	public static final String SPACE = " ";
	public static final String COLON = ":";
	public static final String NEW_LINE = "\n";
	public static final String ACT_TYPE = "act-type";
	public static final String ACT_INTERVAL = "act-interval";
	public static final String ACT_DURATION = "act-duration";
	public static final String SENSOR_INTERVAL = "fire-interval";
	public static final String SENSOR_DURATION = "fire-duration";
	public static final String ACTIVITY = "activity";
	public static final String SENSOR = "sensor";
	public static final String SENSOR_TYPE = "sensor-type";
	public static final String WIFI_EXAMPLES_FILE = "wifiExamples";
	public static final String CHESS_CLASS_MAP_FILE = "chessClassMap";
	public static final String TTT_CLASS_MAP_FILE = "tttClassMap";
	public static final String NMM_CLASS_MAP_FILE = "nmmClassMap";
	public static final String WIFI_CLASS_MAP_FILE = "wifiClassMap";
	public static final String WIFI_ABSTRUCT_EXAMPLES_FILE = "wifiAbstructExamples";
	public static final String SENSOR_START = "start-time";
	public static final String SENSOR_END = "end-time";
	public static final String SENSOR_NO_FIRINGS = "no-firings";
	public static final String MOVE_TYPE = "type";

	public static final String TTT_EXAMPLES_FILE = "tttExamples";
	public static final String TTT_ABSTRUCT_EXAMPLES_FILE = "tttAbstructExamples";
	public static final String NMM_EXAMPLES_FILE = "nmmExamples";
	public static final String NMM_ABSTRUCT_EXAMPLES_FILE = "nmmAbstructExamples";


}
